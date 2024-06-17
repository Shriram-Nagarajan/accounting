package com.um.model;

public class EmailMessage {

	private String fromAddress;
	private String toAddress;
	private String subject;
	private String body;
	
	public EmailMessage(String fromAddress, String toAddress, String subject, String body) {
		this.fromAddress = fromAddress;
		this.toAddress = toAddress;
		this.subject = subject;
		this.body = body;
	}
	
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
}
