package com.merkapack.erp.gwt.server;

import java.util.LinkedList;

import javax.servlet.annotation.WebServlet;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.basic.MkpkDatasource;
import com.merkapack.erp.core.dao.MkpkGo;
import com.merkapack.erp.core.model.Client;
import com.merkapack.erp.core.model.MkpkCoreException;
import com.merkapack.erp.gwt.client.rpc.ClientService;

@WebServlet(name = "Client Service Servlet", urlPatterns = { "/mkpk_gwt/MkpkClient" })
public class ClientServiceImpl extends StatelessRemoteServiceServlet implements ClientService {

	private static final long serialVersionUID = 949123203256791644L;

	@Override
	public LinkedList<Client> getClients() throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			return MkpkGo.getClients(ctx);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public LinkedList<Client> getClients(String query) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			return MkpkGo.getClients(query,ctx);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public Client save(Client client) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			client.setDomain(DOMAIN);
			return MkpkGo.save(ctx,client);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public void delete(Client client) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			client.setDomain(DOMAIN);
			MkpkGo.delete(ctx,client);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

}
