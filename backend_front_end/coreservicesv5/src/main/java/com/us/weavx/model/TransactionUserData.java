package com.us.weavx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="transaction_user_data")
public class TransactionUserData {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	private String lastname;
	@ManyToOne
	@JoinColumn(name="country")
	private Country country;
	private String countryText;
	@ManyToOne
	@JoinColumn(name="state")
	private State state;
	private String stateText;
	@ManyToOne
	@JoinColumn(name="city")
	private City city;
	private String cityText;
	private String address;
	private String postcode;
	private String email;
	private Long customer_user_id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public String getCountryText() {
		return countryText;
	}
	public void setCountryText(String countryText) {
		this.countryText = countryText;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public String getStateText() {
		return stateText;
	}
	public void setStateText(String stateText) {
		this.stateText = stateText;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
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
	public long getCustomer_user_id() {
		return customer_user_id;
	}
	public void setCustomer_user_id(long customer_user_id) {
		this.customer_user_id = customer_user_id;
	}
	@Override
	public String toString() {
		return "TransactionUserData [id=" + id + ", name=" + name + ", lastname=" + lastname + ", country=" + country
				+ ", countryText=" + countryText + ", state=" + state + ", stateText=" + stateText + ", city=" + city
				+ ", cityText=" + cityText + ", address=" + address + ", postcode=" + postcode + ", email=" + email
				+ ", customer_user_id=" + customer_user_id + "]";
	}
	
	

}
