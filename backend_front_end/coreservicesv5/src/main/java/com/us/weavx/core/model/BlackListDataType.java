package com.us.weavx.core.model;

public class BlackListDataType {
	
	private int id;
	private String name;
	private String validRegexp;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValidRegexp() {
		return validRegexp;
	}
	public void setValidRegexp(String validRegexp) {
		this.validRegexp = validRegexp;
	}
	@Override
	public String toString() {
		return "BlackListDataType [id=" + id + ", name=" + name + ", validRegexp=" + validRegexp + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlackListDataType other = (BlackListDataType) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	

}
