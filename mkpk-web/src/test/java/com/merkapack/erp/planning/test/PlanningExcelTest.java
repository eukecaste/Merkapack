package com.merkapack.erp.planning.test;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
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
import com.merkapack.watson.util.MkpkNumberUtils;
import com.merkapack.watson.util.MkpkStringUtils;

public class PlanningExcelTest {
	
	private static final int DOMAIN = 1;
	private static final String USER = "admin";
	private static DBContext ctx;

	SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat INT_FORMATTER = new DecimalFormat("#,##0");
	DecimalFormat DEC_FORMATTER = new DecimalFormat("#,##0.00");

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
		}
		list = PlanningRowCalculator.calculate(list);
		for (Planning pl : list) {
			shortPrint(pl);
		}
		
	}

	private void shortPrint( Planning pl) {
		String line = 
			MkpkStringUtils.rightPad(FORMATTER.format( pl.getDate()), 11) 
			+ MkpkStringUtils.center( MkpkNumberUtils.toString( pl.getOrder()), 5)
			+ MkpkStringUtils.rightPad( MkpkStringUtils.abbreviate(pl.getProduct()!=null?pl.getProduct().getName():"", 20),21)
			+ MkpkStringUtils.rightPad( MkpkStringUtils.abbreviate(pl.getMaterial()!=null?pl.getMaterial().getName():"", 20),21)
			+ MkpkStringUtils.rightPad( MkpkStringUtils.abbreviate(pl.getRoll()!=null?pl.getRoll().getName():"", 20),21)
			+ MkpkStringUtils.leftPad(INT_FORMATTER.format( pl.getAmount()), 10)
			+ MkpkStringUtils.leftPad(INT_FORMATTER.format( pl.getBlowUnits()), 5)
			+ MkpkStringUtils.leftPad(DEC_FORMATTER.format( pl.getMeters()), 10)
			+ MkpkStringUtils.leftPad(DEC_FORMATTER.format( pl.getBlows()), 12)
			+ MkpkStringUtils.leftPad(INT_FORMATTER.format( pl.getBlowsMinute()), 5)
			+ MkpkStringUtils.leftPad(DEC_FORMATTER.format( pl.getHours()), 10)
			+ " "
			+ MkpkStringUtils.rightPad( MkpkStringUtils.abbreviate(pl.getClient()!=null?pl.getClient().getName():"", 20),21)
			+ MkpkStringUtils.rightPad( MkpkStringUtils.abbreviate(pl.getComments()!=null?pl.getComments():"", 40),41)
		;
		System.out.println(line);
	}
}
