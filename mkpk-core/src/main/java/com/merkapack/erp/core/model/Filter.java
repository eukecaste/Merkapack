package com.merkapack.erp.core.model;

import com.merkapack.erp.core.model.Properties.ClientProperties;
import com.merkapack.erp.core.model.Properties.ProductProperties;
import com.merkapack.erp.core.model.Properties.RollProperties;

public interface Filter {
	
	public interface Property<T> {
		Filter eq(T t);
		Filter ne(T t);
		Filter le(T t);
		Filter lt(T t);
		Filter gt(T t);
		Filter ge(T t);
		Filter in(T[] t);
		Filter notIn(T[] t);
		Filter isNull();
		Filter isNotNull();
		Filter like(T t);
		Filter between(T min, T max);
	}
	
	
	public Filter or(Filter filter);
	public Filter and(Filter filter);
	public Filter not(Filter filter);
	
	public Filter page(Integer page);
	public Filter perPage(Integer perPage);

	@FunctionalInterface
	public interface ProductFilter{
		Filter filter(ProductProperties properties);
	}
	
	@FunctionalInterface
	public interface RollFilter{
		Filter filter(RollProperties properties);
	}

	@FunctionalInterface
	public interface ClientFilter{
		Filter filter(ClientProperties properties);
	}
}
