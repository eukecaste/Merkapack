package com.merkapack.erp.core.dao.jooq;

import static com.merkapack.erp.master.jooq.tables.Material.MATERIAL;
import static com.merkapack.erp.master.jooq.tables.Roll.ROLL;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectOnConditionStep;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.dao.jooq.Mapper.RollMapper;
import com.merkapack.erp.core.model.Filter.Property;
import com.merkapack.erp.core.model.Filter.RollFilter;
import com.merkapack.erp.core.model.Properties.RollProperties;
import com.merkapack.erp.core.model.Roll;
import com.merkapack.watson.util.MkpkStringUtils;

public class RollDAO {
	
	private static final RollPropertiesDAO ROLL_PROPERTIES = new RollPropertiesDAO();

	protected static class RollPropertiesDAO implements RollProperties {
		protected Condition[] getConditions(RollFilter filter) {
			FilterDAO filterDAO = (FilterDAO) filter.filter(this);
			if (filterDAO == null) return new Condition[0];
			return new Condition[] { filterDAO.getCondition() };
		}
		@Override public Property<Integer> getIdProperty() {return new FilterDAO.PropertyDAO<Integer>(ROLL.ID);} 
		@Override public Property<Integer> getDomainProperty() {return new FilterDAO.PropertyDAO<Integer>(ROLL.DOMAIN);}
		@Override public Property<String> getNameProperty() {return new FilterDAO.PropertyDAO<String>(ROLL.NAME);}
		@Override public Property<Double> getWidthProperty() {return new FilterDAO.PropertyDAO<Double>(ROLL.WIDTH);}
		@Override public Property<Double> getLengthProperty() {return new FilterDAO.PropertyDAO<Double>(ROLL.LENGTH);}
		@Override public Property<Integer> getMaterialIdProperty() {return new FilterDAO.PropertyDAO<Integer>(MATERIAL.ID);} 
		@Override public Property<String> getMaterialNameProperty() {return new FilterDAO.PropertyDAO<String>(MATERIAL.NAME);}
		@Override public Property<String> getCreationUserProperty() {return new FilterDAO.PropertyDAO<String>(ROLL.CREATION_USER);}
		@Override public Property<Timestamp> getCreationDateProperty() {return new FilterDAO.PropertyDAO<Timestamp>(ROLL.CREATION_DATE);}
		@Override public Property<String> getModificationUserProperty() {return new FilterDAO.PropertyDAO<String>(ROLL.MODIFICATION_USER);}
		@Override public Property<Timestamp> getModificationDateProperty() {return new FilterDAO.PropertyDAO<Timestamp>(ROLL.MODIFICATION_DATE);}
	}

	private static SelectOnConditionStep<Record> getSelect(DBContext ctx) {
		return ctx.getDslContext().select()
				.from( ROLL )
				.join(MATERIAL).on(ROLL.MATERIAL.eq(MATERIAL.ID));
	}
	
	public static Roll getRoll(DBContext ctx, Integer id) {
		return getSelect(ctx)
			.where(ROLL.ID.eq(id))
			.orderBy(MATERIAL.NAME,ROLL.NAME)
			.fetch()
			.stream()
			.map( new RollMapper() )
			.findFirst()
			.orElse(null);
	}
	
	public static LinkedList<Roll> getRolls(DBContext ctx, RollFilter filter){
		return getSelect(ctx)
			.where(ROLL_PROPERTIES.getConditions(filter))
			.fetch()
			.stream()
			.map( new RollMapper() )
			.collect(Collectors.toCollection(LinkedList::new));
	}

	public static LinkedList<Roll> getRolls(DBContext ctx) {
		return getSelect(ctx)
			.orderBy(MATERIAL.NAME,ROLL.NAME)
			.fetch()
			.stream()
			.map( new RollMapper() )
			.collect(Collectors.toCollection(LinkedList::new));
	}
	
	public static LinkedList<Roll> getRolls(DBContext ctx, String query) {
		query = MkpkStringUtils.prependIfMissing(query, "%");
		query = MkpkStringUtils.appendIfMissing(query, "%");
		return getSelect(ctx)
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
