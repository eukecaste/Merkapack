package com.merkapack.erp.gwt.client.view;

import java.util.Date;
import java.util.LinkedList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.merkapack.erp.core.model.Client;
import com.merkapack.erp.core.model.Machine;
import com.merkapack.erp.core.model.Material;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.core.model.Planning.PlanningCalculationStrategy;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.widget.MkpkButton;
import com.merkapack.erp.gwt.client.widget.MkpkClientBox;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog;
import com.merkapack.erp.gwt.client.widget.MkpkDateBox;
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog.MkpkConfirmDialogCallback;
import com.merkapack.watson.util.MkpkMathUtils;
import com.merkapack.erp.gwt.client.widget.MkpkDockLayout;
import com.merkapack.erp.gwt.client.widget.MkpkDoubleBox;
import com.merkapack.erp.gwt.client.widget.MkpkIntegerBox;
import com.merkapack.erp.gwt.client.widget.MkpkMachineBox;
import com.merkapack.erp.gwt.client.widget.MkpkMaterialBox;
import com.merkapack.erp.gwt.client.widget.MkpkProductBox;
import com.merkapack.erp.gwt.client.widget.MkpkTextBox;

public class PlanningView extends MkpkDockLayout  {
	
	private ScrollPanel content;
	private LinkedList<Planning> list;
	
