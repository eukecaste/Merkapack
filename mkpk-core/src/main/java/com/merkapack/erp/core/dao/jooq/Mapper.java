package com.merkapack.erp.core.dao.jooq;

import static com.merkapack.erp.master.jooq.tables.Machine.MACHINE;

import java.util.function.Function;

import org.jooq.Record;

import com.merkapack.erp.core.model.Machine;

public class Mapper {
	protected static class MachineMapper implements Function<Record,Machine> {

		@Override
		public Machine apply(Record rec) {
			return new Machine()
				.setId(rec.get(MACHINE.ID))
				.setName(rec.get(MACHINE.NAME))
				.setCreationUser(rec.getValue(MACHINE.CREATION_USER))
				.setCreationDate(rec.getValue(MACHINE.CREATION_DATE))
				.setModificationUser(rec.getValue(MACHINE.MODIFICATION_USER))
				.setModificationDate(rec.getValue(MACHINE.MODIFICATION_DATE))
				.setDirty(false);
		}
		
	}
}
