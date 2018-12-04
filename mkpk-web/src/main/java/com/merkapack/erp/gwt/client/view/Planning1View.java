package com.merkapack.erp.gwt.client.view;


import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.merkapack.erp.core.model.Client;
import com.merkapack.erp.core.model.Machine;
import com.merkapack.erp.core.model.Material;
import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.core.model.Roll;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.js.JsPlanning;
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
import com.merkapack.erp.gwt.client.widget.MkpkProductBox;
import com.merkapack.erp.gwt.client.widget.MkpkRollBox;
import com.merkapack.erp.gwt.shared.PlanningRowCalculator;
import com.merkapack.erp.gwt.shared.PlanningRowCalculator.Strategy;
import com.merkapack.watson.util.MkpkMathUtils;
import com.merkapack.watson.util.MkpkNumberUtils;
import com.merkapack.watson.util.MkpkStringUtils;

public class Planning1View extends MkpkDockLayout {
	
	private class PlanningProvidesKey implements ProvidesKey<Planning> {
		@Override
		public Object getKey(Planning planning) {
			return planning == null ? null :
				planning.getId() == null?
					 planning.getOrder()		
					:planning.getId();
		}
	}

	
	private static Logger LOGGER = Logger.getLogger(Planning1View.class.getName());
	private static final String FORM_ID = "formID";
	private static final String FILEUPLOAD_ID = "fileUploadID";

	private ScrollPanel content;
	
	private MkpkMachineBox machine;
	private MkpkDateBox startDate;

	private PlanningRow planningRow = new PlanningRow();
	private MkpkDateBox date = new MkpkDateBox();
	private Label orderLabel = new Label();
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

	private PlanningViewTable table; 
	private LinkedList<Planning> list = new LinkedList<Planning>();
	private SingleSelectionModel<Planning> model;
	
