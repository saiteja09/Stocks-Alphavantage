package com.ddtek.stocks.connection;

import com.ddtek.common.ip.SessionContext;
import com.ddtek.common.connection.Status;
import com.ddtek.common.executor.FlowController;
import java.util.*;




public class ConnectionHelper {

	/*
	 *   In case of success, nothing should be returned.
	 *   In case of failure, an exception with proper message should be thrown to caller.
	 */
	
	public void connect(SessionContext sessionContext) throws Exception {
		
		// Authentication
		
		
		
		
		Status validationStatus =  processCustomProperties(sessionContext);

		
		if (!validationStatus.isValid()) {
			throw new Exception(validationStatus.getMessage());
		}
		
		
		/*			
			Users can invoke this method to invoke any requests during connection, such as
			Schema Discovery, Authentication etc.			
					
		*/
		
		invokeConnectionRequests(sessionContext);
		
		
	}
	
	
	private void invokeConnectionRequests(SessionContext sessionContext ) throws Exception {
	

		// Use this method, if you need to invoke any request during connection time.
		// Create an instance of FlowController and call the execute() method.
		// This execute method uses SchemaDataProcessor to invoke requests and process the results.
		// Modify SchemaDataProcess class to build the request and process response.

		
				
		//FlowController flow = FlowController.getInstance();
		//flow.execute(sessionContext);
		
		
		
		
	}
	
	/*
		This method is responsible for validating custom properties, processing and storing them into SessionContext.
		Later these properties can be retrieved from SessionContext.
	*/
	Status processCustomProperties(SessionContext sessionContext ) {
	
		Status validationStatus = new Status(true, null);

//		Map<String, String> customProperties = sessionContext.getIPCustomProperties();
//
//		if (customProperties == null
//				|| customProperties.size() == 0
//				|| !(customProperties.containsKey("AUTHENTICATIONID"))) {
//			validationStatus.setValid(false);
//			validationStatus
//			.setMessage("authentication id missing.");
//
//			return validationStatus;
//		}
		return validationStatus;
	}
	

}
