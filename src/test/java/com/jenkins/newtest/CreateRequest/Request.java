package com.jenkins.newtest.CreateRequest;

public class Request {
	private String request;
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	private String response;
	
	private String responsenotpresent = "";
	public String getResponsenotpresent() {
		return responsenotpresent; 
	}
	public void setResponsenotpresent(String responsenotpresent) {
		this.responsenotpresent = responsenotpresent;
	}

}
