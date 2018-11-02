package com.merkapack.erp.core.dao.jooq;

import static com.merkapack.erp.master.jooq.tables.Machine.MACHINE;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.stream.Collectors;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.dao.jooq.Mapper.MachineMapper;
import com.merkapack.erp.core.model.Machine;

public class MachineDAO {
	public static Machine getMachine(DBContext ctx, Integer id) {
		return ctx.getDslContext().select()
			.from( MACHINE )
			.where(MACHINE.ID.eq(id))
			.fetch()
			.stream()
			.map( new MachineMapper() )
			.findFirst()
			.orElse(null);
	}
	public static LinkedList<Machine> getMachines(DBContext ctx) {
		return ctx.getDslContext().select()
			.from( MACHINE )
			.fetch()
			.stream()
			.map( new MachineMapper() )
			.collect(Collectors.toCollection(LinkedList::new));
	}
	public static Machine save(DBContext ctx, Machine machine) {
		if (machine.getId() == null) {
			return insert(ctx, machine);
		} 
		return update(ctx, machine);
	}
	public static Machine insert(DBContext ctx,Machine machine) {
		Integer id = ctx.getDslContext()
			.insertInto(MACHINE)
			.set(MACHINE.DOMAIN,machine.getDomain())
			.set(MACHINE.NAME,machine.getName())
			.set(MACHINE.CREATION_USER,ctx.getUser())
			.set(MACHINE.CREATION_DATE, new Timestamp( System.currentTimeMillis()) )
			.returning(MACHINE.ID)
			.fetchOne()
			.getValue(MACHINE.ID);
		ctx.log().info("INSERT MACHINE: (id) " + id);		
		return getMachine(ctx, id);
	}
	public static Machine update(DBContext ctx, Machine machine) {
		int count = ctx.getDslContext()
			.update(MACHINE)
			.set(MACHINE.NAME,machine.getName())
			.set(MACHINE.MODIFICATION_USER,ctx.getUser())
			.set(MACHINE.MODIFICATION_DATE, new Timestamp( System.currentTimeMillis()) )
			.where(MACHINE.ID.equal( machine.getId()))
			.execute();
		ctx.log().info("UPDATE MACHINE ("+count+" filas) : (id) " + machine.getId());		
		return getMachine(ctx, machine.getId());
	}
	public static void delete(DBContext ctx, Machine machine) {
		int count = ctx.getDslContext()
				.delete(MACHINE)
				.where(MACHINE.ID.equal( machine.getId()))
				.execute();
			ctx.log().info("DELETE MACHINE ("+count+" filas) : (id) " + machine.getId());		
	}

}
