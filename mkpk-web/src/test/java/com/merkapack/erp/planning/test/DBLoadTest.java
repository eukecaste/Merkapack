package com.merkapack.erp.planning.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gwt.dev.util.collect.HashMap;
import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.basic.MkpkDatasource;
import com.merkapack.erp.core.dao.MkpkGo;
import com.merkapack.erp.core.model.Material;
import com.merkapack.erp.core.model.Product;
import com.merkapack.watson.util.MkpkNumberUtils;
import com.merkapack.watson.util.MkpkStringUtils;

public class DBLoadTest {
	
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
		InputStream in = DBLoadTest.class.getResourceAsStream("DBLoad.xlsx");
		HashMap<String,Material> materials = new HashMap<String,Material>();
		XSSFWorkbook wb = new XSSFWorkbook(in);
		XSSFSheet sheet = wb.getSheetAt(0);
		Iterator<Row> iterator = sheet.rowIterator();
		if (iterator.hasNext()) iterator.next();
		while (iterator.hasNext()) {
			Row row = iterator.next();
			Cell codeCell = row.getCell(0);
			Cell nameCell = row.getCell(1);
			Cell widthCell = row.getCell(2);
			Cell lengthCell = row.getCell(3);
			Cell materialCell = row.getCell(4);
			Cell boxUnitsCell = row.getCell(5);
			Cell moldCell = row.getCell(7);
			
			String code = codeCell.getStringCellValue();
			String name = nameCell.getStringCellValue();
			String width = widthCell.getStringCellValue();
			String length = lengthCell.getStringCellValue();
			String material = MkpkStringUtils.trim(materialCell.getStringCellValue());
			double boxUnits = boxUnitsCell.getNumericCellValue();
			String mold = moldCell.getStringCellValue();
			if (!MkpkStringUtils.isBlank(material)) {
				if (!materials.containsKey(material)) {
					Material m = new Material()
							.setDomain(DOMAIN)
							.setCode(material)
							.setName(material);
					
					m  = MkpkGo.save(ctx, m);
					materials.put(material, m);
				}
				Material m = materials.get(material);
				
				Product p = new Product()
						.setDomain(DOMAIN)
						.setCode(code)
						.setName(name)
						.setWidth(MkpkNumberUtils.toDouble(width))
						.setLength(MkpkNumberUtils.toDouble(length))
						.setMaterialUp(m)
						.setMaterialDown(m)
						.setBoxUnits(boxUnits)
						.setMold(mold);
				MkpkGo.save(ctx, p);
			}
		}
		wb.close();
		in.close();
	}

}
