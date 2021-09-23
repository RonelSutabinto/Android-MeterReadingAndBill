package com.generic.readandbill.database;

import java.util.concurrent.TimeUnit;

import android.text.format.Time;

public class ZoneReport {
	protected Long qtyToBeRead;
	protected Long readingDate;
	protected Long minTime;
	protected Long maxTime;
	protected Long totalTime;

	public Long getQtyToBeRead() {
		return qtyToBeRead;
	}

	public void setQtyToBeRead(Long qtyToBeRead) {
		this.qtyToBeRead = qtyToBeRead;
	}

	public String getReadingDate() {
		Time myTime = new Time();
		myTime.set(readingDate);
		return myTime.format("%D");
	}

	public void setReadingDate(Long readingDate) {
		this.readingDate = readingDate;
	}

	public String getDay() {
		Time myTime = new Time();
		myTime.set(readingDate);
		return myTime.format("%A");
	}

	public String getMinTime() {
		Time myTime = new Time();
		myTime.set(minTime);
		return myTime.format("%H:%M:%S");
	}

	public void setMinTime(Long minTime) {
		this.minTime = minTime;
	}

	public String getMaxTime() {
		Time myTime = new Time();
		myTime.set(maxTime);
		return myTime.format("%H:%M:%S");
	}

	public void setMaxTime(Long maxTime) {
		this.maxTime = maxTime;
	}

	public String getTotalTime() {
		return String.format(
				"%d min %d sec",
				TimeUnit.MILLISECONDS.toMinutes(totalTime),
				TimeUnit.MILLISECONDS.toSeconds(totalTime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(totalTime)));
	}

	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}

	public String getAvgTime() {
		Long myTime = totalTime / getQtyToBeRead();
		return String.format(
				"%d min %d sec",
				TimeUnit.MILLISECONDS.toMinutes(myTime),
				TimeUnit.MILLISECONDS.toSeconds(myTime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(myTime)));
	}


}
