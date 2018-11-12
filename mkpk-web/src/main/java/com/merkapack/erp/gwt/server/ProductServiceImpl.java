package com.merkapack.erp.gwt.server;

import java.util.LinkedList;

import javax.servlet.annotation.WebServlet;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.basic.MkpkDatasource;
import com.merkapack.erp.core.dao.MkpkGo;
import com.merkapack.erp.core.model.MkpkCoreException;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.gwt.client.rpc.ProductService;

@WebServlet(name = "Product Service Servlet", urlPatterns = { "/mkpk_gwt/MkpkProduct" })
public class ProductServiceImpl extends StatelessRemoteServiceServlet implements ProductService {

	private static final long serialVersionUID = 949123203256791644L;

	@Override
	public LinkedList<Product> getProducts() throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			return MkpkGo.getProducts(ctx);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public LinkedList<Product> getProducts(String query) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			LinkedList<Product> list = MkpkGo.getProducts(ctx,query);
			return list;
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public Product save(Product product) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			product.setDomain(DOMAIN);
			return MkpkGo.save(ctx,product);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public void delete(Product product) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			product.setDomain(DOMAIN);
			MkpkGo.delete(ctx,product);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

}
