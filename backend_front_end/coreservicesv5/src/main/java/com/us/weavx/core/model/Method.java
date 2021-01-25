package com.us.weavx.core.model;

public class Method {
	
	private int id;
	private String name;
	private String description;
	private String implementorClass;
	private int methodType;
	private int module;
	
	

	public Method(int id, String name, String description, String implementorClass, int methodType, int module) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.implementorClass = implementorClass;
		this.methodType = methodType;
		this.module = module;
	}



	public Method(String name, String description, String implementorClass, int methodType, int module) {
		super();
		this.name = name;
		this.description = description;
		this.implementorClass = implementorClass;
		this.methodType = methodType;
		this.module = module;
	}



	public Method() {
		// TODO Auto-generated constructor stub
	}



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



	public String getImplementorClass() {
		return implementorClass;
	}



	public void setImplementorClass(String implementorClass) {
		this.implementorClass = implementorClass;
	}



	public int getMethodType() {
		return methodType;
	}



	public void setMethodType(int methodType) {
		this.methodType = methodType;
	}



	public int getModule() {
		return module;
	}



	public void setModule(int module) {
		this.module = module;
	}



	@Override
	public String toString() {
		return "Method [id=" + id + ", name=" + name + ", description=" + description + ", implementorClass="
				+ implementorClass + ", methodType=" + methodType + ", module=" + module + "]";
	}
	
	

}
