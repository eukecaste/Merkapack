package com.merkapack.erp.core.dao.jooq;

import static com.merkapack.erp.master.jooq.tables.Material.MATERIAL;
import static com.merkapack.erp.master.jooq.tables.Product.PRODUCT;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectOnConditionStep;
import org.jooq.impl.DSL;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.dao.jooq.Mapper.ProductMapper;
import com.merkapack.erp.core.model.Filter.ProductFilter;
import com.merkapack.erp.core.model.Filter.Property;
import com.merkapack.erp.core.model.MkpkCoreException;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.core.model.Properties.ProductProperties;
import com.merkapack.erp.master.jooq.tables.Material;

public class ProductDAO {
	
	private static final Logger LOGGER =  Logger.getLogger(ProductDAO.class.getName());
	
	private static Material MATERIAL_UP = MATERIAL.as("MAT_UP");
	private static Material MATERIAL_DOWN = MATERIAL.as("MAT_DOWN");
	
	private static final ProductPropertiesDAO PRODUCT_PROPERTIES = new ProductPropertiesDAO();
	
	protected static class ProductPropertiesDAO implements ProductProperties {
		protected Condition[] getConditions(ProductFilter filter) {
			if (filter == null) return new Condition[] { DSL.trueCondition() }; 
			FilterDAO filterDAO = (FilterDAO) filter.filter(this);
			if (filterDAO == null) return new Condition[0];
			return new Condition[] { filterDAO.getCondition() };
		}
		@Override public Property<Integer> getIdProperty() {return new FilterDAO.PropertyDAO<Integer>(PRODUCT.ID);} 
		@Override public Property<Integer> getDomainProperty() {return new FilterDAO.PropertyDAO<Integer>(PRODUCT.DOMAIN);}
		@Override public Property<String> getCodeProperty() {return new FilterDAO.PropertyDAO<String>(PRODUCT.CODE);}
		@Override public Property<String> getNameProperty() {return new FilterDAO.PropertyDAO<String>(PRODUCT.NAME);}
		@Override public Property<Integer> getMaterialUpIdProperty() {return new FilterDAO.PropertyDAO<Integer>(MATERIAL_UP.ID);} 
		@Override public Property<String> getMaterialUpNameProperty() {return new FilterDAO.PropertyDAO<String>(MATERIAL_UP.NAME);}
		@Override public Property<Integer> getMaterialDownIdProperty() {return new FilterDAO.PropertyDAO<Integer>(MATERIAL_DOWN.ID);} 
		@Override public Property<String> getMaterialDownNameProperty() {return new FilterDAO.PropertyDAO<String>(MATERIAL_DOWN.NAME);}
		@Override public Property<Double> getWidthProperty() {return new FilterDAO.PropertyDAO<Double>(PRODUCT.WIDTH);}
		@Override public Property<Double> getLengthProperty() {return new FilterDAO.PropertyDAO<Double>(PRODUCT.LENGTH);}
		@Override public Property<Double> getBoxUnitsProperty() {return new FilterDAO.PropertyDAO<Double>(PRODUCT.BOX_UNITS);}
		@Override public Property<String> getMoldProperty() {return new FilterDAO.PropertyDAO<String>(PRODUCT.MOLD);}
		@Override public Property<String> getCreationUserProperty() {return new FilterDAO.PropertyDAO<String>(PRODUCT.CREATION_USER);}
		@Override public Property<Timestamp> getCreationDateProperty() {return new FilterDAO.PropertyDAO<Timestamp>(PRODUCT.CREATION_DATE);}
		@Override public Property<String> getModificationUserProperty() {return new FilterDAO.PropertyDAO<String>(PRODUCT.MODIFICATION_USER);}
		@Override public Property<Timestamp> getModificationDateProperty() {return new FilterDAO.PropertyDAO<Timestamp>(PRODUCT.MODIFICATION_DATE);}
	}
	private static SelectOnConditionStep<Record> getSelect(DBContext ctx) {
		return ctx.getDslContext().select()
				.from( PRODUCT )
				.join(MATERIAL_UP).on(PRODUCT.MATERIAL_UP.eq(MATERIAL_UP.ID))
				.join(MATERIAL_DOWN).on(PRODUCT.MATERIAL_DOWN.eq(MATERIAL_DOWN.ID))
				;
	}
	
