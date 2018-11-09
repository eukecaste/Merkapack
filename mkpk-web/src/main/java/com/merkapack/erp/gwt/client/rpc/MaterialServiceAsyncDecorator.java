package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.merkapack.erp.core.model.Material;
import com.merkapack.erp.gwt.client.common.MKPK;

public class MaterialServiceAsyncDecorator implements MaterialServiceAsync {

	private MaterialServiceAsync service;
	
	public MaterialServiceAsyncDecorator(MaterialServiceAsync service) {
		this.service = service;
	}

	@Override
	public void getMaterials(AsyncCallback<LinkedList<Material>> callback) {
		MKPK.start();
		service.getMaterials(callback);
		
	}

	@Override
	public void getMaterials(String query, AsyncCallback<LinkedList<Material>> callback) {
		MKPK.start();
		service.getMaterials(query, callback);
		
	}

	@Override
	public void save(Material material, AsyncCallback<Material> callback) {
		MKPK.start();
		service.save(material, callback);
	}

	@Override
	public void delete(Material material, AsyncCallback<Void> callback) {
		MKPK.start();
		service.delete(material, callback);
	}

}
