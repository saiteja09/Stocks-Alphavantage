package com.ddtek.common.cxf.client;

import java.util.Map;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import com.ddtek.common.executor.Request;


import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;

public class CXFClient {
	
	public com.ddtek.common.executor.Response invoke(Request req) throws Exception{
		return invoke(req.getUrl(), req.getBody(), req.getVerb().toString(), req.getHeaders(), req.getQueryParams());
	}

	public static com.ddtek.common.executor.Response invoke(String url, Object body, String httpMethod, Map<String, String> headers, Map<String, String> queryParams) throws Exception {
		
		Response rs;
		
		try {

			WebClient client = WebClient.create(url);
			
			
			/* For Gzip compression support */
			ClientConfiguration config = WebClient.getConfig(client);
			config.getInInterceptors().add(new GZIPInInterceptor());
			config.getOutInterceptors().add(new GZIPOutInterceptor());
			

			if ( null != queryParams){
				
				for ( Map.Entry<String, String> entry: queryParams.entrySet()){
					client.query(entry.getKey()	, entry.getValue());		
				}
				
			}
			
			if ( null != headers)
				client.headers(new MultivaluedHashMap<String, String>(headers));
			rs = client.invoke(httpMethod, body);
		} catch (Exception e) {
			throw new Exception("Service invoke failed : " + e.getMessage()); 
		}
		
		return new com.ddtek.common.executor.Response(rs);
	}

}
