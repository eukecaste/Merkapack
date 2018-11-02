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
import com.merkapack.erp.core.model.Machine;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.rpc.MachineService;
import com.merkapack.erp.gwt.client.rpc.MachineServiceAsync;
import com.merkapack.erp.gwt.client.rpc.MachineServiceAsyncDecorator;
import com.merkapack.erp.gwt.client.widget.MkpkButton;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog.MkpkConfirmDialogCallback;
import com.merkapack.erp.gwt.client.widget.MkpkDockLayout;
import com.merkapack.erp.gwt.client.widget.MkpkTextBox;

public class MachineView extends MkpkDockLayout  {
	
	private static MachineServiceAsync SERVICE;
	private ScrollPanel content; 
	
	public MachineView() {
		MachineServiceAsync serviceRaw = GWT.create(MachineService.class);
		SERVICE = new MachineServiceAsyncDecorator(serviceRaw);
		content = new ScrollPanel();
		content.setWidget(getContent());
		add(content);
	}

	private Widget getContent() {
		final FlexTable tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkBlockCenter());
		tab.getColumnFormatter().setWidth(0, "auto");
		tab.getColumnFormatter().setWidth(1, "15px");
		
		Label nameLabel = new Label(MKPK.MSG.machines());
		nameLabel.setStyleName(MKPK.CSS.mkpkBold());
		tab.setWidget(0, 0, nameLabel);
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
		return tab;		
	}

	protected void paintRow(FlexTable tab, final Machine machine) {
		MkpkTextBox nameBox = new MkpkTextBox();
		nameBox.setValue(machine.getName(), false);
		nameBox.setMaxLength(32);
		nameBox.setVisibleLength(40);
		int row = tab.getRowCount();
		tab.setWidget(row, 0, nameBox);
		nameBox.addValueChangeHandler( new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				machine.setName( nameBox.getValue() ); 
				SERVICE.save(machine, new AsyncCallback<Machine>() {
					
					@Override
					public void onSuccess(Machine result) {
						machine.setId(result.getId());
						if ((row +1)== tab.getRowCount()) {
							paintRow(tab, new Machine());
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						showError(caught);
					}
				});
			}
		});
		if (machine.getId() != null) {
			
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
			tab.setWidget(row, 1, deleteButton);
		} else {
			tab.setWidget(row, 1, new Label());
		}
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				nameBox.setFocus(true);
			}
		});
	}
}

