package com.ddtek.common.ip;


import java.util.HashMap;
import java.util.Map;

import com.ddtek.stocks.connection.ConnectionHelper;
import com.ddtek.common.executor.QueryExecutor;
import oajava.sql.jdam;
import oajava.sql.oa_ds_info;
import oajava.sql.xo_int;
import oajava.sql.xo_long;

public class RestIP implements oajava.sql.ip {

	private SessionContext sessionContext;
	private RestIPHelper ipConfig = new RestIPHelper();
	private Map<Long, StatementContext> stmtCxtList = new HashMap<Long, StatementContext>();
	private long m_tmHandle = 0;

	@Override
	public int ipConnect(long tmHandle, long dam_hdbc, String sDataSourceName, String sUserName, String sPassword,
			String sCurrentCatalog, String sIPProperties, String sIPCustomProperties) {

		int status = IP_FAILURE;
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		if (cl == null) {
			ClassLoader currentClassesLoader = getClass().getClassLoader();
			Thread.currentThread().setContextClassLoader(currentClassesLoader);
		}
		
		m_tmHandle = tmHandle;
		jdam.trace(m_tmHandle, UL_TM_F_TRACE, "ipConnect() called \n");
		// Initialize SessionContext - Connection Level Data structure
		try {
			sessionContext = new SessionContext(tmHandle);
			sessionContext.init(dam_hdbc, sDataSourceName, sUserName, sPassword, sCurrentCatalog, sIPProperties,
					sIPCustomProperties);
					
			// Use INDEX information only for catalog, so ignore optimization on index.
			jdam.dam_setOption(DAM_CONN_OPTION, dam_hdbc, DAM_CONN_OPTION_INDEX_OPTIMIZATION, DAM_INDEX_IGNORE_ALL);

			
			populateCatalogInformation();
			
			ConnectionHelper con = new ConnectionHelper();
			con.connect(sessionContext);
			status = IP_SUCCESS;
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			jdam.dam_addError(dam_hdbc, 0, DAM_IP_ERROR, 0, errorMessage);
			jdam.trace(m_tmHandle, UL_TM_F_TRACE, errorMessage + " \n");
			status = IP_FAILURE;
		} finally {
			jdam.trace(m_tmHandle, UL_TM_F_TRACE, "ipConnect() returned \n");
			Thread.currentThread().setContextClassLoader(cl);
		}
		return status;
	}


	private void populateCatalogInformation() {
		// TODO .. Obtain these names from Parameters.
		sessionContext.setSessionProperty(RestIPConstants.SQL_DBMS_NAME_PROP, "Stocks");
		sessionContext.setSessionProperty(RestIPConstants.CATALOG_NAME_PROP, "AlphaVantage");
		sessionContext.setSessionProperty(RestIPConstants.SCHEMA_NAME_PROP, "Stocks");
		sessionContext.setSessionProperty(RestIPConstants.SQL_DBMS_VER_PROP, "1.0");

	}
	

	@Override
	public int ipExecute(long dam_hstmt, int iStmtType, long hSearchCol, xo_long piNumResRows) {

		int status = IP_FAILURE;
		
		jdam.trace(m_tmHandle, UL_TM_F_TRACE, "ipExecute() called \n");
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		if (cl == null) {
			ClassLoader currentClassesLoader = getClass().getClassLoader();
			Thread.currentThread().setContextClassLoader(currentClassesLoader);
		}
		

		try {
			switch (iStmtType) {
			case DAM_SELECT:
				try {

					StatementContext stmtCtx = new StatementContext(sessionContext, dam_hstmt);
					stmtCxtList.put(dam_hstmt, stmtCtx);
					QueryExecutor queryExecutor = new QueryExecutor();
					queryExecutor.buildInvocationInfo(stmtCtx);
					status = queryExecutor.execute(stmtCtx, piNumResRows);
				} catch (Exception e) {
					status = handleStatementExecutionError(dam_hstmt, e);
				}
				break;

			case DAM_FETCH:
				
				try {

					StatementContext stmtCtx = stmtCxtList.get(dam_hstmt);
					if (stmtCtx == null) {
						stmtCtx = new StatementContext(sessionContext, dam_hstmt);
					}
					QueryExecutor statementExecutor = new QueryExecutor();
					status = statementExecutor.execute(stmtCtx, piNumResRows);
				} catch (Exception e) {
					status = handleStatementExecutionError(dam_hstmt, e);
				}
				break;

			case DAM_CLOSE:
				stmtCxtList.remove(dam_hstmt);
				status = IP_SUCCESS;
				break;

			default:
				String errorMessage = "Query Execution Failed. Statement type " + iStmtType + " is not supported.";

				jdam.trace(m_tmHandle, UL_TM_ERRORS, errorMessage + "\n");
				jdam.dam_addError(0, dam_hstmt, DAM_IP_ERROR, 0, errorMessage);
				break;
			}
		} finally {		
			jdam.trace(m_tmHandle, UL_TM_F_TRACE, "ipExecute() returned \n");
			Thread.currentThread().setContextClassLoader(cl);		
		}

		return status;
	}
	private int handleStatementExecutionError(long dam_hstmt, Exception e) {
		int status;
		stmtCxtList.remove(dam_hstmt);
		status = IP_FAILURE;
		String errorMessage = "Query Execution Failed. " + e.getMessage();
		jdam.trace(m_tmHandle, UL_TM_F_TRACE, errorMessage + " \n");
		jdam.dam_addError(0, dam_hstmt, DAM_IP_ERROR, 0, errorMessage);
		return status;
	}

