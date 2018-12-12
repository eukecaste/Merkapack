package com.merkapack.erp.planning.test;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.basic.MkpkDatasource;
import com.merkapack.erp.core.dao.MkpkGo;
import com.merkapack.erp.core.model.Material;
import com.merkapack.erp.core.model.Roll;

@Ignore
public class DBRollTest {
	
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
		String[] materials = new String[] {
 			 "COCCION"
			,"M-9"
			,"M-8"
			,"GOFRADAS"
			,"GOFRADA ROLLO"
			,"NEGRA"
			,"NEGRAS"
			,"ORO"
			,"ORO/ORO"
			,"PLATA"
			,"IMPRESO"
			,"PLATA/PLATA"
			,"CALIDAD"
			,"M-12"
			,"M-15"
			,"TRANSPARENTE"
			,"M-14"
			,"M-17"
			,"M-220"
			,"80 My"
			,"PORTUGAL"
		};
		double widths[] = new double[] {500,550,600,650,700,750,800,900,1000,1050,1100,1200};
		double lengths[] = new double[] {3000,1250,1500,1000};
		
		for (String material : materials) {
			Material m = MkpkGo.getMaterials(ctx,material).get(0);
			for ( double width : widths) {
				for ( double length : lengths) {
					String name = "" + ((int) width) + "x" + ((int) length);
					MkpkGo.save(ctx, new Roll()
						.setDomain(DOMAIN)
						.setName(name)
						.setMaterial(m)
						.setWidth(width)
						.setLength(length)
					);
				}
			}
		}
	}

}
