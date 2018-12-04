package com.merkapack.erp.gwt.client.view;

import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.common.css.MkpkCellTable;
import com.merkapack.watson.util.MkpkStringUtils;

public class PlanningViewTable extends CellTable<Planning> {
	private static final CellTable.Resources TABLE_STYLE = GWT.create(MkpkCellTable.class);
	

	public PlanningViewTable(ProvidesKey<Planning> providesKey) {
		super(1,TABLE_STYLE, providesKey);
		this.setKeyboardPagingPolicy(KeyboardPagingPolicy.CHANGE_PAGE);
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		
		addSelectorColumn();
		
		addDateColumn();
		addOrderColumn();
		addProductColumn();
		addMaterialColumn();
		addRollColumn();
		addAmountColumn();
		addBlowUnitsColumn();
		addMetersColumn();
		addBlowsColumn();
		addBlowsMinuteColumn();
		addMinutesColumn();
		addClientColumn();
		addCommentsColumn();
		addDeleteColumn();
		
		this.setEmptyTableWidget(new HTML(MKPK.MSG.noData()));
	}
	
	public PlanningViewTable(ProvidesKey<Planning> providesKey,SelectionChangeEvent.Handler handler) {
		this(providesKey);
	}


	private void addSelectorColumn() {
		final Column<Planning, ImageResource> selectorColumn = new Column<Planning, ImageResource>(
				new ImageResourceCell()) {
			@Override
			public ImageResource getValue(Planning planning) {
				return MKPK.RESOURCES.mkpkIconBullet();
			}
		};
		this.addColumn(selectorColumn);
		this.setColumnWidth(selectorColumn, 20, Unit.PX);
	}

	private void addDateColumn() {
		final TextColumn<Planning> dateColumn = new TextColumn<Planning>() {
			@Override
			public String getValue(Planning planning) {
				return MKPK.DATE_FORMAT.format(planning.getDate());
			}
		};
		this.addColumn(dateColumn, MKPK.MSG.date());
		dateColumn.setCellStyleNames(MKPK.CSS.mkpkTextCenter());
		this.setColumnWidth(dateColumn, 80, Unit.PX);
	}

	private void addOrderColumn() {
		final TextColumn<Planning> orderColumn = new TextColumn<Planning>() {
			@Override
			public String getValue(Planning planning) {
				return Integer.toString(planning.getOrder());
			}
		};
		this.addColumn(orderColumn, "#");
		this.setColumnWidth(orderColumn, 30, Unit.PX);
	}
	
	private void addProductColumn() {
		final TextColumn<Planning> productColumn = new TextColumn<Planning>() {
			@Override
			public String getValue(Planning planning) {
				return planning.getProduct() != null?planning.getProduct().getName():MkpkStringUtils.EMPTY;
			}
		};
		this.addColumn(productColumn, MKPK.MSG.measure());
		this.setColumnWidth(productColumn, 100, Unit.PX);
	}

	private void addMaterialColumn() {
		final TextColumn<Planning> materialColumn = new TextColumn<Planning>() {
			@Override
			public String getValue(Planning planning) {
				return planning.getMaterial() != null?planning.getMaterial().getName():MkpkStringUtils.EMPTY;
			}
		};
		this.addColumn(materialColumn, MKPK.MSG.material());
		this.setColumnWidth(materialColumn, 100, Unit.PX);
	}
	
	private void addRollColumn() {
		final TextColumn<Planning> rollColumn = new TextColumn<Planning>() {
			@Override
			public String getValue(Planning planning) {
				return planning.getRoll() != null?planning.getRoll().getName():MkpkStringUtils.EMPTY;
			}
		};
		this.addColumn(rollColumn, MKPK.MSG.roll());
		this.setColumnWidth(rollColumn, 100, Unit.PX);
	}

