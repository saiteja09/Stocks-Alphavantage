package com.ddtek.common.executor;

import java.util.List;

import com.ddtek.common.ip.InvocationContext;
import com.ddtek.common.ip.StatementContext;
import com.ddtek.common.ip.StatementContext.StatementContextType;
import oajava.sql.ip;
import oajava.sql.xo_int;
import oajava.sql.xo_long;

public class QueryExecutor {

	FlowController flow = FlowController.getInstance();
	
	public QueryExecutor() {
	}

	public int execute(StatementContext stmtCtx, xo_long piNumResRows) throws Exception {
		
		return flow.execute(stmtCtx, piNumResRows);
	}
	
	
	public void buildInvocationInfo(StatementContext stmtCtx) throws Exception{
		
		stmtCtx.trace(ip.UL_TM_F_TRACE, "buildInvocationInfo() called");
		
		List<InvocationContext> invocationContextList = stmtCtx.getInvocationContextList();
		
		if (stmtCtx.getStmtCntxType() == StatementContextType.QUERY_TYPE) {
			List<Long> setOfConditionLists = stmtCtx.getSetOfConditionLists();
			for (Long hCondList : setOfConditionLists) {
				InvocationContext invocation = new InvocationContext(stmtCtx, hCondList);				
				invocationContextList.add(invocation);
			}
		}
		
		else {
			throw new Exception("Invalid statement type to execute.");
		}
		
		stmtCtx.trace(ip.UL_TM_F_TRACE, "buildInvocationInfo() returned");
	}


}
