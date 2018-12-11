package com.merkapack.erp.core.model;

import java.sql.Date;
import java.sql.Timestamp;

import com.merkapack.erp.core.model.Filter.Property;

public interface Properties {
	
	public interface AuditProperties {
		Property<String> getCreationUserProperty();
		Property<Timestamp> getCreationDateProperty();
		Property<String> getModificationUserProperty();
		Property<Timestamp> getModificationDateProperty();
	}

	public interface ProductProperties extends AuditProperties {
		Property<Integer> getIdProperty();
		Property<Integer> getDomainProperty();
		Property<String> getNameProperty();
		Property<Integer> getMaterialIdProperty();
		Property<String> getMaterialNameProperty();
		Property<Double> getWidthProperty();
		Property<Double> getLengthProperty();
	}

	public interface RollProperties extends AuditProperties {
		Property<Integer> getIdProperty();
		Property<Integer> getDomainProperty();
		Property<String> getNameProperty();
		Property<Double> getWidthProperty();
		Property<Double> getLengthProperty();
		Property<Integer> getMaterialIdProperty();
		Property<String> getMaterialNameProperty();
	}

	public interface ClientProperties extends AuditProperties {
		Property<Integer> getIdProperty();
		Property<Integer> getDomainProperty();
		Property<String> getNameProperty();
	}
	
	public interface PlanningProperties extends AuditProperties {
		Property<Integer> getIdProperty();
		Property<Integer> getDomainProperty();
		Property<Date> getDateProperty();
		Property<Integer> getOrderProperty();
		Property<Double> getAmountProperty();
		Property<Integer> getMachineIdProperty();
		Property<String> getMachineNameProperty();
		Property<Integer> getProductIdProperty();
		Property<String> getProductNameProperty();
		Property<Double> getWidthProperty();
		Property<Double> getLengthProperty();
		Property<Integer> getMaterialIdProperty();
		Property<String> getMaterialNameProperty();
		Property<Integer> getRollIdProperty();
		Property<String> getRollNameProperty();
		Property<Double> getRollWidthProperty();
		Property<Double> getRollLengthProperty();
		Property<Integer> getBlowUnitsProperty();
		Property<Double> getMetersProperty();
		Property<Double> getBlowsProperty();
		Property<Double> getBlowsMinuteProperty();
		Property<Double> getMinuteProperty();
	}

}