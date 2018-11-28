package com.merkapack.erp.gwt.client.js;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;

public class JsClient extends JavaScriptObject {
	protected JsClient() {}

	public final native Integer getId() 			 /*-{	return this.id;					}-*/;
	public final native int getDomain() 			 /*-{	return this.domain;				}-*/;
	public final native String getName() 			 /*-{	return this.name;				}-*/;
	public final native boolean isDirty() 			 /*-{	return this.dirty;				}-*/;
	public final native String getCreationUser() 	 /*-{	return this.creationUser;		}-*/;
	public final native Date getCreationDate() 		 /*-{	return this.creationDate;		}-*/;
	public final native String getModificationUser() /*-{	return this.modificationUser;	}-*/;
	public final native Date getModificationDate() 	 /*-{	return this.modificationDate;	}-*/;
	
}
