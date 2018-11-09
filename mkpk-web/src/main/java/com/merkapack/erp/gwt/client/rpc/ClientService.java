package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.merkapack.erp.core.model.Client;
import com.merkapack.erp.core.model.MkpkCoreException;

@RemoteServiceRelativePath("MkpkClient")
public interface ClientService extends RemoteService {
	LinkedList<Client> getClients() throws MkpkCoreException;
	LinkedList<Client> getClients(String query) throws MkpkCoreException;
	Client save(Client client) throws MkpkCoreException;
	void delete(Client client) throws MkpkCoreException;
}
