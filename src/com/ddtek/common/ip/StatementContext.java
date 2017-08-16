package com.ddtek.common.ip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ddtek.common.dataprocessor.DataConvertor;
import com.ddtek.common.schema.ColumnInfo;
import com.ddtek.common.schema.TableInfo;
import oajava.sql.ResultBuffer;
import oajava.sql.ip;
import oajava.sql.jdam;
import oajava.sql.xo_int;


public class StatementContext {

	public static enum StatementContextType {
		SCHEMA_TYPE("SchemaType"), QUERY_TYPE("QueryType");
		
		String contextType;
		StatementContextType(String contextType) {
			this.contextType = contextType;
		}
		
		public String getContextTypeName(){
			return contextType;
		}
	}

	private long				dam_hstmt;		/* DAM handle to the statement */
	private String				sTableName;		/* Name of the table being queried */
	private long				iFetchSize;     /* records to return in cursor mode */
	private long				iTopRows;       /* any setting indicating TOP N clause */
	private long				iMaxRows;       /* Maximum number of rows to be returned */
	private long				iNumResRows; 	/* number of rows created */
	private long				iTotalRowCount; /* total number of rows returned */
	private Map<String, Long>	colsInUse;
	private Map<String, String>	fieldsInUse;

	/* Bulk Fetch */
	int							m_iNoOfResColumns;
	ResultBuffer				m_resultBuffer;

	private SessionContext		sessionContext;
	private List<Long>			setOfConditionLists;

	private TableInfo			tableInfo;

	private int					status;
	private String				statusMessage;	
	private long dam_Conn_Handle;
	private StatementContextType stmtCntxType;
	
	private DataConvertor dataConverter;
	
	public DataConvertor getDataConverter() {
		return dataConverter;
	}
	public void setDataConverter(DataConvertor dataConverter) {
		this.dataConverter = dataConverter;
	}
	private List<InvocationContext> invocationContextList = new ArrayList<InvocationContext>();
	public List<InvocationContext> getInvocationContextList() {
		return invocationContextList;
	}
	public void setInvocationContextList(
			List<InvocationContext> invocationContextList) {
		this.invocationContextList = invocationContextList;
	}
	private int currentInvocationContextIndex = -1;
	
	private Map<String, String> properties = new HashMap<String,String>();

	
	public String getProperty(String key){
		return properties.get(key);
	}
	
	public void setProperty(String key, String value){
		properties.put(key, value);
	}

	public StatementContext(SessionContext sessionContext) throws Exception {
		this.stmtCntxType = StatementContextType.SCHEMA_TYPE;
		this.sessionContext = sessionContext;
		this.dam_Conn_Handle = sessionContext.getDam_Conn_Handle();
		this.dam_hstmt = 0;

		this.iFetchSize = 100; // Schema calls won't create table rows, so didn't bother
	}

	public StatementContext(SessionContext sessionContext, long dam_hstmt) throws Exception {

		xo_int  piValue = new xo_int();
		int		iRetCode;

		StringBuffer sTableNameBuffer = new StringBuffer(ip.DAM_MAX_ID_LEN + 1);
		colsInUse = new HashMap<String, Long>();
		fieldsInUse = new HashMap<String, String>();

		this.stmtCntxType = StatementContextType.QUERY_TYPE;
		this.sessionContext = sessionContext;
		this.dam_Conn_Handle = sessionContext.getDam_Conn_Handle();
		
		trace(ip.UL_TM_F_TRACE, "StatementContext() called");

		this.dam_hstmt = dam_hstmt;
		jdam.dam_describeTable(dam_hstmt, null, null, sTableNameBuffer, null, null);
		this.sTableName = sTableNameBuffer.toString().toUpperCase();
		

		try {
			tableInfo = sessionContext.getSchemaDescriptor().getTableInfo(sTableName);
		} catch (Exception e) {
			throw new Exception("Couldn't find the table " + sTableName + " in the schema.");
		}

		/* get fetch block size */
		iRetCode = jdam.dam_getInfo(0, this.dam_hstmt, oajava.sql.ip.DAM_INFO_FETCH_BLOCK_SIZE,
				null, piValue);
		if (iRetCode != oajava.sql.ip.DAM_SUCCESS)
			this.iFetchSize = 100;
		else
			this.iFetchSize = piValue.getVal();

		iRetCode = jdam.dam_getInfo(0, this.dam_hstmt, oajava.sql.ip.DAM_INFO_QUERY_MAX_ROWS,
				null, piValue);
		this.iMaxRows = piValue.getVal();

		/* check if TOP N clause is set */
		piValue.setVal(0);
		iRetCode = jdam.dam_getInfo(0, this.dam_hstmt, oajava.sql.ip.DAM_INFO_QUERY_TOP_ROWS,
				null, piValue);
		this.iTopRows = piValue.getVal();

		populateConditionList();
		populateColumnsAndFieldsInUse();
		this.dataConverter = new DataConvertor();

		status = ip.DAM_SUCCESS;
		trace(ip.UL_TM_F_TRACE, "StatementContext() returned");
	}

	/**
	 * @return the stmtCntxType
	 */
	public StatementContextType getStmtCntxType() {
		return stmtCntxType;
	}


	public SessionContext getSessionContext() {
		return sessionContext;
	}

	/**
	 * @return the iFetchSize
	 */
	public long getFetchSize() {
		return iFetchSize;
	}

	/**
	 * @return the iTopRows
	 */
	public long getTopRows() {
		return iTopRows;
	}

	/**
	 * @return the iMaxRows
	 */
	public long getMaxRows() {
		return iMaxRows;
	}

	/**
	 * @return the iNumResRows
	 */
	public long getNumResRows() {
		return iNumResRows;
	}

