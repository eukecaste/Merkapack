package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.gwt.client.common.MKPK;

public class ProductServiceAsyncDecorator implements ProductServiceAsync {

	private ProductServiceAsync service;
	
	public ProductServiceAsyncDecorator(ProductServiceAsync service) {
		this.service = service;
	}

	@Override
	public void getProducts(AsyncCallback<LinkedList<Product>> callback) {
		MKPK.start();
		service.getProducts(callback);
		
	}

	@Override
	public void getProducts(String query, AsyncCallback<LinkedList<Product>> callback) {
		MKPK.start();
		service.getProducts(query,callback);
		
	}

	@Override
	public void save(Product product, AsyncCallback<Product> callback) {
		MKPK.start();
		service.save(product, callback);
	}

	@Override
	public void delete(Product product, AsyncCallback<Void> callback) {
		MKPK.start();
		service.delete(product, callback);
	}

}
