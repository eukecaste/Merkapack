package com.merkapack.erp.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.widget.MkpkDockLayout;

public class Menu extends MkpkDockLayout {
	
	public Menu() {
		add(getContent());
	}

	private Widget getContent() {
		FlexTable tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkBlockCenter());
		
		Button planning = new Button(MKPK.MSG.manufacturingPlanning());
		planning.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Planning planning = new Planning();
				planning.onModuleLoad();
			}
		});
		planning.setStyleName(MKPK.CSS.mkpkImgPlanning());
		tab.setWidget(0, 0, planning);

		Button inventory = new Button(MKPK.MSG.manufacturingInventory());
		inventory.setStyleName(MKPK.CSS.mkpkImgManufacturing());
		inventory.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Inventory inventory = new Inventory();
				inventory.onModuleLoad();
			}
		});
		tab.setWidget(1, 0, inventory);

		Button stats = new Button(MKPK.MSG.manufacturingStats());
		stats.setStyleName(MKPK.CSS.mkpkImgStats());
		stats.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Stats stats = new Stats();
				stats.onModuleLoad();
			}
		});
		tab.setWidget(2, 0, stats);
		
		return tab;		
	}

}
