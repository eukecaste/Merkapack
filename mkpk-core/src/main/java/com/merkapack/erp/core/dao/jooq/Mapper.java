package com.merkapack.erp.core.dao.jooq;


import static com.merkapack.erp.master.jooq.tables.Client.CLIENT;
import static com.merkapack.erp.master.jooq.tables.Machine.MACHINE;
import static com.merkapack.erp.master.jooq.tables.Material.MATERIAL;
import static com.merkapack.erp.master.jooq.tables.Planning.PLANNING;
import static com.merkapack.erp.master.jooq.tables.Product.PRODUCT;
import static com.merkapack.erp.master.jooq.tables.Roll.ROLL;

import java.util.function.Function;

import org.jooq.Record;

import com.merkapack.erp.core.model.Client;
import com.merkapack.erp.core.model.Machine;
import com.merkapack.erp.core.model.Material;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.core.model.Roll;

public class Mapper {
	private static com.merkapack.erp.master.jooq.tables.Material MATERIAL_UP = MATERIAL.as("MAT_UP");
	private static com.merkapack.erp.master.jooq.tables.Material MATERIAL_DOWN = MATERIAL.as("MAT_DOWN");
	
	protected static class ClientMapper implements Function<Record,Client> {

		@Override
		public Client apply(Record rec) {
			return new Client()
				.setId(rec.get(CLIENT.ID))
				.setDomain(rec.get(CLIENT.DOMAIN))
				.setName(rec.get(CLIENT.NAME))
				.setCreationUser(rec.getValue(CLIENT.CREATION_USER))
				.setCreationDate(rec.getValue(CLIENT.CREATION_DATE))
				.setModificationUser(rec.getValue(CLIENT.MODIFICATION_USER))
				.setModificationDate(rec.getValue(CLIENT.MODIFICATION_DATE))
				.setDirty(false);
		}
		
	}

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
		private com.merkapack.erp.master.jooq.tables.Material materialTable;
		public MaterialMapper(com.merkapack.erp.master.jooq.tables.Material materialTable) {
			this.materialTable = materialTable;
		}
		

		@Override
		public Material apply(Record rec) {
			return new Material()
				.setId(rec.get(materialTable.ID))
				.setDomain(rec.get(materialTable.DOMAIN))
				.setCode(rec.get(materialTable.CODE))
				.setName(rec.get(materialTable.NAME))
				.setRawMaterial(rec.get(materialTable.RAW_MATERIAL))
				.setRawComposition(rec.get(materialTable.RAW_COMPOSITION))
				.setThickness(rec.get(materialTable.THICKNESS))
				.setCreationUser(rec.getValue(materialTable.CREATION_USER))
				.setCreationDate(rec.getValue(materialTable.CREATION_DATE))
				.setModificationUser(rec.getValue(materialTable.MODIFICATION_USER))
				.setModificationDate(rec.getValue(materialTable.MODIFICATION_DATE))
				.setDirty(false);
		}
		
	}
	
	protected static class RollMapper implements Function<Record,Roll> {

		@Override
		public Roll apply(Record rec) {
			return new Roll()
				.setId(rec.get(ROLL.ID))
				.setDomain(rec.get(ROLL.DOMAIN))
				.setMaterial( (new MaterialMapper(MATERIAL)).apply(rec) )
				.setName(rec.get(ROLL.NAME))
				.setWidth(rec.get(ROLL.WIDTH))
				.setLength(rec.get(ROLL.LENGTH))
				.setCreationUser(rec.getValue(ROLL.CREATION_USER))
				.setCreationDate(rec.getValue(ROLL.CREATION_DATE))
				.setModificationUser(rec.getValue(ROLL.MODIFICATION_USER))
				.setModificationDate(rec.getValue(ROLL.MODIFICATION_DATE))
				.setDirty(false);
		}
		
	}

	protected static class ProductMapper implements Function<Record,Product> {

		@Override
		public Product apply(Record rec) {
			return new Product()
				.setId(rec.get(PRODUCT.ID))
				.setDomain(rec.get(PRODUCT.DOMAIN))
				.setCode(rec.get(PRODUCT.CODE))
				.setName(rec.get(PRODUCT.NAME))
				.setMaterialUp( (new MaterialMapper(MATERIAL_UP)).apply(rec) )
				.setMaterialDown( (new MaterialMapper(MATERIAL_DOWN)).apply(rec) )
				.setWidth(rec.get(PRODUCT.WIDTH))
				.setLength(rec.get(PRODUCT.LENGTH))
				.setBoxUnits(rec.get(PRODUCT.BOX_UNITS))
				.setMold(rec.get(PRODUCT.MOLD))
				.setCreationUser(rec.getValue(PRODUCT.CREATION_USER))
				.setCreationDate(rec.getValue(PRODUCT.CREATION_DATE))
				.setModificationUser(rec.getValue(PRODUCT.MODIFICATION_USER))
				.setModificationDate(rec.getValue(PRODUCT.MODIFICATION_DATE))
				.setDirty(false);
		}
		
	}
	
	protected static class PlanningMapper implements Function<Record,Planning> {

		@Override
		public Planning apply(Record rec) {
			return new Planning()
				.setId(rec.get(PLANNING.ID))
				.setDomain(rec.get(PLANNING.DOMAIN))
				.setDate(rec.get(PLANNING.DATE))
				.setOrder(rec.get(PLANNING.ORDER))
				.setProduct( (new ProductMapper()).apply(rec) )
				.setWidth(rec.get(PLANNING.WIDTH))
				.setLength(rec.get(PLANNING.LENGTH))
				.setMaterialUp( (new MaterialMapper(MATERIAL_UP)).apply(rec) )
				.setRollUp( (new RollMapper()).apply(rec) )
				.setRollUpWidth(rec.get(PLANNING.ROLL_UP_WIDTH))
				.setRollUpLength(rec.get(PLANNING.ROLL_UP_LENGTH))
				.setMaterialDown( (new MaterialMapper(MATERIAL_DOWN)).apply(rec) )
				.setRollDown( (new RollMapper()).apply(rec) )
				.setRollDownWidth(rec.get(PLANNING.ROLL_DOWN_WIDTH))
				.setRollDownLength(rec.get(PLANNING.ROLL_DOWN_LENGTH))
				.setAmount(rec.get(PLANNING.AMOUNT))
				.setBlowUnits(rec.get(PLANNING.BLOW_UNITS))
				.setMeters(rec.get(PLANNING.METERS))
				.setBlows(rec.get(PLANNING.BLOWS))
				.setBlowsMinute(rec.get(PLANNING.BLOWS_MINUTE))
				.setMinutes(rec.get(PLANNING.MINUTES))
				.setClient( (new ClientMapper()).apply(rec) )
				.setComments(rec.get(PLANNING.COMMENT))
				.setCreationUser(rec.getValue(PLANNING.CREATION_USER))
				.setCreationDate(rec.getValue(PLANNING.CREATION_DATE))
				.setModificationUser(rec.getValue(PLANNING.MODIFICATION_USER))
				.setModificationDate(rec.getValue(PLANNING.MODIFICATION_DATE))
				.setDirty(false);
		}
		
	}
}
