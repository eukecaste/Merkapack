package com.merkapack.erp.core.dao.jooq;

import static com.merkapack.erp.master.jooq.tables.Material.MATERIAL;
import static com.merkapack.erp.master.jooq.tables.Product.PRODUCT;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectOnConditionStep;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.dao.jooq.Mapper.ProductMapper;
import com.merkapack.erp.core.model.Filter.ProductFilter;
import com.merkapack.erp.core.model.Filter.Property;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.core.model.Properties.ProductProperties;
import com.merkapack.watson.util.MkpkStringUtils;

public class ProductDAO {
	
	private static final ProductPropertiesDAO PRODUCT_PROPERTIES = new ProductPropertiesDAO();
	
	protected static class ProductPropertiesDAO implements ProductProperties {
		protected Condition[] getConditions(ProductFilter filter) {
			FilterDAO filterDAO = (FilterDAO) filter.filter(this);
			if (filterDAO == null) return new Condition[0];
			return new Condition[] { filterDAO.getCondition() };
		}
		@Override public Property<Integer> getIdProperty() {return new FilterDAO.PropertyDAO<Integer>(PRODUCT.ID);} 
		@Override public Property<Integer> getDomainProperty() {return new FilterDAO.PropertyDAO<Integer>(PRODUCT.DOMAIN);}
		@Override public Property<String> getNameProperty() {return new FilterDAO.PropertyDAO<String>(PRODUCT.NAME);}
		@Override public Property<Integer> getMaterialIdProperty() {return new FilterDAO.PropertyDAO<Integer>(MATERIAL.ID);} 
		@Override public Property<String> getMaterialNameProperty() {return new FilterDAO.PropertyDAO<String>(MATERIAL.NAME);}
		@Override public Property<Double> getWidthProperty() {return new FilterDAO.PropertyDAO<Double>(PRODUCT.WIDTH);}
		@Override public Property<Double> getLengthProperty() {return new FilterDAO.PropertyDAO<Double>(PRODUCT.LENGTH);}
		@Override public Property<String> getCreationUserProperty() {return new FilterDAO.PropertyDAO<String>(PRODUCT.CREATION_USER);}
		@Override public Property<Timestamp> getCreationDateProperty() {return new FilterDAO.PropertyDAO<Timestamp>(PRODUCT.CREATION_DATE);}
		@Override public Property<String> getModificationUserProperty() {return new FilterDAO.PropertyDAO<String>(PRODUCT.MODIFICATION_USER);}
		@Override public Property<Timestamp> getModificationDateProperty() {return new FilterDAO.PropertyDAO<Timestamp>(PRODUCT.MODIFICATION_DATE);}
	}
	private static SelectOnConditionStep<Record> getSelect(DBContext ctx) {
		return ctx.getDslContext().select()
				.from( PRODUCT )
				.join(MATERIAL).on(PRODUCT.MATERIAL.eq(MATERIAL.ID));
	}
	
	public static Product getProduct(DBContext ctx, Integer id) {
		return getSelect(ctx)
			.where(PRODUCT.ID.eq(id))
			.fetch()
			.stream()
			.map( new ProductMapper() )
			.findFirst()
			.orElse(null);
	}
	
	public static LinkedList<Product> getProducts(DBContext ctx, ProductFilter filter){
		return getSelect(ctx)
			.where(PRODUCT_PROPERTIES.getConditions(filter))
			.fetch()
			.stream()
			.map( new ProductMapper() )
			.collect(Collectors.toCollection(LinkedList::new));
	}
	
	public static LinkedList<Product> getProducts(DBContext ctx) {
		return getSelect(ctx)
			.orderBy(PRODUCT.NAME)
			.fetch()
			.stream()
			.map( new ProductMapper() )
			.collect(Collectors.toCollection(LinkedList::new));
	}
	
	public static LinkedList<Product> getProducts(DBContext ctx, String query) {
		query = MkpkStringUtils.prependIfMissing(query, "%");
		query = MkpkStringUtils.appendIfMissing(query, "%");
		return getSelect(ctx)
			.where(PRODUCT.NAME.like(query))
			.fetch()
			.stream()
			.map( new ProductMapper() )
			.collect(Collectors.toCollection(LinkedList::new));
	}
	public static Product save(DBContext ctx, Product product) {
		if (product.getId() == null) {
			return insert(ctx, product);
		} 
		return update(ctx, product);
	}
	public static Product insert(DBContext ctx,Product product) {
		Integer id = ctx.getDslContext()
			.insertInto(PRODUCT)
			.set(PRODUCT.DOMAIN,product.getDomain())
			.set(PRODUCT.NAME,product.getName())
			.set(PRODUCT.MATERIAL,product.getMaterial().getId())
			.set(PRODUCT.WIDTH,product.getWidth())
			.set(PRODUCT.LENGTH,product.getLength())
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
			.set(PRODUCT.MATERIAL,product.getMaterial().getId())
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
