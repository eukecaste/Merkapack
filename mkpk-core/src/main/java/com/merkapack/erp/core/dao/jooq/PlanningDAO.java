package com.merkapack.erp.core.dao.jooq;

import static com.merkapack.erp.master.jooq.tables.Machine.MACHINE;
import static com.merkapack.erp.master.jooq.tables.Material.MATERIAL;
import static com.merkapack.erp.master.jooq.tables.Planning.PLANNING;
import static com.merkapack.erp.master.jooq.tables.Product.PRODUCT;
import static com.merkapack.erp.master.jooq.tables.Roll.ROLL;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectOnConditionStep;
import org.jooq.impl.DSL;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.dao.jooq.Mapper.PlanningMapper;
import com.merkapack.erp.core.model.Filter.PlanningFilter;
import com.merkapack.erp.core.model.Filter.Property;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.core.model.Properties.PlanningProperties;
import com.merkapack.erp.master.jooq.tables.Material;
import com.merkapack.erp.master.jooq.tables.Roll;

public class PlanningDAO {
	
	private static Material MATERIAL_UP = MATERIAL.as("MAT_UP");
	private static Material MATERIAL_DOWN = MATERIAL.as("MAT_DOWN");
	private static Roll ROLL_UP = ROLL.as("ROLL_UP");
	private static Roll ROLL_DOWN = ROLL.as("ROLL_DOWN");
	
	private static final PlanningPropertiesDAO PLANNING_PROPERTIES = new PlanningPropertiesDAO();
	
	protected static class PlanningPropertiesDAO implements PlanningProperties {
		protected Condition[] getConditions(PlanningFilter filter) {
			if (filter == null) return new Condition[] { DSL.trueCondition() }; 
			FilterDAO filterDAO = (FilterDAO) filter.filter(this);
			if (filterDAO == null) return new Condition[0];
			return new Condition[] { filterDAO.getCondition() };
		}
		@Override public Property<Integer> getIdProperty() {return new FilterDAO.PropertyDAO<Integer>(PLANNING.ID);} 
		@Override public Property<Integer> getDomainProperty() {return new FilterDAO.PropertyDAO<Integer>(PLANNING.DOMAIN);}
		@Override public Property<Date> getDateProperty() {return new FilterDAO.PropertyDAO<Date>(PLANNING.DATE);} 
		@Override public Property<Integer> getOrderProperty() {return new FilterDAO.PropertyDAO<Integer>(PLANNING.ORDER);} 
		@Override public Property<Integer> getMachineIdProperty() {return new FilterDAO.PropertyDAO<Integer>(MACHINE.ID);} 
		@Override public Property<String> getMachineNameProperty() {return new FilterDAO.PropertyDAO<String>(MACHINE.NAME);}
		@Override public Property<Integer> getProductIdProperty() {return new FilterDAO.PropertyDAO<Integer>(PRODUCT.ID);} 
		@Override public Property<String> getProductNameProperty() {return new FilterDAO.PropertyDAO<String>(PRODUCT.NAME);}
		@Override public Property<Double> getWidthProperty() {return new FilterDAO.PropertyDAO<Double>(PLANNING.WIDTH);}
		@Override public Property<Double> getLengthProperty() {return new FilterDAO.PropertyDAO<Double>(PLANNING.LENGTH);}
		@Override public Property<Integer> getMaterialUpIdProperty() {return new FilterDAO.PropertyDAO<Integer>(MATERIAL_UP.ID);} 
		@Override public Property<String> getMaterialUpNameProperty() {return new FilterDAO.PropertyDAO<String>(MATERIAL_UP.NAME);}
		@Override public Property<Integer> getRollUpIdProperty() {return new FilterDAO.PropertyDAO<Integer>(ROLL_UP.ID);} 
		@Override public Property<String> getRollUpNameProperty() {return new FilterDAO.PropertyDAO<String>(ROLL_UP.NAME);}
		@Override public Property<Double> getRollUpWidthProperty() {return new FilterDAO.PropertyDAO<Double>(PLANNING.ROLL_UP_WIDTH);}
		@Override public Property<Double> getRollUpLengthProperty() {return new FilterDAO.PropertyDAO<Double>(PLANNING.ROLL_UP_LENGTH);}
		@Override public Property<Integer> getMaterialDownIdProperty() {return new FilterDAO.PropertyDAO<Integer>(MATERIAL_DOWN.ID);} 
		@Override public Property<String> getMaterialDownNameProperty() {return new FilterDAO.PropertyDAO<String>(MATERIAL_DOWN.NAME);}
		@Override public Property<Integer> getRollDownIdProperty() {return new FilterDAO.PropertyDAO<Integer>(ROLL_DOWN.ID);} 
		@Override public Property<String> getRollDownNameProperty() {return new FilterDAO.PropertyDAO<String>(ROLL_DOWN.NAME);}
		@Override public Property<Double> getRollDownWidthProperty() {return new FilterDAO.PropertyDAO<Double>(PLANNING.ROLL_DOWN_WIDTH);}
		@Override public Property<Double> getRollDownLengthProperty() {return new FilterDAO.PropertyDAO<Double>(PLANNING.ROLL_DOWN_LENGTH);}
		@Override public Property<Double> getAmountProperty() {return new FilterDAO.PropertyDAO<Double>(PLANNING.AMOUNT);}
		@Override public Property<Integer> getBlowUnitsProperty() {return new FilterDAO.PropertyDAO<Integer>(PLANNING.BLOW_UNITS);}
		@Override public Property<Double> getMetersProperty() {return new FilterDAO.PropertyDAO<Double>(PLANNING.METERS);}
		@Override public Property<Double> getBlowsProperty() {return new FilterDAO.PropertyDAO<Double>(PLANNING.BLOWS);}
		@Override public Property<Double> getBlowsMinuteProperty() {return new FilterDAO.PropertyDAO<Double>(PLANNING.BLOWS_MINUTE);}
		@Override public Property<Double> getMinuteProperty() {return new FilterDAO.PropertyDAO<Double>(PLANNING.MINUTES);}
		
