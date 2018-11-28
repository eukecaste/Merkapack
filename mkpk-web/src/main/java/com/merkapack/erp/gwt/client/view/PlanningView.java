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
import com.merkapack.erp.gwt.client.widget.MkpkDoubleLabel;
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

public class PlanningView extends MkpkDockLayout {

	private static Logger LOGGER = Logger.getLogger(PlanningView.class.getName());
	private static final String FORM_ID = "formID";
	private static final String FILEUPLOAD_ID = "fileUploadID";

	private ScrollPanel content;
	private FlexTable tab;
	private LinkedList<PlanningRow> rows;
	private LinkedList<Planning> list;
	private MkpkMachineBox machine;
	private MkpkDateBox startDate;

	final FormPanel form;

	public PlanningView() {
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
					content = new ScrollPanel();
					add(content);
					content.setWidget(getTable(machine.getSelected(), startDate.getValue()));
					checkButton.setEnabled(true);
					newLineButton.setEnabled(true);
					fileUpload.setEnabled(true);
					PlanningRow row = paintTable();
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
					content.setWidget(getTable(machine.getSelected(), startDate.getValue()));
					checkButton.setEnabled(true);
					newLineButton.setEnabled(true);
					fileUpload.setEnabled(true);
					PlanningRow row = paintTable();
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							row.setFocus(true);
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
				PlanningRow row = addNewPlanningToList();
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					public void execute() {
						row.setFocus(true);
					}
				});
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

		form = new FormPanel();
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
		addNorth(headerPanel, 35);

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				machine.setFocus(true);
			}
		});
	}
	
	public interface Plannings {
	    void setPlannings(LinkedList<Planning> plannings);
	    LinkedList<Planning> getPlannings();
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
		PlanningRow row = paintTable();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				row.setFocus(true);
			}
		});
	}

	protected void checkList() {
		LOGGER.severe("Start checkList");
		LinkedList<Planning> ret = PlanningRowCalculator.calculate(list);
		list = ret;
		PlanningRow row = paintTable();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				LOGGER.severe("End checkList");
				row.setFocus(true);
			}
		});
	}

	private PlanningRow paintTable() {
		rows = new LinkedList<PlanningView.PlanningRow>();
		tab.removeAllRows();
		paintHeader();
		Date date = null;
		double minutes = 0;
		for (Planning planning : list) {
			if (GWTDateUtils.compare(planning.getDate(), date) != 0) {
				if ( date != null) {
					paintTotalMinutes(minutes);
				}
				date = planning.getDate();
				minutes = 0;
			}
			minutes = minutes + planning.getMinutes();
			addNewPlanningToList(planning);
		}
		if (list.isEmpty()) {
			addNewPlanningToList();
		}
		paintTotalMinutes(minutes);
		return rows.getLast();
	}
	
	private void paintTotalMinutes(double minutes) {
		MkpkDoubleLabel dayMinutes = new MkpkDoubleLabel();
		dayMinutes.addStyleName(MKPK.CSS.mkpkNoBorder());
		dayMinutes.setValue(minutes / 60);
		int row = tab.getRowCount();
		tab.setWidget(row, 8, new Label("Total horas dia"));
		tab.getFlexCellFormatter().setColSpan(row, 8, 2);
		tab.getCellFormatter().setStyleName(row, 8, MKPK.CSS.mkpkTableHeader());
		tab.getCellFormatter().addStyleName(row, 8, MKPK.CSS.mkpkTextRight());
		tab.setWidget(row, 9, dayMinutes);
		tab.getCellFormatter().setStyleName(row, 9, MKPK.CSS.mkpkTableHeader());
	}

	private FlexTable getTable(Machine machine, Date date) {
		list = new LinkedList<Planning>();
		rows = new LinkedList<PlanningRow>();
		tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkTable());
		tab.addStyleName(MKPK.CSS.mkpkWidthAll());
		tab.getColumnFormatter().setWidth(0, "1%");
		tab.getColumnFormatter().setWidth(1, "30px");
		tab.getColumnFormatter().setWidth(2, "1%");
		tab.getColumnFormatter().setWidth(3, "1%");
		tab.getColumnFormatter().setWidth(4, "1%");
		tab.getColumnFormatter().setWidth(5, "1%");
		tab.getColumnFormatter().setWidth(6, "1%");
		tab.getColumnFormatter().setWidth(7, "1%");
		tab.getColumnFormatter().setWidth(8, "1%%");
		tab.getColumnFormatter().setWidth(9, "1%");
		tab.getColumnFormatter().setWidth(10, "1%");
		tab.getColumnFormatter().setWidth(11, "1%");
		tab.getColumnFormatter().setWidth(12, "auto");
		tab.getColumnFormatter().setWidth(13, "1%");
		paintHeader();
		return tab;
	}

	private void paintHeader() {
		String[] labels = new String[] { MKPK.MSG.date(), "#", MKPK.MSG.measure(), MKPK.MSG.material(), MKPK.MSG.roll(),
				MKPK.MSG.unit(), MKPK.MSG.blowUnits(), MKPK.MSG.meters(), MKPK.MSG.blows(), MKPK.MSG.blowsMinutes(),
				MKPK.MSG.time(), MKPK.MSG.client(), MKPK.MSG.comments(), "X" };
		for (int col = 0; col < labels.length; col++) {
			Label label = new Label(labels[col]);
			tab.setWidget(0, col, label);
			tab.getCellFormatter().setStyleName(0, col, MKPK.CSS.mkpkTableHeader());
		}
	}

	private PlanningRow addNewPlanningToList() {
		Planning planning = new Planning();
		planning.setBlowsMinute(machine.getSelected().getBlows());
		planning.setDate(startDate.getValue());
		int order = 0;
		for (Planning pl : list) {
			order = order > pl.getOrder() ? order : pl.getOrder();
		}
		order++;
		planning.setOrder(order);
		list.add(planning);
		return addNewPlanningToList(planning);
	}

	private PlanningRow addNewPlanningToList(Planning planning) {
		PlanningRow planningRow = new PlanningRow();
		rows.add(planningRow);
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

		public void paint(Planning planning, FlexTable tab, int row) {

			int col = 0;

			// DIA
			date.addValueChangeHandler(new ValueChangeHandler<Date>() {
				@Override
				public void onValueChange(ValueChangeEvent<Date> event) {
					planning.setDate(date.getValue());
					PlanningRowCalculator.calculate(planning, Strategy.AMOUNT_CHANGED);
					fire(planning);
				}
			});
			tab.setWidget(row, col, date);
			++col;

			// ORDER
			Label orderLabel = new Label(MkpkNumberUtils.toString(planning.getOrder()));
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
					planning.setWidth(p.getWidth());
					planning.setLength(p.getLength());
					planning.setMaterial(p.getMaterial());
					material.setValue( p.getMaterial(), false);
					PlanningRowCalculator.calculate(planning, Strategy.AMOUNT_CHANGED);
					fire(planning);
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
					planning.setMaterial(event.getSelectedItem());
					PlanningRowCalculator.calculate(planning, Strategy.AMOUNT_CHANGED);
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
					PlanningRowCalculator.calculate(planning, Strategy.AMOUNT_CHANGED);
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
					LOGGER.severe("AMOUNT ...: " + amount.getValue());
					planning.setAmount(amount.getValue());
					PlanningRowCalculator.calculate(planning, Strategy.AMOUNT_CHANGED);
					fire(planning);
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
					planning.setMeters(meters.getValue());
					PlanningRowCalculator.calculate(planning, Strategy.METERS_CHANGED);
					fire(planning);
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
					planning.setBlowsMinute(blowsMinute.getValue());
					PlanningRowCalculator.calculate(planning, Strategy.AMOUNT_CHANGED);
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
					PlanningRowCalculator.calculate(planning, Strategy.TIME_CHANGED);
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
					cd.confirm(MKPK.MSG.deleteConfirmation(), MKPK.MSG.delete(), new MkpkConfirmDialogCallback() {
						@Override
						public void onCancel() {
						}

						@Override
						public void onAccept() {
							list.remove(planning);
							rows.remove(PlanningRow.this);
							checkList();
						}
					});

				}
			});
			tab.setWidget(row, col, deleteButton);
			tab.getCellFormatter().addStyleName(row, col, MKPK.CSS.mkpkTextCenter());

			refresh(planning);
		}

		public void refresh(Planning planning) {
			LOGGER.severe("refresh ..: " + planning.getOrder() + " -- " + planning.getMeters());
			product.setValue(planning.getProduct(), false, false);
			material.setValue(planning.getMaterial(), false, false);
			roll.setValue(planning.getRoll(), false, false);
			amount.setValue(planning.getAmount(), false, false);
			blowUnits.setValue(planning.getBlowUnits(), false, false);
			meters.setValue(planning.getMeters(), false, false);
			blows.setValue(planning.getBlows(), false, false);
			blowsMinute.setValue(planning.getBlowsMinute(), false, false);
			minutes.setValue(planning.getMinutes() / 60, false, false);
			date.setValue(planning.getDate(), false);
			client.setValue(planning.getClient(), false, false);
			if (list != null && !list.isEmpty()) {
				Date startDate = list.get(0).getDate();
				Date currentDate = planning.getDate();
				int days = GWTDateUtils.getDaysBetween(startDate, currentDate);
				if (days % 2 == 0) {
					date.getElement().getStyle().setBackgroundColor("#DDD");
				}
			}
		}

		private void fire(Planning planning) {
			refresh(planning);
			ValueChangeEvent.fire(PlanningRow.this, planning);
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
