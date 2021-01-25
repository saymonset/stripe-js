package com.us.weavx.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="application")
public class Application {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	private String description;
	private long short_token_duration;
	private long long_token_duration;
	private long short_user_token_duration;
	private long long_user_token_duration;
	private long created_by;
	private Timestamp created_at;
	private String application_controller_class;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getShort_token_duration() {
		return short_token_duration;
	}
	public void setShort_token_duration(long short_token_duration) {
		this.short_token_duration = short_token_duration;
	}
	public long getLong_token_duration() {
		return long_token_duration;
	}
	public void setLong_token_duration(long long_token_duration) {
		this.long_token_duration = long_token_duration;
	}
	public long getShort_user_token_duration() {
		return short_user_token_duration;
	}
	public void setShort_user_token_duration(long short_user_token_duration) {
		this.short_user_token_duration = short_user_token_duration;
	}
	public long getLong_user_token_duration() {
		return long_user_token_duration;
	}
	public void setLong_user_token_duration(long long_user_token_duration) {
		this.long_user_token_duration = long_user_token_duration;
	}
	public long getCreated_by() {
		return created_by;
	}
	public void setCreated_by(long created_by) {
		this.created_by = created_by;
	}
	public Timestamp getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	public String getApplication_controller_class() {
		return application_controller_class;
	}
	public void setApplication_controller_class(String application_controller_class) {
		this.application_controller_class = application_controller_class;
	}
	@Override
	public String toString() {
		return "Application [id=" + id + ", name=" + name + ", description=" + description + ", short_token_duration="
				+ short_token_duration + ", long_token_duration=" + long_token_duration + ", short_user_token_duration="
				+ short_user_token_duration + ", long_user_token_duration=" + long_user_token_duration + ", created_by="
				+ created_by + ", created_at=" + created_at + ", application_controller_class="
				+ application_controller_class + "]";
	}
	
	

}
