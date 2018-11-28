package com.merkapack.erp.planning.test;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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
import com.merkapack.erp.gwt.shared.PlanningRowCalculator;
import com.merkapack.watson.util.MkpkMathUtils;

public class PlanningExcelTest {
	
	private static final int DOMAIN = 1;
	private static final String USER = "admin";
	private static DBContext ctx;

	SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

	@BeforeClass
	public static void init() {
		System.out.println("Init!!");
		ctx = MkpkDatasource.getDBContext(DOMAIN, USER);
	}
	
	@AfterClass
	public static void finish() {
		ctx.close();
		System.out.println("Finish!!");
	}

	@Test
	public void testCalculator() throws EncryptedDocumentException, IOException, OpenXML4JException {
		Date startDate = new Date(); 
		Machine machine = MkpkGo.getMachines(ctx, "VERD").get(0);
		
		InputStream in = PlanningExcelTest.class.getResourceAsStream("PlanningExcelTest.xlsx");
		LinkedList<Planning> list = Excel2Planning.importPlanning(ctx, in);
		in.close();
		
		for (Planning pl : list) {
			pl.setDate(startDate);
			pl.setBlowsMinute(machine.getBlows());
			shortPrint(pl);
		}
		list = PlanningRowCalculator.calculate(list);
		System.out.println("******************************");
		for (Planning pl : list) {
			shortPrint(pl);
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
}
