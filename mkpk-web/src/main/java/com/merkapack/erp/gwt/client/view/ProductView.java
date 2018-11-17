package com.merkapack.erp.gwt.client.view;

import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.merkapack.erp.core.model.Material;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.rpc.ProductService;
import com.merkapack.erp.gwt.client.rpc.ProductServiceAsync;
import com.merkapack.erp.gwt.client.rpc.ProductServiceAsyncDecorator;
import com.merkapack.erp.gwt.client.widget.MkpkButton;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog.MkpkConfirmDialogCallback;
import com.merkapack.erp.gwt.client.widget.MkpkDockLayout;
import com.merkapack.erp.gwt.client.widget.MkpkDoubleBox;
import com.merkapack.erp.gwt.client.widget.MkpkMaterialBox;
import com.merkapack.erp.gwt.client.widget.MkpkTextBox;
import com.merkapack.watson.util.MkpkStringUtils;

public class ProductView extends MkpkDockLayout  {
	
	private static ProductServiceAsync SERVICE;
	private SimpleLayoutPanel content;
	private final int deleteIconColumn = 4; 
	
	public ProductView() {
		ProductServiceAsync serviceRaw = GWT.create(ProductService.class);
		SERVICE = new ProductServiceAsyncDecorator(serviceRaw);
		content = new SimpleLayoutPanel();
		content.setWidget(getContent());
		add(content);
	}

	private Widget getContent() {
		ScrollPanel container = new ScrollPanel();
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName(MKPK.CSS.mkpkBlockCenter());
		container.setWidget(panel);
		
		final FlexTable tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkTable());
		tab.addStyleName(MKPK.CSS.mkpkBlockCenter());
		tab.getColumnFormatter().setWidth(0, "auto");
		tab.getColumnFormatter().setWidth(1, "150px");
		tab.getColumnFormatter().setWidth(2, "1%");
		tab.getColumnFormatter().setWidth(3, "1%");
		tab.getColumnFormatter().setWidth(4, "15px");
		
		int col = 0;
		Label nameLabel = new Label(MKPK.MSG.product());
		nameLabel.setStyleName(MKPK.CSS.mkpkBold());
		tab.setWidget(0, col, nameLabel);
		tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		col++;
		
		Label materialLabel = new Label(MKPK.MSG.material());
		materialLabel.setStyleName(MKPK.CSS.mkpkBold());
		tab.setWidget(0, col, materialLabel);
		tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		col++;

		Label lengthLabel = new Label(MKPK.MSG.length());
		lengthLabel.addStyleName(MKPK.CSS.mkpkTextRight());
		tab.setWidget(0, col, lengthLabel);
		tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		col++;
		
		Label widthLabel = new Label(MKPK.MSG.width());
		widthLabel.addStyleName(MKPK.CSS.mkpkTextRight());
		tab.setWidget(0, col, widthLabel);
		tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		col++;
		
		Label deleteLabel = new Label("X");
		tab.setWidget(0, col, deleteLabel);
		tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		col++;
		
		SERVICE.getProducts(new AsyncCallback<LinkedList<Product>>() {
			
			@Override
			public void onSuccess(LinkedList<Product> products) {
				for (Product product : products) {
					paintRow(tab,product);
				}
				paintRow(tab,new Product());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				ProductView.this.showError( caught );
			}
		});
		panel .add(tab);
		
		MkpkButton newLineButton = new MkpkButton();
		newLineButton.addStyleName(MKPK.CSS.mkpkIconPlus());
		newLineButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				paintRow(tab,new Product());
			}
		});
		panel.add(newLineButton);
		return container;		
	}

	protected void paintRow(FlexTable tab, final Product product) {
		int row = tab.getRowCount();
		MkpkTextBox nameBox = paintNameColumn(tab,row,0,product);
		paintMaterialColumn(tab,row,1,product);
		paintLengthColumn(tab, row, 2, product);
		paintWidthColumn(tab, row, 3, product);
		paintDeleteButton(tab,row,product);
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				nameBox.setFocus(true);
			}
		});
	}

	private MkpkTextBox paintNameColumn(FlexTable tab, int row, int col, Product product) {
		MkpkTextBox nameBox = new MkpkTextBox();
		nameBox.setValue(product.getName(), false);
		nameBox.setMaxLength(32);
		nameBox.setVisibleLength(40);
		tab.setWidget(row, col, nameBox);
		nameBox.addValueChangeHandler( new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				product.setName( nameBox.getValue() );
				save(tab,row,product);
			}
		});
		return nameBox;
	}

	private MkpkMaterialBox paintMaterialColumn(FlexTable tab, int row, int col, Product product) {
		MkpkMaterialBox materialBox = new MkpkMaterialBox();
		materialBox.setValue(product.getMaterial(), false);
		tab.setWidget(row, col, materialBox);
		materialBox.addSelectionHandler( new SelectionHandler<Material>() {
			
			@Override
			public void onSelection(SelectionEvent<Material> event) {
				product.setMaterial( event.getSelectedItem());
				save(tab,row,product);
			}
		});
		return materialBox;
	}

	private MkpkDoubleBox paintWidthColumn(FlexTable tab, int row, int col, Product product) {
		MkpkDoubleBox widthBox = new MkpkDoubleBox();
		widthBox.setVisibleLength(6);
		widthBox.setValue(product.getWidth(), false);
		tab.setWidget(row, col, widthBox);
		widthBox.addValueChangeHandler( new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				product.setWidth( widthBox.getValue() );
				save(tab, row, product);
			}
		});
		return widthBox;
	}

	private MkpkDoubleBox paintLengthColumn(FlexTable tab, int row, int col, Product product) {
		MkpkDoubleBox lengthBox = new MkpkDoubleBox();
		lengthBox.setVisibleLength(6);
		lengthBox.setValue(product.getLength(), false);
		tab.setWidget(row, col, lengthBox);
		lengthBox.addValueChangeHandler( new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				product.setLength( lengthBox.getValue() ); 
				save(tab, row, product);
			}
		});
		return lengthBox;
	}

	private void paintDeleteButton(FlexTable tab, int row, Product product) {
		if (product.getId() != null) {
			MkpkButton deleteButton = new MkpkButton();
			deleteButton.setTitle(MKPK.MSG.delete());
			deleteButton.addStyleName(MKPK.CSS.mkpkIconDelete());
			deleteButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					MkpkConfirmDialog cd = new MkpkConfirmDialog();
					cd.confirm(MKPK.MSG.deleteConfirmation(), MKPK.MSG.delete(),new MkpkConfirmDialogCallback() {
						
						@Override
						public void onCancel() {
						}
						
						@Override
						public void onAccept() {
							
							SERVICE.delete(product, new AsyncCallback<Void>() {
								
								@Override
								public void onSuccess(Void nothing) {
									ProductView.this.content.clear();
									ProductView.this.content.setWidget(getContent());
								}
								
								@Override
								public void onFailure(Throwable caught) {
									showError(caught);
								}
							});
						}
					});
					
				}
			});
			tab.setWidget(row, deleteIconColumn, deleteButton);
		} else {
			tab.setWidget(row, deleteIconColumn, new Label());
		}
	}
	private void save(FlexTable tab, int row, Product product) {
		if (MkpkStringUtils.isNotBlank( product.getName() )
			&& product.getMaterial().getId() != null) {
			SERVICE.save(product, new AsyncCallback<Product>() {
				
				@Override
				public void onSuccess(Product result) {
					if (product.getId() == null) {
						product.setId(result.getId());
						paintDeleteButton(tab,row,product);							
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					showError(caught);
				}
			});
		}
	}

}

