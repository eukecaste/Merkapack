package com.merkapack.erp.gwt.server;

import java.util.LinkedList;

import javax.servlet.annotation.WebServlet;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.basic.MkpkDatasource;
import com.merkapack.erp.core.dao.MkpkGo;
import com.merkapack.erp.core.model.Filter;
import com.merkapack.erp.core.model.MkpkCoreException;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.core.model.ProductParams;
import com.merkapack.erp.core.model.Properties.ProductProperties;
import com.merkapack.erp.gwt.client.rpc.ProductService;
import com.merkapack.watson.util.MkpkMathUtils;
import com.merkapack.watson.util.MkpkStringUtils;

@WebServlet(name = "Product Service Servlet", urlPatterns = { "/mkpk_gwt/MkpkProduct" })
public class ProductServiceImpl extends StatelessRemoteServiceServlet implements ProductService {

	private static final long serialVersionUID = 949123203256791644L;

	@Override
	public LinkedList<Product> getProducts(ProductParams params,int offset, int count) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			return MkpkGo.getProducts(ctx,offset,count, p -> getFilter(p,params) );
		} catch (Throwable t) {
			if (t instanceof MkpkCoreException) {
				throw t;
			} 
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	private Filter getFilter(ProductProperties p, ProductParams params) {
		Filter filter = null;
		String property = params.getCode(); 
		if (MkpkStringUtils.isNotBlank(property)) {
			if (MkpkStringUtils.contains(property,MkpkStringUtils.ASTERISK) ) {
				property =  MkpkStringUtils.replace(property,MkpkStringUtils.ASTERISK,MkpkStringUtils.PERCENT);
			}
			property =  MkpkStringUtils.prependIfMissing(property, MkpkStringUtils.PERCENT);
			property =  MkpkStringUtils.appendIfMissing(property, MkpkStringUtils.PERCENT);
			filter = p.getCodeProperty().like(property);
		}
		
		property = params.getName(); 
		if (MkpkStringUtils.isNotBlank(property)) {
			Filter subFilter = null;
			if (MkpkStringUtils.contains(property,MkpkStringUtils.ASTERISK) ) {
				property =  MkpkStringUtils.replace(property,MkpkStringUtils.ASTERISK,MkpkStringUtils.PERCENT);
			}
			property =  MkpkStringUtils.prependIfMissing(property, MkpkStringUtils.PERCENT);
			property =  MkpkStringUtils.appendIfMissing(property, MkpkStringUtils.PERCENT);
			filter = filter ==null?subFilter:filter.and(p.getNameProperty().like(property));
		}
		
		property = params.getMaterialUp().getName();
		if (MkpkStringUtils.isNotBlank(property)) {
			Filter subFilter = null;
			if (MkpkStringUtils.contains(property,MkpkStringUtils.ASTERISK) ) {
				property =  MkpkStringUtils.replace(property,MkpkStringUtils.ASTERISK,MkpkStringUtils.PERCENT);
			}
			property =  MkpkStringUtils.prependIfMissing(property, MkpkStringUtils.PERCENT);
			property =  MkpkStringUtils.appendIfMissing(property, MkpkStringUtils.PERCENT);
			filter = filter ==null?subFilter:filter.and(p.getMaterialUpNameProperty().like(property));
		}
		
		property = params.getMaterialDown().getName();
		if (MkpkStringUtils.isNotBlank(property)) {
			Filter subFilter = null;
			if (MkpkStringUtils.contains(property,MkpkStringUtils.ASTERISK) ) {
				property =  MkpkStringUtils.replace(property,MkpkStringUtils.ASTERISK,MkpkStringUtils.PERCENT);
			}
			property =  MkpkStringUtils.prependIfMissing(property, MkpkStringUtils.PERCENT);
			property =  MkpkStringUtils.appendIfMissing(property, MkpkStringUtils.PERCENT);
			filter = filter ==null?subFilter:filter.and(p.getMaterialDownNameProperty().like(property));
		}
		
		Double doubleProperty = params.getLength();
		if (doubleProperty != null && MkpkMathUtils.isNotZero(doubleProperty)) {
			Filter subFilter = p.getLengthProperty().eq(doubleProperty); 
			filter = filter ==null?subFilter:filter.and(subFilter);
		}

		doubleProperty = params.getWidth();
		if (doubleProperty != null && MkpkMathUtils.isNotZero(doubleProperty)) {
			Filter subFilter = p.getWidthProperty().eq(doubleProperty); 
			filter = filter ==null?subFilter:filter.and(subFilter);
		}
		return filter;
	}

	@Override
	public LinkedList<Product> getProducts(int offset, int count,String query) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			LinkedList<Product> list = MkpkGo.getProducts(ctx,offset,count,query);
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
