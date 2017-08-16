package com.ddtek.stocks.schema;

import com.ddtek.common.executor.Request;
import com.ddtek.common.executor.Response;
import com.ddtek.common.executor.Request.Verb;
import com.ddtek.common.ip.SessionContext;

public class SchemaDataProcessor {

	public SchemaDataProcessor(SessionContext sessionContext) {
	}

	/**
	 * Prepare the HTTP request required to get dynamic schema
	 * 
	 * @param sessionContext
	 * @return
	 */
	public Request buildRequest(SessionContext sessionContext) {

		String url = "https://build_your_url";
		Request req = new Request(url, null, null, Verb.GET, null);
		return req;

	}

	/**
	 * Parse the JSON response and add the columns to appropriate table
	 * 
	 * @param response
	 * @param sessionContext
	 * @throws Exception
	 */
	public void processReponse(Response response, SessionContext sessionContext)
			throws Exception {

		// parse the response 
		
		if ( response == null )
			return;
			
		int httpStatusCode = response.getStatusCode();
		
		if ( 200 <= httpStatusCode && httpStatusCode <= 206 ){
		
			// Successful. Build Schema from response.
			
		}
		else{
		
			handleExceptions(response);
		}
					
			

	}
	
	/**
	 * This method throws exception when an invalid HTTP status code is encountered.
	 * 
	 * @param response
	 * @throws Exception
	 */
	public void handleExceptions(Response response ) throws Exception {
		
		int httpStatusCode = response.getStatusCode();
		
		// Handle exceptions here.
						
	}		 
	
	
	
}
