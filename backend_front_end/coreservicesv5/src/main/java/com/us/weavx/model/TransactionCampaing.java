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
@Table(name="transaction_campaing")
public class TransactionCampaing {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	private String description;
	private Timestamp valid_from;
	private Timestamp valid_to;
	private String key;
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
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
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	@Override
	public String toString() {
		return "TransactionCampaing [id=" + id + ", name=" + name + ", description=" + description + ", valid_from="
				+ valid_from + ", valid_to=" + valid_to + ", key=" + key + "]";
	}
	
	

}
