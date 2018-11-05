package com.merkapack.erp.gwt.server;

import java.util.LinkedList;

import javax.servlet.annotation.WebServlet;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.basic.MkpkDatasource;
import com.merkapack.erp.core.dao.MkpkGo;
import com.merkapack.erp.core.model.Material;
import com.merkapack.erp.core.model.MkpkCoreException;
import com.merkapack.erp.gwt.client.rpc.MaterialService;

@WebServlet(name = "Material Service Servlet", urlPatterns = { "/mkpk_gwt/MkpkMaterial" })
public class MaterialServiceImpl extends StatelessRemoteServiceServlet implements MaterialService {

	private static final long serialVersionUID = 949123203256791644L;

	@Override
	public LinkedList<Material> getMaterials() throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			return MkpkGo.getMaterials(ctx);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public Material save(Material material) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			material.setDomain(DOMAIN);
			return MkpkGo.save(ctx,material);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public void delete(Material material) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			material.setDomain(DOMAIN);
			MkpkGo.delete(ctx,material);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

}
