package com.ddtek.common.ip;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import oajava.sql.ip;
import oajava.sql.jdam;

import com.ddtek.common.schema.SchemaInfo;

public class SessionContext {

	public String baseURL = "https://www.alphavantage.co/query";

	private long dam_Conn_Handle;
	protected Map<Object, Object> sessionProperties;
	private SchemaInfo schemaDescriptor;
	private Map<String, String> IPCustomProperties = new HashMap<String, String>();
	
	public String getBaseURL(){
		return baseURL;
	}
	
	public void setBaseURL(String baseURL){
		this.baseURL = baseURL;
	}


	public static final String RESOURCE_PROPERTIES = "RESOURCE_PROPERTIES";

	
	public Map<String, String> getIPCustomProperties() {
		return IPCustomProperties;
	}

	public void setIPCustomProperties(Map<String, String> iPCustomProperties) {
		IPCustomProperties = iPCustomProperties;
	}

	public SessionContext() {
		sessionProperties = new HashMap<Object, Object>();
	}

	public SchemaInfo getSchemaDescriptor() {
		return schemaDescriptor;
	}

	public SessionContext(long handle) {
		this();
		this.dam_Conn_Handle = handle;
	}

	public Map<Object, Object> getSessionProperties() {
		return sessionProperties;
	}

	public void setSessionProperty(Object key, Object value) {
		this.sessionProperties.put(key, value);
	}

	public long getDam_Conn_Handle() {
		return dam_Conn_Handle;
	}

	public void init(long dam_hdbc, String sDataSourceName, String sUserName, String sPassword, String sCurrentCatalog,
			String sIPProperties, String sIPCustomProperties) throws Exception {

		jdam.trace(getDam_Conn_Handle(), ip.UL_TM_F_TRACE, "SessionContext.init() called\n");
		schemaDescriptor = new SchemaInfo();
		
		try {
			
			String errorMsg = "Check DataSourceIPProperties parameter. The format of this is Resource_Properties=<schema>,  where <schema> should point to the directory path of schema files.";
			
			if ( sIPProperties == null || sIPProperties.trim().length() == 0) {
				throw new Exception(errorMsg);
			} 
			
			String[] str=sIPProperties.trim().split("=");
			
			if ( !str[0].trim().equalsIgnoreCase(RESOURCE_PROPERTIES) ){
				throw new Exception(errorMsg);
			}
			
			schemaDescriptor.initSchema(str[1]);
		} catch (Exception e) {
			throw new Exception("Error parsing schema information: Verify DataSourceIPProperties setting and check the schema folder location. " + e.getMessage());
		}
		

				
		/* parse and build custom properties */
		Map<String, String> customProperties = parseProperties(sIPCustomProperties);
		for (Map.Entry<String, String> custProp : customProperties.entrySet()) {
			String custPropValue = custProp.getValue(); 
			if(custPropValue != null && !custPropValue.isEmpty() && custPropValue.equals("?") == false) {
				IPCustomProperties.put(custProp.getKey(), custPropValue);
			}
		}
		jdam.trace(getDam_Conn_Handle(), ip.UL_TM_F_TRACE, "SessionContext.init() returned\n");
	}

	public Object getProperty(Object key) {
		return sessionProperties.get(key);
	}
	
	
	private Map<String, String> parseProperties(String sProperties)
	{
		StringTokenizer sPropertyTokenizer = new StringTokenizer(sProperties, "=;");
		Map<String, String> properties = new HashMap<String, String>();

		while(sPropertyTokenizer.hasMoreTokens()) { 
			String key = sPropertyTokenizer.nextToken();
			String value = sPropertyTokenizer.nextToken();

			properties.put(key.toUpperCase(), value);
		}
		return properties;
	}
	

}
