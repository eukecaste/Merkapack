package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.merkapack.erp.core.model.Roll;

public interface RollServiceAsync {

	void getRolls(AsyncCallback<LinkedList<Roll>> callback);
	void getRolls(String query,Integer material,AsyncCallback<LinkedList<Roll>> callback);
	void save(Roll roll, AsyncCallback<Roll> callback);
	void delete(Roll roll, AsyncCallback<Void> asyncCallback);

}
