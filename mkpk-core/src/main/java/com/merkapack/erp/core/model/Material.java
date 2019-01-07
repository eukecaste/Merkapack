package com.merkapack.erp.core.model;

import java.io.Serializable;
import java.util.Date;

public class Material implements Serializable, HasAudit {

	private static final long serialVersionUID = -7795449515443023516L;
	
	private Integer id;
	private int domain;
	private String code;
	private String name;
	private String rawMaterial;
	private String rawComposition;
	private double thickness;
	
	private String creationUser;
	private Date creationDate;
	private String modificationUser;
	private Date modificationDate;
	
	private boolean dirty = true;

	public Integer getId() {
		return id;
	}
	public Material setId(Integer id) {
		this.id = id;
		return this;
	}
	
	public int getDomain() {
		return domain;
	}
	public Material setDomain(int domain) {
		this.domain = domain;
		return this;
	}
	
	public String getCode() {
		return code;
	}
	public Material setCode(String code) {
		this.code = code;
		return this;
	}
	
	public String getName() {
		return name;
	}
	public Material setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getRawMaterial() {
		return rawMaterial;
	}
	public Material setRawMaterial(String rawMaterial) {
		this.rawMaterial = rawMaterial;
		return this;
	}
	
	public String getRawComposition() {
		return rawComposition;
	}
	public Material setRawComposition(String rawComposition) {
		this.rawComposition = rawComposition;
		return this;
	}
	
	public double getThickness() {
		return thickness;
	}
	public Material setThickness(double thickness) {
		this.thickness = thickness;
		return this;
	}
	
	// ---------------------------------------------------------- DIRTY
	public boolean isDirty() {
		return dirty;
	}
	public Material setDirty(boolean dirty) {
		this.dirty = dirty;
		return this;
	}
	
	// ---------------------------------------------------------- AUDIT
	@Override
	public String getCreationUser() {
		return creationUser;
	}
	public Material setCreationUser(String creationUser) {
		this.creationUser = creationUser;
		return this;
	}
	@Override
	public Date getCreationDate() {
		return creationDate;
	}
	public Material setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
		return this;
	}
	@Override
	public String getModificationUser() {
		return modificationUser;
	}
	public Material setModificationUser(String modificationUser) {
		this.modificationUser = modificationUser;
		return this;
	}
	@Override
	public Date getModificationDate() {
		return modificationDate;
	}
	public Material setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
		return this;
	}
	
}
