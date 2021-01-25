package com.us.weavx.core.model;

public class SignatureGateway {

	private int id;
	private String name;
	private String sandboxUrl;
	private String productionUrl;
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
	public String getSandboxUrl() {
		return sandboxUrl;
	}
	public void setSandboxUrl(String sandboxUrl) {
		this.sandboxUrl = sandboxUrl;
	}
	public String getProductionUrl() {
		return productionUrl;
	}
	public void setProductionUrl(String productionUrl) {
		this.productionUrl = productionUrl;
	}
	@Override
	public String toString() {
		return "SignatureGateway [id=" + id + ", name=" + name + ", sandboxUrl=" + sandboxUrl + ", productionUrl="
				+ productionUrl + "]";
	}
	
	

}
