package com.merkapack.erp.core.dao.jooq;

import static com.merkapack.erp.master.jooq.tables.Material.MATERIAL;
import static com.merkapack.erp.master.jooq.tables.Roll.ROLL;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.stream.Collectors;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.dao.jooq.Mapper.RollMapper;
import com.merkapack.erp.core.model.Roll;
import com.merkapack.watson.util.MkpkStringUtils;

public class RollDAO {
	
	public static Roll getRoll(DBContext ctx, Integer id) {
		return ctx.getDslContext().select()
			.from( ROLL )
			.join(MATERIAL).on(ROLL.MATERIAL.eq(MATERIAL.ID))
			.where(ROLL.ID.eq(id))
			.fetch()
			.stream()
			.map( new RollMapper() )
			.findFirst()
			.orElse(null);
	}
	public static LinkedList<Roll> getRolls(DBContext ctx) {
		return ctx.getDslContext().select()
			.from( ROLL )
			.join(MATERIAL).on(ROLL.MATERIAL.eq(MATERIAL.ID))
			.orderBy(ROLL.NAME)
			.fetch()
			.stream()
			.map( new RollMapper() )
			.collect(Collectors.toCollection(LinkedList::new));
	}
	public static LinkedList<Roll> getRolls(DBContext ctx, String query) {
		
		query = MkpkStringUtils.prependIfMissing(query, "%");
		query = MkpkStringUtils.appendIfMissing(query, "%");
		return ctx.getDslContext().select()
				.from( ROLL )
				.join(MATERIAL).on(ROLL.MATERIAL.eq(MATERIAL.ID))
				.where(ROLL.NAME.like(query))
				.fetch()
				.stream()
				.map( new RollMapper() )
				.collect(Collectors.toCollection(LinkedList::new));
	}
	public static Roll save(DBContext ctx, Roll roll) {
		if (roll.getId() == null) {
			return insert(ctx, roll);
		} 
		return update(ctx, roll);
	}
	public static Roll insert(DBContext ctx,Roll roll) {
		Integer id = ctx.getDslContext()
			.insertInto(ROLL)
			.set(ROLL.DOMAIN,roll.getDomain())
			.set(ROLL.NAME,roll.getName())
			.set(ROLL.MATERIAL,roll.getMaterial().getId())
			.set(ROLL.WIDTH,roll.getWidth())
			.set(ROLL.LENGTH,roll.getLength())
			.set(ROLL.CREATION_USER,ctx.getUser())
			.set(ROLL.CREATION_DATE, new Timestamp( System.currentTimeMillis()) )
			.returning(ROLL.ID)
			.fetchOne()
			.getValue(ROLL.ID);
		ctx.log().info("INSERT ROLL: (id) " + id);		
		return getRoll(ctx, id);
	}
	
	public static Roll update(DBContext ctx, Roll roll) {
		int count = ctx.getDslContext()
			.update(ROLL)
			.set(ROLL.NAME,roll.getName())
			.set(ROLL.MATERIAL,roll.getMaterial().getId())
			.set(ROLL.WIDTH,roll.getWidth())
			.set(ROLL.LENGTH,roll.getLength())
			.set(ROLL.MODIFICATION_USER,ctx.getUser())
			.set(ROLL.MODIFICATION_DATE, new Timestamp( System.currentTimeMillis()) )
			.where(ROLL.ID.equal( roll.getId()))
			.execute();
		ctx.log().info("UPDATE ROLL ("+count+" filas) : (id) " + roll.getId());		
		return getRoll(ctx, roll.getId());
	}
	public static void delete(DBContext ctx, Roll roll) {
		int count = ctx.getDslContext()
				.delete(ROLL)
				.where(ROLL.ID.equal( roll.getId()))
				.execute();
			ctx.log().info("DELETE ROLL ("+count+" filas) : (id) " + roll.getId());		
	}

}
