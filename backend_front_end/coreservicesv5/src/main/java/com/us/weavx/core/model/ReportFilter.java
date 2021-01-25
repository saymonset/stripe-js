package com.us.weavx.core.model;

public class ReportFilter {
	
	private String fieldName;
	private Object fieldValue;
	public ReportFilter(String fieldName, Object fieldValue) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	public ReportFilter() {
		super();
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Object getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}
	@Override
	public String toString() {
		return "ReportFilter [fieldName=" + fieldName + ", fieldValue=" + fieldValue + "]";
	}

	

}
