package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.merkapack.erp.core.model.Machine;

public interface MachineServiceAsync {

	void getMachines(AsyncCallback<LinkedList<Machine>> callback);
	void getMachines(String query, AsyncCallback<LinkedList<Machine>> callback);
	void save(Machine machine, AsyncCallback<Machine> callback);
	void delete(Machine machine, AsyncCallback<Void> asyncCallback);

}
