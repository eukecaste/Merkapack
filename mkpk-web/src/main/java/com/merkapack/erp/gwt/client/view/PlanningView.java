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
import com.merkapack.erp.core.model.Roll;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.util.GWTDateUtils;
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
import com.merkapack.erp.gwt.client.widget.MkpkMessageDialog;
import com.merkapack.erp.gwt.client.widget.MkpkMessageDialog.MkpkMessageDialogCallback;
import com.merkapack.erp.gwt.client.widget.MkpkProductBox;
import com.merkapack.erp.gwt.client.widget.MkpkRollBox;
import com.merkapack.watson.util.MkpkMathUtils;
import com.merkapack.watson.util.MkpkNumberUtils;
import com.merkapack.watson.util.MkpkPair;

public class PlanningView extends MkpkDockLayout  {
	
	//private static Logger LOGGER = Logger.getLogger(PlanningView.class.getName());
	
	private ScrollPanel content;
	private FlexTable tab; 
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
				if (machine.getSelected() != null && startDate.getValue() != null) {
					content = new ScrollPanel();
					add(content);
					content.setWidget(getTable(machine.getSelected(), startDate.getValue()) );
					PlanningRow row = addNewPlanningToList(machine.getSelected().getBlows(), startDate.getValue());
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							row.setFocus(true);
						}
					});
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
				if (machine.getSelected() != null && startDate.getValue() != null) {
					content = new ScrollPanel();
					add(content);
					content.setWidget(getTable(machine.getSelected(), startDate.getValue()) );
					PlanningRow row = addNewPlanningToList(machine.getSelected().getBlows(), startDate.getValue());
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							row.setFocus(true);
						}
					});
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
		tab = new FlexTable();
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
		return tab;		
	}
	
	private PlanningRow addNewPlanningToList(double blowsMinute, Date date) {
		Planning planning = new Planning();
		planning.setBlowsMinute( blowsMinute );
		planning.setDate( date );
		int order = 0;
		for (Planning pl : list) {
			order = order>pl.getOrder()?order:pl.getOrder();
		}
		order++;
		planning.setOrder(order);
		return addNewPlanningToList(planning);	
	}

	private PlanningRow addNewPlanningToList(Planning planning) {
		list.add(planning);	
		PlanningRow	planningRow = new PlanningRow();
		planningRow.addValueChangeHandler( new ValueChangeHandler<Planning>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Planning> event) {
				Planning pl = event.getValue();
				if (pl != null && pl.getClient() != null && pl.getOrder() == (tab.getRowCount()-1)) {
					MkpkPair<Planning,Planning> pair = PlanningRowCalculator.spltIfNeeded( list, pl );
					if (pair.getRight() != null ) {
						planningRow.refresh(pair.getLeft());	
						addNewPlanningToList(pair.getRight());
					} 
					PlanningRow	row = addNewPlanningToList( pl.getBlowsMinute()
							, pair.getRight() != null?pair.getRight().getDate():pl.getDate());			
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							if (pair.getRight() != null ) {
								MkpkMessageDialog.show("Se ha duplicado la linea, debido a un exceso de horas."
										, new MkpkMessageDialogCallback() {
											@Override
											public void onClose() {
												row.setFocus(true);
											}
										});  
							} else {
								row.setFocus(true);
							}
						}
					});
				}
			}
		});
		planningRow.paint(planning, tab, tab.getRowCount());
		return planningRow;
	}

	private class PlanningRow extends FlowPanel implements HasValueChangeHandlers<Planning>, Focusable {

		private MkpkDateBox date = new MkpkDateBox();
		private MkpkProductBox product = new MkpkProductBox();
		private MkpkMaterialBox material = new MkpkMaterialBox();
		private MkpkButton deleteButton = new MkpkButton();		
		private MkpkRollBox roll = new MkpkRollBox();
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
					material.setValue(p.getMaterial(),false);
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
			roll.setVisibleLength(10);
			tab.setWidget(row, col, roll);
			roll.addSelectionHandler(new SelectionHandler<Roll>() {
				
				@Override
				public void onSelection(SelectionEvent<Roll> event) {
					Roll r = event.getSelectedItem();
					planning.setRoll(r);
					planning.setRollWidth(r.getWidth());
					planning.setRollLength(r.getLength());
					planning.setStrategy(PlanningCalculationStrategy.AMOUNT_CHANGED);
					fire(planning);
				}
			});
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
			product.setValue(planning.getProduct(),false,false);
			material.setValue(planning.getMaterial(),false,false);
			roll.setValue(planning.getRoll(),false,false);
			amount.setValue(planning.getAmount(),false,false);
			blowUnits.setValue(planning.getBlowUnits(),false,false);
			meters.setValue(planning.getMeters(),false,false);
			blows.setValue(planning.getBlows(),false,false);
			blowsMinute.setValue(planning.getBlowsMinute(),false,false);
			minutes.setValue(planning.getMinutes() / 60 ,false,false);
			date.setValue(planning.getDate(),false);
			client.setValue(planning.getClient(),false,false);
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
		
		private static Logger LOGGER = Logger.getLogger(PlanningRowCalculator.class.getName());
		private static final double MINUTES_BREAK = ((16*60) - (0.5*60));

		private static void initialize(Planning planning) {
			planning.setWidth(0);
			planning.setLength(0);
			planning.setRollWidth(0);
			planning.setRollLength(0);
			planning.setAmount(0);
			planning.setMeters(0);
			planning.setBlows(0);
			planning.setMinutes(0);
		}

		public static MkpkPair<Planning,Planning> spltIfNeeded(LinkedList<Planning> list, Planning left) {
			double minutes = 0;
			for (Planning pl : list) {
				if ( GWTDateUtils.compare(left.getDate(), pl.getDate()) == 0 ) {
					minutes = minutes + pl.getMinutes();
					LOGGER.severe("minutes ..: " + minutes);
					if ( minutes > MINUTES_BREAK ) {
						Planning right= left.clone();
								
						double minutes1 = minutes - MINUTES_BREAK;
						left.setMinutes(minutes1);
						left.setStrategy(PlanningCalculationStrategy.TIME_CHANGED);
						calculate(left);
						
						double minutes2 = minutes - minutes1;
						right.setOrder(right.getOrder() + 1);
						right.setMinutes(minutes2);
						right.setDate( new Date( left.getDate().getTime()) );
						right.setDate( GWTDateUtils.addDays(right.getDate(), 1) );
						right.setStrategy(PlanningCalculationStrategy.TIME_CHANGED);
						calculate(right);
						LOGGER.severe("Date change..: " + left.getDate());
						return new MkpkPair<Planning, Planning>(left, right);			
					}
				}
			}
			return new MkpkPair<Planning, Planning>(left, null);
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
			planning.setWidth(planning.getProduct().getWidth());
			planning.setLength(planning.getProduct().getLength());
			
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
			if (planning.getRoll() != null) {
				LOGGER.severe("Roll is null");
				planning.setRollWidth(planning.getRoll().getWidth());
				planning.setRollLength(planning.getRoll().getLength());
				planning.setBlowUnits( (int) MkpkMathUtils.round( planning.getRollWidth() / planning.getWidth(), 0) );
			}
			return !MkpkMathUtils.isZero(planning.getBlowUnits());
		}

		private static void calculateFromAmount(Planning planning) {
			LOGGER.info("calculateFromAmount");
			planning.setMeters( MkpkMathUtils.round((planning.getLength() * planning.getAmount()) / (1000 * planning.getBlowUnits())));
			planning.setBlows(MkpkMathUtils.round(planning.getAmount() / planning.getBlowUnits()));
			planning.setMinutes(MkpkMathUtils.round(planning.getBlows() / planning.getBlowsMinute()));
		}
		
		private static void calculateFromTime(Planning planning) {
			LOGGER.info("calculateFromTime");
			planning.setBlows(MkpkMathUtils.round(planning.getBlowsMinute() * planning.getMinutes()));
			planning.setAmount(MkpkMathUtils.round(planning.getBlows() * planning.getBlowUnits()));
			planning.setMeters( MkpkMathUtils.round((planning.getLength() * planning.getAmount()) / (1000 * planning.getBlowUnits())));
		}
		
		private static void calculateFromMeters(Planning planning) {
			LOGGER.info("calculateFromMeters");
			planning.setAmount(MkpkMathUtils.round( (planning.getMeters() * planning.getBlowUnits() * 1000) / planning.getLength()));
			planning.setBlows(MkpkMathUtils.round(planning.getAmount() / planning.getBlowUnits()));
			planning.setMinutes(MkpkMathUtils.round(planning.getBlows() / planning.getBlowsMinute()));
		}
	}
}

