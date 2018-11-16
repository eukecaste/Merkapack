package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.merkapack.erp.core.model.MkpkCoreException;
import com.merkapack.erp.core.model.Roll;

@RemoteServiceRelativePath("MkpkRoll")
public interface RollService extends RemoteService {
	LinkedList<Roll> getRolls() throws MkpkCoreException;
	LinkedList<Roll> getRolls(String query) throws MkpkCoreException;
	Roll save(Roll roll) throws MkpkCoreException;
	void delete(Roll roll) throws MkpkCoreException;
}
