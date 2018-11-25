package com.merkapack.erp.planning.test;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.basic.MkpkDatasource;
import com.merkapack.erp.core.dao.MkpkGo;
import com.merkapack.erp.core.model.Client;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.core.model.Roll;
import com.merkapack.watson.util.MkpkMathUtils;
import com.merkapack.watson.util.MkpkStringUtils;

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
		InputStream in = PlanningExcelTest.class.getResourceAsStream("PlanningExcelTest.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(in);
		XSSFSheet sheet = wb.getSheetAt(0);
		Iterator<Row> iterator = sheet.rowIterator();
		LinkedList<Planning> list = new LinkedList<Planning>();
		int order = 1;
		while (iterator.hasNext()) {
			Row row = iterator.next();
			Cell amountCell = row.getCell(3);
			if (amountCell.getCellType() != CellType.STRING) {
				Planning pl = new Planning();
				Cell clientCell = row.getCell(0);
				String clientValue = clientCell.getStringCellValue();
				Client client = getClient( clientValue );
				pl.setClient(client);
				
				double amountValue = amountCell.getNumericCellValue();
				pl.setAmount(amountValue);

				Cell productCell = row.getCell(1);
				String productValue = productCell.getStringCellValue();
				Cell materialCell = row.getCell(2);
				String materialValue = materialCell.getStringCellValue();
				Product product = getProduct( productValue , materialValue);
				pl.setOrder(order);
				if (product != null) {
					pl.setProduct( product );
					pl.setWidth( product.getWidth() );
					pl.setLength( product.getLength() );
					pl.setMaterial(product.getMaterial());
					
					Roll roll = getRoll(product);
					if (roll != null) {
						pl.setRoll( roll );
						pl.setRollWidth( roll.getWidth() );
						pl.setRollLength( roll.getLength() );
					}
				}
				list.add(pl);
				order++;
			}
		}
	    wb.close();
	    in.close();
	}

	private Product getProduct(String productValue, String materialValue) {
		productValue = MkpkStringUtils.prependIfMissing(MkpkStringUtils.appendIfMissing(productValue, "%"), "%");
		materialValue = MkpkStringUtils.prependIfMissing(MkpkStringUtils.appendIfMissing(materialValue, "%"), "%");
		
		final String productName = productValue;
		final String materialName = materialValue;
		LinkedList<Product> products  = MkpkGo.getProducts(ctx
				, p-> p.getNameProperty().like(productName)
					.and(p.getMaterialNameProperty().like(materialName))
				);
		if (products != null) {
			if (products.size() == 1) {
				Product p = products.get(0);
				System.out.println("\tProduct found [] --> " + p.getName() + "(" + p.getLength() + "x" + p.getWidth() + ")");
				return p;
			} else {
				System.out.println("\tProduct ambiguous[] --> " + productName + ", " + materialName);
			}
		}
		return null;
	}

	private Client getClient(String clientValue) {
		clientValue = MkpkStringUtils.prependIfMissing(MkpkStringUtils.appendIfMissing(clientValue, "%"), "%");
		final String clientName = clientValue;
		LinkedList<Client> clients  = MkpkGo.getClients(ctx
				, p-> p.getNameProperty().like(clientName));
		if (clients != null) {
			if (clients.size() == 1) {
				Client c = clients.get(0);
				System.out.println("Client found [] --> " + c.getName());
				return c;
			} else {
				System.out.println("Client ambiguous[] --> " + clientName);
			}
		}
		return null;
	}

	private Roll getRoll( Product product ) {
		LinkedList<Roll> rolls  = MkpkGo.getRolls(ctx , p-> p.getMaterialIdProperty().eq(product.getMaterial().getId()) );
		LinkedList<Roll> availableRolls = new LinkedList<Roll>(); 
		for ( Roll roll : rolls) {
			if (MkpkMathUtils.isZero( roll.getWidth() % product.getWidth()) ) {
				availableRolls.add(roll);
			}
		}
		if (availableRolls.size() == 0) {
			System.out.println("\tNo Rolls available --> " + product.getName() + ", " + product.getMaterial().getName());
		} else if (availableRolls.size() == 1) {
			Roll r = availableRolls.get(0);
			System.out.println("\tRoll found [] --> " + r.getName() + "(" + r.getLength() + "x" + r.getWidth() + ")");
			return r;
		} else {
			System.out.println("\tRoll ambiguous[] --> " + product.getName() + ", " + product.getMaterial().getName());
		}
		return null;
	}
}
