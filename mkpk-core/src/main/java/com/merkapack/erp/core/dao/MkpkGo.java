package com.merkapack.erp.core.dao;

import java.util.LinkedList;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.dao.jooq.MachineDAO;
import com.merkapack.erp.core.dao.jooq.MaterialDAO;
import com.merkapack.erp.core.model.Machine;
import com.merkapack.erp.core.model.Material;

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

	public static LinkedList<Material> getMaterials(DBContext ctx) {
		return ctx.getDslContext().transactionResult(
				configuration -> MaterialDAO.getMaterials(ctx));
	}
	
	public static Material save(DBContext ctx,Material material) {
		return ctx.getDslContext().transactionResult(
				configuration -> MaterialDAO.save(ctx,material));
	}

	public static void delete(DBContext ctx, Material material) {
		ctx.getDslContext().transaction( configuration -> MaterialDAO.delete(ctx,material));
	}
}
