package com.merkapack.erp.core.model;

import java.io.Serializable;
import java.util.Date;

public class Client implements Serializable, HasAudit {
	
	private static final long serialVersionUID = -3708099138525916117L;
	
	private Integer id;
	private int domain;
	private String name;

	private String creationUser;
	private Date creationDate;
	private String modificationUser;
	private Date modificationDate;
	
	private boolean dirty = true;

	public Integer getId() {
		return id;
	}
	public Client setId(Integer id) {
		this.id = id;
		return this;
	}
	
	public int getDomain() {
		return domain;
	}
	public Client setDomain(int domain) {
		this.domain = domain;
		return this;
	}
	
	public String getName() {
		return name;
	}
	public Client setName(String name) {
		this.name = name;
		return this;
	}
	
	// ---------------------------------------------------------- DIRTY
	public boolean isDirty() {
		return dirty;
	}
	public Client setDirty(boolean dirty) {
		this.dirty = dirty;
		return this;
	}
	
	// ---------------------------------------------------------- AUDIT
	public String getCreationUser() {
		return creationUser;
	}
	public Client setCreationUser(String creationUser) {
		this.creationUser = creationUser;
		return this;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public Client setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
		return this;
	}
	public String getModificationUser() {
		return modificationUser;
	}
	public Client setModificationUser(String modificationUser) {
		this.modificationUser = modificationUser;
		return this;
	}
	public Date getModificationDate() {
		return modificationDate;
	}
	public Client setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
		return this;
	}
	
}