	public Planning1View() {
		addNorth(getDateMachinePanel(), 35);
		addNorth(getPlanningRowPanel(), 70);
		
		content = new ScrollPanel();
		FlowPanel container = new FlowPanel();
		container.setStyleName(MKPK.CSS.mkpkPadding());
		table = new PlanningViewTable(new PlanningProvidesKey());
		table.addRangeChangeHandler( new Handler() {
			
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				table.setRowData(list);
			}
		});
		model = new SingleSelectionModel<Planning>(new PlanningProvidesKey());
		table.setSelectionModel(model);
		model.addSelectionChangeHandler( 
				new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						planningRow.setPlanning( model.getSelectedObject());
						planningRow.refresh();
					}
				});
		container.add(table);
		refreshList();
		content.setWidget(container);
		add(content);
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				machine.setFocus(true);
			}
		});
	}
	
	private Widget getDateMachinePanel() {
		final FormPanel form = new FormPanel();
		FlowPanel headerPanel = new FlowPanel();

		headerPanel.setStyleName(MKPK.CSS.mkpkBorderBottom());
		machine = new MkpkMachineBox();
		startDate = new MkpkDateBox();
		startDate.setValue(new Date(), false);

		MkpkButton checkButton = new MkpkButton();
		MkpkButton newLineButton = new MkpkButton();
		FileUpload fileUpload = new FileUpload();
		fileUpload.setEnabled(false);
		fileUpload.setStyleName(MKPK.CSS.mkpkFileUpload());
		fileUpload.setName(FILEUPLOAD_ID);

		InlineLabel startDateLabel = new InlineLabel(MKPK.MSG.startDate());
		startDateLabel.setStyleName(MKPK.CSS.mkpkMarginRight());
		startDateLabel.addStyleName(MKPK.CSS.mkpkMarginLeft());
		startDateLabel.addStyleName(MKPK.CSS.mkpkBold());

		startDate.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				if (machine.getSelected() != null && startDate.getValue() != null) {
					planningRow.setEnabled(true);
					planningRow.getPlanning().setDate(startDate.getValue());
					planningRow.getPlanning().setBlowsMinute(machine.getSelected().getBlows());
					planningRow.refresh();
					checkButton.setEnabled(true);
					newLineButton.setEnabled(true);
					fileUpload.setEnabled(true);
					refreshList();
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							product.setFocus(true);
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
					planningRow.setEnabled(true);
					planningRow.getPlanning().setDate(startDate.getValue());
					planningRow.getPlanning().setBlowsMinute(machine.getSelected().getBlows());
					planningRow.refresh();
					checkButton.setEnabled(true);
					newLineButton.setEnabled(true);
					fileUpload.setEnabled(true);
					refreshList();
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							product.setFocus(true);
						}
					});
				}
			}
		});

		checkButton.setEnabled(false);
		checkButton.addStyleName(MKPK.CSS.mkpkButtonCheckList());
		checkButton.addStyleName(MKPK.CSS.mkpkMarginLeft());
		checkButton.setTitle(MKPK.MSG.checkPlanning());
		checkButton.setText(MKPK.MSG.checkPlanning());
		checkButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				checkList();
			}
		});

		newLineButton.setEnabled(false);
		newLineButton.setAccessKey('N');
		newLineButton.addStyleName(MKPK.CSS.mkpkButtonAddList());
		newLineButton.addStyleName(MKPK.CSS.mkpkMarginLeft());
		newLineButton.setTitle(MKPK.MSG.newLine());
		newLineButton.setText(MKPK.MSG.newLine());
		newLineButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addNewPlanningToList();
			}
		});

		fileUpload.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				form.submit();
			}
		});

		headerPanel.add(startDateLabel);
		headerPanel.add(startDate);
		headerPanel.add(machineLabel);
		headerPanel.add(machine);
		headerPanel.add(checkButton);
		headerPanel.add(newLineButton);

		form.setStyleName(MKPK.CSS.mkpkDisplayInline());
		form.addStyleName(MKPK.CSS.mkpkMarginLeft());
		form.setMethod(FormPanel.METHOD_POST);
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setAction(URL.encode(GWT.getModuleBaseURL() + "/MkpkPlanningUpload"));
		form.ensureDebugId(FORM_ID);
		form.add(fileUpload);
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String result = event.getResults();
				parseResults(result);
			}
		});

		headerPanel.add(form);
		return headerPanel;
	}

	private Widget getPlanningRowPanel() {
		FlowPanel planningPanel = new FlowPanel();
		planningPanel.setStyleName(MKPK.CSS.mkpkBorderBottom());
		planningPanel.addStyleName(MKPK.CSS.mkpkPaddingLeft());
		planningPanel.addStyleName(MKPK.CSS.mkpkPaddingRight());
		FlexTable tab = getTable();
		planningRow.setEnabled(false);
		planningRow.setPlanning( new Planning() );
		planningRow.paint(tab);
		planningPanel.add(tab);
		return planningPanel;
	}
	
	private Planning addNewPlanningToList() {
		Planning planning = new Planning();
		planning.setBlowsMinute(machine.getSelected().getBlows());
		planning.setDate(startDate.getValue());
		planningRow.setPlanning( new Planning() );
		planningRow.refresh();
		return planning;
	}
	
	private void refreshList() {
		table.setVisibleRangeAndClearData(table.getVisibleRange(), true);
	}
	
	protected void parseResults(String result) {
		result = MkpkStringUtils.remove(result, "<pre>");
		result = MkpkStringUtils.remove(result, "</pre>");
		JsArray<JsPlanning> array = JsonUtils.safeEval(result);
		list.clear();
		for (int i = 0 ; i < array.length(); i++) {
			JsPlanning jsPlanning = array.get(i);
			list.add(new Planning()
				.setId(jsPlanning.getId())
				.setDomain(jsPlanning.getDomain())
				.setDate( startDate.getValue() )
				.setOrder(jsPlanning.getOrder())
				.setMachine( machine.getSelected() )
				.setProduct( new Product()
					.setId(jsPlanning.getProduct().getId())
					.setDomain(jsPlanning.getProduct().getDomain())
					.setName(jsPlanning.getProduct().getName())
					.setMaterial( new Material()
						.setId(jsPlanning.getProduct().getMaterial().getId())
						.setDomain(jsPlanning.getProduct().getMaterial().getDomain())
						.setName(jsPlanning.getProduct().getMaterial().getName())
						.setThickness(jsPlanning.getProduct().getMaterial().getThickness())
					)
					.setWidth(jsPlanning.getProduct().getWidth())
					.setLength(jsPlanning.getProduct().getLength())
				)
				.setWidth(jsPlanning.getWidth())
				.setLength(jsPlanning.getLength())
				.setMaterial(new Material()
						.setId(jsPlanning.getMaterial().getId())
						.setDomain(jsPlanning.getMaterial().getDomain())
						.setName(jsPlanning.getMaterial().getName())
						.setThickness(jsPlanning.getMaterial().getThickness())
					)
				.setRoll(jsPlanning.getRoll()==null?null
					:new Roll()
					.setId(jsPlanning.getRoll().getId())
					.setDomain(jsPlanning.getRoll().getDomain())
					.setName(jsPlanning.getRoll().getName())
					.setWidth(jsPlanning.getRoll().getWidth())
					.setLength(jsPlanning.getRoll().getLength())
				)
				.setRollWidth(jsPlanning.getRollWidth())
				.setRollLength(jsPlanning.getRollLength())
				.setAmount(jsPlanning.getAmount())
				.setBlowUnits(jsPlanning.getBlowUnits())
				.setMeters(jsPlanning.getMeters())
				.setBlows(jsPlanning.getBlows())
				.setBlowsMinute(machine.getSelected().getBlows())
				.setMinutes(jsPlanning.getMinutes())
				.setClient(new Client()
					.setId(jsPlanning.getClient().getId())
					.setDomain(jsPlanning.getClient().getDomain())
					.setName(jsPlanning.getClient().getName()))
				.setComments(jsPlanning.getComments())
			);
		}
		table.setVisibleRangeAndClearData(table.getVisibleRange(), true);
//		PlanningRow row = paintTable();
//		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
//			public void execute() {
//				row.setFocus(true);
//			}
//		});
	}

	protected void checkList() {
		LOGGER.severe("Start checkList");
		LinkedList<Planning> ret = PlanningRowCalculator.calculate(list);
		list = ret;
//		PlanningRow row = paintTable();
//		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
//			public void execute() {
//				LOGGER.severe("End checkList");
//				row.setFocus(true);
//			}
//		});
	}

