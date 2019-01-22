package com.merkapack.erp.gwt.client.view;

import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.merkapack.erp.core.model.Material;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.core.model.ProductParams;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.rpc.ProductService;
import com.merkapack.erp.gwt.client.rpc.ProductServiceAsync;
import com.merkapack.erp.gwt.client.rpc.ProductServiceAsyncDecorator;
import com.merkapack.erp.gwt.client.widget.MkpkButton;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog.MkpkConfirmDialogCallback;
import com.merkapack.erp.gwt.client.widget.MkpkDoubleBox;
import com.merkapack.erp.gwt.client.widget.MkpkTextBox;
import com.merkapack.watson.util.MkpkNumberUtils;

public class ProductList extends DockLayoutPanel  {
	
	private static ProductServiceAsync SERVICE;
	private int offset = 0;
	private int count = 20;
	
	private MkpkTextBox codeBox = new MkpkTextBox();
	private MkpkTextBox nameBox = new MkpkTextBox();
	private MkpkTextBox materialUpBox = new MkpkTextBox();
	private MkpkTextBox materialDownBox = new MkpkTextBox();
	private MkpkDoubleBox lengthBox = new MkpkDoubleBox();
	private MkpkDoubleBox widthBox = new MkpkDoubleBox();
	
	private ScrollPanel scroll = new ScrollPanel();

	public ProductList( ) {
		super(Unit.PX);
		setStyleName(MKPK.CSS.mkpkWidthAll());
		ProductServiceAsync serviceRaw = GWT.create(ProductService.class);
		SERVICE = new ProductServiceAsyncDecorator(serviceRaw);
		addNorth(getFilterTable(), 60);
		add(scroll);
		search();
	}
	private void search() {
		scroll.setWidget(getContent());
	}
	
	private FlexTable defineFlexTable() {
		final FlexTable tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkWidth90p());
		tab.addStyleName(MKPK.CSS.mkpkTable());
		tab.getColumnFormatter().setWidth(0, "50px");
		tab.getColumnFormatter().setWidth(1, "150px");
		tab.getColumnFormatter().setWidth(2, "auto");
		tab.getColumnFormatter().setWidth(3, "150px");
		tab.getColumnFormatter().setWidth(4, "150px");
		tab.getColumnFormatter().setWidth(5, "70px");
		tab.getColumnFormatter().setWidth(6, "70px");
		tab.getColumnFormatter().setWidth(7, "30px");

		int col = 0;
		Label numberLabel = new Label("#");
		numberLabel.setStyleName(MKPK.CSS.mkpkBold());
		tab.setWidget(0, col, numberLabel);
		tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		col++;
		
		Label codeLabel = new Label(MKPK.MSG.code());
		codeLabel.setStyleName(MKPK.CSS.mkpkBold());
		tab.setWidget(0, col, codeLabel);
		tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		col++;

		Label descriptionLabel = new Label(MKPK.MSG.description());
		descriptionLabel.setStyleName(MKPK.CSS.mkpkBold());
		tab.setWidget(0, col, descriptionLabel);
		tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		col++;

		Label materialUpLabel = new Label(MKPK.MSG.material() + " superior");
		materialUpLabel.setStyleName(MKPK.CSS.mkpkBold());
		tab.setWidget(0, col, materialUpLabel);
		tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		col++;

		Label materialDownLabel = new Label(MKPK.MSG.material() + " inferior");
		materialDownLabel.setStyleName(MKPK.CSS.mkpkBold());
		tab.setWidget(0, col, materialDownLabel);
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

