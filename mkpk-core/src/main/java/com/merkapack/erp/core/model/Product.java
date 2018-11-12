package com.merkapack.erp.core.model;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable, HasAudit {

	private static final long serialVersionUID = -4570869853880477932L;
	
	private Integer id;
	private int domain;
	private String name;
	private Material material;
	private Double width;
	private Double length;
	
	private String creationUser;
	private Date creationDate;
	private String modificationUser;
	private Date modificationDate;
	
	private boolean dirty = true;

	public Integer getId() {
		return id;
	}
	public Product setId(Integer id) {
		this.id = id;
		return this;
	}
	
	public int getDomain() {
		return domain;
	}
	public Product setDomain(int domain) {
		this.domain = domain;
		return this;
	}
	
	public String getName() {
		return name;
	}
	public Product setName(String name) {
		this.name = name;
		return this;
	}
	
	public Material getMaterial() {
		return material;
	}
	public Product setMaterial(Material material) {
		this.material = material;
		return this;
	}
	public Double getWidth() {
		return width;
	}
	public Product setWidth(Double width) {
		this.width = width;
		return this;
	}
	public Double getLength() {
		return length;
	}
	public Product setLength(Double length) {
		this.length = length;
		return this;
	}
	// ---------------------------------------------------------- DIRTY
	public boolean isDirty() {
		return dirty;
	}
	public Product setDirty(boolean dirty) {
		this.dirty = dirty;
		return this;
	}
	
	// ---------------------------------------------------------- AUDIT
	@Override
	public String getCreationUser() {
		return creationUser;
	}
	public Product setCreationUser(String creationUser) {
		this.creationUser = creationUser;
		return this;
	}
	@Override
	public Date getCreationDate() {
		return creationDate;
	}
	public Product setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
		return this;
	}
	@Override
	public String getModificationUser() {
		return modificationUser;
	}
	public Product setModificationUser(String modificationUser) {
		this.modificationUser = modificationUser;
		return this;
	}
	@Override
	public Date getModificationDate() {
		return modificationDate;
	}
	public Product setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
		return this;
	}
	
}