		@Override public Property<String> getCreationUserProperty() {return new FilterDAO.PropertyDAO<String>(PLANNING.CREATION_USER);}
		@Override public Property<Timestamp> getCreationDateProperty() {return new FilterDAO.PropertyDAO<Timestamp>(PLANNING.CREATION_DATE);}
		@Override public Property<String> getModificationUserProperty() {return new FilterDAO.PropertyDAO<String>(PLANNING.MODIFICATION_USER);}
		@Override public Property<Timestamp> getModificationDateProperty() {return new FilterDAO.PropertyDAO<Timestamp>(PLANNING.MODIFICATION_DATE);}
	}
	private static SelectOnConditionStep<Record> getSelect(DBContext ctx) {
		return ctx.getDslContext().select()
				.from( PLANNING )
				.join(MACHINE).on(PLANNING.MACHINE.eq(MACHINE.ID))
				.join(PRODUCT).on(PLANNING.PRODUCT.eq(PRODUCT.ID))
				.join(MATERIAL_UP).on(PLANNING.MATERIAL_UP.eq(MATERIAL.ID))
				.join(ROLL_UP).on(PLANNING.ROLL_UP.eq(ROLL.ID))
				.join(MATERIAL_DOWN).on(PLANNING.MATERIAL_DOWN.eq(MATERIAL.ID))
				.join(ROLL_DOWN).on(PLANNING.ROLL_DOWN.eq(ROLL.ID))
				;
	}
	
	public static Planning getPlanning(DBContext ctx, Integer id) {
		return getSelect(ctx)
			.where(PLANNING.ID.eq(id))
			.fetch()
			.stream()
			.map( new PlanningMapper() )
			.findFirst()
			.orElse(null);
	}
	
	public static LinkedList<Planning> getPlannings(DBContext ctx, PlanningFilter filter){
		return getSelect(ctx)
			.where(PLANNING_PROPERTIES.getConditions(filter))
			.fetch()
			.stream()
			.map( new PlanningMapper() )
			.collect(Collectors.toCollection(LinkedList::new));
	}
	
	public static LinkedList<Planning> getPlannings(DBContext ctx) {
		return getSelect(ctx)
			.orderBy(PRODUCT.NAME)
			.fetch()
			.stream()
			.map( new PlanningMapper() )
			.collect(Collectors.toCollection(LinkedList::new));
	}
	
	public static LinkedList<Planning> save(DBContext ctx, LinkedList<Planning> list) {
		LinkedList<Planning> saved = new LinkedList<Planning>();
		for (Planning pl : list) {
			saved.add(save(ctx, pl));	
		}
		return saved;
	}
	
	public static Planning save(DBContext ctx, Planning planning) {
		if (planning.getId() == null) {
			return insert(ctx, planning);
		} 
		return update(ctx, planning);
	}
	
