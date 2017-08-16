package com.ddtek.common.executor;

import java.util.Map;

public class Request {
	
	public enum Verb {
		GET, PUT, POST, DELETE
	};
	
	private Verb verb;
	private String url;
	private String body;
	private Map<String,String> headers;
	private Map<String,String> queryParams;
	
	public Map<String, String> getQueryParams() {
		return queryParams;
	}
	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public Request(String url, String body, Map<String, String> headers, Verb verb, Map<String,String> query) {
		super();
		this.url = url;
		this.body = body;
		this.headers = headers;
		this.verb = verb;
		this.queryParams = query;
	}
	public Verb getVerb() {
		return verb;
	}
	public void setVerb(Verb verb) {
		this.verb = verb;
	}
	
}