	@Override
	public String ipGetInfo(int iInfoType) {
		return ipConfig.getIPInfo(iInfoType, sessionContext);
	}


	@Override
	public int ipGetSupport(int iSupportType) {
		return (RestIPHelper.IP_SUPPORT_ARRAY[iSupportType]);
	}

	@Override
	public int ipNative(long arg0, int arg1, String arg2, xo_long arg3) {
		return IP_FAILURE;
	}

	@Override
	public int ipPrivilege(int arg0, String arg1, String arg2, String arg3, String arg4) {
		return IP_FAILURE;
	}

	@Override
	public int ipProcedure(long arg0, int arg1, xo_long arg2) {
		return IP_FAILURE;
	}

	@Override
	public int ipProcedureDynamic(long arg0, int arg1, xo_long arg2) {
		return IP_FAILURE;
	}

	@Override
	public int ipSchema(long dam_hdbc, long pMemTree, int iType, long pList, Object pSearchObj) {

		jdam.trace(m_tmHandle, UL_TM_F_TRACE, "ipSchema() called \n");
		try {
			switch (iType) {
			case DAMOBJ_TYPE_CATALOG:
				ipConfig.handleDAMSchemaOrCatalog(pMemTree, iType, pList, pSearchObj, false, sessionContext);
				break;
			case DAMOBJ_TYPE_SCHEMA:
				ipConfig.handleDAMSchemaOrCatalog(pMemTree, iType, pList, pSearchObj, true, sessionContext);
				break;
			case DAMOBJ_TYPE_TABLETYPE:
				ipConfig.handleTableType(pMemTree, iType, pList, pSearchObj);
				break;
			case DAMOBJ_TYPE_TABLE:
				ipConfig.handleTable(pMemTree, iType, pList, pSearchObj, sessionContext);
				break;
			case DAMOBJ_TYPE_COLUMN:
				ipConfig.handleColumn(pMemTree, iType, pList, pSearchObj, sessionContext);
				break;
			case DAMOBJ_TYPE_STAT:
				ipConfig.handleStat(pMemTree, iType, pList, pSearchObj, sessionContext);
				break;
			case DAMOBJ_TYPE_FKEY:
				ipConfig.handleFkey(pMemTree, iType, pList, pSearchObj, sessionContext);
				break;
			case DAMOBJ_TYPE_PKEY:
				ipConfig.handlePKey(pMemTree, iType, pList, pSearchObj, sessionContext);
				break;
			case DAMOBJ_TYPE_PROC:
				break;
			case DAMOBJ_TYPE_PROC_COLUMN:
				break;
			default:
				break;
			}

		} catch (Exception e) {
			jdam.dam_addError(0, 0, DAM_IP_ERROR, 0, e.getMessage());
			jdam.trace(m_tmHandle, UL_TM_F_TRACE, e.getMessage() + " \n");
			return IP_FAILURE;
		}
		jdam.trace(m_tmHandle, UL_TM_F_TRACE, "ipSchema() returned \n");
		return IP_SUCCESS;
	}