	public static Planning insert(DBContext ctx,Planning planning) {
		Integer id = ctx.getDslContext()
			.insertInto(PLANNING)
			.set(PLANNING.DOMAIN,planning.getDomain())
			.set(PLANNING.DATE,new Date( planning.getDate().getTime()) )
			.set(PLANNING.ORDER,planning.getOrder())
			.set(PLANNING.PRODUCT,planning.getProduct().getId())
			.set(PLANNING.WIDTH,planning.getWidth())
			.set(PLANNING.LENGTH,planning.getLength())
			.set(PLANNING.MATERIAL_UP,planning.getMaterialUp().getId())
			.set(PLANNING.ROLL_UP,planning.getRollUp().getId())
			.set(PLANNING.ROLL_UP_WIDTH,planning.getRollUpWidth())
			.set(PLANNING.ROLL_UP_LENGTH,planning.getRollUpLength())
			.set(PLANNING.MATERIAL_DOWN,planning.getMaterialDown().getId())
			.set(PLANNING.ROLL_DOWN,planning.getRollDown().getId())
			.set(PLANNING.ROLL_DOWN_WIDTH,planning.getRollDownWidth())
			.set(PLANNING.ROLL_DOWN_LENGTH,planning.getRollDownLength())
			.set(PLANNING.AMOUNT,planning.getAmount())
			.set(PLANNING.BLOW_UNITS,planning.getBlowUnits())
			.set(PLANNING.METERS,planning.getMeters())
			.set(PLANNING.BLOWS,planning.getBlows())
			.set(PLANNING.BLOWS_MINUTE,planning.getBlowsMinute())
			.set(PLANNING.MINUTES,planning.getMinutes())
			.set(PLANNING.CLIENT,planning.getClient().getId())
			.set(PLANNING.COMMENT,planning.getComments())
			.set(PLANNING.CREATION_USER,ctx.getUser())
			.set(PLANNING.CREATION_DATE, new Timestamp( System.currentTimeMillis()) )
			.returning(PLANNING.ID)
			.fetchOne()
			.getValue(PLANNING.ID);
		ctx.log().info("INSERT PLANNING: (id) " + id);		
		return getPlanning(ctx, id);
	}
	
	public static Planning update(DBContext ctx, Planning planning) {
		int count = ctx.getDslContext()
			.update(PLANNING)
			.set(PLANNING.DATE,new Date( planning.getDate().getTime()) )
			.set(PLANNING.ORDER,planning.getOrder())
			.set(PLANNING.PRODUCT,planning.getProduct().getId())
			.set(PLANNING.WIDTH,planning.getWidth())
			.set(PLANNING.LENGTH,planning.getLength())
			.set(PLANNING.MATERIAL_UP,planning.getMaterialUp().getId())
			.set(PLANNING.ROLL_UP,planning.getRollUp().getId())
			.set(PLANNING.ROLL_UP_WIDTH,planning.getRollUpWidth())
			.set(PLANNING.ROLL_UP_LENGTH,planning.getRollUpLength())
			.set(PLANNING.MATERIAL_DOWN,planning.getMaterialDown().getId())
			.set(PLANNING.ROLL_DOWN,planning.getRollDown().getId())
			.set(PLANNING.ROLL_DOWN_WIDTH,planning.getRollDownWidth())
			.set(PLANNING.ROLL_DOWN_LENGTH,planning.getRollDownLength())
			.set(PLANNING.AMOUNT,planning.getAmount())
			.set(PLANNING.BLOW_UNITS,planning.getBlowUnits())
			.set(PLANNING.METERS,planning.getMeters())
			.set(PLANNING.BLOWS,planning.getBlows())
			.set(PLANNING.BLOWS_MINUTE,planning.getBlowsMinute())
			.set(PLANNING.MINUTES,planning.getMinutes())
			.set(PLANNING.CLIENT,planning.getClient().getId())
			.set(PLANNING.COMMENT,planning.getComments())
			.set(PLANNING.MODIFICATION_USER,ctx.getUser())
			.set(PLANNING.MODIFICATION_DATE, new Timestamp( System.currentTimeMillis()) )
			.where(PLANNING.ID.equal( planning.getId()))
			.execute();
		ctx.log().info("UPDATE PLANNING ("+count+" filas) : (id) " + planning.getId());		
		return getPlanning(ctx, planning.getId());
	}
	public static void delete(DBContext ctx, Planning planning) {
		int count = ctx.getDslContext()
				.delete(PLANNING)
				.where(PLANNING.ID.equal( planning.getId()))
				.execute();
			ctx.log().info("DELETE PLANNING ("+count+" filas) : (id) " + planning.getId());		
	}

}