	private void addAmountColumn() {
		final TextColumn<Planning> amountColumn = new TextColumn<Planning>() {
			@Override
			public String getValue(Planning planning) {
				return MKPK.FMT.format(planning.getAmount()) ;
			}
		};
		this.addColumn(amountColumn, MKPK.MSG.unit());
		amountColumn.setCellStyleNames(MKPK.CSS.mkpkTextRight());
		this.setColumnWidth(amountColumn, 80, Unit.PX);
	}	

	private void addBlowUnitsColumn() {
		final TextColumn<Planning> blowUnitsColumn = new TextColumn<Planning>() {
			@Override
			public String getValue(Planning planning) {
				return MKPK.FMT.format(planning.getBlowUnits()) ;
			}
		};
		this.addColumn(blowUnitsColumn, MKPK.MSG.blowUnits());
		blowUnitsColumn.setCellStyleNames(MKPK.CSS.mkpkTextRight());
		this.setColumnWidth(blowUnitsColumn, 80, Unit.PX);
	}
	
	private void addMetersColumn() {
		final TextColumn<Planning> metersColumn = new TextColumn<Planning>() {
			@Override
			public String getValue(Planning planning) {
				return MKPK.FMT.format(planning.getMeters()) ;
			}
		};
		this.addColumn(metersColumn, MKPK.MSG.meters());
		metersColumn.setCellStyleNames(MKPK.CSS.mkpkTextRight());
		this.setColumnWidth(metersColumn, 80, Unit.PX);
	}
	
	private void addBlowsColumn() {
		final TextColumn<Planning> blowsColumn = new TextColumn<Planning>() {
			@Override
			public String getValue(Planning planning) {
				return MKPK.FMT.format(planning.getBlows()) ;
			}
		};
		this.addColumn(blowsColumn, MKPK.MSG.blows());
		blowsColumn.setCellStyleNames(MKPK.CSS.mkpkTextRight());
		this.setColumnWidth(blowsColumn, 80, Unit.PX);
	}
	
	private void addBlowsMinuteColumn() {
		final TextColumn<Planning> blowsMinuteColumn = new TextColumn<Planning>() {
			@Override
			public String getValue(Planning planning) {
				return MKPK.FMT.format(planning.getBlowsMinute()) ;
			}
		};
		this.addColumn(blowsMinuteColumn, MKPK.MSG.blowsMinutes());
		blowsMinuteColumn.setCellStyleNames(MKPK.CSS.mkpkTextRight());
		this.setColumnWidth(blowsMinuteColumn, 80, Unit.PX);
	}

	private void addMinutesColumn() {
		final TextColumn<Planning> minutesColumn = new TextColumn<Planning>() {
			@Override
			public String getValue(Planning planning) {
				return MKPK.FMT.format(planning.getMinutes()) ;
			}
		};
		this.addColumn(minutesColumn, MKPK.MSG.time());
		minutesColumn.setCellStyleNames(MKPK.CSS.mkpkTextRight());
		this.setColumnWidth(minutesColumn, 80, Unit.PX);
	}

	private void addClientColumn() {
		final TextColumn<Planning> clientColumn = new TextColumn<Planning>() {
			@Override
			public String getValue(Planning planning) {
				return planning.getClient() != null?planning.getClient().getName():MkpkStringUtils.EMPTY;
			}
		};
		this.addColumn(clientColumn, MKPK.MSG.client());
		this.setColumnWidth(clientColumn, 90, Unit.PX);
	}

	private void addCommentsColumn() {
		final TextColumn<Planning> commentsColumn = new TextColumn<Planning>() {
			@Override
			public String getValue(Planning planning) {
				return planning.getClient().getName();
			}
		};
		this.addColumn(commentsColumn, MKPK.MSG.comments());
		this.setColumnWidth(commentsColumn, "auto");
	}

	private void addDeleteColumn() {
		final Column<Planning, ImageResource> deleteColumn = new Column<Planning, ImageResource>(
				new ImageResourceCell()) {
			@Override
			public ImageResource getValue(Planning planning) {
				return MKPK.RESOURCES.mkpkIconDelete();
			}
		};
		this.addColumn(deleteColumn, "X");
		this.setColumnWidth(deleteColumn, 20, Unit.PX);
	}

}
