package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.merkapack.erp.core.model.MkpkCoreException;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.core.model.PlanningParams;

@RemoteServiceRelativePath("MkpkPlanning")
public interface PlanningService extends RemoteService {

	LinkedList<Planning> getPlannings(PlanningParams params) throws MkpkCoreException;
	Planning save(Planning planning) throws MkpkCoreException;
	LinkedList<Planning> save(LinkedList<Planning> list) throws MkpkCoreException;
	void delete(Planning planning) throws MkpkCoreException;
}
