package com.merkapack.erp.gwt.shared;

import java.io.Serializable;

import com.merkapack.watson.util.MkpkMathUtils;

public class PlanningCalculatorParams implements Serializable {
	
	private static final long serialVersionUID = -7718863342559517207L;
	
	private double blowsMinute;
	private double workHoursInADay;
	private double hoursMargin;
	
	public double getBlowsMinute() {
		return blowsMinute;
	}
	public PlanningCalculatorParams setBlowsMinute(double blowsMinute) {
		this.blowsMinute = blowsMinute;
		return this;
	}
	public double getHoursMargin() {
		return hoursMargin;
	}
	public PlanningCalculatorParams setHoursMargin(double hoursMargin) {
		this.hoursMargin = hoursMargin;
		return this;
	}
	
	public double getWorkHoursInADay() {
		return workHoursInADay;
	}
	public PlanningCalculatorParams setWorkHoursInADay(double workHoursInADay) {
		this.workHoursInADay = workHoursInADay;
		return this;
	}
	public double getMinutesBreak() {
		return MkpkMathUtils.round(getWorkHoursInADay() * 60) - (getHoursMargin() * 60);
	}
 
}
















