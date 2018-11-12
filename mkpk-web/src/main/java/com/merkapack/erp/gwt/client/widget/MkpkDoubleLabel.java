package com.merkapack.erp.gwt.client.widget;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.merkapack.erp.gwt.client.common.MKPK;

public class MkpkDoubleLabel extends Label{

	private static final int CHANGE_DISPLAY_MILLIS = 4000;
	
	public MkpkDoubleLabel() {
		super();
		setStyleName(MKPK.CSS.mkpkInputText());
		addStyleName(MKPK.CSS.mkpkNumberLabel());
	}
	
	public void setValue(Double value) {
		if (value == null) value = 0.0;
		super.setText(MKPK.FMT.format(value));
	}
	
	public void setValue(Double value, boolean shouldDisplayChange) {
		setValue(value);
		if (shouldDisplayChange) {
			addStyleName(MKPK.CSS.mkpkValueChanged());
			if (CHANGE_DISPLAY_MILLIS > 0)
				new Timer() {
					@Override
					public void run() {
						removeStyleName(MKPK.CSS.mkpkValueChanged());
					}
				}.schedule(CHANGE_DISPLAY_MILLIS);
		}
	}

}
