package com.merkapack.erp.gwt.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.dao.MkpkGo;
import com.merkapack.erp.core.model.Client;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.core.model.Roll;
import com.merkapack.watson.util.MkpkMathUtils;
import com.merkapack.watson.util.MkpkStringUtils;

public class Excel2Planning {

	public static LinkedList<Planning> importPlanning(DBContext ctx, InputStream in) throws IOException {
		LinkedList<Planning> list = new LinkedList<Planning>();
		XSSFWorkbook wb = new XSSFWorkbook(in);
		XSSFSheet sheet = wb.getSheetAt(0);
		Iterator<Row> iterator = sheet.rowIterator();
		int order = 1;
		while (iterator.hasNext()) {
			Row row = iterator.next();
			Cell amountCell = row.getCell(2);
			if (amountCell.getCellType() != CellType.STRING) {
				Planning pl = new Planning();
				Cell clientCell = row.getCell(0);
				String clientValue = clientCell.getStringCellValue();
				Client client = getClient(ctx, clientValue);
				pl.setClient(client);

				double amountValue = amountCell.getNumericCellValue();
				pl.setAmount(amountValue);

				Cell productCell = row.getCell(1);
				String productValue = productCell.getStringCellValue();
				Product product = getProduct(ctx, productValue);
				pl.setOrder(order);
				if (product != null) {
					pl.setProduct(product);
					pl.setWidth(product.getWidth());
					pl.setLength(product.getLength());
					pl.setMaterialUp(product.getMaterialUp());

					Roll roll = getRoll(ctx, product);
					if (roll != null) {
						pl.setRollUp(roll);
						pl.setRollUpWidth(roll.getWidth());
						pl.setRollUpLength(roll.getLength());
					}
				}
				list.add(pl);
				order++;
			}
		}
		wb.close();
		return list;
	}

	private static Product getProduct(DBContext ctx, String productCode) {
		LinkedList<Product> products = MkpkGo.getProducts(ctx,0,10,
				p -> p.getCodeProperty().eq(productCode));
		if (products != null) {
			if (products.size() == 1) {
				return products.get(0);
			}
		}
		return null;
	}

	private static Client getClient(DBContext ctx, String clientValue) {
		clientValue = MkpkStringUtils.prependIfMissing(MkpkStringUtils.appendIfMissing(clientValue, "%"), "%");
		final String clientName = clientValue;
		LinkedList<Client> clients = MkpkGo.getClients(ctx, p -> p.getNameProperty().like(clientName));
		if (clients != null && clients.size() > 0) {
//			if (clients.size() == 1) {
				return clients.get(0);
//			}
		}
		return null;
	}

	private static Roll getRoll(DBContext ctx, Product product) {
		LinkedList<Roll> rolls = MkpkGo.getRolls(ctx, p -> p.getMaterialIdProperty().eq(product.getMaterialUp().getId()));
		LinkedList<Roll> availableRolls = new LinkedList<Roll>();
		for (Roll roll : rolls) {
			if (MkpkMathUtils.isZero(roll.getWidth() % product.getWidth())) {
				availableRolls.add(roll);
			}
		}
		if (availableRolls.size() > 0) {
			return availableRolls.get(0);
		}
		return null;
	}

}
