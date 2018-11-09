package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.merkapack.erp.core.model.Client;
import com.merkapack.erp.gwt.client.common.MKPK;

public class ClientServiceAsyncDecorator implements ClientServiceAsync {

	private ClientServiceAsync service;
	
	public ClientServiceAsyncDecorator(ClientServiceAsync service) {
		this.service = service;
	}

	@Override
	public void getClients(AsyncCallback<LinkedList<Client>> callback) {
		MKPK.start();
		service.getClients(callback);
		
	}

	@Override
	public void getClients(String query, AsyncCallback<LinkedList<Client>> callback) {
		MKPK.start();
		service.getClients(query, callback);
		
	}

	@Override
	public void save(Client client, AsyncCallback<Client> callback) {
		MKPK.start();
		service.save(client, callback);
	}

	@Override
	public void delete(Client client, AsyncCallback<Void> callback) {
		MKPK.start();
		service.delete(client, callback);
	}

}
