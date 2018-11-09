package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.merkapack.erp.core.model.Material;

public interface MaterialServiceAsync {

	void getMaterials(AsyncCallback<LinkedList<Material>> callback);
	void getMaterials(String query, AsyncCallback<LinkedList<Material>> asyncCallback);
	void save(Material material, AsyncCallback<Material> callback);
	void delete(Material material, AsyncCallback<Void> asyncCallback);

}
