package com.merkapack.erp.core.model;

import java.io.Serializable;
import java.util.Date;

public class Planning implements Serializable, HasAudit {
	
	public static enum PlanningCalculationStrategy implements Serializable {
		AMOUNT_CHANGED,
		METERS_CHANGED,
		TIME_CHANGED
	}
	
	private static final long serialVersionUID = -5728018105374452335L;
	
	private Integer id;
	private int domain;
	private Date date;
	private int order;
	private Machine machine;
	private Product product;
	private double width;
	private double length;
	private Material material;
	private Roll roll;
	private double rollWidth;
	private double rollLength;
	private double amount;
	private int blowUnits;
	private double meters;
	private double blows;
	private double blowsMinute;
	private double minutes;
	private Client client;
	private String comments;

	private String creationUser;
	private Date creationDate;
	private String modificationUser;
	private Date modificationDate;
	
	private boolean dirty = true;
	private PlanningCalculationStrategy strategy; 
	public Integer getId() {
		return id;
	}
	public Planning setId(Integer id) {
		this.id = id;
		return this;
	}
	
	public int getDomain() {
		return domain;
	}
	public Planning setDomain(int domain) {
		this.domain = domain;
		return this;
	}

	public Date getDate() {
		return date;
	}
	public Planning setDate(Date date) {
		this.date = date;
		return this;
	}
	public int getOrder() {
		return order;
	}
	public Planning setOrder(int order) {
		this.order = order;
		return this;
	}
	public Machine getMachine() {
		return machine;
	}
	public Planning setMachine(Machine machine) {
		this.machine = machine;
		return this;
	}
	public Product getProduct() {
		return product;
	}
	public Planning setProduct(Product product) {
		this.product = product;
		return this;
	}
	public double getWidth() {
		return width;
	}
	public Planning setWidth(double width) {
		this.width = width;
		return this;
	}
	public double getLength() {
		return length;
	}
	public Planning setLength(double length) {
		this.length = length;
		return this;
	}
	public Material getMaterial() {
		return material;
	}
	public Planning setMaterial(Material material) {
		this.material = material;
		return this;
	}
	public Roll getRoll() {
		return roll;
	}
	public Planning setRoll(Roll roll) {
		this.roll = roll;
		return this;
	}
	public double getRollWidth() {
		return rollWidth;
	}
	public Planning setRollWidth(double rollWidth) {
		this.rollWidth = rollWidth;
		return this;
	}
	public double getRollLength() {
		return rollLength;
	}
	public Planning setRollLength(double rollLength) {
		this.rollLength = rollLength;
		return this;
	}
	public double getAmount() {
		return amount;
	}
	public Planning setAmount(double amount) {
		this.amount = amount;
		return this;
	}
	public int getBlowUnits() {
		return this.blowUnits;
	}
	public Planning setBlowUnits(int blowUnits) {
		this.blowUnits = blowUnits;
		return this;
	}
	public double getMeters() {
		return meters;
	}
	public Planning setMeters(double meters) {
		this.meters = meters;
		return this;
	}
	public double getBlows() {
		return blows;
	}
	public Planning setBlows(double blows) {
		this.blows = blows;
		return this;
	}
	public double getBlowsMinute() {
		return blowsMinute;
	}
	public Planning setBlowsMinute(double blowsMinute) {
		this.blowsMinute = blowsMinute;
		return this;
	}
	public double getMinutes() {
		return minutes;
	}
	public Planning setMinutes(double minutes) {
		this.minutes = minutes;
		return this;
	}
	public Client getClient() {
		return client;
	}
	public Planning setClient(Client client) {
		this.client = client;
		return this;
	}
	public String getComments() {
		return comments;
	}
	public Planning setComments(String comments) {
		this.comments = comments;
		return this;
	}
	// ---------------------------------------------------------- DIRTY
	public boolean isDirty() {
		return dirty;
	}
	public Planning setDirty(boolean dirty) {
		this.dirty = dirty;
		return this;
	}
	public PlanningCalculationStrategy getStrategy() {
		return strategy;
	}
	public Planning setStrategy(PlanningCalculationStrategy strategy) {
		this.strategy = strategy;
		return this;
	}
	// ---------------------------------------------------------- AUDIT
	@Override
	public String getCreationUser() {
		return creationUser;
	}
	public Planning setCreationUser(String creationUser) {
		this.creationUser = creationUser;
		return this;
	}
	@Override
	public Date getCreationDate() {
		return creationDate;
	}
	public Planning setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
		return this;
	}
	@Override
	public String getModificationUser() {
		return modificationUser;
	}
	public Planning setModificationUser(String modificationUser) {
		this.modificationUser = modificationUser;
		return this;
	}
	@Override
	public Date getModificationDate() {
		return modificationDate;
	}
	public Planning setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
		return this;
	}
}
