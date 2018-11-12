package com.merkapack.erp.core.dao.jooq;

import static com.merkapack.erp.master.jooq.tables.Material.MATERIAL;
import static com.merkapack.erp.master.jooq.tables.Product.PRODUCT;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.stream.Collectors;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.dao.jooq.Mapper.ProductMapper;
import com.merkapack.erp.core.model.Product;
import com.merkapack.watson.util.MkpkStringUtils;

public class ProductDAO {
	
	public static Product getProduct(DBContext ctx, Integer id) {
		return ctx.getDslContext().select()
			.from( PRODUCT )
			.join(MATERIAL).on(PRODUCT.MATERIAL.eq(MATERIAL.ID))
			.where(PRODUCT.ID.eq(id))
			.fetch()
			.stream()
			.map( new ProductMapper() )
			.findFirst()
			.orElse(null);
	}
	public static LinkedList<Product> getProducts(DBContext ctx) {
		return ctx.getDslContext().select()
			.from( PRODUCT )
			.join(MATERIAL).on(PRODUCT.MATERIAL.eq(MATERIAL.ID))
			.orderBy(PRODUCT.NAME)
			.fetch()
			.stream()
			.map( new ProductMapper() )
			.collect(Collectors.toCollection(LinkedList::new));
	}
	public static LinkedList<Product> getProducts(DBContext ctx, String query) {
		
		query = MkpkStringUtils.prependIfMissing(query, "%");
		query = MkpkStringUtils.appendIfMissing(query, "%");
		return ctx.getDslContext().select()
				.from( PRODUCT )
				.join(MATERIAL).on(PRODUCT.MATERIAL.eq(MATERIAL.ID))
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
