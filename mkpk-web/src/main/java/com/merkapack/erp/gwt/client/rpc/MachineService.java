package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.merkapack.erp.core.model.Machine;
import com.merkapack.erp.core.model.MkpkCoreException;

@RemoteServiceRelativePath("MkpkMachine")
public interface MachineService extends RemoteService {
	LinkedList<Machine> getMachines() throws MkpkCoreException;
	Machine save(Machine machine) throws MkpkCoreException;
	void delete(Machine machine) throws MkpkCoreException;
}
