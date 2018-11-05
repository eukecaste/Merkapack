package com.merkapack.erp.gwt.client.view;

import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.merkapack.erp.core.model.Material;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.rpc.MaterialService;
import com.merkapack.erp.gwt.client.rpc.MaterialServiceAsync;
import com.merkapack.erp.gwt.client.rpc.MaterialServiceAsyncDecorator;
import com.merkapack.erp.gwt.client.widget.MkpkButton;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog.MkpkConfirmDialogCallback;
import com.merkapack.erp.gwt.client.widget.MkpkDockLayout;
import com.merkapack.erp.gwt.client.widget.MkpkDoubleBox;
import com.merkapack.erp.gwt.client.widget.MkpkTextBox;

public class MaterialView extends MkpkDockLayout  {
	
	private static MaterialServiceAsync SERVICE;
	private ScrollPanel content; 
	
	public MaterialView() {
		MaterialServiceAsync serviceRaw = GWT.create(MaterialService.class);
		SERVICE = new MaterialServiceAsyncDecorator(serviceRaw);
		content = new ScrollPanel();
		content.setWidget(getContent());
		add(content);
	}

	private Widget getContent() {
		final FlexTable tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkBlockCenter());
		tab.getColumnFormatter().setWidth(0, "auto");
		tab.getColumnFormatter().setWidth(1, "100px");
		tab.getColumnFormatter().setWidth(2, "15px");
		
		int col = 0;
		Label nameLabel = new Label(MKPK.MSG.materials());
		nameLabel.setStyleName(MKPK.CSS.mkpkBold());
		tab.setWidget(0, col, nameLabel);
		col++;
		Label thicknessLabel = new Label(MKPK.MSG.thickness());
		thicknessLabel.setStyleName(MKPK.CSS.mkpkBold());
		tab.setWidget(0, col, thicknessLabel);
		col++;
		
		Label deleteLabel = new Label("X");
		deleteLabel.setStyleName(MKPK.CSS.mkpkBold());
		tab.setWidget(0, col, deleteLabel);
		col++;
		
		SERVICE.getMaterials(new AsyncCallback<LinkedList<Material>>() {
			
			@Override
			public void onSuccess(LinkedList<Material> materials) {
				for (Material material : materials) {
					paintRow(tab,material);
				}
				paintRow(tab,new Material());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				MaterialView.this.showError( caught );
			}
		});
		return tab;		
	}

	protected void paintRow(FlexTable tab, final Material material) {
		int row = tab.getRowCount();
		MkpkTextBox nameBox = paintNameColumn(tab,row,0,material);
		paintThicknessColumn(tab,row,1,material);
		paintDeleteButton(tab,row,2,material);
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				nameBox.setFocus(true);
			}
		});
	}

	private MkpkTextBox paintNameColumn(FlexTable tab, int row, int col, Material material) {
		MkpkTextBox nameBox = new MkpkTextBox();
		nameBox.setValue(material.getName(), false);
		nameBox.setMaxLength(32);
		nameBox.setVisibleLength(40);
		tab.setWidget(row, col, nameBox);
		nameBox.addValueChangeHandler( new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				material.setName( nameBox.getValue() ); 
				SERVICE.save(material, new AsyncCallback<Material>() {
					
					@Override
					public void onSuccess(Material result) {
						if (material.getId() == null) {
							material.setId(result.getId());
							paintDeleteButton(tab,row,2,material);							
						}
						if ((row +1)== tab.getRowCount()) {
							paintRow(tab, new Material());
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						showError(caught);
					}
				});
			}
		});
		return nameBox;
	}

	private MkpkDoubleBox paintThicknessColumn(FlexTable tab, int row, int col, Material material) {
		MkpkDoubleBox thicknessBox = new MkpkDoubleBox();
		thicknessBox.setValue(material.getThickness(), false);
		tab.setWidget(row, col, thicknessBox);
		thicknessBox.addValueChangeHandler( new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				material.setThickness( thicknessBox.getValue() ); 
				SERVICE.save(material, new AsyncCallback<Material>() {
					
					@Override
					public void onSuccess(Material result) {
						if (material.getId() == null) {
							material.setId(result.getId());
							paintDeleteButton(tab,row,2,material);							
						}
						if ((row +1)== tab.getRowCount()) {
							paintRow(tab, new Material());
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						showError(caught);
					}
				});
			}
		});
		return thicknessBox;
	}

	private void paintDeleteButton(FlexTable tab, int row, int col,Material material) {
		if (material.getId() != null) {
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
							
							SERVICE.delete(material, new AsyncCallback<Void>() {
								
								@Override
								public void onSuccess(Void nothing) {
									MaterialView.this.content.clear();
									MaterialView.this.content.setWidget(getContent());
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
}

