package com.merkapack.erp.gwt.client.view;

import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.rpc.ProductService;
import com.merkapack.erp.gwt.client.rpc.ProductServiceAsync;
import com.merkapack.erp.gwt.client.rpc.ProductServiceAsyncDecorator;
import com.merkapack.erp.gwt.client.widget.MkpkButton;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog.MkpkConfirmDialogCallback;
import com.merkapack.watson.util.MkpkNumberUtils;

public class ProductList extends SimpleLayoutPanel  {
	
	private static ProductServiceAsync SERVICE;
	private int offset = 0;
	private int count = 20;
	
	public ProductList( ) {
		setStyleName(MKPK.CSS.mkpkWidthAll());
		ProductServiceAsync serviceRaw = GWT.create(ProductService.class);
		SERVICE = new ProductServiceAsyncDecorator(serviceRaw);
		setWidget(getContent());
	}

	private Widget getContent() {
		ScrollPanel container = new ScrollPanel();
		
		final FlexTable tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkWidth90p());
		tab.addStyleName(MKPK.CSS.mkpkTable());
		tab.getColumnFormatter().setWidth(0, "20px");
		tab.getColumnFormatter().setWidth(1, "150px");
		tab.getColumnFormatter().setWidth(2, "auto");
		tab.getColumnFormatter().setWidth(3, "150px");
		tab.getColumnFormatter().setWidth(4, "150px");
		tab.getColumnFormatter().setWidth(5, "70px");
		tab.getColumnFormatter().setWidth(6, "70px");
		tab.getColumnFormatter().setWidth(7, "20px");
		
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
		
		SERVICE.getProducts(offset,count,new AsyncCallback<LinkedList<Product>>() {
			
			@Override
			public void onSuccess(LinkedList<Product> products) {
				int row = 1;
				for (Product product : products) {
					paintRow(tab,product,row);
					row++;
				}
				
				if (offset > 0) {
					MkpkButton previousButton = new MkpkButton();
					previousButton.addStyleName(MKPK.CSS.mkpkIconPrevious());
					previousButton.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							offset = offset - count;
							if (offset < 0 ) offset = 0;
							setWidget(getContent());
						}
					});
					tab.setWidget(row, 0, previousButton);
				}

				MkpkButton nextButton = new MkpkButton();
				nextButton.addStyleName(MKPK.CSS.mkpkIconNext());
				nextButton.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						offset = offset + count;
						setWidget(getContent());
					}
				});
				tab.setWidget(row, 7, nextButton);

			}
			
			@Override
			public void onFailure(Throwable caught) {
				ProductList.this.showError( caught );
			}
		});

		container.setWidget(tab);
		
//		MkpkButton newLineButton = new MkpkButton();
//		newLineButton.addStyleName(MKPK.CSS.mkpkIconPlus());
//		newLineButton.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				// TODO
//			}
//		});
//		panel.add(newLineButton);
		return container;		
	}

	protected void paintRow(FlexTable tab, final Product product, int row) {
		int col = 0;
		tab.setWidget(row, col++, new Label(MKPK.FMT_INT.format(row + offset)));
		tab.setWidget(row, col++, new Label(product.getCode()));
		tab.setWidget(row, col++, new Label(product.getName()));
		tab.setWidget(row, col++, new Label(product.getMaterialUp().getName()));
		tab.setWidget(row, col++, new Label(product.getMaterialDown().getName()));
		tab.setWidget(row, col++, new Label(MKPK.FMT.format( MkpkNumberUtils.zeroIfNull( product.getLength()))));
		tab.setWidget(row, col++, new Label(MKPK.FMT.format( MkpkNumberUtils.zeroIfNull( product.getWidth()))));
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
									ProductList.this.clear();
									ProductList.this.setWidget(getContent());
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
//	private void save(FlexTable tab, int row, Product product) {
//		if (MkpkStringUtils.isNotBlank( product.getName() )
//			&& product.getMaterialUp().getId() != null) {
//			SERVICE.save(product, new AsyncCallback<Product>() {
//				
//				@Override
//				public void onSuccess(Product result) {
//					if (product.getId() == null) {
//						
//						product.setId(result.getId());
//						paintDeleteButton(tab,row,6,product);							
//					}
//				}
//				
//				@Override
//				public void onFailure(Throwable caught) {
//					showError(caught);
//				}
//			});
//		}
//	}

	protected void showError(Throwable caught) {
		MkpkConfirmDialog dialog = new MkpkConfirmDialog();
		dialog.accept("Error", caught.getMessage(), null );
	}
}

