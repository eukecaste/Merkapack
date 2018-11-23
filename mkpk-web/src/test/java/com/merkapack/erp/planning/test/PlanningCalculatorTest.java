package com.merkapack.erp.planning.test;

import java.util.Date;
import java.util.LinkedList;

import org.junit.Test;

import com.ibm.icu.text.SimpleDateFormat;
import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.basic.MkpkDatasource;
import com.merkapack.erp.core.dao.MkpkGo;
import com.merkapack.erp.core.model.Machine;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.core.model.Roll;
import com.merkapack.erp.gwt.shared.PlanningRowCalculator;
import com.merkapack.erp.gwt.shared.PlanningRowCalculator.Strategy;
import com.merkapack.watson.util.MkpkMathUtils;

public class PlanningCalculatorTest {
	
	private static final int DOMAIN = 1;
	private static final String USER = "admin";
	SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

	@Test
	public void testCalculator() {
		
		DBContext ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
		Planning pl = new Planning();
		
		pl.setDate(new Date());
		pl.setOrder(1);
		
		LinkedList<Machine> machines = MkpkGo.getMachines(ctx, "ver");
		Machine m = machines.get(0);
		pl.setMachine(m);
		pl.setBlowsMinute(m.getBlows());
		
		LinkedList<Product> products = MkpkGo.getProducts(ctx, "170");
		Product p = products.get(0);
		pl.setProduct(p);
		pl.setWidth(p.getWidth());
		pl.setLength(p.getLength());
		pl.setMaterial(p.getMaterial());
		
		LinkedList<Roll> rolls = MkpkGo.getRolls(ctx, "1000");
		Roll r = rolls.get(0);
		pl.setRoll(r);
		pl.setRollWidth(r.getWidth());
		pl.setRollLength(r.getLength());
		pl.setClient(MkpkGo.getClients(ctx, "VAC").get(0));

		pl.setMinutes(17*60);
		PlanningRowCalculator.calculate(pl, Strategy.TIME_CHANGED);
		

//		Planning pl2 = pl.clone();
//		pl2.setOrder(2);
//		pl2.setClient(MkpkGo.getClients(ctx, "PAC").get(0));
//		pl2.setMeters(12005);
//		PlanningRowCalculator.calculate(pl2, Strategy.METERS_CHANGED);
		
		LinkedList<Planning> list = new LinkedList<Planning>();
		list.add(pl);
//		list.add(pl2);
		
		LinkedList<Planning> ret = PlanningRowCalculator.calculate(list);
		for( Planning pln : ret ) {
			print(pln);
		}
		
		for( Planning pln : ret ) {
			shortPrint(pln);
		}
	}
	
	private void shortPrint( Planning pl) {
		String line = "" +  pl.getOrder() + "  -"  
			+ "\t" + FORMATTER.format( pl.getDate())  
			+ "\t" + pl.getMeters() + " mts." 
			+ "\t" + pl.getMinutes() + "' (" + MkpkMathUtils.round(pl.getMinutes() / 60) + " Horas" + ")"
		;
		System.out.println(line);
	}

	private void print( Planning pl) {
		String line = 
				"id ..." + pl.getId() + "\n" +
				"\tdomain ..." +  pl.getDomain() + "\n" +
				"\tdate ..." +  pl.getDate() + "\n" +
				"\torder ..." +  pl.getOrder() + "\n" +
				"\tmachine ..." +  pl.getMachine().getName()  + "\n" +
				"\tproduct ..." +  pl.getProduct().getName() + "\n" +
				"\twidth ..." +  pl.getWidth() + "\n" +
				"\tlength ..." +  pl.getLength() + "\n" +
				"\tmaterial ..." +  pl.getMaterial().getName() + "\n" +
				"\troll ..." +  pl.getRoll().getName() + "\n" +
				"\trollWidth ..." +  pl.getRollWidth() + "\n" +
				"\trollLength ..." +  pl.getRollLength() + "\n" +
				"\tamount ..." +  pl.getAmount() + "\n" +
				"\tblowUnits ..." + pl.getBlowUnits() + "\n" +
				"\tmeters ..." + pl.getMeters() + "\n" +
				"\tblows ..." + pl.getBlows() + "\n" +
				"\tblowsMinute ..." + pl.getBlowsMinute() + "\n" +
				"\tminutes ..." + pl.getMinutes() + " --- " + MkpkMathUtils.round(pl.getMinutes() / 60) + " Horas\n" +
				"\tclient ..." + pl.getClient().getName() + "\n"
			;
			System.out.println(line);
	}
}
