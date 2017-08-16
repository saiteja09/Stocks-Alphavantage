package com.ddtek.common.dataprocessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;

import com.ddtek.common.executor.Request;
import com.ddtek.common.executor.Response;
import com.ddtek.common.ip.InvocationContext;
import com.ddtek.common.ip.StatementContext;
import com.ddtek.common.schema.ColumnInfo;
import com.ddtek.common.schema.TableInfo;
import oajava.sql.ip;
import oajava.sql.jdam;

public abstract class DataProcessor {
	
	
	public abstract Request buildRequest(StatementContext ctx, InvocationContext invCtx) throws Exception;
	
	public abstract ArrayList<HashMap<String, String>> parseJSONResponse(StatementContext ctx, Response res, InvocationContext invCtx) throws Exception;
	
	public int processResponse(StatementContext stmtCtx, InvocationContext invCtx, Request rqst, Response response) throws Exception {
	
		stmtCtx.trace(ip.UL_TM_F_TRACE, "processResponse() called");
	
		if ( response == null )
			return 0;
			
		int httpStatusCode = response.getStatusCode();
		
		if ( 200 <= httpStatusCode  && httpStatusCode <= 206 ){
		
			ArrayList<HashMap<String, String>> list = null;
			try {
				list = parseJSONResponse(stmtCtx,response, invCtx);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
	
			for (int i = 0; i < list.size(); i++) {
				addResultRow(list.get(i), stmtCtx, invCtx);
			}
			
		}
		else
			handleExceptions(stmtCtx, response);
					
		stmtCtx.trace(ip.UL_TM_F_TRACE, "processResponse() returned");
		return 0;	
		
	}
	
	/**
	 * This method throws exception when an invalid HTTP status code is encountered.
	 * @param stmtCtx 

	 * @param response
	 * @throws Exception
	 */
	public void handleExceptions(StatementContext stmtCtx, Response response ) throws Exception {
		
		stmtCtx.trace(ip.UL_TM_F_TRACE, "handleExceptions() called");
		
		int httpStatusCode = response.getStatusCode();
				
		switch ( httpStatusCode){	
				
		case 404:
		
			// Resource not found. This case will arise when the specified resource is not found. In relational perspective, this should not be reported as error. 
			//This should be treated as query without a matching results or Zero results.			
			response.setStatus(ip.DAM_SUCCESS);
			response.setStatusCode(200);
			break;
					
			
		default:
			String errorMsg = httpStatusCode + ", " + HttpStatusCodeMapper.get(httpStatusCode);
			stmtCtx.addError(ip.DAM_IP_ERROR, errorMsg);
			stmtCtx.trace(ip.UL_TM_ERRORS, errorMsg + "\n");
			throw new Exception(errorMsg);
			
		}
		
	}		 

	/**
	 * Builds a row and adds it to the OpenAccess. Before adding the row,
	 * the row is validated against the set of conditions and is added
	 * only if all the conditions are satisfied.
	 * 
	 * @param c
	 * @param stmtCntx
	 * @param currInvocationContext
	 * @throws Exception
	 */
	public void addResultRow ( HashMap<String, String> c, StatementContext stmtCntx, InvocationContext currInvocationContext) throws Exception{

		long hStmt = stmtCntx.getStatementHandle();
		Map<String, Long> columnsInUse = stmtCntx.getColumnsInUse();
		TableInfo tableInfo = stmtCntx.getTableInfo();
		
		Map <String, ColumnInfo> columnInfoMap = (LinkedHashMap<String,ColumnInfo>) tableInfo.getColumns();
		Map<String, Object> eqlConditionsInUse = currInvocationContext.getEqlConditionsInUse();
		
		long hrow = jdam.dam_allocRow(hStmt);

		int status = ip.DAM_FAILURE;
		for (Map.Entry<String, Long> columnEntry : columnsInUse.entrySet()) {
			
			String columnName = columnEntry.getKey();
			long hColumn = columnEntry.getValue().longValue();
			ColumnInfo columnInfo = columnInfoMap.get(columnName);
			
			short columnDataType = columnInfo.getDataType();
			
			String columnValue = c.get(columnInfo.getFieldName());
			
			if ( columnValue != null){
				
				try {
					// pre-process the column value to check NULL string
					columnValue = columnValue.trim();
					if (columnValue.isEmpty() || columnValue.equalsIgnoreCase("NULL")) {
						columnValue = null;
					}
					
					
					else if (stmtCntx.getDataConverter() != null) {
						columnValue = stmtCntx.getDataConverter().ConvertValueForOA(columnValue, columnInfo);
					}
				} catch (Exception e) {
					String errorMsg = "Issue with data conversion, " + e.getMessage();
					stmtCntx.addError(ip.DAM_IP_ERROR, errorMsg);
					stmtCntx.trace(ip.UL_TM_ERRORS, errorMsg + "\n");
				}
			} else if (eqlConditionsInUse.containsKey(columnName)){
				columnValue = eqlConditionsInUse.get(columnName).toString();
			}
			
			switch (columnDataType) {
			case ip.XO_TYPE_CHAR:
			case ip.XO_TYPE_VARCHAR:
			case ip.XO_TYPE_LONGVARCHAR:
			case ip.XO_TYPE_WCHAR:
			case ip.XO_TYPE_WVARCHAR:
			case ip.XO_TYPE_WLONGVARCHAR:
				
				status = jdam.dam_addWCharValToRow(hStmt, hrow, hColumn, columnValue, (columnValue != null) ? ip.XO_NTS : ip.XO_NULL_DATA);
				break;
				
			case ip.XO_TYPE_NUMERIC:
			case ip.XO_TYPE_DECIMAL:
			case ip.XO_TYPE_INTEGER:
			case ip.XO_TYPE_FLOAT:
			case ip.XO_TYPE_REAL:
			case ip.XO_TYPE_DOUBLE:
			case ip.XO_TYPE_DATE:
			case ip.XO_TYPE_TIME:
			case ip.XO_TYPE_TIMESTAMP:
			case ip.XO_TYPE_DATE_TYPE:
			case ip.XO_TYPE_TIME_TYPE:
			case ip.XO_TYPE_TIMESTAMP_TYPE:
			case ip.XO_TYPE_VARBINARY:
			case ip.XO_TYPE_BINARY:
			case ip.XO_TYPE_LONGVARBINARY:
			case ip.XO_TYPE_BIGINT:
			case ip.XO_TYPE_TINYINT:
			case ip.XO_TYPE_BIT:
			
				status = jdam.dam_addCharValToRow(hStmt, hrow, hColumn, columnValue,   (columnValue != null) ? ip.XO_NTS : ip.XO_NULL_DATA);
				break;

			default:
				break;
			}
			
			if (status != ip.DAM_SUCCESS) {
				stmtCntx.setStatus(status);
				return;
			}

		} // done with row building

		Iterator<Long> conditionListItr = stmtCntx.getSetOfConditionLists().iterator();
		long currConditionListID = currInvocationContext.getConditionListID();
		boolean addRowToTable = true; // no condition lists

		while (conditionListItr.hasNext())
		{
			long hcur_condlist = conditionListItr.next().longValue();
			int isTargetRow = ip.DAM_FALSE;

			if (hcur_condlist == 0) break;

			isTargetRow = jdam.dam_isTargetRowForConditionList(hStmt, hrow, hcur_condlist);

			// search conditions met
			if (isTargetRow == ip.DAM_TRUE) {
				// but not on current condition list
				if (hcur_condlist != currConditionListID)
					addRowToTable = false;

				break;
			}

			// reached current condition list
			if (hcur_condlist == currConditionListID) {
				// but search condition not met
				if (isTargetRow != ip.DAM_TRUE)
					addRowToTable = false;

				break;
			}
		}

		if (addRowToTable == true) {
			jdam.dam_addRowToTable(hStmt, hrow);
			stmtCntx.incRowCount(1);
		}
		else {
			jdam.dam_freeRow(hrow);
		}
	}
}