	public static Stream<Product> getProducts(DBContext ctx, int offset, int count, ProductFilter filter){
		return getSelect(ctx)
			.where(PRODUCT_PROPERTIES.getConditions(filter))
			.limit(offset,count)
			.fetch()
			.stream()
			.map( new ProductMapper() )
			;
	}
	
	public static LinkedList<Product> getProductList(DBContext ctx, int offset, int count, ProductFilter filter){
		System.out.println("getProductList");
		return getProducts(ctx, offset, count,filter)
				.collect(Collectors.toCollection(LinkedList::new));
	}
	
	public static Product getProduct(DBContext ctx, Integer id) {
		return getProducts(ctx, 0, 1, p -> p.getIdProperty().eq(id))
				.findFirst()
				.orElse(null);
	}
	
	public static Product save(DBContext ctx, Product product) {
		try {
			if (product.getId() == null) {
				return insert(ctx, product);
			} 
			return update(ctx, product);
		} catch (Throwable t) {
			LOGGER.severe(t.getMessage());
			t.printStackTrace();
			throw new MkpkCoreException("No se ha podido guardar el artículo.", t );
		}
	}
	public static Product insert(DBContext ctx,Product product) {
		Integer id = ctx.getDslContext()
			.insertInto(PRODUCT)
			.set(PRODUCT.DOMAIN,product.getDomain())
			.set(PRODUCT.CODE,product.getCode())
			.set(PRODUCT.NAME,product.getName())
			.set(PRODUCT.MATERIAL_UP,product.getMaterialUp().getId())
			.set(PRODUCT.MATERIAL_DOWN,product.getMaterialDown().getId())
			.set(PRODUCT.WIDTH,product.getWidth())
			.set(PRODUCT.LENGTH,product.getLength())
			.set(PRODUCT.BOX_UNITS,product.getBoxUnits())
			.set(PRODUCT.MOLD,product.getMold())
			.set(PRODUCT.CREATION_USER,ctx.getUser())
			.set(PRODUCT.CREATION_DATE, new Timestamp( System.currentTimeMillis()) )
			.returning(PRODUCT.ID)
			.fetchOne()
			.getValue(PRODUCT.ID);
		ctx.log().info("INSERT PRODUCT: (id) " + id);		
		return getProduct(ctx, id);
	}
	
	public static Product update(DBContext ctx, Product product) {
		int count = ctx.getDslContext()
			.update(PRODUCT)
			.set(PRODUCT.NAME,product.getName())
			.set(PRODUCT.MATERIAL_UP,product.getMaterialUp().getId())
			.set(PRODUCT.MATERIAL_DOWN,product.getMaterialDown().getId())
			.set(PRODUCT.WIDTH,product.getWidth())
			.set(PRODUCT.LENGTH,product.getLength())
			.set(PRODUCT.MODIFICATION_USER,ctx.getUser())
			.set(PRODUCT.MODIFICATION_DATE, new Timestamp( System.currentTimeMillis()) )
			.where(PRODUCT.ID.equal( product.getId()))
			.execute();
		ctx.log().info("UPDATE PRODUCT ("+count+" filas) : (id) " + product.getId());		
		return getProduct(ctx, product.getId());
	}
	public static void delete(DBContext ctx, Product product) {
		int count = ctx.getDslContext()
				.delete(PRODUCT)
				.where(PRODUCT.ID.equal( product.getId()))
				.execute();
			ctx.log().info("DELETE PRODUCT ("+count+" filas) : (id) " + product.getId());		
	}

}
