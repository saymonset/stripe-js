package com.us.weavx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="transaction_detail")
public class TransactionDetail {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="transaction_id")
	private Transaction transaction;
	@ManyToOne
	@JoinColumn(name="fund_id")
	private Fund fund;
	private double amount;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public Transaction getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	public Fund getFund() {
		return fund;
	}
	public void setFund(Fund fund) {
		this.fund = fund;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "TransactionDetail [id=" + id + ", transaction=" + transaction + ", fund=" + fund + ", amount=" + amount
				+ "]";
	}
	
	
	
}
