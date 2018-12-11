package com.merkapack.erp.planning.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.LinkedList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.basic.MkpkDatasource;
import com.merkapack.erp.core.dao.MkpkGo;
import com.merkapack.erp.core.model.Machine;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.gwt.server.Excel2Planning;
import com.merkapack.erp.gwt.shared.PlanningCalculatorParams;
import com.merkapack.erp.gwt.shared.PlanningCalculator;

public class PlanningExcelTest {
	
	private static final int DOMAIN = 1;
	private static final String USER = "admin";
	private static DBContext ctx;

	private static Writer writer; 
	
	@BeforeClass
	public static void init() {
		writer = new OutputStreamWriter( System.out );
		System.out.println("Init!!");
		ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
	}
	
	@AfterClass
	public static void finish() throws IOException {
		writer.close(); 
		ctx.close();
		System.out.println("Finish!!");
	}

	@Test
	public void testCalculator() throws EncryptedDocumentException, IOException, OpenXML4JException {
		Date startDate = new Date(); 
		Machine machine = MkpkGo.getMachines(ctx, "VERD").get(0);
		PlanningCalculatorParams params = new PlanningCalculatorParams()
				.setWorkHoursInADay(16 * 60 )
				.setHoursMargin( 0.5 * 60 );
		
		InputStream in = PlanningExcelTest.class.getResourceAsStream("PlanningExcelTest.xlsx");
		LinkedList<Planning> list = Excel2Planning.importPlanning(ctx, in);
		in.close();
		for (Planning pl : list) {
			pl.setDate(startDate);
			pl.setBlowsMinute(machine.getBlows());
			PlanningCalculator.calculate(params,pl);

		}
		list = PlanningCalculator.calculate( params ,list);
		PlanningCalculatorTest.print(writer, list);
	}

}
