package com.merkapack.erp.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.merkapack.erp.gwt.client.common.MKPK;

public class MainEntryPoint implements EntryPoint {

	@Override
	public void onModuleLoad() {
		MKPK.ensureInjected();
		Menu menu = new Menu();
		menu.onModuleLoad();
	}
	
}
