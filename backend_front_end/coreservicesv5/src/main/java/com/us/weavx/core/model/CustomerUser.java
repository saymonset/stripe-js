package com.us.weavx.core.model;

import java.util.Date;

public class CustomerUser implements Cloneable {
	
	private long id;
	private long customerId;
	private long userId;
	private String firstName;
	private String lastName;
	private String password;
	private int genderId;
	private Date birthDate;
	private String profileImage;
	private int countryId;
	private int statedId;
	private String stateText;
	private int cityId;
	private String cityText;
	private String address;
	private String postalCode;
	private String phoneNumber;
	public CustomerUser(long id, long customerId, long userId, String firstName, String lastName, String password,
			int genderId, Date birthDate, String profileImage, int countryId, int statedId, String stateText,
			int cityId, String cityText, String address, String postalCode, String phoneNumber) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.genderId = genderId;
		this.birthDate = birthDate;
		this.profileImage = profileImage;
		this.countryId = countryId;
		this.statedId = statedId;
		this.stateText = stateText;
		this.cityId = cityId;
		this.cityText = cityText;
		this.address = address;
		this.postalCode = postalCode;
		this.phoneNumber = phoneNumber;
	}
	public CustomerUser(long customerId, long userId, String firstName, String lastName, String password, int genderId,
			Date birthDate, String profileImage, int countryId, int statedId, String stateText, int cityId,
			String cityText, String address, String postalCode, String phoneNumber) {
		super();
		this.customerId = customerId;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.genderId = genderId;
		this.birthDate = birthDate;
		this.profileImage = profileImage;
		this.countryId = countryId;
		this.statedId = statedId;
		this.stateText = stateText;
		this.cityId = cityId;
		this.cityText = cityText;
		this.address = address;
		this.postalCode = postalCode;
		this.phoneNumber = phoneNumber;
	}
	public CustomerUser() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getGenderId() {
		return genderId;
	}
	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public int getStatedId() {
		return statedId;
	}
	public void setStatedId(int statedId) {
		this.statedId = statedId;
	}
	public String getStateText() {
		return stateText;
	}
	public void setStateText(String stateText) {
		this.stateText = stateText;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public String getCityText() {
		return cityText;
	}
	public void setCityText(String cityText) {
		this.cityText = cityText;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public String toString() {
		return "CustomerUser [id=" + id + ", customerId=" + customerId + ", userId=" + userId + ", firstName="
				+ firstName + ", lastName=" + lastName + ", password=" + password + ", genderId=" + genderId
				+ ", birthDate=" + birthDate + ", profileImage=" + profileImage + ", countryId=" + countryId
				+ ", statedId=" + statedId + ", stateText=" + stateText + ", cityId=" + cityId + ", cityText="
				+ cityText + ", address=" + address + ", postalCode=" + postalCode + ", phoneNumber=" + phoneNumber
				+ "]";
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	

	
}