		return tab;
	}
	
	private Widget getContent() {
		ScrollPanel scrollPanel = new ScrollPanel();
		FlowPanel container = new FlowPanel();
		final FlexTable tab = defineFlexTable();
		container.add(tab);

		SERVICE.getProducts(getParams(), offset,count,new AsyncCallback<LinkedList<Product>>() {
			
			@Override
			public void onSuccess(LinkedList<Product> products) {
				int row = 1;
				for (Product product : products) {
					paintRow(tab,product,row);
					row++;
				}
				int nextCol = 1;
				if (offset > 0) {
					MkpkButton previousButton = new MkpkButton();
					previousButton.addStyleName(MKPK.CSS.mkpkIconPrevious());
					previousButton.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							offset = offset - count;
							if (offset < 0 ) offset = 0;
							add(getContent());
						}
					});
					tab.setWidget(row, 0, previousButton);
					tab.setWidget(row, 1, new Label());
					tab.getFlexCellFormatter().setColSpan(row, 1, 6);
					nextCol = 2;
				} else {
					tab.setWidget(row, 0, new Label());
					tab.getFlexCellFormatter().setColSpan(row, 0, 7);
				}

				MkpkButton nextButton = new MkpkButton();
				nextButton.addStyleName(MKPK.CSS.mkpkIconNext());
				nextButton.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						offset = offset + count;
						ProductList.this.scroll.setWidget(getContent());
					}
				});
				tab.setWidget(row, nextCol, nextButton);

			}
			
			@Override
			public void onFailure(Throwable caught) {
				ProductList.this.showError( caught );
			}
		});

		scrollPanel.setWidget(container);
		return scrollPanel;		
	}

	private ProductParams getParams() {
		ProductParams params = new ProductParams();
		params.setCode(codeBox.getValue());
		params.setName(nameBox.getValue());
		params.setMaterialUp(new Material().setName(materialUpBox.getValue()));
		params.setMaterialDown(new Material().setName(materialDownBox.getValue()));
		params.setLength(lengthBox.getValue());
		params.setWidth(widthBox.getValue());
		return params;
	}

	protected void paintRow(FlexTable tab, final Product product, int row) {
		int col = 0;
		tab.setWidget(row, col, new Label(MKPK.FMT_INT.format(row + offset)));
		tab.getCellFormatter().setStyleName(row, col, MKPK.CSS.mkpkTextCenter());
		col++;
		tab.setWidget(row, col++, new Label(product.getCode()));
		tab.setWidget(row, col++, new Label(product.getName()));
		tab.setWidget(row, col++, new Label(product.getMaterialUp().getName()));
		tab.setWidget(row, col++, new Label(product.getMaterialDown().getName()));
		tab.setWidget(row, col, new Label(MKPK.FMT.format( MkpkNumberUtils.zeroIfNull( product.getLength()))));
		tab.getCellFormatter().setStyleName(row, col, MKPK.CSS.mkpkTextRight());
		col++;
		tab.setWidget(row, col, new Label(MKPK.FMT.format( MkpkNumberUtils.zeroIfNull( product.getWidth()))));
		tab.getCellFormatter().setStyleName(row, col, MKPK.CSS.mkpkTextRight());
		col++;
		paintDeleteButton(tab,row,col,product);
	}

	private void paintDeleteButton(FlexTable tab, int row, int col, Product product) {
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
									ProductList.this.scroll.setWidget(getContent());
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
			tab.setWidget(row, col, deleteButton);
		} else {
			tab.setWidget(row, col, new Label());
		}
	}

	protected void showError(Throwable caught) {
		MkpkConfirmDialog dialog = new MkpkConfirmDialog();
		dialog.accept("Error", caught.getMessage(), null );
	}
	
	private FlexTable getFilterTable() {
		FlexTable filter = defineFlexTable();
		filter.addStyleName(MKPK.CSS.mkpkMarginBottom());
		
		codeBox.setVisibleLength(10);
		codeBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				search();
			}
		});
		filter.setWidget(1, 1, codeBox);
		
		nameBox.setVisibleLength(20);
		nameBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				search();
			}
		});
		filter.setWidget(1, 2, nameBox);
		
		materialUpBox.setVisibleLength(10);
		materialUpBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				search();
			}
		});
		filter.setWidget(1, 3, materialUpBox);

		materialDownBox.setVisibleLength(10);
		materialDownBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				search();
			}
		});
		filter.setWidget(1, 4, materialDownBox);

		lengthBox.setVisibleLength(3);
		lengthBox.addValueChangeHandler(new ValueChangeHandler<Double>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				search();
			}
		});
		filter.setWidget(1, 5, lengthBox);

		widthBox.setVisibleLength(3);
		widthBox.addValueChangeHandler(new ValueChangeHandler<Double>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				search();
			}
		});
		filter.setWidget(1, 6, widthBox);
		
		MkpkButton cleanButton = new MkpkButton();
		cleanButton.setTitle(MKPK.MSG.delete());
		cleanButton.addStyleName(MKPK.CSS.mkpkIconClean());
		cleanButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				codeBox.setValue(null,false);
				nameBox.setValue(null,false);
				materialUpBox.setValue(null,false);
				materialDownBox.setValue(null,false);
				lengthBox.setValue(null,false);
				widthBox.setValue(null,false);
				search();
			}
		});
		filter.setWidget(1, 7, cleanButton);
		
		return filter;
	}
	
}

