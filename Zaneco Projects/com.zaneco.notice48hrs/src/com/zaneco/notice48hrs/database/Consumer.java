package com.zaneco.notice48hrs.database;

public class Consumer extends com.generic.notice48hrs.database.Consumer {

    protected long code;
    protected int servedr;
    
    public Consumer() {
        super();
        this.code = -1;
        this.servedr = 0;
        //this.served = false;
    }

    public long getCode() {
        return code;
    }
    public void setCode(long code) {
        this.code = code;
    }
   public int getServedr() {
		return servedr;
	}

    public void setServedr(int served) {
        this.servedr = served;
    }

   
}
