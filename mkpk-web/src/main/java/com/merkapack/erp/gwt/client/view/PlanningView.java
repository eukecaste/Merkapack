package com.merkapack.erp.gwt.client.view;

import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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
import com.google.gwt.user.client.ui.Focusable;
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
import com.merkapack.erp.gwt.client.widget.MkpkConfirmDialog.MkpkConfirmDialogCallback;
import com.merkapack.erp.gwt.client.widget.MkpkDateBox;
import com.merkapack.erp.gwt.client.widget.MkpkDockLayout;
import com.merkapack.erp.gwt.client.widget.MkpkDoubleBox;
import com.merkapack.erp.gwt.client.widget.MkpkIntegerBox;
import com.merkapack.erp.gwt.client.widget.MkpkMachineBox;
import com.merkapack.erp.gwt.client.widget.MkpkMaterialBox;
import com.merkapack.erp.gwt.client.widget.MkpkProductBox;
import com.merkapack.erp.gwt.client.widget.MkpkTextBox;
import com.merkapack.watson.util.MkpkMathUtils;
import com.merkapack.watson.util.MkpkNumberUtils;

public class PlanningView extends MkpkDockLayout  {
	
	private static Logger LOGGER = Logger.getLogger(PlanningView.class.getName());
	
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
		addNorth(headerPanel, 35 );
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				machine.setFocus(true);
			}
		});
		
	}

	private FlexTable getTable(Machine machine, Date date) {
		final FlexTable tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkTable());
		tab.addStyleName(MKPK.CSS.mkpkWidthAll());
		tab.getColumnFormatter().setWidth( 0, "1%");
		tab.getColumnFormatter().setWidth( 1, "30px");
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
			 MKPK.MSG.date() 
			,"#"
			,MKPK.MSG.measure()
			,MKPK.MSG.material()
			,MKPK.MSG.roll()
			,MKPK.MSG.unit() 
			,MKPK.MSG.blowUnits()
			,MKPK.MSG.meters()
			,MKPK.MSG.blows()
			,MKPK.MSG.blowsMinutes()
			,MKPK.MSG.time()
			,MKPK.MSG.client()
			,MKPK.MSG.comments()
			,"X"
			};
		for (int col = 0; col < labels.length; col++) {
			Label label = new Label( labels[col]);
			tab.setWidget(0, col, label);
			tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		}
		list = new LinkedList<Planning>();
		addNewPlanningToList( list, tab, machine, new Date() , 1);
		return tab;		
	}

	private Planning addNewPlanningToList(LinkedList<Planning> list, FlexTable tab, Machine machine, Date date, int order) {
		Planning planning = new Planning();
		planning.setBlowsMinute( machine.getBlows() );
		planning.setDate( date );
		planning.setOrder(order);
		
		list.add(planning);	
		PlanningRow	planningRow = new PlanningRow();
		planningRow.addValueChangeHandler( new ValueChangeHandler<Planning>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Planning> event) {
				Planning pl = event.getValue();
				if (pl != null && pl.getClient() != null) {
					addNewPlanningToList( list, tab, machine, new Date(), order+1 );			
				}
			}
		});
		planningRow.paint(planning, tab, tab.getRowCount());
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				planningRow.setFocus(true);
			}
		});
		return planning;
	}

	private class PlanningRow extends FlowPanel implements HasValueChangeHandlers<Planning>, Focusable {

		private MkpkDateBox date = new MkpkDateBox();
		private MkpkProductBox product = new MkpkProductBox();
		private MkpkMaterialBox material = new MkpkMaterialBox();
		private MkpkButton deleteButton = new MkpkButton();		
		private MkpkTextBox roll = new MkpkTextBox();
		private MkpkDoubleBox amount = new MkpkDoubleBox();
		private MkpkIntegerBox blowUnits = new MkpkIntegerBox();
		private MkpkDoubleBox meters = new MkpkDoubleBox();
		private MkpkDoubleBox blows = new MkpkDoubleBox();
		private MkpkDoubleBox blowsMinute = new MkpkDoubleBox();
		private MkpkDoubleBox minutes = new MkpkDoubleBox();
		private MkpkClientBox client = new MkpkClientBox();
		private Label comment = new Label();

		public PlanningRow() {
			super();
		}
		
		public void paint(Planning planning,FlexTable tab, int row ) {	
			
			int col = 0;
			
			// DIA
			date.addValueChangeHandler(new ValueChangeHandler<Date>() {
				@Override
				public void onValueChange(ValueChangeEvent<Date> event) {
					planning.setDate( date.getValue() );
					planning.setStrategy(PlanningCalculationStrategy.AMOUNT_CHANGED);		
					fire(planning);
				}
			});
			tab.setWidget(row, col, date);
			++col;

			// ORDER
			Label orderLabel = new Label( MkpkNumberUtils.toString(planning.getOrder()));
			add(orderLabel);
			tab.setWidget(row, col, this);
			tab.getCellFormatter().addStyleName(row, col, MKPK.CSS.mkpkTextCenter());
			col++;
			
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
			roll.addStyleName(MKPK.CSS.mkpkTextCenter());
			roll.setVisibleLength(10);
			roll.setEnabled(false);
			tab.setWidget(row, col, roll);
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
			blowUnits.addStyleName(MKPK.CSS.mkpkPaddingLeft2px());
			blowUnits.setEnabled(false);
			blowUnits.setVisibleLength(4);
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
			blows.addStyleName(MKPK.CSS.mkpkPaddingLeft2px());
			blows.setEnabled(false);
			blows.setVisibleLength(6);
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
			tab.getCellFormatter().addStyleName(row, col, MKPK.CSS.mkpkTextCenter());
			refresh(planning);
		}
		
		public void refresh( Planning planning) {
			product.setValue(planning.getProduct(),false);
			material.setValue(planning.getMaterial(),false);
			roll.setValue(planning.getRollLength()+" x "+planning.getRollWidth(),false);
			amount.setValue(planning.getAmount(),false);
			blowUnits.setValue(planning.getBlowUnits(),false);
			meters.setValue(planning.getMeters(),false);
			blows.setValue(planning.getBlows(),false);
			blowsMinute.setValue(planning.getBlowsMinute(),false);
			minutes.setValue(planning.getMinutes() / 60 ,false);
			date.setValue(planning.getDate(),false);
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

		@Override
		public int getTabIndex() {
			// TODO Auto-generated method stub
			return date.getTabIndex();
		}

		@Override
		public void setAccessKey(char key) {}

		@Override
		public void setFocus(boolean focused) {
			product.setFocus(focused);
		}

		@Override
		public void setTabIndex(int index) {
			date.setTabIndex(index);
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

		private static boolean basicCalculate(Planning planning) {
			LOGGER.severe("basicCalculate");
			if (planning.getProduct() == null) {
				LOGGER.severe("Product null (return false)");
				initialize(planning);
				return false;
			}
			if (planning.getMaterial() == null) {
				LOGGER.severe("Material null");
				if (planning.getProduct().getMaterial() == null) {
					LOGGER.severe("No Material in product (return false)");
					initialize(planning);
					return false;
				}
				LOGGER.severe("Material set from product");
				planning.setMaterial( planning.getProduct().getMaterial());
			}
			planning.setWidth(planning.getProduct().getWidth());
			planning.setLength(planning.getProduct().getLength());
			planning.setRollWidth(planning.getMaterial().getWidth());
			planning.setRollLength(planning.getMaterial().getLength());
			planning.setBlowUnits( (int) MkpkMathUtils.round( planning.getRollWidth() / planning.getWidth(), 0) );
			return true;
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