	/**
	 */
	public void resetNumResRows() {
		iNumResRows = 0;
	}

	public void incRowCount(int numberOfRowsAdded) {
		if (numberOfRowsAdded > 0) {
			iNumResRows += numberOfRowsAdded;
			iTotalRowCount += numberOfRowsAdded;
		}
	}
	/**
	 * @return the iTotalRowCount
	 */
	public long getTotalRowCount() {
		return iTotalRowCount;
	}

	private int populateConditionList() {
		xo_int pbPartialLists = new xo_int(0);
		setOfConditionLists = new ArrayList<Long>();

		long hset_of_condlist = jdam.dam_getSetOfConditionListsEx(this.dam_hstmt,
				oajava.sql.ip.SQL_SET_CONDLIST_UNION, 0, pbPartialLists);
		long  hcur_condlist = jdam.dam_getFirstCondList(hset_of_condlist);

		do {
			setOfConditionLists.add(hcur_condlist);

			if (hcur_condlist == 0)
				break;

			hcur_condlist = jdam.dam_getNextCondList(hset_of_condlist);
		} while (hcur_condlist != 0);// end of condition lists

		return oajava.sql.ip.IP_SUCCESS;
	}

	private void populateColumnsAndFieldsInUse() {
		
		Map <String, ColumnInfo> columnInfoMap = (LinkedHashMap<String,ColumnInfo>) tableInfo.getColumns();
				
		long hcol; /* DAM_HCOL */
		StringBuffer sColNameBuffer = new StringBuffer(ip.DAM_MAX_ID_LEN + 1);

		hcol = jdam.dam_getFirstCol(this.dam_hstmt, ip.DAM_COL_IN_USE);

		while (hcol != 0) {
			jdam.dam_describeCol(hcol, null, sColNameBuffer, null, null);
			String sColName = sColNameBuffer.toString().toUpperCase();
			ColumnInfo colInfo = columnInfoMap.get(sColName);

			this.colsInUse.put(sColName, hcol);
			this.fieldsInUse.put(sColName, colInfo.getFieldName());

			hcol =  jdam.dam_getNextCol(dam_hstmt);
		}
	}

	/**
	 * @return the setOfConditionLists
	 */
	public List<Long> getSetOfConditionLists() {
		return setOfConditionLists;
	}

	public long getStatementHandle()
	{
		return dam_hstmt;
	}

	public String getTableName()
	{
		return sTableName;
	}

	public Map<String, Long> getColumnsInUse()
	{
		return colsInUse;
	}

	public Map<String, String> getFieldsInUse()
	{
		return fieldsInUse;
	}

	/**
	 * @return the tableInfo
	 */
	public TableInfo getTableInfo() {
		return tableInfo;
	}

	/**
	 * @return the opQNameList
	 */


	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		setStatus(status, null);
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status, String statusMessage) {
		this.status = status;
		this.statusMessage = statusMessage; 
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @return the dam_Logger_Handle
	 */
	public long getDam_Logger_Handle() {
		return dam_Conn_Handle;
	}

	/** This function is used to trace a message in the trace file. The value specified for
	 * maskLevel will determine if the message will be displayed or not at run-time. 
	 * <p>
	 *
	 * @masklevel		Type of the message. The value is a mask of the following flags:
	 *					<ul>
	 *					<li>UL_TM_FATAL - Indicates fatal errors, such as cannot get memory, or invalid address
	 *					<li>UL_TM_SNO - Specify "should not occur" errors.
	 *					<li>UL_TM_PARM - Specify that a bad or missing parameter was passed.
	 *					<li>UL_TM_ERRORS - Specifies general errors
	 *					<li>UL_TM_MAJOR_EV - Indicates a major application event
	 *					<li>UL_TM_MINOR_EV - Indicates minor or secondary application events to give more detail
	 *					<li>UL_TM_INFO - Specifies general information to give details about the events
	 *					<li>UL_TM_F_TRACE - Specify a function call trace (program flow)
	 *					<li>UL_TM_TRIVIA - Specify trivial information of value only to the implementation
	 *					</ul>
	 * @pszMesg			Message to be traced
	 */	
	public void trace(int masklevel, String pszMesg) {
		jdam.trace(dam_Conn_Handle, masklevel, pszMesg + "\n");
	}

	/** Add an error to the error list maintained by the DAM.  An error is added at the statement level.  
	 *	This error string will be returned to the client.  This is the only way to pass specific error information to the client.  
	 *	An IP function returns error by adding an error using this function and then returning DAM_FAILURE. 
	 *	<P>
	 *	Each error code has an error message string associated with it.  This is the string sent to the client by default.  
	 *	You can override this string with your own.  Refer to the OpenAccess Programmer's Reference for a list of defined error codes.
	 * <p>
	 *
	 * @param iErrorIndex	Index into the error message list.
	 * @param iNativeError	Native error as defined by the IP.
	 * @param sErrorText	Text to use in place of the standard text. Data from this buffer is copied into the error queue. 
	 *						Set to NULL to use the default error string.
	 */
	public void addError(int iErrorIndex, int iNativeError, String sErrorText) {
		jdam.dam_addError(dam_Conn_Handle, dam_hstmt, iErrorIndex, iNativeError, sErrorText);
	}

	public void addError(int iErrorIndex, String sErrorText) {
		jdam.dam_addError(dam_Conn_Handle, dam_hstmt, iErrorIndex, 0, sErrorText);
	}


	public int getCurrentInvocationContextIndex() {
		return currentInvocationContextIndex;
	}

	public void setCurrentInvocationContextIndex(int currentInvocationContextIndex) {
		this.currentInvocationContextIndex = currentInvocationContextIndex;
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

}
