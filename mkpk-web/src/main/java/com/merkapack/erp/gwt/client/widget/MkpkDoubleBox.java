package com.merkapack.erp.gwt.client.widget;

import java.text.ParseException;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasErrorHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ValueBox;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.watson.util.MkpkMathUtils;
import com.merkapack.watson.util.MkpkStringUtils;

public class MkpkDoubleBox extends ValueBox<Double> implements HasErrorHandlers{
	Logger LOGGER = Logger.getLogger(MkpkDoubleBox.class.getName());
	
	public static final String EQUAL = MkpkStringUtils.EQUAL;
	public static final int VISIBLE_LENGTH = 12;
	public static final int PRECISION = 2;
	public static final int MAX_LENGTH = 15;
	private static final int CHANGE_DISPLAY_MILLIS = 4000;
	private int precision;
	
	private static final Renderer<Double> RENDERER = new AbstractRenderer<Double>() {

		@Override
		public String render(Double object) {
			if (object == null)
				return "";
			return Double.toString(object);
		}
	};

	private static final Parser<Double> PARSER = new Parser<Double>() {

		@Override
		public Double parse(CharSequence text) throws ParseException {
			if (MkpkStringUtils.isEmpty(text))
				return 0.0;
			try {
				return Double.parseDouble(text.toString());
			} catch (NumberFormatException e) {
				throw new ParseException(e.getMessage(), 0);
			}

		}
	};
	
	public MkpkDoubleBox() {
		this(VISIBLE_LENGTH, PRECISION );
	}
	public MkpkDoubleBox(int visibleLength) {
		this(visibleLength, PRECISION );
	}
	public MkpkDoubleBox(int visibleLength, int precision) {
		super(Document.get().createTextInputElement(), RENDERER, PARSER);
		setPrecision( precision );
		setVisibleLength(visibleLength);
		setMaxLength(MAX_LENGTH);
		setStyleName(MKPK.CSS.mkpkInputText());
		addStyleName(MKPK.CSS.mkpkNumberBox());
		addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if ( !MkpkStringUtils.startsWith(getText(),EQUAL) ) {
					removeStyleName(MKPK.CSS.mkpkInputCalc());
					try {
						getValueOrThrow();
						removeStyleName(MKPK.CSS.mkpkInputError());
					} catch (ParseException e) {
						addStyleName(MKPK.CSS.mkpkInputError());
					}
				} else {
					setMaxLength(Integer.MAX_VALUE);
					addStyleName(MKPK.CSS.mkpkInputCalc());	
					if ( event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
						String exp = MkpkStringUtils.substringAfter(getText(), MkpkDoubleBox.EQUAL);
						event.preventDefault();
						event.stopPropagation();
						try {
							LOGGER.severe("Calc value");							
							setValue(nativeEval(exp), true, true);
							setMaxLength(MAX_LENGTH);
							removeStyleName(MKPK.CSS.mkpkInputError());
							removeStyleName(MKPK.CSS.mkpkInputCalc());
						} catch (Throwable e) {
							setMaxLength(MAX_LENGTH);
							removeStyleName(MKPK.CSS.mkpkInputCalc());
							addStyleName(MKPK.CSS.mkpkInputError());
							NativeEvent ev = Document.get().createErrorEvent();
							DomEvent.fireNativeEvent(ev, MkpkDoubleBox.this);
						}
					}
				}
			}
		});
	}
	
	private final native double nativeEval(String expression) /*-{
		return eval(expression);
	}-*/;
	
	private int getPrecision() {
		return this.precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	@Override
	public void setValue(Double value, boolean fireEvents) {
		if (value == null) value = 0.0;
		super.setValue(MkpkMathUtils.round(value,getPrecision()), fireEvents);
	}
	
	public void setValue(Double value, boolean fireEvents, boolean shouldDisplayChange) {
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
