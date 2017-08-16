package com.ddtek.common.dataprocessor;


import java.util.HashMap;

public class HttpStatusCodeMapper {
	
	public static HashMap<Integer,String> mapper = new HashMap<Integer, String>();
	
	static{
		
		mapper.put(0, "Could not send request");
		mapper.put(100, "Continue");
		mapper.put(101, "Switching Protocols");
		mapper.put(103, "Checkpoint");
		mapper.put(200, "OK");
		mapper.put(201, "Created");
		mapper.put(202, "Accepted");
		mapper.put(203, "Non-Authoritative Information");
		mapper.put(204, "No Content");
		mapper.put(205, "Reset Content");
		mapper.put(206, "Partial Content");
		mapper.put(300, "Multiple Choices");
		mapper.put(301, "Moved Permanently");
		mapper.put(302, "Found");
		mapper.put(303, "See Other");
		mapper.put(304, "Not Modified");
		mapper.put(306, "Switch Proxy");
		mapper.put(307, "Temporary Redirect");
		mapper.put(308, "Resume Incomplete");
		mapper.put(400, "Bad Request");
		mapper.put(401, "Unauthorized");
		mapper.put(402, "Payment Required");
		mapper.put(403, "Forbidden");
		mapper.put(404, "Not Found");
		mapper.put(405, "Method Not Allowed");
		mapper.put(406, "Not Acceptable");
		mapper.put(407, "Proxy Authentication Required");
		mapper.put(408, "Request Timeout");
		mapper.put(409, "Conflict");
		mapper.put(410, "Gone");
		mapper.put(411, "Length Required");
		mapper.put(412, "Precondition Failed");
		mapper.put(413, "Request Entity Too Large");
		mapper.put(414, "Request-URI Too Long");
		mapper.put(415, "Unsupported Media Type");
		mapper.put(416, "Requested Range Not Satisfiable");
		mapper.put(417, "Expectation Failed");
		mapper.put(500, "Internal Server Error");
		mapper.put(501, "Not Implemented");
		mapper.put(502, "Bad Gateway");
		mapper.put(503, "Service Unavailable");
		mapper.put(504, "Gateway Timeout");
		mapper.put(505, "HTTP Version Not Supported");
		mapper.put(511, "Network Authentication Required");
		
	}

	public static String get(int httpStatusCode) {
		return mapper.get(httpStatusCode);
	}
	
	

}
