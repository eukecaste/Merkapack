package com.merkapack.erp.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.view.ClientView;
import com.merkapack.erp.gwt.client.view.MachineView;
import com.merkapack.erp.gwt.client.view.MaterialView;
import com.merkapack.erp.gwt.client.view.PlanningView;
import com.merkapack.erp.gwt.client.view.ProductView;
import com.merkapack.erp.gwt.client.view.RollView;
import com.merkapack.erp.gwt.client.widget.MkpkDockLayout;

public class Menu extends MkpkDockLayout {
	
	public Menu() {
		FlexTable tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkBlockCenter());
		tab.addStyleName(MKPK.CSS.mkpkWidth90p());
		tab.getColumnFormatter().setWidth(0, "50%");
		tab.getColumnFormatter().setWidth(1, "50%");
		tab.setWidget(0, 0, getLeftTab());
		tab.getCellFormatter().setStyleName(0, 0, MKPK.CSS.mkpkMenuLeftCell());
		tab.setWidget(0, 1, getRightTab());
		tab.getCellFormatter().setStyleName(0, 0, MKPK.CSS.mkpkMenuRightCell());
		add(tab);
	}

	private FlexTable getRightTab() {
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

	private Widget getLeftTab() {
		FlexTable toc = new FlexTable();
		toc.setStyleName(MKPK.CSS.mkpkWidthAll());
		
		int row = 0;
		Button machines = new Button(MKPK.MSG.machines());
		machines.setStyleName(MKPK.CSS.mkpkImgMachine());
		machines.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				MachineView machine = new MachineView();
				machine.onModuleLoad();
			}
		});
		toc.setWidget(row, 0, machines);
		++row;
		
		Button materials = new Button(MKPK.MSG.materials());
		materials.setStyleName(MKPK.CSS.mkpkImgMaterial());
		materials.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				MaterialView material = new MaterialView();
				material.onModuleLoad();
			}
		});
		toc.setWidget(row, 0, materials);
		++row;
		
		Button products = new Button(MKPK.MSG.products());
		products.setStyleName(MKPK.CSS.mkpkImgProduct());
		products.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ProductView product = new ProductView();
				product.onModuleLoad();
			}
		});
		toc.setWidget(row, 0, products);
		++row;

		Button rolls = new Button(MKPK.MSG.rolls());
		rolls.setStyleName(MKPK.CSS.mkpkImgRoll());
		rolls.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				RollView roll = new RollView();
				roll.onModuleLoad();
			}
		});
		toc.setWidget(row, 0, rolls);
		++row;

		Button clients = new Button(MKPK.MSG.clients());
		clients.setStyleName(MKPK.CSS.mkpkImgClient());
		clients.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ClientView client = new ClientView();
				client.onModuleLoad();
			}
		});
		toc.setWidget(row, 0, clients);
		++row;
		return toc;
	}

}
