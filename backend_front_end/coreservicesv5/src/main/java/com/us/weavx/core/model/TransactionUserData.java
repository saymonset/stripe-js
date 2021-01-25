package com.us.weavx.core.model;

public class TransactionUserData {
	
	private long id;
	private String name;
	private String lastname;
	private Integer	country;
	private Integer	state;
	private String stateText;
	private Integer city;
	private String cityText;
	private String address;
	private String postcode;
	private String email;
	private Long customerUserId;
	private String countryText;
	public TransactionUserData(long id, String name, String lastname, Integer country, Integer state, String stateText,
			Integer city, String cityText, String address, String postcode, String email, Long customerUserId,
			String countryText) {
		super();
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.country = country;
		this.state = state;
		this.stateText = stateText;
		this.city = city;
		this.cityText = cityText;
		this.address = address;
		this.postcode = postcode;
		this.email = email;
		this.customerUserId = customerUserId;
		this.countryText = countryText;
	}
	public TransactionUserData(String name, String lastname, Integer country, Integer state, String stateText,
			Integer city, String cityText, String address, String postcode, String email, Long customerUserId,
			String countryText) {
		super();
		this.name = name;
		this.lastname = lastname;
		this.country = country;
		this.state = state;
		this.stateText = stateText;
		this.city = city;
		this.cityText = cityText;
		this.address = address;
		this.postcode = postcode;
		this.email = email;
		this.customerUserId = customerUserId;
		this.countryText = countryText;
	}
	
	public TransactionUserData() {
		super();
	}
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
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Integer getCountry() {
		return country;
	}
	public void setCountry(Integer country) {
		this.country = country;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getStateText() {
		return stateText;
	}
	public void setStateText(String stateText) {
		this.stateText = stateText;
	}
	public Integer getCity() {
		return city;
	}
	public void setCity(Integer city) {
		this.city = city;
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
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getCustomerUserId() {
		return customerUserId;
	}
	public void setCustomerUserId(Long customerUserId) {
		this.customerUserId = customerUserId;
	}
	public String getCountryText() {
		return countryText;
	}
	public void setCountryText(String countryText) {
		this.countryText = countryText;
	}
	@Override
	public String toString() {
		return "TransactionUserData [id=" + id + ", name=" + name + ", lastname=" + lastname + ", country=" + country
				+ ", state=" + state + ", stateText=" + stateText + ", city=" + city + ", cityText=" + cityText
				+ ", address=" + address + ", postcode=" + postcode + ", email=" + email + ", customerUserId="
				+ customerUserId + ", countryText=" + countryText + "]";
	}
	
	
}
