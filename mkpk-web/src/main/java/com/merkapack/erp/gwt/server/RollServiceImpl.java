package com.merkapack.erp.gwt.server;

import java.util.LinkedList;

import javax.servlet.annotation.WebServlet;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.basic.MkpkDatasource;
import com.merkapack.erp.core.dao.MkpkGo;
import com.merkapack.erp.core.model.MkpkCoreException;
import com.merkapack.erp.core.model.Roll;
import com.merkapack.erp.gwt.client.rpc.RollService;

@WebServlet(name = "Roll Service Servlet", urlPatterns = { "/mkpk_gwt/MkpkRoll" })
public class RollServiceImpl extends StatelessRemoteServiceServlet implements RollService {

	private static final long serialVersionUID = 949123203256791644L;

	@Override
	public LinkedList<Roll> getRolls() throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			return MkpkGo.getRolls(ctx);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public LinkedList<Roll> getRolls(String query,Integer material) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			LinkedList<Roll> list = MkpkGo.getRolls(ctx,query,material);
			return list;
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public Roll save(Roll roll) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			roll.setDomain(DOMAIN);
			return MkpkGo.save(ctx,roll);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public void delete(Roll roll) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			roll.setDomain(DOMAIN);
			MkpkGo.delete(ctx,roll);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

}
