package com.merkapack.erp.gwt.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.widget.MkpkDockLayout;

public class Product extends MkpkDockLayout  {
	
	public Product() {
		add(getContent());
	}

	private Widget getContent() {
		FlexTable tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkBlockCenter());
		
		Label planning = new Label(MKPK.MSG.products());
		tab.setWidget(0, 0, planning);
		
		return tab;		
	}
}
