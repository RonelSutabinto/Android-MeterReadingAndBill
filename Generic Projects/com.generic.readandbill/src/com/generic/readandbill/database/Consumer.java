package com.generic.readandbill.database;

public class Consumer {
	protected long id;
	protected String accountNumber;
	protected String name;
	protected String address;
	protected String rateCode;
	protected String rcode;
	protected String meterSerial;
	protected String initialReadingDate;
	protected double initialReading;
	protected double multiplier;
	protected double transformerRental;
	protected double demand;
	//protected double demandRdng;
	protected Integer connectionCode;
	protected Boolean cmSwitch;
	protected double cmMultiplier;
	protected double cmReading;
	protected double cmInitialReading;
	protected double cmDemand;
	protected long code;
	protected Integer isfixdemand;
	protected String poleNumber;
	protected String xFormerKVA;
	protected Integer xFormerQty;
	protected Integer isGram;


	public Consumer() {
		this.id = 0;
		this.accountNumber = "";
		this.name = "";
		this.address = "";
		this.rateCode = "";
		this.meterSerial = "";
		this.initialReadingDate = "";
		this.initialReading = 0;
		this.multiplier = 0;
		this.transformerRental = 0;
		this.demand = 0;
		//this.demandRdng = 0;
		this.connectionCode = 0;
		this.cmSwitch = false;
		this.cmMultiplier = 0;
		this.cmReading = 0;
		this.cmInitialReading = 0;
		this.cmDemand = 0;
		this.code = 0;
		this.isfixdemand = 0;
		this.poleNumber = "";
		this.xFormerQty = 0;
		this.xFormerKVA = "";
		this.rcode="";
		this.isGram = 0;
		
	}
	
	public Integer getisGram(){
		return isGram;
	}
	public void setisGram(Integer isgram){
		this.isGram = isgram;
	}
	public void setRcode(String rCode)
	{
		this.rcode = rCode;
	}
    public String getRcode()
    {
    	return rcode;
    }
	public void setXFormerQty(Integer xformerqty)
	{
		this.xFormerQty = xformerqty; 
	}
	public Integer getXFormerQty()
	{
		return xFormerQty;
	}
	public void setXFormerKVA(String xformerkva)
	{
		this.xFormerKVA = xformerkva;
	}
	public String getXFormerKVA()
	{
		return xFormerKVA;
	}
	public void setPoleNumber(String pole) {
		this.poleNumber = pole;
	}

	public String getPoleNumber() {
		return poleNumber;
	}

	public void setIsfixdemand(Integer isdemand) {
		this.isfixdemand = isdemand;
	}

	public Integer getIsfixdemand() {
		return isfixdemand;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
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

	public String getRateCode() {
		return rateCode;
	}

	public void setRateCode(String rateRef) {
		this.rateCode = rateRef;
	}

	public String getMeterSerial() {
		return meterSerial;
	}

	public void setMeterSerial(String meterSerial) {
		this.meterSerial = meterSerial;
	}

	public double getInitialReading() {
		return initialReading;
	}

	public void setInitialReading(double initialReading) {
		this.initialReading = initialReading;
	}

	public String getInitialReadingDate() {
		return initialReadingDate;
	}

	public void setInitialReadingDate(String initialReadingDate) {
		this.initialReadingDate = initialReadingDate;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		if (multiplier == 0) {
			this.multiplier = 1.0;
		} else {
			this.multiplier = multiplier;
		}
	}

	public double getTransformerRental() {
		return transformerRental;
	}

	public void setTransformerRental(double transformerRental) {
		this.transformerRental = transformerRental;
	}

	@Override
	public String toString() {
		return String.valueOf(id);
	}

	/*public double getDemandRdng()
	{
		return demandRdng;
	}
	public void setDemandRdng(double demandrdng)
	{
		this.demandRdng = demandrdng;
	}*/
	public double getDemand() {
		return demand;
	}

	public void setDemand(double demand) {
		this.demand = demand;
	}

	public Integer getConnectionCode() {
		return connectionCode;
	}

	public void setConnectionCode(Integer connectionCode) {
		this.connectionCode = connectionCode;
	}

	public Boolean getCmSwitch() {
		if (cmSwitch == true) {
			return true;
		} else {
			return false;
		}
	}

	public void setCmSwitch(Boolean cmSwitch) {
		this.cmSwitch = cmSwitch;
	}
	
	
	public double getCmMultiplier() {
		return cmMultiplier;
	}

	public void setCmMultiplier(double cmMultiplier) {
		this.cmMultiplier = cmMultiplier;
	}

	public double getCmInitialReading() {
		return cmInitialReading;
	}

	public void setCmInitialReading(double cmInitialReading) {
		this.cmInitialReading = cmInitialReading;
	}

	public double getCmReading() {
		return cmReading;
	}

	public void setCmReading(double cmReading) {
		this.cmReading = cmReading;
	}

	public double getCmDemand() {
		return cmDemand;
	}

	public void setCmDemand(double cmDemand) {
		this.cmDemand = cmDemand;
	}

	public double getCMKilowatthourRead() {
		double kwhr = 0.0;
		double CMmul = getCmMultiplier();

		if (CMmul == 0)
			CMmul = 1;

		if (getCmSwitch()) {
			kwhr = getCmReading() - getCmInitialReading();
			kwhr = kwhr * CMmul;
		}
		return kwhr;
	}
}
