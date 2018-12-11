package com.merkapack.erp.core.model;

import java.io.Serializable;
import java.util.Date;

public class PlanningParams implements Serializable{
	
	private static final long serialVersionUID = -6019361163826878617L;
	
	private Integer machine;
	private Date date;
	
	public Integer getMachine() {
		return machine;
	}
	public PlanningParams setMachine(Integer machine) {
		this.machine = machine;
		return this;
	}
	public Date getDate() {
		return date;
	}
	public PlanningParams setDate(Date date) {
		this.date = date;
		return this;
	}
	
}
