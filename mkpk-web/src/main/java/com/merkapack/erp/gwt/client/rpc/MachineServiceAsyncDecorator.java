package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.merkapack.erp.core.model.Machine;
import com.merkapack.erp.gwt.client.common.MKPK;

public class MachineServiceAsyncDecorator implements MachineServiceAsync {

	private MachineServiceAsync service;
	
	public MachineServiceAsyncDecorator(MachineServiceAsync service) {
		this.service = service;
	}

	@Override
	public void getMachines(AsyncCallback<LinkedList<Machine>> callback) {
		MKPK.start();
		service.getMachines(callback);
	}

	@Override
	public void getMachines(String query, AsyncCallback<LinkedList<Machine>> callback) {
		MKPK.start();
		service.getMachines(query, callback);
	}

	@Override
	public void save(Machine machine, AsyncCallback<Machine> callback) {
		MKPK.start();
		service.save(machine, callback);
	}

	@Override
	public void delete(Machine machine, AsyncCallback<Void> callback) {
		MKPK.start();
		service.delete(machine, callback);
	}

}
