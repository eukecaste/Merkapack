package com.merkapack.erp.gwt.server;

import java.sql.Date;
import java.util.LinkedList;

import javax.servlet.annotation.WebServlet;

import com.ibm.icu.util.Calendar;
import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.basic.MkpkDatasource;
import com.merkapack.erp.core.dao.MkpkGo;
import com.merkapack.erp.core.model.Filter;
import com.merkapack.erp.core.model.MkpkCoreException;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.core.model.PlanningParams;
import com.merkapack.erp.core.model.Properties.PlanningProperties;
import com.merkapack.erp.gwt.client.rpc.PlanningService;
import com.merkapack.watson.server.MkpkServerDateUtils;

@WebServlet(name = "Planning Service Servlet", urlPatterns = { "/mkpk_gwt/MkpkPlanning" })
public class PlanningServiceImpl extends StatelessRemoteServiceServlet implements PlanningService {

	private static final long serialVersionUID = 949123203256791644L;
	
	private static Filter getFilter(PlanningProperties p,PlanningParams params) {
		Filter filter = null;
		if ( params.getDate() != null) {
			filter = p.getDateProperty().ge(new Date(params.getDate().getTime()));
			Date nextSevenDays = new Date(params.getDate().getTime());
			nextSevenDays = MkpkServerDateUtils.add(nextSevenDays, Calendar.DAY_OF_MONTH , 7);
			filter = p.getDateProperty().le(new Date(params.getDate().getTime()));
		}
		if ( params.getMachine() != null) {
			filter = filter.and(p.getMachineIdProperty().eq(params.getMachine()));
		}
		return filter;
	}
	
	@Override
	public LinkedList<Planning> getPlannings(PlanningParams params) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			return MkpkGo.getPlannings(ctx, p -> getFilter(p, params));
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public Planning save(Planning planning) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			planning.setDomain(DOMAIN);
			return MkpkGo.save(ctx,planning);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public LinkedList<Planning> save(LinkedList<Planning> list) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			return MkpkGo.save(ctx,list);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

	@Override
	public void delete(Planning planning) throws MkpkCoreException {
		DBContext ctx = null;
		try {
			ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
			MkpkGo.delete(ctx,planning);
		} catch (Throwable t) {
			throw new MkpkCoreException("Se ha producido un error ["+ t.getMessage() +"]", t);
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}

}
