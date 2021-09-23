package com.zaneco.readandbill.database;

public class NewConnection {
	protected Long id;
	protected String name;
	protected String serial;
	protected Double reading;
	protected String remarks;
	protected Long transactionDate;
	protected String route;
	protected Integer state;
	
	public NewConnection() {
		this.id = (long) -1;
		this.name = "";
		this.serial = "";
		this.reading = 0.0;
		this.remarks = "";
		this.transactionDate = (long) -1;
		this.route = "";
		this.state = 0;
	}
	
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
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public Double getReading() {
		return reading;
	}
	public void setReading(Double reading) {
		this.reading = reading;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Long getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Long transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
}
