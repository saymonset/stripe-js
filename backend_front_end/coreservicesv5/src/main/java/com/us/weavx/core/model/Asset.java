package com.us.weavx.core.model;

public class Asset {

	public static final int URL_PLAYER = 1;
	public static final int BANNER_RIGHT_IMAGE = 2;
	public static final int BANNER_RIGHT_ACTION = 3;
	public static final int BANNER_LEFT_IMAGE = 4;
	public static final int BANNER_LEFT_ACTION = 5;
	public static final int LIVE_BACKGROUND_IMAGE = 6;
	
	private int id;
	private String name;
	private String description;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
		Asset other = (Asset) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Asset [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
	
	

}
