package com.merkapack.erp.gwt.client.widget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

public final class RootLayoutPanel extends LayoutPanel {
	
	private static final String ELEMENT_ID = "rootPanel"; 
	
	public static void load(MkpkDockLayout mkpkDockLayout) {
		RootPanel.get(ELEMENT_ID).clear();
		RootLayoutPanel.get().add(mkpkDockLayout);
	}

	public static RootLayoutPanel get() {
		RootLayoutPanel rootLayoutPanel = new RootLayoutPanel();
		RootPanel.get(ELEMENT_ID).add(rootLayoutPanel);
		return rootLayoutPanel;
	}

	private RootLayoutPanel() {
		Window.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				RootLayoutPanel.this.onResize();
			}
		});
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		fillParent();
	}

	private void fillParent() {
		
		Element elem = getElement();
		Element parent = elem.getParentElement();
		
		int top = parent.getOffsetTop();
		int left = parent.getOffsetLeft();
		
		Style style = elem.getStyle();
		style.setPosition(Position.ABSOLUTE);
		style.setLeft(left, Unit.PX);
		style.setTop(top, Unit.PX);
		style.setRight(0, Unit.PX);
		style.setBottom(0, Unit.PX);
	}
	
}