package com.merkapack.erp.gwt.client.widget;

import java.text.ParseException;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasErrorHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ValueBox;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.watson.util.MkpkStringUtils;

public class MkpkIntegerBox extends ValueBox<Integer> implements HasErrorHandlers{

	public static final String EQUAL = MkpkStringUtils.EQUAL;
	public static final int VISIBLE_LENGTH = 12;
	public static final int PRECISION = 2;
	public static final int MAX_LENGTH = 15;
	private static final int CHANGE_DISPLAY_MILLIS = 4000;
	private int precision;
	
	private static final Renderer<Integer> RENDERER = new AbstractRenderer<Integer>() {

		@Override
		public String render(Integer object) {
			if (object == null)
				return "";
			return Integer.toString(object);
		}
	};

	private static final Parser<Integer> PARSER = new Parser<Integer>() {

		@Override
		public Integer parse(CharSequence text) throws ParseException {
			if (MkpkStringUtils.isEmpty(text))
				return 0;
			try {
				return Integer.parseInt(text.toString());
			} catch (NumberFormatException e) {
				throw new ParseException(e.getMessage(), 0);
			}

		}
	};
	
	public MkpkIntegerBox() {
		this(VISIBLE_LENGTH, PRECISION );
	}
	public MkpkIntegerBox(int visibleLength) {
		this(visibleLength, PRECISION );
	}
	public MkpkIntegerBox(int visibleLength, int precision) {
		super(Document.get().createTextInputElement(), RENDERER, PARSER);
		setPrecision( precision );
		setVisibleLength(visibleLength);
		setMaxLength(MAX_LENGTH);
		setStyleName(MKPK.CSS.mkpkInputText());
		addStyleName(MKPK.CSS.mkpkNumberBox());
		addResolver();
		addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if ( !MkpkStringUtils.startsWith(getText(),EQUAL) ) {
					removeStyleName(MKPK.CSS.mkpkInputCalc());
					try {
						getValueOrThrow();
						removeStyleName(MKPK.CSS.mkpkInputError());
					} catch (ParseException e) {
						addStyleName(MKPK.CSS.mkpkInputError());
						
					}
				}
			}
		});
	}
	
	private final native int nativeEval(String expression) /*-{
		return eval(expression);
	}-*/;
	private void addResolver() {
		addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if ( MkpkStringUtils.startsWith(getText(),EQUAL)) {
					setMaxLength(Integer.MAX_VALUE);
					addStyleName(MKPK.CSS.mkpkInputCalc());	
					if ( event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						String exp = MkpkStringUtils.substringAfter(getText(), MkpkIntegerBox.EQUAL);
						try {
							setValue(nativeEval(exp), true, true);
							setMaxLength(MAX_LENGTH);
							removeStyleName(MKPK.CSS.mkpkInputError());
							removeStyleName(MKPK.CSS.mkpkInputCalc());
						} catch (Throwable e) {
							setMaxLength(MAX_LENGTH);
							removeStyleName(MKPK.CSS.mkpkInputCalc());
							addStyleName(MKPK.CSS.mkpkInputError());
							NativeEvent ev = Document.get().createErrorEvent();
							DomEvent.fireNativeEvent(ev, MkpkIntegerBox.this);
						}
					}
				}
			}
		});
	}
	
	private int getPrecision() {
		return this.precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	@Override
	public void setValue(Integer value, boolean fireEvents) {
		if (value == null) value = 0;
		super.setValue(value, fireEvents);
	}
	
	public void setValue(Integer value, boolean fireEvents, boolean shouldDisplayChange) {
		setValue(value, fireEvents);
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

	@Override
	public HandlerRegistration addErrorHandler(ErrorHandler handler) {
		return addHandler(handler, ErrorEvent.getType());
	}
}
