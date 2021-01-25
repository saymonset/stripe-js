package com.us.weavx.core.model;

public class ScheduledDonationSettingsLang {
	
	private long customer_id;
	private long application_id;
	private int lang_id;
	private String created_email_template;
	private String created_email_row_template;
	private String created_email_subject;
	private String created_email_from;
	private String created_email_from_name;
	private String confirmation_before_email_template;
	private String confirmation_before_email_subject;
	private String confirmation_before_email_from;
	private String confirmation_before_email_from_name;
	private String status_change_email_template;
	private String status_change_email_subject;
	private String status_change_email_from;
	private String status_change_email_from_name;
	private String failed_email_template;
	private String failed_email_subject;
	private String failed_email_from;
	private String failed_email_from_name;

	public ScheduledDonationSettingsLang() {
		
	}

	public long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
	}

	public long getApplication_id() {
		return application_id;
	}

	public void setApplication_id(long application_id) {
		this.application_id = application_id;
	}

	public int getLang_id() {
		return lang_id;
	}

	public void setLang_id(int lang_id) {
		this.lang_id = lang_id;
	}

	public String getCreated_email_template() {
		return created_email_template;
	}

	public void setCreated_email_template(String created_email_template) {
		this.created_email_template = created_email_template;
	}

	public String getCreated_email_row_template() {
		return created_email_row_template;
	}

	public void setCreated_email_row_template(String created_email_row_template) {
		this.created_email_row_template = created_email_row_template;
	}

	public String getCreated_email_subject() {
		return created_email_subject;
	}

	public void setCreated_email_subject(String created_email_subject) {
		this.created_email_subject = created_email_subject;
	}

	public String getCreated_email_from() {
		return created_email_from;
	}

	public void setCreated_email_from(String created_email_from) {
		this.created_email_from = created_email_from;
	}

	public String getCreated_email_from_name() {
		return created_email_from_name;
	}

	public void setCreated_email_from_name(String created_email_from_name) {
		this.created_email_from_name = created_email_from_name;
	}

	public String getConfirmation_before_email_template() {
		return confirmation_before_email_template;
	}

	public void setConfirmation_before_email_template(String confirmation_before_email_template) {
		this.confirmation_before_email_template = confirmation_before_email_template;
	}

	public String getConfirmation_before_email_subject() {
		return confirmation_before_email_subject;
	}

	public void setConfirmation_before_email_subject(String confirmation_before_email_subject) {
		this.confirmation_before_email_subject = confirmation_before_email_subject;
	}

	public String getConfirmation_before_email_from() {
		return confirmation_before_email_from;
	}

	public void setConfirmation_before_email_from(String confirmation_before_email_from) {
		this.confirmation_before_email_from = confirmation_before_email_from;
	}

	public String getConfirmation_before_email_from_name() {
		return confirmation_before_email_from_name;
	}

	public void setConfirmation_before_email_from_name(String confirmation_before_email_from_name) {
		this.confirmation_before_email_from_name = confirmation_before_email_from_name;
	}

	public String getStatus_change_email_template() {
		return status_change_email_template;
	}

	public void setStatus_change_email_template(String status_change_email_template) {
		this.status_change_email_template = status_change_email_template;
	}

	public String getStatus_change_email_subject() {
		return status_change_email_subject;
	}

	public void setStatus_change_email_subject(String status_change_email_subject) {
		this.status_change_email_subject = status_change_email_subject;
	}

	public String getStatus_change_email_from() {
		return status_change_email_from;
	}

	public void setStatus_change_email_from(String status_change_email_from) {
		this.status_change_email_from = status_change_email_from;
	}

	public String getStatus_change_email_from_name() {
		return status_change_email_from_name;
	}

	public void setStatus_change_email_from_name(String status_change_email_from_name) {
		this.status_change_email_from_name = status_change_email_from_name;
	}

	public String getFailed_email_template() {
		return failed_email_template;
	}

	public void setFailed_email_template(String failed_email_template) {
		this.failed_email_template = failed_email_template;
	}

	public String getFailed_email_subject() {
		return failed_email_subject;
	}

	public void setFailed_email_subject(String failed_email_subject) {
		this.failed_email_subject = failed_email_subject;
	}

	public String getFailed_email_from() {
		return failed_email_from;
	}

	public void setFailed_email_from(String failed_email_from) {
		this.failed_email_from = failed_email_from;
	}

	public String getFailed_email_from_name() {
		return failed_email_from_name;
	}

	public void setFailed_email_from_name(String failed_email_from_name) {
		this.failed_email_from_name = failed_email_from_name;
	}

	@Override
	public String toString() {
		return "ScheduledDonationSettingsLang [customer_id=" + customer_id + ", application_id=" + application_id
				+ ", lang_id=" + lang_id + ", created_email_template=" + created_email_template
				+ ", created_email_row_template=" + created_email_row_template + ", created_email_subject="
				+ created_email_subject + ", created_email_from=" + created_email_from + ", created_email_from_name="
				+ created_email_from_name + ", confirmation_before_email_template=" + confirmation_before_email_template
				+ ", confirmation_before_email_subject=" + confirmation_before_email_subject
				+ ", confirmation_before_email_from=" + confirmation_before_email_from
				+ ", confirmation_before_email_from_name=" + confirmation_before_email_from_name
				+ ", status_change_email_template=" + status_change_email_template + ", status_change_email_subject="
				+ status_change_email_subject + ", status_change_email_from=" + status_change_email_from
				+ ", status_change_email_from_name=" + status_change_email_from_name + ", failed_email_template="
				+ failed_email_template + ", failed_email_subject=" + failed_email_subject + ", failed_email_from="
				+ failed_email_from + ", failed_email_from_name=" + failed_email_from_name + "]";
	}

	
}
