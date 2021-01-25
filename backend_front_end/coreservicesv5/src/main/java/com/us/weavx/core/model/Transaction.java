package com.us.weavx.core.model;

import java.sql.Timestamp;

public class Transaction {
	
	
	public static final int INITIATED = 0;
	public static final int PAYMENT_IN_PROGRESS = 1;
	public static final int PAYMENT_DENIED = 2;
	public static final int PAYMENT_ERROR = 3;
	public static final int CHARGED = 4;
	public static final int SUCCESS_TX = 5;
	public static final int FAILED_TX = 6;
	public static final int ABORTED = 7;
	public static final int NOT_PROVISIONED = 8;
	public static final int IN_BLACKLIST = 9;
	public static final int USER_EXPIRED_ACCESS = 10;
	
	private long id;
	private String transactionId;
	private Timestamp transactionDate;
	private long transactionUserDataId;
	private long transactionPaymentDataId;
	private int transactionStatus;
	private long customerId;
	private long applicationId;
	private double amount;
	private Integer	paymentGwId;
	private String authGw1;
	private String authGw2;
	private Integer sourceId;
	private Long campaingId;
	private int langId;
	private boolean isScheduled;
	private Long mediumId;
	private double discount;
	private double commission;
	private String comments;
	private Long operatorId;
	private String operatorEmail;
	private String ipAddress;
	private String userAgent;
	
	public Transaction(long id, String transactionId, Timestamp transactionDate, long transactionUserDataId,
			long transactionPaymentDataId, int transactionStatus, long customerId, long applicationId, double amount,
			Integer paymentGwId, String authGw1, String authGw2, Integer sourceId, Long campaingId, long mediumId, int langId) {
		super();
		this.id = id;
		this.transactionId = transactionId;
		this.transactionDate = transactionDate;
		this.transactionUserDataId = transactionUserDataId;
		this.transactionPaymentDataId = transactionPaymentDataId;
		this.transactionStatus = transactionStatus;
		this.customerId = customerId;
		this.applicationId = applicationId;
		this.amount = amount;
		this.paymentGwId = paymentGwId;
		this.authGw1 = authGw1;
		this.authGw2 = authGw2;
		this.sourceId = sourceId;
		this.campaingId = campaingId;
		this.mediumId = mediumId;
		this.langId = langId;
	}
	public Transaction() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public Timestamp getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}
	public long getTransactionUserDataId() {
		return transactionUserDataId;
	}
	public void setTransactionUserDataId(long transactionUserDataId) {
		this.transactionUserDataId = transactionUserDataId;
	}
	public long getTransactionPaymentDataId() {
		return transactionPaymentDataId;
	}
	public void setTransactionPaymentDataId(long transactionPaymentDataId) {
		this.transactionPaymentDataId = transactionPaymentDataId;
	}
	public int getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(int transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Integer getPaymentGwId() {
		return paymentGwId;
	}
	public void setPaymentGwId(Integer paymentGwId) {
		this.paymentGwId = paymentGwId;
	}
	public String getAuthGw1() {
		return authGw1;
	}
	public void setAuthGw1(String authGw1) {
		this.authGw1 = authGw1;
	}
	public String getAuthGw2() {
		return authGw2;
	}
	public void setAuthGw2(String authGw2) {
		this.authGw2 = authGw2;
	}
	public Integer getSourceId() {
		return sourceId;
	}
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}
	public Long getCampaingId() {
		return campaingId;
	}
	public void setCampaingId(Long campaingId) {
		this.campaingId = campaingId;
	}
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}
	public boolean isScheduled() {
		return isScheduled;
	}
	public void setScheduled(boolean isScheduled) {
		this.isScheduled = isScheduled;
	}
	
	
	public Long getMediumId() {
		return mediumId;
	}
	public void setMediumId(Long mediumId) {
		this.mediumId = mediumId;
	}
	
	
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	
	
	public double getCommission() {
		return commission;
	}
	public void setCommission(double commission) {
		this.commission = commission;
	}
	
	
	
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	
	public String getOperatorEmail() {
		return operatorEmail;
	}
	public void setOperatorEmail(String operatorEmail) {
		this.operatorEmail = operatorEmail;
	}
	
	
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transaction [id=");
		builder.append(id);
		builder.append(", transactionId=");
		builder.append(transactionId);
		builder.append(", transactionDate=");
		builder.append(transactionDate);
		builder.append(", transactionUserDataId=");
		builder.append(transactionUserDataId);
		builder.append(", transactionPaymentDataId=");
		builder.append(transactionPaymentDataId);
		builder.append(", transactionStatus=");
		builder.append(transactionStatus);
		builder.append(", customerId=");
		builder.append(customerId);
		builder.append(", applicationId=");
		builder.append(applicationId);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", paymentGwId=");
		builder.append(paymentGwId);
		builder.append(", authGw1=");
		builder.append(authGw1);
		builder.append(", authGw2=");
		builder.append(authGw2);
		builder.append(", sourceId=");
		builder.append(sourceId);
		builder.append(", campaingId=");
		builder.append(campaingId);
		builder.append(", langId=");
		builder.append(langId);
		builder.append(", isScheduled=");
		builder.append(isScheduled);
		builder.append(", mediumId=");
		builder.append(mediumId);
		builder.append(", discount=");
		builder.append(discount);
		builder.append(", commission=");
		builder.append(commission);
		builder.append(", comments=");
		builder.append(comments);
		builder.append(", operatorId=");
		builder.append(operatorId);
		builder.append(", operatorEmail=");
		builder.append(operatorEmail);
		builder.append(", ipAddress=");
		builder.append(ipAddress);
		builder.append(", userAgent=");
		builder.append(userAgent);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	
	
	
	
	

	

	
	

}
