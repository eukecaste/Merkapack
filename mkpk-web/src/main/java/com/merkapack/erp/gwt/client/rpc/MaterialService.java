package com.merkapack.erp.gwt.client.rpc;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.merkapack.erp.core.model.Material;
import com.merkapack.erp.core.model.MkpkCoreException;

@RemoteServiceRelativePath("MkpkMaterial")
public interface MaterialService extends RemoteService {
	LinkedList<Material> getMaterials() throws MkpkCoreException;
	Material save(Material material) throws MkpkCoreException;
	void delete(Material material) throws MkpkCoreException;
}
