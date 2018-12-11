package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.core.model.PlanningParams;

public interface PlanningServiceAsync {

	void getPlannings(PlanningParams params, AsyncCallback<LinkedList<Planning>> callback);
	void save(Planning planning, AsyncCallback<Planning> callback);
	void save(LinkedList<Planning> list, AsyncCallback<LinkedList<Planning>> callback);
	void delete(Planning planning, AsyncCallback<Void> callback);

}
