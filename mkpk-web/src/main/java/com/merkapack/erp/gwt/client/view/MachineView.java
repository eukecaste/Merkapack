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
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.merkapack.erp.core.model.Machine;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.rpc.MachineService;
import com.merkapack.erp.gwt.client.rpc.MachineServiceAsync;
import com.merkapack.erp.gwt.client.rpc.MachineServiceAsyncDecorator;
import com.merkapack.erp.gwt.client.widget.MkpkButton;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog.MkpkConfirmDialogCallback;
import com.merkapack.erp.gwt.client.widget.MkpkDockLayout;
import com.merkapack.erp.gwt.client.widget.MkpkDoubleBox;
import com.merkapack.erp.gwt.client.widget.MkpkTextBox;

public class MachineView extends MkpkDockLayout  {
	
	private static MachineServiceAsync SERVICE;
	private SimpleLayoutPanel content;
	private final int deleteButtonColumn = 2; 
	
	public MachineView() {
		MachineServiceAsync serviceRaw = GWT.create(MachineService.class);
		SERVICE = new MachineServiceAsyncDecorator(serviceRaw);
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
		tab.getColumnFormatter().setWidth(1, "15px");
		
		int col = 0;
		Label nameLabel = new Label(MKPK.MSG.machines());
		tab.setWidget(0, col, nameLabel);
		tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		col++;

		Label blowsLabel = new Label(MKPK.MSG.blows());
		blowsLabel.setStyleName(MKPK.CSS.mkpkBold());
		tab.setWidget(0, col, blowsLabel);
		tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		col++;
		
		Label deleteLabel = new Label("X");
		tab.setWidget(0, col, deleteLabel);
		tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		col++;

		SERVICE.getMachines(new AsyncCallback<LinkedList<Machine>>() {
			
			@Override
			public void onSuccess(LinkedList<Machine> machines) {
				for (Machine machine : machines) {
					paintRow(tab,machine);
				}
				paintRow(tab,new Machine());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				MachineView.this.showError( caught );
			}
		});
		panel .add(tab);
		
		MkpkButton newLineButton = new MkpkButton();
		newLineButton.addStyleName(MKPK.CSS.mkpkIconPlus());
		newLineButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				paintRow(tab,new Machine());
			}
		});
		panel.add(newLineButton);
		return container;		
	}

	protected void paintRow(FlexTable tab, final Machine machine) {
		int row = tab.getRowCount();
		MkpkTextBox nameBox = paintNameColumn(tab,row,machine);
		paintBlowsColumn(tab,row,1,machine);
		if (machine.getId() != null) {
			paintDeleteButton(tab,row,machine);
		} else {
			tab.setWidget(row, 2, new Label());
		}
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				nameBox.setFocus(true);
			}
		});
	}

	private MkpkTextBox paintNameColumn(FlexTable tab, int row, Machine machine) {
		MkpkTextBox nameBox = new MkpkTextBox();
		nameBox.setValue(machine.getName(), false);
		nameBox.setMaxLength(32);
		nameBox.setVisibleLength(40);
		tab.setWidget(row, 0, nameBox);
		nameBox.addValueChangeHandler( new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				machine.setName( nameBox.getValue() ); 
				save(tab, row, machine);
			}
		});
		return nameBox;
	}
	
	private MkpkDoubleBox paintBlowsColumn(FlexTable tab, int row, int col, Machine machine) {
		MkpkDoubleBox blowsBox = new MkpkDoubleBox();
		blowsBox.setValue(machine.getBlows(), false);
		tab.setWidget(row, col, blowsBox);
		blowsBox.addValueChangeHandler( new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				machine.setBlows(blowsBox.getValue() );
				save(tab, row, machine);
			}
		});
		return blowsBox;
	}

	private void paintDeleteButton(FlexTable tab, int row, Machine machine) {
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
						
						SERVICE.delete(machine, new AsyncCallback<Void>() {
							
							@Override
							public void onSuccess(Void nothing) {
								MachineView.this.content.clear();
								MachineView.this.content.setWidget(getContent());
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
		tab.setWidget(row, deleteButtonColumn, deleteButton);
	}
	
	private void save(FlexTable tab, int row, Machine machine) {
		SERVICE.save(machine, new AsyncCallback<Machine>() {
			
			@Override
			public void onSuccess(Machine result) {
				if (machine.getId() == null) {
					machine.setId(result.getId());
					paintDeleteButton(tab,row,machine);							
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				showError(caught);
			}
		});
	}
	
}

