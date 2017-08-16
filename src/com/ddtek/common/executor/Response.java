package com.ddtek.common.executor;

import java.io.IOException;
import java.io.InputStream;

import org.apache.cxf.helpers.IOUtils;

public class Response {

	private int statusCode;
	private String body;
	private String exception;
	private javax.ws.rs.core.Response jaxrsResponse;
	private boolean isBodyRead;
	private int status;
	private String offset;
	
	

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public Response(){
		
	}
	public Response(javax.ws.rs.core.Response jaxrsResponse) {
		this.jaxrsResponse = jaxrsResponse;
		setStatusCode(jaxrsResponse.getStatus());
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getBody() throws Exception {
		if (!isBodyRead) {
			readBody();
		}
		return body;
	}

	private synchronized void readBody() throws Exception {
		if (!isBodyRead) {
			try {
				body = IOUtils.toString((InputStream) jaxrsResponse.getEntity());
			} catch (IOException e) {
				throw new Exception("Failed to read the body :: " , e);
			}
		}
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status ){
		this.status = status;
	}

}
