package com.merkapack.erp.gwt.client.view;

import java.util.LinkedList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.merkapack.erp.core.model.Material;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.widget.MkpkButton;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog.MkpkConfirmDialogCallback;
import com.merkapack.erp.gwt.client.widget.MkpkDockLayout;
import com.merkapack.erp.gwt.client.widget.MkpkDoubleBox;
import com.merkapack.erp.gwt.client.widget.MkpkMaterialBox;
import com.merkapack.erp.gwt.client.widget.MkpkProductBox;
import com.merkapack.erp.gwt.client.widget.MkpkTextBox;

public class PlanningView extends MkpkDockLayout  {
	
	private ScrollPanel content; 
	
	public PlanningView() {
		content = new ScrollPanel();
		content.setWidget(getContent());
		add(content);
	}

	private Widget getContent() {
		final FlexTable tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkBlockCenter());
		tab.addStyleName(MKPK.CSS.mkpkWidthAll());
		tab.getColumnFormatter().setWidth( 0, "1%");
		tab.getColumnFormatter().setWidth( 1, "1%");
		tab.getColumnFormatter().setWidth( 2, "1%");
		tab.getColumnFormatter().setWidth( 3, "1%");
		tab.getColumnFormatter().setWidth( 4, "1%");
		tab.getColumnFormatter().setWidth( 5, "1%");
		tab.getColumnFormatter().setWidth( 6, "1%");
		tab.getColumnFormatter().setWidth( 7, "1%");
		tab.getColumnFormatter().setWidth( 8, "1%%");
		tab.getColumnFormatter().setWidth( 9, "1%");
		tab.getColumnFormatter().setWidth(10, "1%");
		tab.getColumnFormatter().setWidth(11, "1%");
		tab.getColumnFormatter().setWidth(12, "auto");
		tab.getColumnFormatter().setWidth(13, "1%");
		
		
		String[] labels = new String[] {
			 " "
			,MKPK.MSG.measure()
			,MKPK.MSG.material()
			,"Bobina"
			,"Unidad"
			,"Unids/Golpe"
			,"Metros"
			,"Golpes"
			,"Golpes/Min."
			,"Tiempo"
			,"Dias"
			,"Cliente"
			,"Comentarios"
			,"X"
			};
		for (int col = 0; col < labels.length; col++) {
			Label label = new Label( labels[col]);
			label.setStyleName(MKPK.CSS.mkpkBold());
			tab.setWidget(0, col, label);
		}
		LinkedList<Planning> list = new LinkedList<Planning>();
		for (int col = 0; col < labels.length; col++) {
			Planning plan = new Planning();
			list.add(plan);	
			paintRow(tab,plan);
		}
		return tab;		
	}

	protected void paintRow(FlexTable tab, final Planning plan) {
		int row = tab.getRowCount();
		
		
		MkpkProductBox product = new MkpkProductBox();
		MkpkMaterialBox material = new MkpkMaterialBox();
		MkpkButton deleteButton = new MkpkButton();		
		MkpkDoubleBox rollWidth = new MkpkDoubleBox();
		MkpkDoubleBox unit = new MkpkDoubleBox();
		MkpkDoubleBox blowUnits = new MkpkDoubleBox();
		MkpkDoubleBox meters = new MkpkDoubleBox();
		MkpkDoubleBox blows = new MkpkDoubleBox();
		MkpkDoubleBox blowsMinute = new MkpkDoubleBox();
		MkpkDoubleBox minutes = new MkpkDoubleBox();
		MkpkTextBox date = new MkpkTextBox();
		MkpkTextBox client = new MkpkTextBox();
		Label comment = new Label();
		
		int col = 0;
		
		// DRAGGER
		col++;
		
		// PRODUCT
		product.setVisibleLength(10);
		tab.setWidget(row, col, product);
		product.addSelectionHandler(new SelectionHandler<Product>() {
			
			@Override
			public void onSelection(SelectionEvent<Product> event) {
				Product p = event.getSelectedItem();
				plan.setProduct(p);
				plan.setMaterial(p.getMaterial());
				plan.setWidth(p.getWidth());
				plan.setHeight(p.getHeight());
				material.setValue(p.getMaterial(), false);
			}
		});
		++col;
		
		// MATERIAL
		material.setVisibleLength(10);
		tab.setWidget(row, col, material);
		material.addSelectionHandler(new SelectionHandler<Material>() {
			@Override
			public void onSelection(SelectionEvent<Material> event) {
				plan.setMaterial(event.getSelectedItem());
			}
		});
		++col;
		
		// BOBINA
		rollWidth.setVisibleLength(5);
		tab.setWidget(row, col, rollWidth);
		++col;
		// BOBINA
		unit.setVisibleLength(5);
		tab.setWidget(row, col, unit);
		++col;
		// UNIDAD
		blowUnits.setVisibleLength(5);
		tab.setWidget(row, col, blowUnits);
		++col;
		// UNIDADES GOLPES
		meters.setVisibleLength(5);
		tab.setWidget(row, col, meters);
		++col;
		// METROS
		blows.setVisibleLength(5);
		tab.setWidget(row, col, blows);
		++col;
		// GOLPES
		blowsMinute.setVisibleLength(5);
		tab.setWidget(row, col, blowsMinute);
		++col;
		// GOLPES MINUTO
		minutes.setVisibleLength(5);
		tab.setWidget(row, col, minutes);
		++col;
		// TIEMPO
		date.setVisibleLength(10);
		tab.setWidget(row, col, date);
		++col;
		// DIA
		client.setVisibleLength(10);
		tab.setWidget(row, col, client);
		++col;
		// COMENTARIOS
		tab.setWidget(row, col, comment);
		++col;

		

		// DELETE BUTTON
		deleteButton.setTitle(MKPK.MSG.delete());
		deleteButton.addStyleName(MKPK.CSS.mkpkIconDelete());
		deleteButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MkpkConfirmDialog cd = new MkpkConfirmDialog();
				cd.confirm(MKPK.MSG.deleteConfirmation(), MKPK.MSG.delete(),new MkpkConfirmDialogCallback() {
					
					@Override public void onCancel() {}
					@Override public void onAccept() {}
				});
				
			}
		});
		tab.setWidget(row, col, deleteButton);
	}
}

