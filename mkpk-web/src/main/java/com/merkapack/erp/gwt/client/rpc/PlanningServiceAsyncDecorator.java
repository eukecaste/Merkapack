package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.core.model.PlanningParams;
import com.merkapack.erp.gwt.client.common.MKPK;

public class PlanningServiceAsyncDecorator implements PlanningServiceAsync {

	private PlanningServiceAsync service;
	
	public PlanningServiceAsyncDecorator(PlanningServiceAsync service) {
		this.service = service;
	}

	@Override
	public void getPlannings(PlanningParams params, AsyncCallback<LinkedList<Planning>> callback) {
		MKPK.start();
		service.getPlannings(params,callback);
		
	}

	@Override
	public void save(LinkedList<Planning> list, AsyncCallback<LinkedList<Planning>> callback) {
		MKPK.start();
		service.save(list, callback);
	}

	@Override
	public void save(Planning planning, AsyncCallback<Planning> callback) {
		MKPK.start();
		service.save(planning, callback);
	}

	@Override
	public void delete(Planning planning, AsyncCallback<Void> callback) {
		MKPK.start();
		service.delete(planning, callback);
	}

}
