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
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class TimeSeriesDataProcessor extends DataProcessor {

	/**
	 * This method is to build appropriate HTTP request that is to be invoked
	 * to get the required data from the table TimeSeries 
	 * 
	 * @param // statement context of the statement
	 * @param  //current invocation context
	 * @return A HTTP Request that is to be executed
	 * @throws Exception
	 */
	@Override
	public Request buildRequest(StatementContext ctx, InvocationContext invCtx) throws Exception {
		String url = null;
		
		//Gets the equal conditins in WHERE CLAUSE to map (Say you have a query SELECT * FROM Person WHERE NAME='XXX', 
		//The condition NAME='XXX' can be accessed here, where NAME will be a key and XXX will the value in the below map)
		Map<String, Object> equalsConditions = invCtx.getEqlConditionsInUse();
		
		Map<String, String> queryParams = new HashMap<String, String> ();

		//Gets the base URL that you have set in input.props file
		String baseURL =  ctx.getSessionContext().getBaseURL();
		
		//Gets the Custom Properties of the driver that you set while connecting through ODBC/JDBC, in this case it's API KEY
		Map<String, String> customProperties = ctx.getSessionContext().getIPCustomProperties();
		

		if (equalsConditions.containsKey("FUNCTION") && equalsConditions.containsKey("SYMBOL")) {

			String infoFunction = equalsConditions.get("FUNCTION").toString();
			String outputSize = null;

            //Set output size if specified in query
			if(equalsConditions.containsKey("OUTPUTSIZE"))
			{
				outputSize = equalsConditions.get("OUTPUTSIZE").toString();
			}


            //Build URL based on SQL Query conditions
			if(outputSize == null) {
				url = baseURL + "?function=" + infoFunction + "&symbol=" + equalsConditions.get("SYMBOL").toString()  + "&apikey=" + customProperties.get("APIKEY");
			}
			else
			{
				url = baseURL + "?function=" + infoFunction + "&symbol=" + equalsConditions.get("SYMBOL").toString()  + "&outputsize=" + outputSize + "&apikey=" + customProperties.get("APIKEY");
			}

            if(infoFunction.equalsIgnoreCase("TIME_SERIES_INTRADAY") &&  equalsConditions.containsKey("INTERVAL"))
            {
                url = url + "&interval=" + equalsConditions.get("INTERVAL").toString();
            }

		}
		return new Request(url, null, null, Verb.GET, queryParams);
	}

	@Override
	public ArrayList<HashMap<String,String>> parseJSONResponse(StatementContext statmentCtx, Response response, InvocationContext invCtx) throws Exception {
        Map<String, Object> equalsConditions = invCtx.getEqlConditionsInUse();
		return processAll(statmentCtx, response,  invCtx, equalsConditions.get("FUNCTION").toString());

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
		InvocationContext invCtx, String infoFunction) throws Exception {

		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
		
		try {
            //JSON Response
			String json = response.getBody();


            //MetaData Information
            String information = null;
            String symbol = null;
            String lastRefreshed = null;
            String interval = null;
            String output_size = null;
            String time_zone = null;


            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readValue(json, JsonNode.class);
            Map<String, Object> mapRootNode = mapper.convertValue(rootNode, Map.class);


            for(String key:  mapRootNode.keySet())
            {

                //Get Metadata from response. This data is used for every row that we send back to OpenAccess Engine.
                if (key.toLowerCase().contains("meta"))
                {
                    Map<String, Object> metadataRoot = (Map<String, Object>)mapRootNode.get(key);
                    
                    //Parse metadata depending on type of function and response.
                    information = metadataRoot.get("1. Information").toString();
                    symbol = metadataRoot.get("2. Symbol").toString();
                    lastRefreshed = metadataRoot.get("3. Last Refreshed").toString();
                    if(metadataRoot.containsKey("4. Interval")) {
                        interval = metadataRoot.get("4. Interval").toString();
                        output_size = metadataRoot.get("5. Output Size").toString();
                        time_zone = metadataRoot.get("6. Time Zone").toString();
                    }else if(metadataRoot.containsKey("4. Output Size"))
                    {
                        output_size = metadataRoot.get("4. Output Size").toString();
                        time_zone = metadataRoot.get("5. Time Zone").toString();
                    } else
                    {
                        time_zone = metadataRoot.get("4. Time Zone").toString();
                    }
                }

                //Parse through the time series data of stocks and send the data to OpenAccess Engine.
                else if (key.toLowerCase().contains("time"))
                {
                    Map<String, Object> timeseriesRoot = (Map<String, Object>)mapRootNode.get(key);


                    for(String timestamp_recorded: timeseriesRoot.keySet())
                    {
                        HashMap<String, String> single_row = new HashMap<>();
                        Map<String, String> recorded_values = (Map<String, String>)timeseriesRoot.get(timestamp_recorded);


                        //Add row with Column names and their respective values
                        single_row.put("Information", information);
                        single_row.put("Function", infoFunction);
                        single_row.put("Symbol", symbol);
                        single_row.put("LastRefreshed", lastRefreshed);
                        single_row.put("Interval", interval);
                        if(output_size != null && output_size.equalsIgnoreCase("Full size"))
                        {
                            single_row.put("OutputSize", "full");
                        }
                        else {
                            single_row.put("OutputSize", output_size);
                        }
                        single_row.put("TimeZone", time_zone);
                        single_row.put("Timestamp_Recorded", timestamp_recorded);
                        single_row.put("open", recorded_values.get("1. open"));
                        single_row.put("high", recorded_values.get("2. high"));
                        single_row.put("low", recorded_values.get("3. low"));
                        single_row.put("close", recorded_values.get("4. close"));
                        single_row.put("volume", recorded_values.get("5. volume"));

                        //Add the row to final list
                        list.add(single_row);

                    }

                }
            }

		} catch (Exception e) {
			throw new Exception("Error parsing response " + e.getMessage());
		}
		return list;
	}
	
}
