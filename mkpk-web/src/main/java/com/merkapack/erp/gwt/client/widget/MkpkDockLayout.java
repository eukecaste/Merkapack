package com.merkapack.erp.gwt.client.widget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.merkapack.erp.gwt.client.Menu;
import com.merkapack.erp.gwt.client.common.MKPK;

public class MkpkDockLayout extends DockLayoutPanel implements EntryPoint {

	private FlexTable tabHeader; 
//	private Widget sidebar;
	
	public MkpkDockLayout() {
		super(Unit.PX);
		
		MKPK.ensureInjected();
		
		setStyleName(MKPK.CSS.mkpkNoMargin()); 
		addStyleName(MKPK.CSS.mkpkNoPadding());
		addNorth(getHeader(), 70);
		addSouth(getFooter(), 25);
//		sidebar = getSidebar();
//		addEast(sidebar, 0);
	}
	
	@Override
	public void onModuleLoad() {
		MKPK.ensureInjected();
		RootLayoutPanel.load(this);
	}

	private Widget getHeader() {
		tabHeader = new FlexTable();		
		tabHeader.setStyleName(MKPK.CSS.mkpkWidthAll());
		tabHeader.addStyleName(MKPK.CSS.mkpkBorderBottom());
		
		Image logoImage = new Image(MKPK.RESOURCES.mkpkLogo());
		logoImage.setWidth("250px");
		logoImage.setHeight("60px");
		logoImage.addStyleName(MKPK.CSS.mkpkPointer());
		logoImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Menu menu = new Menu();
				menu.onModuleLoad();
			}
		});
		tabHeader.setWidget(0, 0, logoImage);
		tabHeader.getCellFormatter().setStyleName(0, 0, MKPK.CSS.mkpkWidth10p());
		tabHeader.getFlexCellFormatter().setRowSpan(0, 0, 2);
		
		Label title = new Label(MKPK.MSG.manufacturing());
		title.setStyleName(MKPK.CSS.mkpkAppTitle());
		tabHeader.setWidget(0, 1, title);
		tabHeader.getCellFormatter().setStyleName(0, 1, MKPK.CSS.mkpkWidthAuto());
		tabHeader.getCellFormatter().addStyleName(0, 1, MKPK.CSS.mkpkTextCenter());

		FlowPanel buttonsContainer = new FlowPanel(); 

//		Button openSidebar = new Button();
//		openSidebar.setTitle("Men\u00FA");
//		openSidebar.setStyleName(MKPK.CSS.mkpkMenu());
//		openSidebar.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//		openSidebar.addStyleName(MKPK.CSS.mkpkNoBorder());
//		
//		Button closeSidebar = new Button();
//		closeSidebar.setTitle("Cerrar");
//		closeSidebar.setStyleName(MKPK.CSS.mkpkClose());
//		closeSidebar.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//		closeSidebar.addStyleName(MKPK.CSS.mkpkNoBorder());
//		closeSidebar.setVisible(false);
		
//		buttonsContainer.add(openSidebar);
//		buttonsContainer.add(closeSidebar);
		tabHeader.setWidget(1, 1, buttonsContainer);
		tabHeader.getCellFormatter().setStyleName(1, 1, MKPK.CSS.mkpkTextRight());
		tabHeader.getCellFormatter().addStyleName(1, 1, MKPK.CSS.mkpkPaddingRight());
		
//		openSidebar.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				openSidebar.setVisible(false);
//				closeSidebar.setVisible(true);
//				MkpkDockLayout.this.setWidgetSize(sidebar, 200);
//				MkpkDockLayout.this.animate(500);
//			}
//		});
//		
//		closeSidebar.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				openSidebar.setVisible(true);
//				closeSidebar.setVisible(false);
//				MkpkDockLayout.this.setWidgetSize(sidebar, 0);
//				MkpkDockLayout.this.animate(500);
//			}
//		});
		
		return tabHeader;
	}

	private Widget getFooter() {
		FlowPanel footerPanel = new FlowPanel();
		footerPanel.addStyleName(MKPK.CSS.mkpkBorderTop());
		Label footer = new Label("\u00A9 MERKAPACK. C/ Uzbina, 21, Pol\u00EDgono industrial de Jundiz 01015 - Vitoria - Gasteiz (Alava) - 945 288 787");
		footerPanel.add(footer);
		return footerPanel;
	}

//	private Widget getSidebar() {
//		ScrollPanel scroll = new ScrollPanel();
//		scroll.setStyleName(MKPK.CSS.mkpkBorderLeft());
//		
//		FlexTable toc = new FlexTable();
//		toc.setStyleName(MKPK.CSS.mkpkWidthAll());
//		
//		int row = 0;
//		Button machines = new Button(MKPK.MSG.machines());
//		machines.setStyleName(MKPK.CSS.mkpkIconBullet());
//		machines.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//		machines.addStyleName(MKPK.CSS.mkpkNoBorder());
//		machines.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				MachineView machine = new MachineView();
//				machine.onModuleLoad();
//			}
//		});
//		toc.setWidget(row, 0, machines);
//		++row;
//
//		Button materials = new Button(MKPK.MSG.materials());
//		materials.setStyleName(MKPK.CSS.mkpkIconBullet());
//		materials.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//		materials.addStyleName(MKPK.CSS.mkpkNoBorder());
//		materials.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				MaterialView material = new MaterialView();
//				material.onModuleLoad();
//			}
//		});
//		toc.setWidget(row, 0, materials);
//		++row;
//		
//		Button products = new Button(MKPK.MSG.products());
//		products.setStyleName(MKPK.CSS.mkpkIconBullet());
//		products.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//		products.addStyleName(MKPK.CSS.mkpkNoBorder());
//		products.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				ProductView product = new ProductView();
//				product.onModuleLoad();
//			}
//		});
//		toc.setWidget(row, 0, products);
//		++row;
//
//		Button rolls = new Button(MKPK.MSG.rolls());
//		rolls.setStyleName(MKPK.CSS.mkpkIconBullet());
//		rolls.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//		rolls.addStyleName(MKPK.CSS.mkpkNoBorder());
//		rolls.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				RollView roll = new RollView();
//				roll.onModuleLoad();
//			}
//		});
//		toc.setWidget(row, 0, rolls);
//		++row;
//
//		Button clients = new Button(MKPK.MSG.clients());
//		clients.setStyleName(MKPK.CSS.mkpkIconBullet());
//		clients.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//		clients.addStyleName(MKPK.CSS.mkpkNoBorder());
//		clients.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				ClientView client = new ClientView();
//				client.onModuleLoad();
//			}
//		});
//		toc.setWidget(row, 0, clients);
//		++row;
//		
//		scroll.add(toc);
//		
//		return scroll;
//	}

	protected void showError(Throwable caught) {
		MkpkCustomDialog dialog = new MkpkCustomDialog();
		dialog.setCaption(MKPK.MSG.configuration());
		dialog.setGlassEnabled(true);
		dialog.setAnimationEnabled(true);
		Label errorLabel = new Label(caught.getMessage());
		errorLabel.setStyleName(MKPK.CSS.mkpkErrorLabel());
		dialog.add(errorLabel);
		dialog.center();
		dialog.show();
	}
}
