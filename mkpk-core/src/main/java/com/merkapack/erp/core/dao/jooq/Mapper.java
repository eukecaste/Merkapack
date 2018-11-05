package com.merkapack.erp.core.dao.jooq;

import static com.merkapack.erp.master.jooq.tables.Machine.MACHINE;
import static com.merkapack.erp.master.jooq.tables.Material.MATERIAL;

import java.util.function.Function;

import org.jooq.Record;

import com.merkapack.erp.core.model.Machine;
import com.merkapack.erp.core.model.Material;

public class Mapper {
	protected static class MachineMapper implements Function<Record,Machine> {

		@Override
		public Machine apply(Record rec) {
			return new Machine()
				.setId(rec.get(MACHINE.ID))
				.setDomain(rec.get(MACHINE.DOMAIN))
				.setName(rec.get(MACHINE.NAME))
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
				.setCreationUser(rec.getValue(MACHINE.CREATION_USER))
				.setCreationDate(rec.getValue(MACHINE.CREATION_DATE))
				.setModificationUser(rec.getValue(MACHINE.MODIFICATION_USER))
				.setModificationDate(rec.getValue(MACHINE.MODIFICATION_DATE))
				.setDirty(false);
		}
		
	}
}
