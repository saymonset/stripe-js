package com.us.weavx.core.model;

import java.sql.Timestamp;

public class OTPKey {
	
	private long id;
	private long customer_user_id;
	private long application_id;
	private String code;
	private int otp_key_status_id;
	private int validation_try_count;
	private Timestamp created_at;
	private Timestamp last_validation_try;
	public OTPKey() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCustomer_user_id() {
		return customer_user_id;
	}
	public void setCustomer_user_id(long customer_user_id) {
		this.customer_user_id = customer_user_id;
	}
	public long getApplication_id() {
		return application_id;
	}
	public void setApplication_id(long application_id) {
		this.application_id = application_id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getOtp_key_status_id() {
		return otp_key_status_id;
	}
	public void setOtp_key_status_id(int otp_key_status_id) {
		this.otp_key_status_id = otp_key_status_id;
	}
	public int getValidation_try_count() {
		return validation_try_count;
	}
	public void setValidation_try_count(int validation_try_count) {
		this.validation_try_count = validation_try_count;
	}
	
	public Timestamp getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	public Timestamp getLast_validation_try() {
		return last_validation_try;
	}
	public void setLast_validation_try(Timestamp last_validation_try) {
		this.last_validation_try = last_validation_try;
	}
	@Override
	public String toString() {
		return "OTPKey [id=" + id + ", customer_user_id=" + customer_user_id + ", application_id=" + application_id
				+ ", code=" + code + ", otp_key_status_id=" + otp_key_status_id + ", validation_try_count="
				+ validation_try_count + ", created_at=" + created_at + ", last_validation_try=" + last_validation_try
				+ "]";
	}
	

	

}
