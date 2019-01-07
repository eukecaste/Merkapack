package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.merkapack.erp.core.model.MkpkCoreException;
import com.merkapack.erp.core.model.Product;

@RemoteServiceRelativePath("MkpkProduct")
public interface ProductService extends RemoteService {
	LinkedList<Product> getProducts(int offset, int count) throws MkpkCoreException;
	LinkedList<Product> getProducts(int offset, int count,String query) throws MkpkCoreException;
	Product save(Product product) throws MkpkCoreException;
	void delete(Product product) throws MkpkCoreException;
}
