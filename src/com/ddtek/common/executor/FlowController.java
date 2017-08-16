package com.ddtek.common.executor;

import java.util.List;

import com.ddtek.common.cxf.client.CXFClient;
import com.ddtek.common.dataprocessor.CompositeDataProcessor;
import com.ddtek.common.dataprocessor.DataProcessor;
import com.ddtek.common.ip.RestIP;
import com.ddtek.common.ip.InvocationContext;
import com.ddtek.common.ip.SessionContext;
import com.ddtek.common.ip.StatementContext;
import com.ddtek.stocks.schema.SchemaDataProcessor;
import com.ddtek.stocks.connection.RequestProcessor;

import oajava.sql.ip;
import oajava.sql.jdam;
import oajava.sql.xo_int;
import oajava.sql.xo_long;

public class FlowController {
	
	private static FlowController flowController = new FlowController();

	public static FlowController getInstance(){
		return flowController;
	}


	CXFClient invoker = new CXFClient();

	public int execute(StatementContext stmtCtx, xo_long piNumResRows ) throws Exception {

		int status = -1;
		InvocationContext currInvocationContext = null;


		List<InvocationContext> invocationContextList = stmtCtx.getInvocationContextList();

		if (invocationContextList.size() == 0) {
			stmtCtx.setStatus(1, "Issue with InvocationContextList of size zero.");
			return status;
		}


		Response response = null;

		do {

			int invocationContextIndx = stmtCtx.getCurrentInvocationContextIndex();

			// On first invocation
			if (invocationContextIndx == -1) {
				stmtCtx.setCurrentInvocationContextIndex(++invocationContextIndx);
				currInvocationContext = invocationContextList.get(invocationContextIndx);
			}
			// Consequent invocations
			else{
				currInvocationContext = invocationContextList.get(invocationContextIndx);
				status = currInvocationContext.getInvocationStatus();


				response = invokeService ( currInvocationContext, stmtCtx);			

				status = response.getStatus();

				currInvocationContext.setInvocationStatus(status);

				if (status == RestIP.DAM_SUCCESS)
				{
					stmtCtx.setCurrentInvocationContextIndex(++invocationContextIndx);

					if (invocationContextIndx >= invocationContextList.size()) {
						break;
					}
					currInvocationContext = invocationContextList.get(invocationContextIndx);
				}
				else if(status == RestIP.DAM_FAILURE )
				{

					break;
				}

				else  {
					long maxRows = stmtCtx.getMaxRows();
					long topRows = stmtCtx.getTopRows();
					long totalRowCount = stmtCtx.getTotalRowCount();
					long rowCount = stmtCtx.getNumResRows();
					long fetchSize = stmtCtx.getFetchSize();

					piNumResRows.setVal(stmtCtx.getNumResRows());

					// check for MAX or TOP rows processed
					if ((maxRows > 0 && totalRowCount >= maxRows) ||
							(topRows > 0 && totalRowCount >= topRows)) {
						status = RestIP.DAM_SUCCESS;
						response.setStatus(RestIP.DAM_SUCCESS);
						break;
					}

					// check for FetchBlockSize rows processed
					if (rowCount >= fetchSize) {
						response.setStatus(RestIP.DAM_SUCCESS_WITH_RESULT_PENDING);
						break;
					}
				}			


			} 
		}while (status != RestIP.DAM_FAILURE);


		return status;

	}

	private Response invokeService(InvocationContext currInvocationContext, StatementContext stmtCtx) throws Exception{
		
		stmtCtx.trace(ip.UL_TM_F_TRACE, "invokeService() called");
		
		DataProcessor processor = CompositeDataProcessor.getInstance().getDataProcessor(stmtCtx);
		Request request = null;
		Response response = null;
		
		if ( null == processor) {
			throw new Exception("Dataprocessor for table " + stmtCtx.getTableName() + " missing.");
		}
		
		request = processor.buildRequest(stmtCtx, currInvocationContext);
		
		preProcessRequest(stmtCtx.getSessionContext(), request);
		
		stmtCtx.trace(ip.UL_TM_MAJOR_EV, "invoking new " + request.getVerb() + " request");
		stmtCtx.trace(ip.UL_TM_MAJOR_EV, "URL : " + request.getUrl());
		
		response = invoker.invoke(request);
				
		processor.processResponse(stmtCtx, currInvocationContext, request, response);
		
		stmtCtx.trace(ip.UL_TM_MAJOR_EV, "response = " + response.getStatusCode());
		
		stmtCtx.trace(ip.UL_TM_F_TRACE, "invokeService() returned");
		
		return response;
		
	}
	

	public void execute(SessionContext sessionContext) throws Exception {
	
		jdam.trace(sessionContext.getDam_Conn_Handle(), ip.UL_TM_F_TRACE, "execute() called\n");
		SchemaDataProcessor sdp = new SchemaDataProcessor(sessionContext);
		Request req = sdp.buildRequest(sessionContext);
		
		preProcessRequest(sessionContext, req);
					
		Response response = invoker.invoke(req);
		
		sdp.processReponse(response, sessionContext);
		jdam.trace(sessionContext.getDam_Conn_Handle(), ip.UL_TM_F_TRACE, "execute() returned\n");
				
	}
		
	
	
	public void preProcessRequest( SessionContext sessionContext, Request request){
		RequestProcessor.adjustRequest(sessionContext, request );
	}
}
