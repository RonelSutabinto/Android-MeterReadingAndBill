package com.zaneco.notice48hrs.database;

public class Unpaid {
	protected long id;
	protected long code;
	protected String billMonth;
	protected double amount;

    public Unpaid() {
        this.id = -1;
        this.code = -1;
        this.billMonth = "";
        this.amount = 0;
    }

    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCode() {
		return code;
	}
	public void setCode(long code) {
		this.code = code;
	}
	public String getBillMonth() {
		return billMonth;
	}
	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
