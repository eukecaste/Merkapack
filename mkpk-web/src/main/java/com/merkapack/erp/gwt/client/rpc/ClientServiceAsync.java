package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.merkapack.erp.core.model.Client;

public interface ClientServiceAsync {

	void getClients(AsyncCallback<LinkedList<Client>> callback);
	void getClients(String query, AsyncCallback<LinkedList<Client>> asyncCallback);
	void save(Client client, AsyncCallback<Client> callback);
	void delete(Client client, AsyncCallback<Void> asyncCallback);

}
