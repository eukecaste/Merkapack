package com.merkapack.erp.gwt.client.widget;


import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.PopupPanel;
import com.merkapack.erp.gwt.client.common.MKPK;

public class MkpkContextMenu extends PopupPanel{

	class ContextCommand implements ScheduledCommand{
		
		private ScheduledCommand cmd;
		
		public ContextCommand(ScheduledCommand cmd ) {
			this.cmd = cmd;
		}
		
		@Override
		public void execute() {
			MkpkContextMenu.this.hide();
			cmd.execute();
		}
	}
	
	MenuBar menuBar;
	
	public MkpkContextMenu() {
		super();
		menuBar = new MenuBar(true /*Vertical*/);
		add(menuBar);
		setStyleName("gwt-MenuBarPopup");
		setAutoHideEnabled(true);
	}
	
	public MenuItemSeparator addSeparator() {
		return menuBar.addSeparator();
	}
	
	public MenuItem addItem(SafeHtml html, MenuBar popup) {
		return menuBar.addItem(html, popup);
	}

	public MenuItem addItem(String text, MenuBar popup) {
		return menuBar.addItem(text, popup);
	}

	public MenuItemSeparator addSeparator(MenuItemSeparator separator) {
		return menuBar.addSeparator(separator);
	}

	public MenuItem addItem(String text, ScheduledCommand cmd) {
		return menuBar.addItem(text, new ContextCommand(cmd));
	}
	
	public MenuItem addItem(SafeHtml html, ScheduledCommand cmd) {
		return menuBar.addItem(html, new ContextCommand(cmd));
	}

	public MenuItem addItem(String text, boolean asHTML, MenuBar popup) {
		return menuBar.addItem(text, asHTML, popup);
	}

	public MenuItem addItem(String text, MenuBar popup, String ...styles) {
		return menuBar.addItem( getHTML(text, styles), true, popup);
	}

	public MenuItem addItem(String text, ScheduledCommand cmd, String ...styles) {
		return menuBar.addItem( getHTML(text, styles), true, new ContextCommand(cmd));
	}

	public MenuItem addItem(String model, String text, ScheduledCommand cmd) {
		return menuBar.addItem( getModelHTML(model,text), true, new ContextCommand(cmd));
	}

	
	public static String getHTML(String text, String ...styles ) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<span class='");
		for (String style : styles)
			buffer.append(style + ' ' );
		buffer.append("' >");
		buffer.append(text);
		buffer.append("</span>");
		
		return buffer.toString();
	}
	
	public static String getModelHTML(String model, String text) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div>");
		buffer.append("<span class='");
		buffer.append(MKPK.CSS.mkpkContextMenuOption());
		buffer.append("' style='width: 35px; padding: 2px;'>");
		buffer.append(model);
		buffer.append("</span>");
		buffer.append("<span class='");
		buffer.append(MKPK.CSS.mkpkMarginLeft());
		buffer.append("'>");
		buffer.append(text);
		buffer.append("</span>");
		buffer.append("</div>");
		return buffer.toString();
	}
}