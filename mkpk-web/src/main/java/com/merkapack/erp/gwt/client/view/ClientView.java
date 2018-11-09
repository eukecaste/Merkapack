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
import com.merkapack.erp.core.model.Client;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.rpc.ClientService;
import com.merkapack.erp.gwt.client.rpc.ClientServiceAsync;
import com.merkapack.erp.gwt.client.rpc.ClientServiceAsyncDecorator;
import com.merkapack.erp.gwt.client.widget.MkpkButton;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog.MkpkConfirmDialogCallback;
import com.merkapack.erp.gwt.client.widget.MkpkDockLayout;
import com.merkapack.erp.gwt.client.widget.MkpkTextBox;

public class ClientView extends MkpkDockLayout  {
	
	private static ClientServiceAsync SERVICE;
	private ScrollPanel content;
	private final int deleteColumnIndex = 1; 
	
	public ClientView() {
		ClientServiceAsync serviceRaw = GWT.create(ClientService.class);
		SERVICE = new ClientServiceAsyncDecorator(serviceRaw);
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
		Label nameLabel = new Label(MKPK.MSG.clients());
		nameLabel.setStyleName(MKPK.CSS.mkpkBold());
		tab.setWidget(0, col, nameLabel);
		col++;
		
		Label deleteLabel = new Label("X");
		deleteLabel.setStyleName(MKPK.CSS.mkpkBold());
		tab.setWidget(0, col, deleteLabel);
		col++;
		
		SERVICE.getClients(new AsyncCallback<LinkedList<Client>>() {
			
			@Override
			public void onSuccess(LinkedList<Client> clients) {
				for (Client client : clients) {
					paintRow(tab,client);
				}
				paintRow(tab,new Client());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				ClientView.this.showError( caught );
			}
		});
		return tab;		
	}

	protected void paintRow(FlexTable tab, final Client client) {
		int row = tab.getRowCount();
		MkpkTextBox nameBox = paintNameColumn(tab,row,0,client);
		paintDeleteButton(tab,row,client);
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				nameBox.setFocus(true);
			}
		});
	}

	private MkpkTextBox paintNameColumn(FlexTable tab, int row, int col, Client client) {
		MkpkTextBox nameBox = new MkpkTextBox();
		nameBox.setValue(client.getName(), false);
		nameBox.setMaxLength(32);
		nameBox.setVisibleLength(40);
		tab.setWidget(row, col, nameBox);
		nameBox.addValueChangeHandler( new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				client.setName( nameBox.getValue() ); 
				SERVICE.save(client, new AsyncCallback<Client>() {
					
					@Override
					public void onSuccess(Client result) {
						if (client.getId() == null) {
							client.setId(result.getId());
							paintDeleteButton(tab,row,client);							
						}
						if ((row +1)== tab.getRowCount()) {
							paintRow(tab, new Client());
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

	private void paintDeleteButton(FlexTable tab, int row, Client client) {
		if (client.getId() != null) {
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
							
							SERVICE.delete(client, new AsyncCallback<Void>() {
								
								@Override
								public void onSuccess(Void nothing) {
									ClientView.this.content.clear();
									ClientView.this.content.setWidget(getContent());
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
			tab.setWidget(row, deleteColumnIndex, deleteButton);
		} else {
			tab.setWidget(row, deleteColumnIndex, new Label());
		}
	}
}