	public PlanningView() {
		FlowPanel headerPanel = new FlowPanel();
		
		headerPanel.setStyleName(MKPK.CSS.mkpkBorderBottom());
		MkpkMachineBox machine = new MkpkMachineBox();
		MkpkDateBox startDate = new MkpkDateBox();
		startDate.setValue( new Date(), false );
		
		InlineLabel startDateLabel = new InlineLabel(MKPK.MSG.startDate());
		startDateLabel.setStyleName(MKPK.CSS.mkpkMarginRight());
		startDateLabel.addStyleName(MKPK.CSS.mkpkMarginLeft());
		startDateLabel.addStyleName(MKPK.CSS.mkpkBold());
		
		startDate.addValueChangeHandler(new ValueChangeHandler<Date>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				if (machine.getSelected() != null) {
					content = new ScrollPanel();
					content.setWidget(getTable(machine.getSelected(), startDate.getValue()) );
					add(content);
				}
			}
		});

		InlineLabel machineLabel = new InlineLabel(MKPK.MSG.machine());
		machineLabel.setStyleName(MKPK.CSS.mkpkMarginRight());
		machineLabel.addStyleName(MKPK.CSS.mkpkMarginLeft());
		machineLabel.addStyleName(MKPK.CSS.mkpkBold());

		machine.addSelectionHandler(new SelectionHandler<Machine>() {
			
			@Override
			public void onSelection(SelectionEvent<Machine> event) {
				if (startDate.getValue() != null) {
					content = new ScrollPanel();
					content.setWidget(getTable(machine.getSelected(), startDate.getValue()) );
					add(content);
				}
			}
		});
		headerPanel.add(startDateLabel);
		headerPanel.add(startDate);
		headerPanel.add(machineLabel);
		headerPanel.add(machine);
		addNorth(headerPanel, 50);
		
	}

	private FlexTable getTable(Machine machine, Date date) {
		final FlexTable tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkBlockCenter());
		tab.addStyleName(MKPK.CSS.mkpkWidthAll());
		tab.getColumnFormatter().setWidth( 0, "1%");
		tab.getColumnFormatter().setWidth( 1, "1%");
		tab.getColumnFormatter().setWidth( 2, "1%");
		tab.getColumnFormatter().setWidth( 3, "1%");
		tab.getColumnFormatter().setWidth( 4, "1%");
		tab.getColumnFormatter().setWidth( 5, "1%");
		tab.getColumnFormatter().setWidth( 6, "1%");
		tab.getColumnFormatter().setWidth( 7, "1%");
		tab.getColumnFormatter().setWidth( 8, "1%%");
		tab.getColumnFormatter().setWidth( 9, "1%");
		tab.getColumnFormatter().setWidth(10, "1%");
		tab.getColumnFormatter().setWidth(11, "1%");
		tab.getColumnFormatter().setWidth(12, "auto");
		tab.getColumnFormatter().setWidth(13, "1%");
		
		
		String[] labels = new String[] {
			 "#"
			,MKPK.MSG.measure()
			,MKPK.MSG.material()
			,"Bobina"
			,"Unidad"
			,"Un./Golpe"
			,"Metros"
			,"Golpes"
			,"Golpes/Min."
			,"Tiempo"
			,"Dia"
			,"Cliente"
			,"Comentarios"
			,"X"
			};
		for (int col = 0; col < labels.length; col++) {
			Label label = new Label( labels[col]);
			label.setStyleName(MKPK.CSS.mkpkBold());
			tab.setWidget(0, col, label);
		}
		list = new LinkedList<Planning>();
		addNewPlanningToList( list, tab, machine, new Date() );
		return tab;		
	}

	private Planning addNewPlanningToList(LinkedList<Planning> list, FlexTable tab, Machine machine, Date date) {
		Planning planning = new Planning();
		planning.setBlowsMinute( machine.getBlows() );
		
		setDate(list,planning);
		
		planning.setDate( date );
		
		list.add(planning);	
		PlanningRow	planningRow = new PlanningRow();
		planningRow.addValueChangeHandler( new ValueChangeHandler<Planning>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Planning> event) {
				Planning pl = event.getValue();
				if (pl != null && pl.getClient() != null) {
					addNewPlanningToList( list, tab, machine, new Date() );			
				}
			}
		});
		planningRow.paint(planning, tab, tab.getRowCount());
		return planning;
	}

	private void setDate(LinkedList<Planning> list, Planning planning) {
		double minutes = 0.0;
		for ( Planning pl : list) {
			minutes = MkpkMathUtils.round(minutes + pl.getMinutes());
			if (minutes > 16*60 ) {
				// FIN DEL DIA LABORAL
			}
		}
	}

	private class PlanningRow extends FlowPanel implements HasValueChangeHandlers<Planning> {

		private MkpkProductBox product = new MkpkProductBox();
		private MkpkMaterialBox material = new MkpkMaterialBox();
		private MkpkButton deleteButton = new MkpkButton();		
		private MkpkDoubleBox rollWidth = new MkpkDoubleBox();
		private MkpkDoubleBox amount = new MkpkDoubleBox();
		private MkpkIntegerBox blowUnits = new MkpkIntegerBox();
		private MkpkDoubleBox meters = new MkpkDoubleBox();
		private MkpkDoubleBox blows = new MkpkDoubleBox();
		private MkpkDoubleBox blowsMinute = new MkpkDoubleBox();
		private MkpkDoubleBox minutes = new MkpkDoubleBox();
		private MkpkTextBox startDate = new MkpkTextBox();
		private MkpkClientBox client = new MkpkClientBox();
		private Label comment = new Label();

		public PlanningRow() {
			super();
		}
		
		public void paint(Planning planning,FlexTable tab, int row ) {	
			
			int col = 0;
			
			// DRAGGER
			col++;
			tab.setWidget(row, col, this);
			
			// PRODUCT
			product.setVisibleLength(10);
			tab.setWidget(row, col, product);
			product.addSelectionHandler(new SelectionHandler<Product>() {
				
				@Override
				public void onSelection(SelectionEvent<Product> event) {
					Product p = event.getSelectedItem();
					planning.setProduct(p);
					planning.setMaterial(p.getMaterial());
					planning.setStrategy(PlanningCalculationStrategy.AMOUNT_CHANGED);
					fire(planning);
				}
			});
			++col;
			
			// MATERIAL
			material.setVisibleLength(10);
			tab.setWidget(row, col, material);
			material.addSelectionHandler(new SelectionHandler<Material>() {
				@Override
				public void onSelection(SelectionEvent<Material> event) {
					planning.setMaterial(event.getSelectedItem());
					planning.setStrategy(PlanningCalculationStrategy.AMOUNT_CHANGED);
					fire(planning);
				}
			});
			++col;
			
			// BOBINA
			rollWidth.setVisibleLength(5);
			rollWidth.setEnabled(false);
			tab.setWidget(row, col, rollWidth);
			++col;
			
			// UNIDAD
			amount.setVisibleLength(5);
			tab.setWidget(row, col, amount);
			amount.addValueChangeHandler(new ValueChangeHandler<Double>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					planning.setAmount(amount.getValue());
					planning.setStrategy(PlanningCalculationStrategy.AMOUNT_CHANGED);
					fire(planning);
				}
			});
			
			++col;
			// UNIDADES GOLPES
			blowUnits.setEnabled(false);
			blowUnits.setVisibleLength(5);
			tab.setWidget(row, col, blowUnits);
			
			++col;
			// METROS
			meters.setVisibleLength(5);
			tab.setWidget(row, col, meters);
			meters.addValueChangeHandler(new ValueChangeHandler<Double>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					planning.setMeters(meters.getValue());
					planning.setStrategy(PlanningCalculationStrategy.METERS_CHANGED);
					fire(planning);
				}
			});
			
			++col;
			// GOLPES
			blows.setEnabled(false);
			blows.setVisibleLength(5);
			tab.setWidget(row, col, blows);
			++col;
			
			// GOLPES MINUTO
			blowsMinute.setVisibleLength(5);
			tab.setWidget(row, col, blowsMinute);
			blowsMinute.addValueChangeHandler(new ValueChangeHandler<Double>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					planning.setBlowsMinute(blowsMinute.getValue());
					planning.setStrategy(PlanningCalculationStrategy.AMOUNT_CHANGED);
					fire(planning);
				}
			});
			
			++col;
			
			// TIEMPO
			minutes.setVisibleLength(5);
			tab.setWidget(row, col, minutes);
			minutes.addValueChangeHandler(new ValueChangeHandler<Double>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					planning.setMinutes(MkpkMathUtils.round(minutes.getValue() * 60));
					planning.setStrategy(PlanningCalculationStrategy.TIME_CHANGED);		
					fire(planning);
				}
			});
			++col;
			
			// DIA
			startDate.setVisibleLength(10);
			startDate.setEnabled(false);
			tab.setWidget(row, col, startDate);
			++col;
			
			// CLIENTE
			client.setVisibleLength(10);
			tab.setWidget(row, col, client);
			client.addSelectionHandler(new SelectionHandler<Client>() {
				@Override
				public void onSelection(SelectionEvent<Client> event) {
					planning.setClient(event.getSelectedItem());
					fire(planning);
				}
			});
			++col;
			
			// COMENTARIOS
			tab.setWidget(row, col, comment);
			++col;

			// DELETE BUTTON
			deleteButton.setTitle(MKPK.MSG.delete());
			deleteButton.addStyleName(MKPK.CSS.mkpkIconDelete());
			deleteButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					MkpkConfirmDialog cd = new MkpkConfirmDialog();
					cd.confirm(MKPK.MSG.deleteConfirmation(), MKPK.MSG.delete(),new MkpkConfirmDialogCallback() {
						@Override public void onCancel() {}
						@Override public void onAccept() {}
					});
					
				}
			});
			tab.setWidget(row, col, deleteButton);
			refresh(planning);
		}
		
		public void refresh( Planning planning) {
			product.setValue(planning.getProduct(),false);
			material.setValue(planning.getMaterial(),false);
			rollWidth.setValue(planning.getRollWidth(),false);
			amount.setValue(planning.getAmount(),false);
			blowUnits.setValue(planning.getBlowUnits(),false);
			meters.setValue(planning.getMeters());
			blows.setValue(planning.getBlows());
			blowsMinute.setValue(planning.getBlowsMinute());
			minutes.setValue(planning.getMinutes() / 60 );
			startDate.setText(MKPK.DAY_FORMAT.format(planning.getDate()));
		}
		private void fire( Planning planning) {
			PlanningRowCalculator.calculate(planning);
			refresh(planning);
			ValueChangeEvent.fire(PlanningRow.this, planning );
		}
		@Override
        public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Planning> handler) {
                return super.addHandler(handler, ValueChangeEvent.getType());
        }
	}
	
	private static class PlanningRowCalculator {
		
		private static void initialize(Planning planning) {
			planning.setWidth(0);
			planning.setLength(0);
			planning.setRollWidth(0);
			planning.setRollLength(0);
			planning.setAmount(0);
			planning.setMeters(0);
			planning.setBlows(0);
			planning.setBlowsMinute(0);
			planning.setMinutes(0);
		}

		private static boolean basicCalculate(Planning planning) {
			if (planning.getProduct() == null) {
				initialize(planning);
				return false;
			}
			if (planning.getMaterial() == null) {
				if (planning.getProduct().getMaterial() == null) {
					initialize(planning);
					return false;
				}
				planning.setMaterial( planning.getProduct().getMaterial());
			}
			planning.setWidth(planning.getProduct().getWidth());
			planning.setLength(planning.getProduct().getLength());
			planning.setRollWidth(planning.getMaterial().getWidth());
			planning.setRollLength(planning.getMaterial().getLength());
			planning.setBlowUnits( (int) MkpkMathUtils.round( planning.getRollWidth() / planning.getWidth(), 0) );
			return true;
		}

		private static void calculate(Planning planning) {
			if (basicCalculate(planning)) {
				if (planning.getStrategy() == PlanningCalculationStrategy.TIME_CHANGED) {
					calculateFromTime(planning);
				} else if (planning.getStrategy() == PlanningCalculationStrategy.METERS_CHANGED) {
					calculateFromMeters(planning);
				} else {  // PlanningCalculationStrategy.AMOUNT_CHANGED or DEFAULT
					calculateFromAmount(planning);
				}
			}
		}
		private static void calculateFromAmount(Planning planning) {
			planning.setMeters( MkpkMathUtils.round((planning.getLength() * planning.getAmount()) / (1000 * planning.getBlowUnits())));
			planning.setBlows(MkpkMathUtils.round(planning.getAmount() / planning.getBlowUnits()));
			planning.setMinutes(MkpkMathUtils.round(planning.getBlows() / planning.getBlowsMinute()));
		}
		
		private static void calculateFromTime(Planning planning) {
			planning.setBlows(MkpkMathUtils.round(planning.getBlowsMinute() * planning.getMinutes()));
			planning.setAmount(MkpkMathUtils.round(planning.getBlows() * planning.getBlowUnits()));
			planning.setMeters( MkpkMathUtils.round((planning.getLength() * planning.getAmount()) / (1000 * planning.getBlowUnits())));
		}
		
		private static void calculateFromMeters(Planning planning) {
			planning.setAmount(MkpkMathUtils.round( (planning.getMeters() * planning.getBlowUnits() * 1000) / planning.getLength()));
			planning.setBlows(MkpkMathUtils.round(planning.getAmount() / planning.getBlowUnits()));
			planning.setMinutes(MkpkMathUtils.round(planning.getBlows() / planning.getBlowsMinute()));
		}
	}
}