	/********************************************************************************************
	 * Method: ipGetDSInfo() Description: returns DS information Return: Object[]
	 *********************************************************************************************/
	public oa_ds_info[] ipGetDSInfo() {
		/* DS INFO */
		oa_ds_info[] dsInfo = new oa_ds_info[122];
		int j = 0;
		jdam.trace(m_tmHandle, UL_TM_F_TRACE, "ipGetDSInfo called\n");

		dsInfo[j++] = new oa_ds_info("SQL_ACTIVE_STATEMENTS", 1, 0, DAMOBJ_NOTSET, "",
				"The maximum number of statements supported");
		dsInfo[j++] = new oa_ds_info("SQL_ROW_UPDATES", 11, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "N",
				"Y if driver can detect row changes between multiple fetches of");
		dsInfo[j++] = new oa_ds_info("SQL_ODBC_SQL_CONFORMANCE", 15, 0, DAMOBJ_NOTSET, "", "SQL Grammar supported by the driver");
		dsInfo[j++] = new oa_ds_info("SQL_SEARCH_PATTERN_ESCAPE", 14, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "\\", "");

		String DBMS_Name = (String) sessionContext.getSessionProperties().get(RestIPConstants.SQL_DBMS_NAME_PROP);
		if (DBMS_Name == null || DBMS_Name.length() == 0)
			DBMS_Name = RestIPConstants.DEFAULT_SQL_DBMS_NAME;
		dsInfo[j++] = new oa_ds_info(RestIPConstants.SQL_DBMS_NAME_PROP, 17, DAMOBJ_NOTSET, DAMOBJ_NOTSET, DBMS_Name, "");

		String DBMS_Ver = (String) sessionContext.getSessionProperties().get(RestIPConstants.SQL_DBMS_VER_PROP);
		if (DBMS_Ver == null || DBMS_Ver.length() == 0)
			DBMS_Ver = RestIPConstants.DEFAULT_SQL_DBMS_VER;
		dsInfo[j++] = new oa_ds_info(RestIPConstants.SQL_DBMS_VER_PROP, 18, DAMOBJ_NOTSET, DAMOBJ_NOTSET, DBMS_Ver,
				"Version of current DBMS product of the form ##.## ex: 01.00");
		dsInfo[j++] = new oa_ds_info("SQL_ACCESSIBLE_TABLES", 19, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "Y",
				"Y if user is guaranted access to all tables returned by SQL_TAB");
		dsInfo[j++] = new oa_ds_info("SQL_ACCESSIBLE_PROCEDURES", 20, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "N",
				"Y if dat source supports procedures");
		dsInfo[j++] = new oa_ds_info("SQL_PROCEDURES", 21, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "N",
				"Y if user can execute returned by SQL_PROCEDURES");
		dsInfo[j++] = new oa_ds_info("SQL_CONCAT_NULL_BEHAVIOR", 22, 0, DAMOBJ_NOTSET, "1",
				"0 if string+NULL=NULL / 1 if result is string");
		dsInfo[j++] = new oa_ds_info("SQL_DATA_SOURCE_READ_ONLY", 25, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "Y",
				"Y if data source set to read only");
		dsInfo[j++] = new oa_ds_info("SQL_EXPRESSIONS_IN_ORDERBY", 27, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "Y",
				"Y if driver supports ORDER BY expression");
		dsInfo[j++] = new oa_ds_info("SQL_IDENTIFIER_CASE", 28, 4, DAMOBJ_NOTSET, "",
				"1= case insensitive(stored upper), 2 = lower case, 3 = case sensitive, stored mixed, 4 = case    insensitive, stored mixed");
		dsInfo[j++] = new oa_ds_info("SQL_IDENTIFIER_QUOTE_CHAR", 29, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "\"",
				"the character string used to surround a delimiter identifier. b");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_COLUMN_NAME_LEN", 30, 128, DAMOBJ_NOTSET, "",
				"Max length of a column name in the data source");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_OWNER_NAME_LEN", 32, 128, DAMOBJ_NOTSET, "",
				"Max length of an owner name in the data source");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_PROCEDURE_NAME_LEN", 33, 128, DAMOBJ_NOTSET, "",
				"Max length of a procedure name in the data source");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_QUALIFIER_NAME_LEN", 34, 128, DAMOBJ_NOTSET, "",
				"Max length of a qualifier name in the data source");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_TABLE_NAME_LEN", 35, 128, DAMOBJ_NOTSET, "",
				"Max length of a table name in the data source");
		dsInfo[j++] = new oa_ds_info("SQL_MULT_RESULT_SETS", 36, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "Y",
				"Y if databases support multiple result sets");
		dsInfo[j++] = new oa_ds_info("SQL_MULTIPLE_ACTIVE_TXN", 37, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "Y",
				"Y if transact. on multiple connection are allowed");
		dsInfo[j++] = new oa_ds_info("SQL_OUTER_JOINS", 38, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "Y",
				"Y if data source supports outer joins.");
		dsInfo[j++] = new oa_ds_info("SQL_PROCEDURE_TERM", 40, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "procedure",
				"the vendor's name for procedure");
		dsInfo[j++] = new oa_ds_info("SQL_QUALIFIER_NAME_SEPARATOR", 41, DAMOBJ_NOTSET, DAMOBJ_NOTSET, ".",
				"the character string defines as a separator between the qualifi");
		dsInfo[j++] = new oa_ds_info("SQL_TABLE_TERM", 45, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "table", "the vendor's name for table");
		dsInfo[j++] = new oa_ds_info("SQL_TXN_CAPABLE", 46, 1, DAMOBJ_NOTSET, "",
				"0= transact not supported. 1 = transact contains only DML state, 2=DML or DDL");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_FUNCTIONS", 48, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_NUMERIC_FUNCTIONS", 49, DAMOBJ_NOTSET, 16777215, "",
				"bitmask enumerating scalar numeric functions supported by the d");
		dsInfo[j++] = new oa_ds_info("SQL_STRING_FUNCTIONS", 50, DAMOBJ_NOTSET, 16547839, "",
				"bitmask enumerating scalar string functions supported by the dr");
		dsInfo[j++] = new oa_ds_info("SQL_SYSTEM_FUNCTIONS", 51, DAMOBJ_NOTSET, 3, "",
				"bitmask enumerating scalar system functions supported by the dr");
		dsInfo[j++] = new oa_ds_info("SQL_TIMEDATE_FUNCTIONS", 52, DAMOBJ_NOTSET, 630783, "",
				"bitmask enumerating scalar time and date functions supported by");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_BIGINT", 53, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_BINARY", 54, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_BIT", 55, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_CHAR", 56, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_DATE", 57, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_DECIMAL", 58, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_DOUBLE", 59, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_FLOAT", 60, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_INTEGER", 61, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_LONGVARCHAR", 62, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_NUMERIC", 63, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_REAL", 64, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_SMALLINT", 65, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_TIME", 66, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_TIMESTAMP", 67, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_TINYINT", 68, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_VARBINARY", 69, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_VARCHAR", 70, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CONVERT_LONGVARBINARY", 71, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_ODBC_SQL_OPT_IEF", 73, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "N",
				"Specifies whether the data source supports Integrity Enhancement");
		dsInfo[j++] = new oa_ds_info("SQL_TXN_ISOLATION_OPTION", 72, DAMOBJ_NOTSET, 7, "",
				"bitmask enumarating transaction isolation levels.");
		dsInfo[j++] = new oa_ds_info("SQL_CORRELATION_NAME", 74, 2, DAMOBJ_NOTSET, "",
				"16 bits integer indicating if correlation names are supported");
		dsInfo[j++] = new oa_ds_info("SQL_NON_NULLABLE_COLUMNS", 75, 1, DAMOBJ_NOTSET, "",
				"16 bit int specifying whether the data source supports non null");
		dsInfo[j++] = new oa_ds_info("SQL_GETDATA_EXTENSIONS", 81, DAMOBJ_NOTSET, 3, "",
				"32 bit bitamask enumarating extensions to SQLGetData");
		dsInfo[j++] = new oa_ds_info("SQL_NULL_COLLATION", 85, 1, DAMOBJ_NOTSET, "", "Specifies where null are sorted in a list.");
		dsInfo[j++] = new oa_ds_info("SQL_ALTER_TABLE", 86, DAMOBJ_NOTSET, 37867, "",
				"Bitmask enumerating supported ALTER TABLE clauses.");
		dsInfo[j++] = new oa_ds_info("SQL_COLUMN_ALIAS", 87, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "Y",
				"Y if driver supports column alias");
		dsInfo[j++] = new oa_ds_info("SQL_GROUP_BY", 88, 2, DAMOBJ_NOTSET, "",
				"16 bit int specifying the relationship between col in group by. 2=SQL_GB_GROUP_BY_CONTAINS_SELECT");
		dsInfo[j++] = new oa_ds_info("SQL_KEYWORDS", 89, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "HINT,IDENTIFIED",
				"List of source specific Keywords");
		dsInfo[j++] = new oa_ds_info("SQL_ORDER_BY_COLUMNS_IN_SELECT", 90, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "N",
				"Y if the columns in the ORDER BY stmt clause must be in the sel");
		dsInfo[j++] = new oa_ds_info("SQL_OWNER_USAGE", 91, DAMOBJ_NOTSET, 15, "",
				"Enumarates the statements in which owners can be used.");
		dsInfo[j++] = new oa_ds_info("SQL_QUALIFIER_USAGE", 92, DAMOBJ_NOTSET, 7, "",
				"bitmask enumerating the statements in which a qualifier can be");
		dsInfo[j++] = new oa_ds_info("SQL_QUOTED_IDENTIFIER_CASE", 93, 4, DAMOBJ_NOTSET, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_SPECIAL_CHARACTERS", 94, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "*()(}|:;",
				"List of special characters that can be used in an object name");
		dsInfo[j++] = new oa_ds_info("SQL_SUBQUERIES", 95, DAMOBJ_NOTSET, 31, "",
				"bitmask enumerating predicates that support subqueries");
		dsInfo[j++] = new oa_ds_info("SQL_UNION", 96, DAMOBJ_NOTSET, 3, "",
				"Bitmask enumarating the support for the union clause");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_COLUMNS_IN_GROUP_BY", 97, 0, DAMOBJ_NOTSET, "",
				"Max number of columns in a GROUP BY stmt. 0 if unknown");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_COLUMNS_IN_INDEX", 98, 0, DAMOBJ_NOTSET, "",
				"Max number of columns in an index. 0 if unknown");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_COLUMNS_IN_ORDER_BY", 99, 0, DAMOBJ_NOTSET, "",
				"Max number of columns in an ORDER BY clause. 0 if unknown");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_COLUMNS_IN_SELECT", 100, 0, DAMOBJ_NOTSET, "",
				"Max column in SELECT stmt. 0 if unknown");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_COLUMNS_IN_TABLE", 101, 0, DAMOBJ_NOTSET, "", "Max number of tables in a table");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_INDEX_SIZE", 102, DAMOBJ_NOTSET, 0, "",
				"Max number of bytes allowed in the combined field of an index.");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_ROW_SIZE_INCLUDES_LONG", 103, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "N",
				"Y if MAX_ROW_SIZE includes the length of all long Data types.");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_ROW_SIZE", 104, DAMOBJ_NOTSET, 0, "",
				"Max size of a row in a datasource. This limitation comes from t");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_STATEMENT_LEN", 105, DAMOBJ_NOTSET, 32768, "", "Max length of an SQL stmt");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_TABLES_IN_SELECT", 106, 0, DAMOBJ_NOTSET, "",
				"Max table number in Select stmt. 0 if unknown");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_CHAR_LITERAL_LEN", 108, DAMOBJ_NOTSET, 8192, "",
				"32 bits int specifying max length of a character literal in a S");
		dsInfo[j++] = new oa_ds_info("SQL_TIMEDATE_ADD_INTERVALS", 109, DAMOBJ_NOTSET, 0, "",
				"bitmask enumerating the timestamp intervals supported in TIMEST");
		dsInfo[j++] = new oa_ds_info("SQL_TIMEDATE_DIFF_INTERVALS", 110, DAMOBJ_NOTSET, 0, "",
				"bitmask enumerating the timestamp intervals supported in TIMEST");
		dsInfo[j++] = new oa_ds_info("SQL_MAX_BINARY_LITERAL_LEN", 112, DAMOBJ_NOTSET, 8192, "",
				"32 bits int specifying max length of a binary literal");
		dsInfo[j++] = new oa_ds_info("SQL_LIKE_ESCAPE_CLAUSE", 113, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "Y",
				"Y if data source supports escape character in LIKE clause");
		dsInfo[j++] = new oa_ds_info("SQL_QUALIFIER_LOCATION", 114, 1, DAMOBJ_NOTSET, "",
				"indicates the position of the qualifier in a qualified table name");
		dsInfo[j++] = new oa_ds_info("SQL_OJ_CAPABILITIES", 115, DAMOBJ_NOTSET, 0x49, "",
				" bitmask enumerating the types of outer joins supported by the driver ");
		dsInfo[j++] = new oa_ds_info("SQL_ALTER_DOMAIN", 117, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_SQL_CONFORMANCE", 118, DAMOBJ_NOTSET, 1, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_DATETIME_LITERALS", 119, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_BATCH_ROW_COUNT", 120, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_BATCH_SUPPORT", 121, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CREATE_ASSERTION", 127, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CREATE_CHARACTER_SET", 128, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CREATE_COLLATION", 129, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CREATE_DOMAIN", 130, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CREATE_SCHEMA", 131, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CREATE_TABLE", 132, DAMOBJ_NOTSET, 1, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CREATE_TRANSLATION", 133, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_CREATE_VIEW", 134, DAMOBJ_NOTSET, 1, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_DROP_ASSERTION", 136, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_DROP_CHARACTER_SET", 137, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_DROP_COLLATION", 138, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_DROP_DOMAIN", 139, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_DROP_SCHEMA", 140, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_DROP_TABLE", 141, DAMOBJ_NOTSET, 1, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_DROP_TRANSLATION", 142, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_DROP_VIEW", 143, DAMOBJ_NOTSET, 1, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_INDEX_KEYWORDS", 148, DAMOBJ_NOTSET, 3, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_INFO_SCHEMA_VIEWS", 149, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_SQL92_DATETIME_FUNCTIONS", 155, DAMOBJ_NOTSET, 7, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_SQL92_FOREIGN_KEY_DELETE_RULE", 156, DAMOBJ_NOTSET, 2, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_SQL92_FOREIGN_KEY_UPDATE_RULE", 157, DAMOBJ_NOTSET, 2, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_SQL92_GRANT", 158, DAMOBJ_NOTSET, 3184, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_SQL92_NUMERIC_VALUE_FUNCTIONS", 159, DAMOBJ_NOTSET, 63, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_SQL92_PREDICATES", 160, DAMOBJ_NOTSET, 16135, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_SQL92_RELATIONAL_JOIN_OPERATORS", 161, DAMOBJ_NOTSET, 592, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_SQL92_REVOKE", 162, DAMOBJ_NOTSET, 3184, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_SQL92_ROW_VALUE_CONSTRUCTOR", 163, DAMOBJ_NOTSET, 11, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_SQL92_STRING_FUNCTIONS", 164, DAMOBJ_NOTSET, 238, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_SQL92_VALUE_EXPRESSIONS", 165, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_AGGREGATE_FUNCTIONS", 169, DAMOBJ_NOTSET, 127, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_DDL_INDEX", 170, DAMOBJ_NOTSET, 0, "", "");
		dsInfo[j++] = new oa_ds_info("SQL_INSERT_STATEMENT", 172, DAMOBJ_NOTSET, 3, "", "");
		dsInfo[j] = new oa_ds_info("SQL_COLLATION_SEQ", 10004, DAMOBJ_NOTSET, DAMOBJ_NOTSET, "ISO 8859-1",
				"The name of the collation sequence for the default character set (for example, 'ISO 8859-1' or EBCDIC). ");

		jdam.trace(sessionContext.getDam_Conn_Handle(), UL_TM_F_TRACE, "ipGetDSInfo return\n");

		return dsInfo;
	}
	
	//************************* Unimplemented methods **********************************//
	
	
	@Override
	public int ipDCL(long arg0, int arg1, xo_long arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int ipDDL(long arg0, int arg1, xo_long arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int ipDisconnect(long arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int ipEndTransaction(long arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int ipSchemaEx(long arg0, long arg1, int arg2, long arg3, Object arg4) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int ipSetInfo(int arg0, String arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int ipStartTransaction(long arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
