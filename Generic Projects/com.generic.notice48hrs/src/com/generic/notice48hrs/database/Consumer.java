package com.generic.notice48hrs.database;

public class Consumer {
	protected long id;
    protected String name;
    protected String address;
    protected String accountNumber;
    protected double arrears;
    protected boolean served;
    protected String meterSerial;
    /*private String route;*/

    public Consumer() {
        this.id = -1;
        this.name = "";
        this.address = "";
        this.accountNumber = "";
        this.arrears = 0;
        this.served = false;
        this.meterSerial = "";
    }

    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getArrears() {
		return arrears;
	}
	public void setArrears(double arrears) {
		this.arrears = arrears;
	}
	
	public boolean isServed() {
		return served;
	}

    public void setServed(boolean served) {
        this.served = served;
    }

    public int valueServed(){
        if (served)
            return 1;
        else
            return 0;
    }
    public String getMeterSerial() {
        return meterSerial;
    }
    public void setMeterSerial(String meterSerial) {
        this.meterSerial = meterSerial;
    }

    /*public void setRoute(String route) {
        this.route = route;
    }

    public String getRoute() {
        return route;
    }  */
}
