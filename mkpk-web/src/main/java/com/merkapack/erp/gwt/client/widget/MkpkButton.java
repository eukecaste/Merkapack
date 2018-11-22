package com.merkapack.erp.gwt.client.widget;

import com.google.gwt.user.client.ui.Button;
import com.merkapack.erp.gwt.client.common.MKPK;

public class MkpkButton extends Button {
	
	public MkpkButton() {
		this( false );
	}
	
	public MkpkButton( boolean border) {
		super();
		setStyleName(MKPK.CSS.mkpkButton());
		if (border) {
			addStyleName(MKPK.CSS.mkpkBorder());
		}
	}
}
