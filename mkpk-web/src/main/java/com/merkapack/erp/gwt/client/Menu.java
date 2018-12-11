package com.merkapack.erp.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.view.PlanningView;
import com.merkapack.erp.gwt.client.widget.MkpkDockLayout;

public class Menu extends MkpkDockLayout {
	
	public Menu() {
		add(getContent());
	}

	private Widget getContent() {
		FlexTable tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkBlockCenter());
		tab.addStyleName(MKPK.CSS.mkpkWidth400());
		
		Button planning = new Button(MKPK.MSG.manufacturingPlanning());
		planning.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				PlanningView planning = new PlanningView();
				planning.onModuleLoad();
			}
		});
		planning.setStyleName(MKPK.CSS.mkpkImgPlanning());
		tab.setWidget(0, 0, planning);
/*
		Button planning1 = new Button(MKPK.MSG.manufacturingPlanning());
		planning1.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Planning1View planning1 = new Planning1View();
				planning1.onModuleLoad();
			}
		});
		planning1.setStyleName(MKPK.CSS.mkpkImgPlanning());
		tab.setWidget(1, 0, planning1);

		Button planning2 = new Button(MKPK.MSG.manufacturingPlanning());
		planning2.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Planning2View planning2 = new Planning2View();
				planning2.onModuleLoad();
			}
		});
		planning2.setStyleName(MKPK.CSS.mkpkImgPlanning());
		tab.setWidget(2, 0, planning2);
*/
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
