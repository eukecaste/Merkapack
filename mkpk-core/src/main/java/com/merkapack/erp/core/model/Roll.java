package com.merkapack.erp.core.model;

import java.io.Serializable;
import java.util.Date;

public class Roll implements Serializable, HasAudit {

	private static final long serialVersionUID = -5799101237589801244L;
	
	private Integer id;
	private int domain;
	private Material material;
	private String name;
	private double width;
	private double length;
	
	private String creationUser;
	private Date creationDate;
	private String modificationUser;
	private Date modificationDate;
	
	private boolean dirty = true;

	public Integer getId() {
		return id;
	}
	public Roll setId(Integer id) {
		this.id = id;
		return this;
	}
	
	public int getDomain() {
		return domain;
	}
	public Roll setDomain(int domain) {
		this.domain = domain;
		return this;
	}
	
	public Material getMaterial() {
		return material;
	}
	public Roll setMaterial(Material material) {
		this.material = material;
		return this;
	}
	public String getName() {
		return name;
	}
	public Roll setName(String name) {
		this.name = name;
		return this;
	}
	
	public double getWidth() {
		return width;
	}
	public Roll setWidth(double width) {
		this.width = width;
		return this;
	}

	public double getLength() {
		return length;
	}
	public Roll setLength(double length) {
		this.length = length;
		return this;
	}
	
	// ---------------------------------------------------------- DIRTY
	public boolean isDirty() {
		return dirty;
	}
	public Roll setDirty(boolean dirty) {
		this.dirty = dirty;
		return this;
	}
	
	// ---------------------------------------------------------- AUDIT
	@Override
	public String getCreationUser() {
		return creationUser;
	}
	public Roll setCreationUser(String creationUser) {
		this.creationUser = creationUser;
		return this;
	}
	@Override
	public Date getCreationDate() {
		return creationDate;
	}
	public Roll setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
		return this;
	}
	@Override
	public String getModificationUser() {
		return modificationUser;
	}
	public Roll setModificationUser(String modificationUser) {
		this.modificationUser = modificationUser;
		return this;
	}
	@Override
	public Date getModificationDate() {
		return modificationDate;
	}
	public Roll setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
		return this;
	}
	
}
