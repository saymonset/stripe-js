package com.us.weavx.core.model;

import java.util.List;
import java.util.Map;

public class SendEmailRequest {
	
	private List<EmailRecipient> recipients;
	private String content;
	private String subject;
	private String fromEmail;
	private String fromName;
	private Map<String, String> fillInfo;
	private Map<String, String> settings;
	private List<Attachment> attachments;
	
	public SendEmailRequest() {
		
	}

	public List<EmailRecipient> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<EmailRecipient> recipients) {
		this.recipients = recipients;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public Map<String, String> getFillInfo() {
		return fillInfo;
	}

	public void setFillInfo(Map<String, String> fillInfo) {
		this.fillInfo = fillInfo;
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return "SendEmailRequest [recipients=" + recipients + ", content=" + content + ", subject=" + subject
				+ ", fromEmail=" + fromEmail + ", fromName=" + fromName + ", fillInfo=" + fillInfo + ", settings="
				+ settings + "]";
	}

	

}
