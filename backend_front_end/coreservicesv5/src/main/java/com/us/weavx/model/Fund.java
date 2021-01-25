package com.us.weavx.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="fund")
public class Fund {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	private String business_code;
	private boolean is_default;
	private Timestamp created_at;
	private String name;
	private Timestamp valid_from;
	private Timestamp valid_to;
	private boolean isschedulable;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getBusiness_code() {
		return business_code;
	}
	public void setBusiness_code(String business_code) {
		this.business_code = business_code;
	}
	public boolean isIs_default() {
		return is_default;
	}
	public void setIs_default(boolean is_default) {
		this.is_default = is_default;
	}
	public Timestamp getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getValid_from() {
		return valid_from;
	}
	public void setValid_from(Timestamp valid_from) {
		this.valid_from = valid_from;
	}
	public Timestamp getValid_to() {
		return valid_to;
	}
	public void setValid_to(Timestamp valid_to) {
		this.valid_to = valid_to;
	}
	public boolean isIsschedulable() {
		return isschedulable;
	}
	public void setIsschedulable(boolean isschedulable) {
		this.isschedulable = isschedulable;
	}
	
	

}
