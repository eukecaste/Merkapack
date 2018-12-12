package com.merkapack.erp.core.model;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable, HasAudit {

	private static final long serialVersionUID = -4570869853880477932L;
	
	private Integer id;
	private int domain;
	private String code;
	private String name;
	private Material material;
	private Double width;
	private Double length;
	private Double boxUnits;
	private String mold;
	
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
	
	public String getCode() {
		return code;
	}
	public Product setCode(String code) {
		this.code = code;
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
	public String getMeasure() {
		return   (this.length==null?"":("" +this.length.intValue())
			+"x"+(this.width ==null?"":("" +this.width.intValue())))
				;
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
	
	public Double getBoxUnits() {
		return boxUnits;
	}
	public Product setBoxUnits(Double boxUnits) {
		this.boxUnits = boxUnits;
		return this;
	}
	
	public String getMold() {
		return mold;
	}
	public Product setMold(String mold) {
		this.mold = mold;
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
