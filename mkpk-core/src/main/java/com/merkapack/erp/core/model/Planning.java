package com.merkapack.erp.core.model;

import java.io.Serializable;
import java.util.Date;

public class Planning implements Serializable, HasAudit, Cloneable {
	
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
	private boolean selected;
	
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
		if (!isDirty()) setDirty( true );
		this.domain = domain;
		return this;
	}

	public Date getDate() {
		return date;
	}
	public Planning setDate(Date date) {
		if (!isDirty()) setDirty( true );
		this.date = date;
		return this;
	}
	public int getOrder() {
		return order;
	}
	public Planning setOrder(int order) {
		if (!isDirty()) setDirty( true );
		this.order = order;
		return this;
	}
	public Machine getMachine() {
		return machine;
	}
	public Planning setMachine(Machine machine) {
		if (!isDirty()) setDirty( true );
		this.machine = machine;
		return this;
	}
	public Product getProduct() {
		return product;
	}
	public Planning setProduct(Product product) {
		if (!isDirty()) setDirty( true );
		this.product = product;
		return this;
	}
	public double getWidth() {
		return width;
	}
	public Planning setWidth(double width) {
		if (!isDirty()) setDirty( true );
		this.width = width;
		return this;
	}
	public double getLength() {
		return length;
	}
	public Planning setLength(double length) {
		if (!isDirty()) setDirty( true );
		this.length = length;
		return this;
	}
	public Material getMaterial() {
		return material;
	}
	public Planning setMaterial(Material material) {
		if (!isDirty()) setDirty( true );
		this.material = material;
		return this;
	}
	public Roll getRoll() {
		return roll;
	}
	public Planning setRoll(Roll roll) {
		if (!isDirty()) setDirty( true );
		this.roll = roll;
		return this;
	}
	public double getRollWidth() {
		return rollWidth;
	}
	public Planning setRollWidth(double rollWidth) {
		if (!isDirty()) setDirty( true );
		this.rollWidth = rollWidth;
		return this;
	}
	public double getRollLength() {
		return rollLength;
	}
	public Planning setRollLength(double rollLength) {
		if (!isDirty()) setDirty( true );
		this.rollLength = rollLength;
		return this;
	}
	public double getAmount() {
		return amount;
	}
	public Planning setAmount(double amount) {
		if (!isDirty()) setDirty( true );
		this.amount = amount;
		return this;
	}
	public int getBlowUnits() {
		return this.blowUnits;
	}
	public Planning setBlowUnits(int blowUnits) {
		if (!isDirty()) setDirty( true );
		this.blowUnits = blowUnits;
		return this;
	}
	public double getMeters() {
		return meters;
	}
	public Planning setMeters(double meters) {
		if (!isDirty()) setDirty( true );
		this.meters = meters;
		return this;
	}
	public double getBlows() {
		return blows;
	}
	public Planning setBlows(double blows) {
		if (!isDirty()) setDirty( true );
		this.blows = blows;
		return this;
	}
	public double getBlowsMinute() {
		return blowsMinute;
	}
	public Planning setBlowsMinute(double blowsMinute) {
		if (!isDirty()) setDirty( true );
		this.blowsMinute = blowsMinute;
		return this;
	}
	public double getMinutes() {
		return minutes;
	}
	public Planning setMinutes(double minutes) {
		if (!isDirty()) setDirty( true );
		this.minutes = minutes;
		return this;
	}
	public double getHours() {
		return minutes / 60;
	}
	public Planning setHours(double hours) {
		if (!isDirty()) setDirty( true );
		this.minutes = hours * 60;
		return this;
	}
	public Client getClient() {
		return client;
	}
	public Planning setClient(Client client) {
		if (!isDirty()) setDirty( true );
		this.client = client;
		return this;
	}
	public String getComments() {
		return comments;
	}
	public Planning setComments(String comments) {
		if (!isDirty()) setDirty( true );
		this.comments = comments;
		return this;
	}
	// ---------------------------------------------------------- SELECTED
	public boolean isSelected() {
		return selected;
	}
	public Planning setSelected(boolean selected) {
		this.selected = selected;
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
	// ---------------------------------------------------------- AUDIT
	@Override
	public String getCreationUser() {
		return creationUser;
	}
	public Planning setCreationUser(String creationUser) {
		if (!isDirty()) setDirty( true );
		this.creationUser = creationUser;
		return this;
	}
	@Override
	public Date getCreationDate() {
		return creationDate;
	}
	public Planning setCreationDate(Date creationDate) {
		if (!isDirty()) setDirty( true );
		this.creationDate = creationDate;
		return this;
	}
	@Override
	public String getModificationUser() {
		return modificationUser;
	}
	public Planning setModificationUser(String modificationUser) {
		if (!isDirty()) setDirty( true );
		this.modificationUser = modificationUser;
		return this;
	}
	@Override
	public Date getModificationDate() {
		return modificationDate;
	}
	public Planning setModificationDate(Date modificationDate) {
		if (!isDirty()) setDirty( true );
		this.modificationDate = modificationDate;
		return this;
	}
	
	public void initialize() {
		this.setWidth(0);
		this.setLength(0);
		this.setRollWidth(0);
		this.setRollLength(0);
		this.setAmount(0);
		this.setMeters(0);
		this.setBlows(0);
		this.setMinutes(0);
	}
	
	public Planning clone() {
		return 	new Planning()
			.setId(this.id)
			.setDomain(this.domain)
			.setDate(this.date) 
			.setOrder(this.order) 
			.setMachine(this.machine) 
			.setProduct(this.product) 
			.setWidth(this.width) 
			.setLength(this.length) 
			.setMaterial(this.material)
			.setRoll(this.roll) 
			.setRollWidth(this.rollWidth)
			.setRollLength(this.rollLength) 
			.setAmount(this.amount) 
			.setBlowUnits(this.blowUnits) 
			.setMeters(this.meters) 
			.setBlows(this.blows) 
			.setBlowsMinute(this.blowsMinute)
			.setMinutes(this.minutes) 
			.setClient(this.client) 
			.setComments(this.comments) 
			.setCreationUser(this.creationUser) 
			.setCreationDate(this.creationDate) 
			.setModificationUser(this.modificationUser) 
			.setModificationDate(this.modificationDate)
			.setSelected(this.isSelected())
			;
	}
}