//	private PlanningRow paintTable() {
//		rows = new LinkedList<PlanningView.PlanningRow>();
//		tab.removeAllRows();
//		paintHeader();
//		for (Planning planning : list) {
//			addNewPlanningToList(planning);
//		}
//		if (list.isEmpty()) {
//			addNewPlanningToList();
//		}
//		return rows.getLast();
//	}

	private FlexTable getTable() {
		FlexTable tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkTable());
		tab.addStyleName(MKPK.CSS.mkpkWidthAll());
		tab.getColumnFormatter().setWidth(0, "103px");
		tab.getColumnFormatter().setWidth(1, "31px");
		tab.getColumnFormatter().setWidth(2, "1%");
		tab.getColumnFormatter().setWidth(3, "1%");
		tab.getColumnFormatter().setWidth(4, "1%");
		tab.getColumnFormatter().setWidth(5, "1%");
		tab.getColumnFormatter().setWidth(6, "80px");
		tab.getColumnFormatter().setWidth(7, "1%");
		tab.getColumnFormatter().setWidth(8, "1%");
		tab.getColumnFormatter().setWidth(9, "90px");
		tab.getColumnFormatter().setWidth(10, "1%");
		tab.getColumnFormatter().setWidth(11, "1%");
		tab.getColumnFormatter().setWidth(12, "auto");
		tab.getColumnFormatter().setWidth(13, "1%");
		paintHeader( tab );
		return tab;
	}

	private void paintHeader(FlexTable tab) {
		String[] labels = new String[] { MKPK.MSG.date()
				, "#", MKPK.MSG.measure(), MKPK.MSG.material(), MKPK.MSG.roll(),
				MKPK.MSG.unit(), MKPK.MSG.blowUnits(), MKPK.MSG.meters(), MKPK.MSG.blows(), MKPK.MSG.blowsMinutes(),
				MKPK.MSG.time(), MKPK.MSG.client(), MKPK.MSG.comments(), "X" };
		for (int col = 0; col < labels.length; col++) {
			Label label = new Label(labels[col]);
			tab.setWidget(0, col, label);
			tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		}
	}

	private class PlanningRow extends FlowPanel implements HasValueChangeHandlers<Planning>, Focusable {

		public PlanningRow() {
			super();
		}
		private final int row = 1; 
		private Planning planning;
		
		public Planning getPlanning() {
			return this.planning;			
		}
		public void setPlanning(Planning planning) {
			this.planning = planning;			
		}

		public void setEnabled(boolean enabled) {
			date.setEnabled(enabled);
			product.setEnabled(enabled);
			material.setEnabled(enabled);
			roll.setEnabled(enabled);
			amount.setEnabled(enabled);
			blowUnits.setEnabled(enabled);
			meters.setEnabled(enabled);
			blows.setEnabled(enabled);
			blowsMinute.setEnabled(enabled);
			minutes.setEnabled(enabled);
			client.setEnabled(enabled);
			//comment.setEnabled(enabled);
			deleteButton.setEnabled(enabled);
		}
		
		public void paint(FlexTable tab) {

			int col = 0;

			// DIA
			date.addValueChangeHandler(new ValueChangeHandler<Date>() {
				@Override
				public void onValueChange(ValueChangeEvent<Date> event) {
					getPlanning().setDate(date.getValue());
					PlanningRowCalculator.calculate(getPlanning(), Strategy.AMOUNT_CHANGED);
					fire();
				}
			});
			tab.setWidget(row, col, date);
			++col;

			// ORDER
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
					getPlanning().setProduct(p);
					getPlanning().setWidth(p.getWidth());
					getPlanning().setLength(p.getLength());
					getPlanning().setMaterial(p.getMaterial());
					material.setValue( p.getMaterial(), false);
					PlanningRowCalculator.calculate(getPlanning(), Strategy.AMOUNT_CHANGED);
					fire();
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							roll.setFocus(true);
						}
					});
				}
			});
			++col;

			// MATERIAL
			material.setVisibleLength(10);
			tab.setWidget(row, col, material);
			material.addSelectionHandler(new SelectionHandler<Material>() {
				@Override
				public void onSelection(SelectionEvent<Material> event) {
					getPlanning().setMaterial(event.getSelectedItem());
					PlanningRowCalculator.calculate(getPlanning(), Strategy.AMOUNT_CHANGED);
					fire();
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
					getPlanning().setRoll(r);
					getPlanning().setRollWidth(r.getWidth());
					getPlanning().setRollLength(r.getLength());
					PlanningRowCalculator.calculate(getPlanning(), Strategy.AMOUNT_CHANGED);
					fire();
				}
			});
			++col;

			// UNIDAD
			amount.setVisibleLength(5);
			tab.setWidget(row, col, amount);
			amount.addValueChangeHandler(new ValueChangeHandler<Double>() {

				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					LOGGER.severe("AMOUNT ...: " + amount.getValue());
					getPlanning().setAmount(amount.getValue());
					PlanningRowCalculator.calculate(getPlanning(), Strategy.AMOUNT_CHANGED);
					fire();
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							client.setFocus(true);
						}
					});
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
					getPlanning().setMeters(meters.getValue());
					PlanningRowCalculator.calculate(getPlanning(), Strategy.METERS_CHANGED);
					fire();
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							client.setFocus(true);
						}
					});
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
					getPlanning().setBlowsMinute(blowsMinute.getValue());
					PlanningRowCalculator.calculate(getPlanning(), Strategy.AMOUNT_CHANGED);
					fire();
				}
			});

			++col;

			// TIEMPO
			minutes.setVisibleLength(5);
			tab.setWidget(row, col, minutes);
			minutes.addValueChangeHandler(new ValueChangeHandler<Double>() {

				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					getPlanning().setMinutes(MkpkMathUtils.round(minutes.getValue() * 60));
					PlanningRowCalculator.calculate(getPlanning(), Strategy.TIME_CHANGED);
					fire();
				}
			});
			++col;

			// CLIENTE
			client.setVisibleLength(10);
			tab.setWidget(row, col, client);
			client.addSelectionHandler(new SelectionHandler<Client>() {
				@Override
				public void onSelection(SelectionEvent<Client> event) {
					getPlanning().setClient(event.getSelectedItem());
					fire();
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
					cd.confirm(MKPK.MSG.deleteConfirmation(), MKPK.MSG.delete(), new MkpkConfirmDialogCallback() {
						@Override
						public void onCancel() {
						}

						@Override
						public void onAccept() {
							list.remove(getPlanning());
//							rows.remove(PlanningRow.this);
							checkList();
						}
					});

				}
			});
			tab.setWidget(row, col, deleteButton);
			tab.getCellFormatter().addStyleName(row, col, MKPK.CSS.mkpkTextCenter());
		}

		public void refresh() {
			date.setValue(getPlanning().getDate(), false);
			orderLabel.setText(MkpkNumberUtils.toString(getPlanning().getOrder()));
			product.setValue(getPlanning().getProduct(), false, false);
			material.setValue(getPlanning().getMaterial(), false, false);
			roll.setValue(getPlanning().getRoll(), false, false);
			amount.setValue(getPlanning().getAmount(), false, false);
			blowUnits.setValue(getPlanning().getBlowUnits(), false, false);
			meters.setValue(getPlanning().getMeters(), false, false);
			blows.setValue(getPlanning().getBlows(), false, false);
			blowsMinute.setValue(getPlanning().getBlowsMinute(), false, false);
			minutes.setValue(getPlanning().getMinutes() / 60, false, false);
			client.setValue(getPlanning().getClient(), false, false);
			if (list != null && !list.isEmpty()) {
				Date startDate = list.get(0).getDate();
				Date currentDate = getPlanning().getDate();
				int days = GWTDateUtils.getDaysBetween(startDate, currentDate);
				if (days % 2 == 0) {
					date.getElement().getStyle().setBackgroundColor("#DDD");
				}
			}
		}

		private void fire() {
			refresh();
			refreshList();
			ValueChangeEvent.fire(PlanningRow.this, getPlanning());
		}

		@Override
		public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Planning> handler) {
			return super.addHandler(handler, ValueChangeEvent.getType());
		}

		@Override
		public int getTabIndex() {
			return product.getTabIndex();
		}

		@Override
		public void setAccessKey(char key) {
		}

		@Override
		public void setFocus(boolean focused) {
			product.setFocus(focused);
		}

		@Override
		public void setTabIndex(int index) {
			product.setTabIndex(index);
		}
	}
}
