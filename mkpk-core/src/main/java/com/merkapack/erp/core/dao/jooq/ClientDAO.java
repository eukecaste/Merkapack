package com.merkapack.erp.core.dao.jooq;

import static com.merkapack.erp.master.jooq.tables.Client.CLIENT;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectJoinStep;

import com.merkapack.erp.core.basic.DBContext;
import com.merkapack.erp.core.dao.jooq.Mapper.ClientMapper;
import com.merkapack.erp.core.model.Client;
import com.merkapack.erp.core.model.Filter.ClientFilter;
import com.merkapack.erp.core.model.Filter.Property;
import com.merkapack.erp.core.model.Properties.ClientProperties;
import com.merkapack.watson.util.MkpkStringUtils;

public class ClientDAO {
	private static final ClientPropertiesDAO CLIENT_PROPERTIES = new ClientPropertiesDAO();

	protected static class ClientPropertiesDAO implements ClientProperties {
		protected Condition[] getConditions(ClientFilter filter) {
			FilterDAO filterDAO = (FilterDAO) filter.filter(this);
			if (filterDAO == null) return new Condition[0];
			return new Condition[] { filterDAO.getCondition() };
		}
		@Override public Property<Integer> getIdProperty() {return new FilterDAO.PropertyDAO<Integer>(CLIENT.ID);} 
		@Override public Property<Integer> getDomainProperty() {return new FilterDAO.PropertyDAO<Integer>(CLIENT.DOMAIN);}
		@Override public Property<String> getNameProperty() {return new FilterDAO.PropertyDAO<String>(CLIENT.NAME);}
		@Override public Property<String> getCreationUserProperty() {return new FilterDAO.PropertyDAO<String>(CLIENT.CREATION_USER);}
		@Override public Property<Timestamp> getCreationDateProperty() {return new FilterDAO.PropertyDAO<Timestamp>(CLIENT.CREATION_DATE);}
		@Override public Property<String> getModificationUserProperty() {return new FilterDAO.PropertyDAO<String>(CLIENT.MODIFICATION_USER);}
		@Override public Property<Timestamp> getModificationDateProperty() {return new FilterDAO.PropertyDAO<Timestamp>(CLIENT.MODIFICATION_DATE);}
	}
	
	private static SelectJoinStep<Record> getSelect(DBContext ctx) {
		return ctx.getDslContext().select()
				.from( CLIENT );
	}

	public static Client getClient(DBContext ctx, Integer id) {
		return getSelect(ctx)
			.where(CLIENT.ID.eq(id))
			.fetch()
			.stream()
			.map( new ClientMapper() )
			.findFirst()
			.orElse(null);
	}
	public static LinkedList<Client> getClients(DBContext ctx) {
		return getSelect(ctx)
			.orderBy(CLIENT.NAME)
			.fetch()
			.stream()
			.map( new ClientMapper() )
			.collect(Collectors.toCollection(LinkedList::new));
	}
	public static LinkedList<Client> getClients(DBContext ctx, ClientFilter filter) {
		return getSelect(ctx)
			.where(CLIENT_PROPERTIES.getConditions(filter))
			.orderBy(CLIENT.NAME)
			.fetch()
			.stream()
			.map( new ClientMapper() )
			.collect(Collectors.toCollection(LinkedList::new));
	}
	public static LinkedList<Client> getClients(DBContext ctx, String query) {
		query = MkpkStringUtils.prependIfMissing(query, "%");
		query = MkpkStringUtils.appendIfMissing(query, "%");
		return getSelect(ctx)
				.where(CLIENT.NAME.like(query))
				.orderBy(CLIENT.NAME)
				.fetch()
				.stream()
				.map( new ClientMapper() )
				.collect(Collectors.toCollection(LinkedList::new));
	}
	public static Client save(DBContext ctx, Client machine) {
		if (machine.getId() == null) {
			return insert(ctx, machine);
		} 
		return update(ctx, machine);
	}
	public static Client insert(DBContext ctx,Client machine) {
		Integer id = ctx.getDslContext()
			.insertInto(CLIENT)
			.set(CLIENT.DOMAIN,machine.getDomain())
			.set(CLIENT.NAME,machine.getName())
			.set(CLIENT.CREATION_USER,ctx.getUser())
			.set(CLIENT.CREATION_DATE, new Timestamp( System.currentTimeMillis()) )
			.returning(CLIENT.ID)
			.fetchOne()
			.getValue(CLIENT.ID);
		ctx.log().info("INSERT CLIENT: (id) " + id);		
		return getClient(ctx, id);
	}
	public static Client update(DBContext ctx, Client machine) {
		int count = ctx.getDslContext()
			.update(CLIENT)
			.set(CLIENT.NAME,machine.getName())
			.set(CLIENT.MODIFICATION_USER,ctx.getUser())
			.set(CLIENT.MODIFICATION_DATE, new Timestamp( System.currentTimeMillis()) )
			.where(CLIENT.ID.equal( machine.getId()))
			.execute();
		ctx.log().info("UPDATE CLIENT ("+count+" filas) : (id) " + machine.getId());		
		return getClient(ctx, machine.getId());
	}
	public static void delete(DBContext ctx, Client machine) {
		int count = ctx.getDslContext()
				.delete(CLIENT)
				.where(CLIENT.ID.equal( machine.getId()))
				.execute();
			ctx.log().info("DELETE CLIENT ("+count+" filas) : (id) " + machine.getId());		
	}

}
