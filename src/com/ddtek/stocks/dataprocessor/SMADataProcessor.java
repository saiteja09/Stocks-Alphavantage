package com.ddtek.stocks.dataprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ddtek.common.dataprocessor.DataProcessor;
import com.ddtek.common.executor.Request;
import com.ddtek.common.executor.Request.Verb;
import com.ddtek.common.executor.Response;
import com.ddtek.common.ip.InvocationContext;
import com.ddtek.common.ip.StatementContext;

public class SMADataProcessor extends DataProcessor {

	/**
	 * This method is to build appropriate HTTP request that is to be invoked
	 * to get the required data from the table SMA 
	 * 
	 * @param The statement context of the statement
	 * @param The current invocation context
	 * @return A HTTP Request that is to be executed
	 * @throws Exception
	 */
	@Override
	public Request buildRequest(StatementContext ctx, InvocationContext invCtx) throws Exception {
		String url;
		Map<String, Object> equalsConditions = invCtx.getEqlConditionsInUse();
		Map<String, String> queryParams = new HashMap<String, String> ();
		
		String baseURL =  ctx.getSessionContext().getBaseURL();
		
		// XXX clause is specified. Use API to query by XXX
		if (equalsConditions.containsKey("XXX")) {
			
			url = baseURL + "/api/v1/data/xxx/"
						+ equalsConditions.get("XXX") 
						+ "/profile";
		
		// Fetch all data
		} else {
			
			url = baseURL + "/api/v1/lists/all/data/all?count=100";

			// Offset stored in the statement context can be
			// used in creating the next request
			String offset = ctx.getProperty("offset");

			if (null != offset && offset.trim().length() != 0){
				url += ("&offset=" + offset);
			}
		}
							
		return new Request(url, null, null, Verb.GET, queryParams);
	}

	@Override
	public ArrayList<HashMap<String,String>> parseJSONResponse(StatementContext statmentCtx, Response response, InvocationContext invCtx) throws Exception {
		
		Map<String, Object> equalsConditions = invCtx.getEqlConditionsInUse();
		
		// Parse and return the data as List of HashMap
		// where HashMap is a single row with column - value pair
		if (equalsConditions.containsKey("XXX")) {
			return processByXXX(statmentCtx, response,  invCtx);
		} else {
			return processAll(statmentCtx, response,  invCtx);
		}
	}
	
	/**
	 * Process the JSON response and return the
	 * results as List of Map.
	 * The status of the response is to be set
	 * based on the results.
	 * There are three options for status
	 * 
	 * <p><tt>RestIP.DAM_SUCCESS</tt> - Fetched all the results.
	 * <br><tt>RestIP.DAM_SUCCESS_WITH_RESULT_PENDING</tt> - More rows to be fetched.
	 * In this case offset may be required to be set in statement context.
	 * <br><tt>RestIP.DAM_FAILURE</tt> - In case of some failure.
	 * 
	 *  
	 * @param statmentCtx
	 * @param response
	 * @param invCtx
	 * @return a list of Map containing the rows with column name as key and column value as value.
	 * @throws Exception
	 */
	private ArrayList<HashMap<String, String>> processAll(
		StatementContext statmentCtx, Response response,
		InvocationContext invCtx) throws Exception {

		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
		
		try {
			String json = response.getBody();
			
			// parse the json
			
		} catch (Exception e) {
			throw new Exception("Error parsing response " + e.getMessage());
		}
		return list;
	}
	
	/**
	 * Process the JSON response and return the
	 * results as List of Map.
	 * The status of the response is to be set
	 * based on the results.
	 * There are three options for status
	 * 
	 * <p><tt>RestIP.DAM_SUCCESS</tt> - Fetched all the results.
	 * <br><tt>RestIP.DAM_SUCCESS_WITH_RESULT_PENDING</tt> - More rows to be fetched.
	 * In this case offset may be required to be set in statement context.
	 * <br><tt>RestIP.DAM_FAILURE</tt> - In case of some failure.
	 * 
	 *  
	 * @param statmentCtx
	 * @param response
	 * @param invCtx
	 * @return a list of Map containing the rows with column name as key and column value as value.
	 * @throws Exception
	 */
	private ArrayList<HashMap<String, String>> processByXXX(
		StatementContext statmentCtx, Response response,
		InvocationContext invCtx) throws Exception {

		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
		
		try {
			String json = response.getBody();
			
			// parse the json
			
		} catch (Exception e) {
			throw new Exception("Error parsing response " + e.getMessage());
		}
		return list;
	}
}
