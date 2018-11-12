package com.merkapack.erp.gwt.server;

import java.util.LinkedList;

import javax.servlet.annotation.WebServlet;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.basic.MkpkDatasource;
import com.merkapack.erp.core.dao.MkpkGo;
import com.merkapack.erp.core.model.Machine;
import com.merkapack.erp.core.model.MkpkCoreException;
import com.merkapack.erp.gwt.client.rpc.MachineService;

@WebServlet(name = "Machine Service Servlet", urlPatterns = { "/mkpk_gwt/MkpkMachine" })
public class MachineServiceImpl extends StatelessRemoteServiceServlet implements MachineService {

	private static final long serialVersionUID = 949123203256791644L;

	@Override
	public LinkedList<Machine> getMachines() throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			return MkpkGo.getMachines(ctx);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public LinkedList<Machine> getMachines(String query) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			return MkpkGo.getMachines(ctx,query);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public Machine save(Machine machine) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			machine.setDomain(DOMAIN);
			return MkpkGo.save(ctx,machine);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public void delete(Machine machine) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			machine.setDomain(DOMAIN);
			MkpkGo.delete(ctx,machine);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

}
