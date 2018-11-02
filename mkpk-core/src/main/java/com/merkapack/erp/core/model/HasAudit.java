package com.merkapack.erp.core.model;

import java.util.Date;

public interface HasAudit {

	String getCreationUser();
	Date getCreationDate();
	String getModificationUser();
	Date getModificationDate();

}
