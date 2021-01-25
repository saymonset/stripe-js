package com.us.weavx.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name="transaction")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String tx_id;
	private Timestamp tx_date;
	@OneToOne
	@JoinColumn(name="tx_user_data_id")
	private TransactionUserData tx_user_data;
	private long tx_payment_data_id;
	@ManyToOne
	@JoinColumn(name="tx_status_id")
	private TransactionStatus transactionStatus;
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	@ManyToOne
	@JoinColumn(name="application_id")
	private Application application;
	private double amount;
	private String gwauthid_1;
	private String gwauthid_2;
	private int tx_payment_gw_id;
	@ManyToOne
	@JoinColumn(name="tx_transaction_source_id")
	private TransactionSource tx_transaction_source;
	@ManyToOne
	@JoinColumn(name="tx_transaction_campaing_id")
	private TransactionCampaing tx_transaction_campaing;
	@ManyToOne
	@JoinColumn(name="tx_lang_id")
	private Lang tx_lang;
	private boolean is_scheduled;
	@ManyToOne
	@JoinColumn(name="tx_transaction_medium_id")
	private TransactionMedium tx_transaction_medium;
	
	@OneToMany(mappedBy="transaction", fetch=FetchType.EAGER)
	List<TransactionDetail> tx_details;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTx_id() {
		return tx_id;
	}

	public void setTx_id(String tx_id) {
		this.tx_id = tx_id;
	}

	public Timestamp getTx_date() {
		return tx_date;
	}

	public void setTx_date(Timestamp tx_date) {
		this.tx_date = tx_date;
	}

	public TransactionUserData getTx_user_data() {
		return tx_user_data;
	}

	public void setTx_user_data(TransactionUserData tx_user_data) {
		this.tx_user_data = tx_user_data;
	}

	public long getTx_payment_data_id() {
		return tx_payment_data_id;
	}

	public void setTx_payment_data_id(long tx_payment_data_id) {
		this.tx_payment_data_id = tx_payment_data_id;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getGwauthid_1() {
		return gwauthid_1;
	}

	public void setGwauthid_1(String gwauthid_1) {
		this.gwauthid_1 = gwauthid_1;
	}

	public String getGwauthid_2() {
		return gwauthid_2;
	}

	public void setGwauthid_2(String gwauthid_2) {
		this.gwauthid_2 = gwauthid_2;
	}

	public int getTx_payment_gw_id() {
		return tx_payment_gw_id;
	}

	public void setTx_payment_gw_id(int tx_payment_gw_id) {
		this.tx_payment_gw_id = tx_payment_gw_id;
	}

	public TransactionSource getTx_transaction_source() {
		return tx_transaction_source;
	}

	public void setTx_transaction_source(TransactionSource tx_transaction_source) {
		this.tx_transaction_source = tx_transaction_source;
	}

	public TransactionCampaing getTx_transaction_campaing() {
		return tx_transaction_campaing;
	}

	public void setTx_transaction_campaing(TransactionCampaing tx_transaction_campaing) {
		this.tx_transaction_campaing = tx_transaction_campaing;
	}

	public Lang getTx_lang() {
		return tx_lang;
	}

	public void setTx_lang(Lang tx_lang) {
		this.tx_lang = tx_lang;
	}

	public boolean isIs_scheduled() {
		return is_scheduled;
	}

	public void setIs_scheduled(boolean is_scheduled) {
		this.is_scheduled = is_scheduled;
	}

	public TransactionMedium getTx_transaction_medium() {
		return tx_transaction_medium;
	}

	public void setTx_transaction_medium(TransactionMedium tx_transaction_medium) {
		this.tx_transaction_medium = tx_transaction_medium;
	}

	public List<TransactionDetail> getTx_details() {
		return tx_details;
	}

	public void setTx_details(List<TransactionDetail> tx_details) {
		this.tx_details = tx_details;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", tx_id=" + tx_id + ", tx_date=" + tx_date + ", tx_user_data=" + tx_user_data
				+ ", tx_payment_data_id=" + tx_payment_data_id + ", transactionStatus=" + transactionStatus
				+ ", customer=" + customer + ", application=" + application + ", amount=" + amount + ", gwauthid_1="
				+ gwauthid_1 + ", gwauthid_2=" + gwauthid_2 + ", tx_payment_gw_id=" + tx_payment_gw_id
				+ ", tx_transaction_source=" + tx_transaction_source + ", tx_transaction_campaing="
				+ tx_transaction_campaing + ", tx_lang=" + tx_lang + ", is_scheduled=" + is_scheduled
				+ ", tx_transaction_medium=" + tx_transaction_medium + ", tx_details=" + tx_details + "]";
	}
	

	

}
