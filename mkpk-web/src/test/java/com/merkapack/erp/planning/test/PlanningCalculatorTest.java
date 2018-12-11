package com.merkapack.erp.planning.test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
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
import com.merkapack.erp.gwt.client.util.GWTDateUtils;
import com.merkapack.erp.gwt.shared.PlanningCalculatorParams;
import com.merkapack.erp.gwt.shared.PlanningCalculatorStrategy;
import com.merkapack.erp.gwt.shared.PlanningCalculator;
import com.merkapack.watson.util.MkpkNumberUtils;
import com.merkapack.watson.util.MkpkStringUtils;

public class PlanningCalculatorTest {

	private static final int DOMAIN = 1;
	private static final String USER = "admin";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final DecimalFormat FMT = new DecimalFormat("#,##0.00");
	private static final DecimalFormat FMT_INT = new DecimalFormat("#,##0");

	@Test
	public void testCalculator() throws IOException {

		DBContext ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
		Planning pl = new Planning();
		PlanningCalculatorParams params =  new PlanningCalculatorParams()	
				.setWorkHoursInADay(16 * 60 )
				.setHoursMargin( 0.5 * 60 );

		pl.setDate(new Date());
		pl.setOrder(1);

		LinkedList<Machine> machines = MkpkGo.getMachines(ctx, "ver");
		Machine m = machines.get(0);
		pl.setMachine(m);
		pl.setBlowsMinute(m.getBlows());

		Product p = MkpkGo.getProducts(ctx, "170x250").get(0);

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

		pl.setMinutes(17 * 60);
		PlanningCalculator.calculate(params, PlanningCalculatorStrategy.TIME_CHANGED, pl);

		LinkedList<Planning> list = new LinkedList<Planning>();
		list.add(pl);

		LinkedList<Planning> ret = PlanningCalculator.calculate(params, list);
		print(new OutputStreamWriter( System.out ),ret);
	}

	public static void print(Writer writer, LinkedList<Planning> list) throws IOException {
		if (list != null && list.size() > 0) {
			writer.write("\n");
			String line1 = 
				  '|' + MkpkStringUtils.center("Fecha", 12) 
				+ '|' + MkpkStringUtils.center("#", 3)
				+ '|' + MkpkStringUtils.rightPad("Medida", 14) 
				+ '|' + MkpkStringUtils.rightPad("Material", 13) 
				+ '|' + MkpkStringUtils.rightPad("Bobina", 14)
				+ '|' + MkpkStringUtils.leftPad("Unidad", 10) 
				+ '|' + MkpkStringUtils.leftPad("U/G", 11)
				+ '|' + MkpkStringUtils.leftPad("Metros", 10) 
				+ '|' + MkpkStringUtils.leftPad("Golpes", 10) 
				+ '|' + MkpkStringUtils.leftPad("G/M", 5) 
				+ '|' + MkpkStringUtils.leftPad("Tiempo", 10) 
				+ '|' + MkpkStringUtils.rightPad("Cliente", 20)
				+ '|' + MkpkStringUtils.rightPad("Comentarios", 21) 
				+ '|';
			;
			writer.write(line1);
			writer.write("\n");
			writer.write(MkpkStringUtils.repeat("-", line1.length()) );
			writer.write("\n");
			Date date = null;
			double minutes = 0;
			for (Planning pl : list) {
				if (GWTDateUtils.compare(pl.getDate(), date) != 0) {
					if (date != null) {
						writer.write(MkpkStringUtils.repeat("-", line1.length()) );
						writer.write("\n");
						String lineTotalMinutes = MkpkStringUtils
								.leftPad("Total horas dia (" + DATE_FORMAT.format(date) + ")", 113)
								+ MkpkStringUtils.leftPad(FMT.format(minutes), 10);
						writer.write(lineTotalMinutes);
						writer.write("\n");
						writer.write("\n");
					}
					date = pl.getDate();
					minutes = 0;
				}
				minutes = minutes + pl.getMinutes();

				String line = 
					  '|' + MkpkStringUtils.center(DATE_FORMAT.format(pl.getDate()), 12) 
					+ '|' + MkpkStringUtils.center(MkpkNumberUtils.toString(pl.getOrder()), 3) 
					+ '|' + MkpkStringUtils.rightPad(MkpkStringUtils.abbreviate(pl.getProduct() != null ? pl.getProduct().getName() : "", 13), 14)
					+ '|' + MkpkStringUtils.rightPad(MkpkStringUtils.abbreviate(pl.getMaterial() != null ? pl.getMaterial().getName() : "", 12), 13)
					+ '|' + MkpkStringUtils.rightPad(MkpkStringUtils.abbreviate(pl.getRoll() != null ? pl.getRoll().getName() : "", 13), 14)
					+ '|' + MkpkStringUtils.leftPad(FMT_INT.format(pl.getAmount()), 10) 
					+ '|' + MkpkStringUtils.leftPad(FMT_INT.format(pl.getBlowUnits()), 11) 
					+ '|' + MkpkStringUtils.leftPad(FMT.format(pl.getMeters()), 10) 
					+ '|' + MkpkStringUtils.leftPad(FMT.format(pl.getBlows()), 10) 
					+ '|' + MkpkStringUtils.leftPad(FMT_INT.format(pl.getBlowsMinute()), 5) 
					+ '|' + MkpkStringUtils.leftPad(FMT.format(pl.getMinutes()), 10) 
					+ '|' + MkpkStringUtils.rightPad(MkpkStringUtils.abbreviate(pl.getClient() != null ? pl.getClient().getName() : "", 19),20)
					+ '|' + MkpkStringUtils.rightPad( MkpkStringUtils.abbreviate(pl.getComments() != null ? pl.getComments() : ".....", 20),							21)
					+ '|';
				writer.write(line);
				writer.write("\n");
			}
			if (minutes != 0) {
				writer.write(MkpkStringUtils.repeat("-", line1.length()) );
				writer.write("\n");
				String lineTotalMinutes = MkpkStringUtils.leftPad("Total horas dia (" + DATE_FORMAT.format(date) + ")",
						113) + MkpkStringUtils.leftPad(FMT.format(minutes / 60), 10);
				writer.write(lineTotalMinutes);
				writer.write("\n");
			}
		} else {
			System.out.println("No hay Datos");
		}

	}
}
