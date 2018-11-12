package com.merkapack.erp.gwt.client.widget;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.merkapack.erp.gwt.client.common.MKPK;

public class MkpkIntegerLabel extends Label{

	private static final int CHANGE_DISPLAY_MILLIS = 4000;
	
	public MkpkIntegerLabel() {
		super();
		setStyleName(MKPK.CSS.mkpkInputText());
		addStyleName(MKPK.CSS.mkpkNumberLabel());
	}
	
	public void setValue(Integer value) {
		if (value == null) value = 0;
		super.setText(MKPK.FMT_INT.format(value));
	}
	
	public void setValue(Integer value, boolean shouldDisplayChange) {
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
