package com.merkapack.erp.gwt.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.merkapack.erp.gwt.client.common.css.CSS;
import com.merkapack.erp.gwt.client.common.css.Resources;
import com.merkapack.erp.gwt.client.common.i18n.MkpkMessages;

public class MKPK {
	
	public static final MkpkMessages MSG = GWT.create(MkpkMessages.class);
	public static final Resources RESOURCES = GWT.create(Resources.class);
	public static final CSS CSS = GWT.<Resources> create(Resources.class).css();
	
	public static final NumberFormat FMT = NumberFormat.getFormat(MSG.decimalPattern());
	public static final NumberFormat FMT_INT = NumberFormat.getFormat(MSG.integerPattern());
	public static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat(MSG.datePattern());
	public static final DateTimeFormat DAY_FORMAT = DateTimeFormat.getFormat(MSG.dayPattern());

	public static void ensureInjected() {
		CSS.ensureInjected();
	}
	
	// Controls for RPC Methods
	public static void start() {}
	public static void stop() {}
	public static void fail() {}

}
