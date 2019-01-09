package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.core.model.ProductParams;
import com.merkapack.erp.gwt.client.common.MKPK;

public class ProductServiceAsyncDecorator implements ProductServiceAsync {

	private ProductServiceAsync service;
	
	public ProductServiceAsyncDecorator(ProductServiceAsync service) {
		this.service = service;
	}

	@Override
	public void getProducts(ProductParams params, int offset, int count,AsyncCallback<LinkedList<Product>> callback) {
		MKPK.start();
		service.getProducts(params,offset, count, callback);
		
	}

	@Override
	public void getProducts(int offset, int count,String query, AsyncCallback<LinkedList<Product>> callback) {
		MKPK.start();
		service.getProducts(offset, count, query,callback);
		
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
