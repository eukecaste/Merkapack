package com.merkapack.erp.gwt.client.js;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;

public class JsPlanning extends JavaScriptObject {

	protected JsPlanning() {}
	
	public final native Integer getId() 			 /*-{	return this.id;					}-*/;
	public final native int getDomain() 			 /*-{	return this.domain;				}-*/;
	public final native Date getDate() 				 /*-{	return this.date;				}-*/;
	public final native int getOrder() 				 /*-{	return this.order;				}-*/;
	public final native JsMachine getMachine() 		 /*-{	return this.machine;			}-*/;
	public final native JsProduct getProduct() 		 /*-{	return this.product;			}-*/;
	public final native double getWidth() 			 /*-{	return this.width;				}-*/;
	public final native double getLength() 			 /*-{	return this.length;				}-*/;
	public final native JsMaterial getMaterial() 	 /*-{	return this.material;			}-*/;
	public final native JsRoll getRoll() 			 /*-{	return this.roll;				}-*/;
	public final native double getRollWidth() 		 /*-{	return this.rollWidth;			}-*/;
	public final native double getRollLength() 		 /*-{	return this.rollLength;			}-*/;
	public final native double getAmount() 			 /*-{	return this.amount;				}-*/;
	public final native int getBlowUnits() 			 /*-{	return this.blowUnits;			}-*/;
	public final native double getMeters() 			 /*-{	return this.meters;				}-*/;
	public final native double getBlows() 			 /*-{	return this.blows;				}-*/;
	public final native double getBlowsMinute() 	 /*-{	return this.blowsMinute;		}-*/;
	public final native double getMinutes() 		 /*-{	return this.minutes;			}-*/;
	public final native JsClient getClient() 		 /*-{	return this.client;				}-*/;
	public final native String getComments() 		 /*-{	return this.comments;			}-*/;
	public final native boolean isDirty() 			 /*-{	return this.dirty;				}-*/;
	public final native boolean isSelected()		 /*-{	return this.selected;			}-*/;
	public final native String getCreationUser() 	 /*-{	return this.creationUser;		}-*/;
	public final native Date getCreationDate() 		 /*-{	return this.creationDate;		}-*/;
	public final native String getModificationUser() /*-{	return this.modificationUser;	}-*/;
	public final native Date getModificationDate() 	 /*-{	return this.modificationDate;	}-*/;
}
