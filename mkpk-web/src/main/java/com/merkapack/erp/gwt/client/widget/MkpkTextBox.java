package com.merkapack.erp.gwt.client.widget;

import com.google.gwt.user.client.ui.TextBox;
import com.merkapack.erp.gwt.client.common.MKPK;

public class MkpkTextBox extends TextBox {
	
	public MkpkTextBox() {
		super();
		setStyleName(MKPK.CSS.mkpkInputText());
	}
}
