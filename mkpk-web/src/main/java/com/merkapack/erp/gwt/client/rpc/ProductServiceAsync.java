package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.merkapack.erp.core.model.Product;

public interface ProductServiceAsync {

	void getProducts(int offset, int count,AsyncCallback<LinkedList<Product>> callback);
	void getProducts(int offset, int count,String query,AsyncCallback<LinkedList<Product>> callback);
	void save(Product product, AsyncCallback<Product> callback);
	void delete(Product product, AsyncCallback<Void> asyncCallback);

}
