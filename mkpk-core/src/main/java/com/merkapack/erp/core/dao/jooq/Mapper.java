package com.merkapack.erp.core.dao.jooq;


import static com.merkapack.erp.master.jooq.tables.Product.PRODUCT;
import static com.merkapack.erp.master.jooq.tables.Machine.MACHINE;
import static com.merkapack.erp.master.jooq.tables.Material.MATERIAL;

import java.util.function.Function;

import org.jooq.Record;

import com.merkapack.erp.core.model.Machine;
import com.merkapack.erp.core.model.Material;
import com.merkapack.erp.core.model.Product;

public class Mapper {
	protected static class MachineMapper implements Function<Record,Machine> {

		@Override
		public Machine apply(Record rec) {
			return new Machine()
				.setId(rec.get(MACHINE.ID))
				.setDomain(rec.get(MACHINE.DOMAIN))
				.setName(rec.get(MACHINE.NAME))
				.setBlows(rec.get(MACHINE.BLOWS))
				.setCreationUser(rec.getValue(MACHINE.CREATION_USER))
				.setCreationDate(rec.getValue(MACHINE.CREATION_DATE))
				.setModificationUser(rec.getValue(MACHINE.MODIFICATION_USER))
				.setModificationDate(rec.getValue(MACHINE.MODIFICATION_DATE))
				.setDirty(false);
		}
		
	}

	protected static class MaterialMapper implements Function<Record,Material> {

		@Override
		public Material apply(Record rec) {
			return new Material()
				.setId(rec.get(MATERIAL.ID))
				.setDomain(rec.get(MATERIAL.DOMAIN))
				.setName(rec.get(MATERIAL.NAME))
				.setThickness(rec.get(MATERIAL.THICKNESS))
				.setCreationUser(rec.getValue(MATERIAL.CREATION_USER))
				.setCreationDate(rec.getValue(MATERIAL.CREATION_DATE))
				.setModificationUser(rec.getValue(MATERIAL.MODIFICATION_USER))
				.setModificationDate(rec.getValue(MATERIAL.MODIFICATION_DATE))
				.setDirty(false);
		}
		
	}
	
	protected static class ProductMapper implements Function<Record,Product> {

		@Override
		public Product apply(Record rec) {
			return new Product()
				.setId(rec.get(PRODUCT.ID))
				.setDomain(rec.get(PRODUCT.DOMAIN))
				.setName(rec.get(PRODUCT.NAME))
				.setMaterial( (new MaterialMapper()).apply(rec) )
				.setWidth(rec.get(PRODUCT.WIDTH))
				.setHeight(rec.get(PRODUCT.HEIGHT))
				.setCreationUser(rec.getValue(PRODUCT.CREATION_USER))
				.setCreationDate(rec.getValue(PRODUCT.CREATION_DATE))
				.setModificationUser(rec.getValue(PRODUCT.MODIFICATION_USER))
				.setModificationDate(rec.getValue(PRODUCT.MODIFICATION_DATE))
				.setDirty(false);
		}
		
	}
	
}
