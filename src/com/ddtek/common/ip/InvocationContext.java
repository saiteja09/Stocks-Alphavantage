package com.ddtek.common.ip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oajava.sql.ip;
import oajava.sql.jdam;


public class InvocationContext {

	int invocationStatus;
	StatementContext stmtCtx;
	long ConditionListID;
	List<OAConditionInfo> listOfConditions = new ArrayList<OAConditionInfo>();
	Map<String, Object> eqlConditionsInUse = new HashMap<String, Object>();
	
	int requestType;
	public int getRequestType() {
		return requestType;
	}

	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}

	
	public int getInvocationStatus() {
		return invocationStatus;
	}

	public void setInvocationStatus(int invocationStatus) {
		this.invocationStatus = invocationStatus;
	}

	
	public InvocationContext(StatementContext stmtCtx, long conditionListID) {
		super();
		this.stmtCtx = stmtCtx;
		this.ConditionListID = conditionListID;
		populateConditionList();
		populateEqualConnditions();
	}

	private void populateEqualConnditions() {
		stmtCtx.trace(ip.UL_TM_F_TRACE, "populateEqualConnditions() called");
		for (OAConditionInfo conditionInfo : listOfConditions) {
			if (conditionInfo.getOperator() == oajava.sql.ip.SQL_OP_EQUAL) {
				eqlConditionsInUse.put(conditionInfo.getColumnName(), conditionInfo.getValue());
			}
		}
		stmtCtx.trace(ip.UL_TM_F_TRACE, "populateEqualConnditions() returned");
	}

	private void populateConditionList() {
		stmtCtx.trace(ip.UL_TM_F_TRACE, "populateConditionList() called");
		if (this.ConditionListID == 0)
			return;
		long hStmt = this.stmtCtx.getStatementHandle();
		long hcond = jdam.dam_getFirstCond(hStmt,
				this.ConditionListID);

		while (hcond != 0) {
			OAConditionInfo condInfo = new OAConditionInfo(this.stmtCtx, hcond);
			listOfConditions.add(condInfo);
			hcond = jdam.dam_getNextCond(hStmt,
					this.ConditionListID);
		}// end of while on condition list
		stmtCtx.trace(ip.UL_TM_F_TRACE, "populateConditionList() returned");
	}

	/**
	 * @return the StatementContext
	 */
	public StatementContext getStatementContext() {
		return stmtCtx;
	}
	
	/**
	 * @return the eqlConditionsInUse
	 */
	public Map<String, Object> getEqlConditionsInUse() {
		return eqlConditionsInUse;
	}

	/**
	 * @return the conditionListID
	 */
	public long getConditionListID() {
		return ConditionListID;
	}

	/**
	 * @return the listOfConditions
	 */
	public List<OAConditionInfo> getListOfConditions() {
		return listOfConditions;
	}
	
	
	
}
