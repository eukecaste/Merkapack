package com.merkapack.erp.core.dao;

import java.util.LinkedList;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.dao.jooq.MachineDAO;
import com.merkapack.erp.core.model.Machine;

public class MkpkGo {
	
	public static LinkedList<Machine> getMachines(DBContext ctx) {
		return ctx.getDslContext().transactionResult(
				configuration -> MachineDAO.getMachines(ctx));
	}
	
	public static Machine save(DBContext ctx,Machine machine) {
		return ctx.getDslContext().transactionResult(
				configuration -> MachineDAO.save(ctx,machine));
	}

	public static void delete(DBContext ctx, Machine machine) {
		ctx.getDslContext().transaction( configuration -> MachineDAO.delete(ctx,machine));
	}

}
