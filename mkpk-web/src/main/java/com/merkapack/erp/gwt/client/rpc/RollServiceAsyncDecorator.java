package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.merkapack.erp.core.model.Roll;
import com.merkapack.erp.gwt.client.common.MKPK;

public class RollServiceAsyncDecorator implements RollServiceAsync {

	private RollServiceAsync service;
	
	public RollServiceAsyncDecorator(RollServiceAsync service) {
		this.service = service;
	}

	@Override
	public void getRolls(AsyncCallback<LinkedList<Roll>> callback) {
		MKPK.start();
		service.getRolls(callback);
		
	}

	@Override
	public void getRolls(String query,Integer material, AsyncCallback<LinkedList<Roll>> callback) {
		MKPK.start();
		service.getRolls(query,material,callback);
		
	}

	@Override
	public void save(Roll roll, AsyncCallback<Roll> callback) {
		MKPK.start();
		service.save(roll, callback);
	}

	@Override
	public void delete(Roll roll, AsyncCallback<Void> callback) {
		MKPK.start();
		service.delete(roll, callback);
	}

}
