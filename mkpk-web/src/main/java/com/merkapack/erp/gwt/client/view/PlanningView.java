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
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
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
import com.merkapack.erp.gwt.client.widget.MkpkCustomDialog;
import com.merkapack.erp.gwt.client.widget.MkpkDateBox;
import com.merkapack.erp.gwt.client.widget.MkpkDockLayout;
import com.merkapack.erp.gwt.client.widget.MkpkDoubleBox;
import com.merkapack.erp.gwt.client.widget.MkpkIntegerBox;
import com.merkapack.erp.gwt.client.widget.MkpkMachineBox;
import com.merkapack.erp.gwt.client.widget.MkpkMaterialBox;
import com.merkapack.erp.gwt.client.widget.MkpkProductBox;
import com.merkapack.erp.gwt.client.widget.MkpkRollBox;
import com.merkapack.erp.gwt.shared.PlanningCalculator;
import com.merkapack.erp.gwt.shared.PlanningCalculatorParams;
import com.merkapack.erp.gwt.shared.PlanningCalculatorStrategy;
import com.merkapack.watson.util.MkpkMathUtils;
import com.merkapack.watson.util.MkpkNumberUtils;
import com.merkapack.watson.util.MkpkStringUtils;

public class PlanningView extends MkpkDockLayout {
	
	private static Logger LOGGER = Logger.getLogger(PlanningView.class.getName());
	private static final String FORM_ID = "formID";
	private static final String FILEUPLOAD_ID = "fileUploadID";

	private SimpleLayoutPanel contentContainer;
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

	private MkpkDoubleBox workHoursInADay = new MkpkDoubleBox();
	private MkpkDoubleBox hoursMargin = new MkpkDoubleBox();
	private MkpkDoubleBox defaultBlowsMinute = new MkpkDoubleBox();

	
	private LinkedList<Planning> list = new LinkedList<Planning>();
	
	public PlanningView() {
		addNorth(getDateMachinePanel(), 35);
		addNorth(getPlanningRowPanel(), 70);
		contentContainer = new SimpleLayoutPanel();
		content = new ScrollPanel();
		contentContainer.setWidget(content);
		add(contentContainer);
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				machine.setFocus(true);
			}
		});
	}
	
	private PlanningCalculatorParams getParams() {
		return new PlanningCalculatorParams()
			.setHoursMargin(hoursMargin.getValue() )
			.setWorkHoursInADay(workHoursInADay.getValue() )
			.setBlowsMinute(defaultBlowsMinute.getValue());
	}
	
	private Widget getDateMachinePanel() {
		HorizontalPanel headerPanel = new HorizontalPanel();
		headerPanel.setStyleName(MKPK.CSS.mkpkWidthAll());
		headerPanel.addStyleName(MKPK.CSS.mkpkBorderBottom());
		machine = new MkpkMachineBox();
		startDate = new MkpkDateBox();
		startDate.setValue(new Date(), false);

		MkpkButton wizardButton = new MkpkButton();
		MkpkButton newLineButton = new MkpkButton();
		MkpkButton configButton = new MkpkButton();
		FileUpload fileUpload = new FileUpload();

		InlineLabel startDateLabel = new InlineLabel(MKPK.MSG.startDate());
		startDateLabel.setStyleName(MKPK.CSS.mkpkMarginRight());
		startDateLabel.addStyleName(MKPK.CSS.mkpkMarginLeft());
		startDateLabel.addStyleName(MKPK.CSS.mkpkBold());

		startDate.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				if (machine.getSelected() != null && startDate.getValue() != null) {
					planningRow.setEnabled(true);
					wizardButton.setEnabled(true);
					newLineButton.setEnabled(true);
					fileUpload.setEnabled(true);
					addNewPlanningToList();
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
					wizardButton.setEnabled(true);
					newLineButton.setEnabled(true);
					fileUpload.setEnabled(true);
					addNewPlanningToList();
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							product.setFocus(true);
						}
					});
				}
			}
		});

		wizardButton.setEnabled(false);
		wizardButton.addStyleName(MKPK.CSS.mkpkIconMagic());
		wizardButton.addStyleName(MKPK.CSS.mkpkMarginLeft());
		wizardButton.setTitle("Wizard");
		wizardButton.setText("Wizard");
		wizardButton.addClickHandler(new ClickHandler() {

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
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					public void execute() {
						product.setFocus(true);
					}
				});
			}
		});


		MkpkCustomDialog configDialog = new MkpkCustomDialog();
		configDialog.setCaption(MKPK.MSG.configuration());
		configDialog.setGlassEnabled(true);
		configDialog.setAnimationEnabled(true);
		
		FlexTable table = new FlexTable();
		
		InlineLabel workHoursInADayLabel = new InlineLabel( MKPK.MSG.workHoursInADay());
		workHoursInADayLabel.setStyleName(MKPK.CSS.mkpkMarginRight());
		workHoursInADayLabel.addStyleName(MKPK.CSS.mkpkMarginLeft());
		workHoursInADayLabel.addStyleName(MKPK.CSS.mkpkBold());
		workHoursInADay.setValue( 16.0 );
		table.setWidget(0, 0, workHoursInADayLabel);
		table.setWidget(0, 1, workHoursInADay);
		
		InlineLabel hoursMarginLabel = new InlineLabel(MKPK.MSG.hoursMargin());
		hoursMarginLabel.setStyleName(MKPK.CSS.mkpkMarginRight());
		hoursMarginLabel.addStyleName(MKPK.CSS.mkpkMarginLeft());
		hoursMarginLabel.addStyleName(MKPK.CSS.mkpkBold());
		hoursMargin.setValue( 0.5 );
		table.setWidget(1, 0, hoursMarginLabel);
		table.setWidget(1, 1, hoursMargin);
		
		InlineLabel defaultBlowsMinuteLabel = new InlineLabel(MKPK.MSG.defaultBlowsMiniute());
		defaultBlowsMinuteLabel.setStyleName(MKPK.CSS.mkpkMarginRight());
		defaultBlowsMinuteLabel.addStyleName(MKPK.CSS.mkpkMarginLeft());
		defaultBlowsMinuteLabel.addStyleName(MKPK.CSS.mkpkBold());
		defaultBlowsMinute.setValue( 80.0 );
		table.setWidget(2, 0, defaultBlowsMinuteLabel);
		table.setWidget(2, 1, defaultBlowsMinute);

		final FormPanel form = new FormPanel();
		form.setStyleName(MKPK.CSS.mkpkDisplayInline());
		form.addStyleName(MKPK.CSS.mkpkMarginLeft());
		form.setMethod(FormPanel.METHOD_POST);
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setAction(URL.encode(GWT.getModuleBaseURL() + "/MkpkPlanningUpload"));
		form.ensureDebugId(FORM_ID);
		fileUpload.setEnabled(false);
		fileUpload.setStyleName(MKPK.CSS.mkpkFileUpload());
		fileUpload.setName(FILEUPLOAD_ID);
		form.add(fileUpload);
		fileUpload.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				form.submit();
			}
		});
		
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String result = event.getResults();
				parseResults(result);
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					public void execute() {
						configDialog.hide();
					}
				});
			}
		});
		
		InlineLabel fileUploadLabel = new InlineLabel(MKPK.MSG.uploadFile());
		fileUploadLabel.setStyleName(MKPK.CSS.mkpkMarginRight());
		fileUploadLabel.addStyleName(MKPK.CSS.mkpkMarginLeft());
		fileUploadLabel.addStyleName(MKPK.CSS.mkpkBold());
		table.setWidget(3, 0, form);
		table.setWidget(3, 1, form);
		configDialog.setWidget(table);

		configButton.addStyleName(MKPK.CSS.mkpkButtonConfig());
		configButton.addStyleName(MKPK.CSS.mkpkMarginLeft());
		configButton.setText(MKPK.MSG.configuration());
		configButton.setTitle(MKPK.MSG.configuration());
		configButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				configDialog.center();
				configDialog.show();
			}
		});
		
		

		headerPanel.add(startDateLabel);
		headerPanel.add(startDate);
		headerPanel.add(machineLabel);
		headerPanel.add(machine);
		headerPanel.add(newLineButton);
		headerPanel.add(wizardButton);
		headerPanel.add(configButton);
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
		list.add(planning);
		planningRow.setPlanning( planning );
		planningRow.refresh();
		refreshList();
		content.scrollToBottom();
		return planning;
	}
	
	protected void parseResults(String result) {
		result = MkpkStringUtils.remove(result, "<pre style=\"word-wrap: break-word; white-space: pre-wrap;\">");
		result = MkpkStringUtils.remove(result, "<pre>");
		result = MkpkStringUtils.remove(result, "</pre>");
		JsArray<JsPlanning> array = JsonUtils.safeEval(result);
		list.clear();
		for (int i = 0 ; i < array.length(); i++) {
			JsPlanning jsPlanning = array.get(i);
			Planning pl = new Planning()
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
				.setClient(jsPlanning.getClient()==null?null
					:new Client()
					.setId(jsPlanning.getClient().getId())
					.setDomain(jsPlanning.getClient().getDomain())
					.setName(jsPlanning.getClient().getName()))
				.setComments(jsPlanning.getComments())
			;
			if (MkpkMathUtils.isZero( pl.getBlowsMinute())) {
				pl.setBlowsMinute(getParams().getBlowsMinute());
			}
			PlanningCalculator.calculate(getParams(),pl);	
			list.add(pl);
		}
		refreshList();
	}

	protected void checkList() {
		LinkedList<Planning> ret = PlanningCalculator.calculate(getParams(),list);
		list = ret;
		refreshList();
	}
	
	private FlexTable getTable() {
		FlexTable tab = new FlexTable();
		tab.setStyleName(MKPK.CSS.mkpkTable());
		tab.addStyleName(MKPK.CSS.mkpkWidthAll());
		tab.getColumnFormatter().setWidth( 0, "20px");
		tab.getColumnFormatter().setWidth( 1, "80px");
		tab.getColumnFormatter().setWidth( 2, "30px");
		tab.getColumnFormatter().setWidth( 3, "100px");
		tab.getColumnFormatter().setWidth( 4, "100px");
		tab.getColumnFormatter().setWidth( 5, "100px");
		tab.getColumnFormatter().setWidth( 6, "80px");
		tab.getColumnFormatter().setWidth( 7, "45px");
		tab.getColumnFormatter().setWidth( 8, "80px");
		tab.getColumnFormatter().setWidth( 9, "80px");
		tab.getColumnFormatter().setWidth(10, "45px");
		tab.getColumnFormatter().setWidth(11, "65px");
		tab.getColumnFormatter().setWidth(12, "100px");
		tab.getColumnFormatter().setWidth(13, "auto");
		tab.getColumnFormatter().setWidth(14, "20px");
		paintHeader( tab );
		return tab;
	}

	private void paintHeader(FlexTable tab) {
		String[] labels = new String[] { ".", MKPK.MSG.date()
				, "#", MKPK.MSG.measure(), MKPK.MSG.material(), MKPK.MSG.roll(),
				MKPK.MSG.unit(), MKPK.MSG.blowUnitsAbbr(), MKPK.MSG.meters(), MKPK.MSG.blows(), MKPK.MSG.blowsMinutesAbbrv(),
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

			// ORDER
			tab.setWidget(row, col, new Label("+"));
			tab.getCellFormatter().addStyleName(row, col, MKPK.CSS.mkpkTextCenter());
			col++;
			
			// DIA
			date.addValueChangeHandler(new ValueChangeHandler<Date>() {
				@Override
				public void onValueChange(ValueChangeEvent<Date> event) {
					getPlanning().setDate(date.getValue());
					PlanningCalculator.calculate(getParams(), PlanningCalculatorStrategy.AMOUNT_CHANGED, getPlanning());
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
					PlanningCalculator.calculate(getParams(), PlanningCalculatorStrategy.AMOUNT_CHANGED, getPlanning());
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
					PlanningCalculator.calculate(getParams(), PlanningCalculatorStrategy.AMOUNT_CHANGED, getPlanning());
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
					PlanningCalculator.calculate(getParams(), PlanningCalculatorStrategy.AMOUNT_CHANGED, getPlanning());
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
					PlanningCalculator.calculate(getParams(), PlanningCalculatorStrategy.AMOUNT_CHANGED, getPlanning());
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
			blowUnits.setVisibleLength(2);
			blowUnits.setWidth("40px");
			tab.setWidget(row, col, blowUnits);

			++col;
			// METROS
			meters.setVisibleLength(5);
			tab.setWidget(row, col, meters);
			meters.addValueChangeHandler(new ValueChangeHandler<Double>() {

				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					getPlanning().setMeters(meters.getValue());
					PlanningCalculator.calculate(getParams(), PlanningCalculatorStrategy.METERS_CHANGED, getPlanning());
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
			blowsMinute.setVisibleLength(2);
			blowUnits.setWidth("40px");
			tab.setWidget(row, col, blowsMinute);
			blowsMinute.addValueChangeHandler(new ValueChangeHandler<Double>() {

				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					getPlanning().setBlowsMinute(blowsMinute.getValue());
					PlanningCalculator.calculate(getParams(), PlanningCalculatorStrategy.AMOUNT_CHANGED, getPlanning());
					fire();
				}
			});

			++col;

			// TIEMPO
			minutes.setVisibleLength(4);
			blowUnits.setWidth("60px");
			tab.setWidget(row, col, minutes);
			minutes.addValueChangeHandler(new ValueChangeHandler<Double>() {

				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					getPlanning().setMinutes(MkpkMathUtils.round(minutes.getValue() * 60));
					PlanningCalculator.calculate(getParams(), PlanningCalculatorStrategy.TIME_CHANGED, getPlanning());
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
							refreshList();
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
			validatePlanningRow();
		}
		
		private void validatePlanningRow() {
			validateBlowUnitRoll(getPlanning(),blowUnits,roll);
			validateMeters(getPlanning(),meters);
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
	
	private void validateMeters(Planning planning, Widget meters) {
		if ( MkpkMathUtils.isNotZero(planning.getRollLength())) {
			if ( MkpkMathUtils.isNotZero(planning.getMeters())) {
				if ( planning.getMeters() > planning.getRollLength()) {
					meters.addStyleName(MKPK.CSS.mkpkColorRed());
				} else {
					meters.removeStyleName(MKPK.CSS.mkpkColorRed());
				}
			}
		}
	}
	
	private void validateBlowUnitRoll(Planning planning, Widget blowUnits, Widget roll) {
		if ( MkpkMathUtils.isNotZero(planning.getRollWidth())) {
			if ( MkpkMathUtils.isNotZero(planning.getWidth())) {
				if (MkpkMathUtils.isNotZero(MkpkMathUtils.round(planning.getRollWidth() % planning.getWidth()))) {
					blowUnits.addStyleName(MKPK.CSS.mkpkColorRed());
					roll.addStyleName(MKPK.CSS.mkpkColorRed());
				} else {
					blowUnits.removeStyleName(MKPK.CSS.mkpkColorRed());
					roll.removeStyleName(MKPK.CSS.mkpkColorRed());
				}
			}
		}
	}
	
	private FocusPanel getLineWidget(Planning pl) {
		FocusPanel focusContainer = new FocusPanel();
		focusContainer.setStyleName(MKPK.CSS.mkpkPointer());
		focusContainer.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				planningRow.setPlanning(pl);
				planningRow.refresh();
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					public void execute() {
						amount.setFocus(true);
					}
				});
			}
		});
		FlowPanel container = new FlowPanel();
		focusContainer.setWidget(container);
		container.setStyleName(MKPK.CSS.mkpkFlexPanel());
		if (pl.isSelected()) {
			container.addStyleName(MKPK.CSS.mkpkFlexPanelSelected());
		}
		
		InlineLabel selector = new InlineLabel();
		selector.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		selector.addStyleName(MKPK.CSS.mkpkFlexPanelChildSelector());
		selector.addStyleName(pl.isSelected()?MKPK.CSS.mkpkIconChecked():MKPK.CSS.mkpkIconUnchecked());
		selector.addStyleName(MKPK.CSS.mkpkPointer());
		selector.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				event.stopPropagation();
				selector.removeStyleName(pl.isSelected()?MKPK.CSS.mkpkIconChecked():MKPK.CSS.mkpkIconUnchecked());
				pl.setSelected(!pl.isSelected());
				selector.addStyleName(pl.isSelected()?MKPK.CSS.mkpkIconChecked():MKPK.CSS.mkpkIconUnchecked());
				if (pl.isSelected()) {
					container.addStyleName(MKPK.CSS.mkpkFlexPanelSelected());
				} else {
					container.removeStyleName(MKPK.CSS.mkpkFlexPanelSelected());
				}
			}
		});
		container.add(selector);
		
		InlineLabel date = new InlineLabel(MKPK.DATE_FORMAT.format(pl.getDate()));
		date.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		date.addStyleName(MKPK.CSS.mkpkFlexPanelChildDate());
		container.add(date);

		InlineLabel order = new InlineLabel(MkpkNumberUtils.toString(pl.getOrder()));
		order.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		order.addStyleName(MKPK.CSS.mkpkFlexPanelChildOrder());
		container.add(order);

		InlineLabel product = new InlineLabel(pl.getProduct() != null ? pl.getProduct().getName() : "" );
		product.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		product.addStyleName(MKPK.CSS.mkpkFlexPanelChildProduct());
		container.add(product);
		
		InlineLabel material = new InlineLabel(pl.getMaterial() != null ? pl.getMaterial().getName() : "");
		material.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		material.addStyleName(MKPK.CSS.mkpkFlexPanelChildMaterial());
		container.add(material);

		InlineLabel roll = new InlineLabel(pl.getRoll() != null ? pl.getRoll().getName() : "");
		roll.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		roll.addStyleName(MKPK.CSS.mkpkFlexPanelChildRoll());
		container.add(roll);

		InlineLabel amount = new InlineLabel(MKPK.FMT_INT.format(pl.getAmount()));
		amount.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		amount.addStyleName(MKPK.CSS.mkpkFlexPanelChildAmount());
		container.add(amount);
		
		InlineLabel blowUnits = new InlineLabel(MKPK.FMT_INT.format(pl.getBlowUnits()));
		blowUnits.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		blowUnits.addStyleName(MKPK.CSS.mkpkFlexPanelChildBlowUnits());
		container.add(blowUnits);
		
		InlineLabel meters = new InlineLabel(MKPK.FMT.format(pl.getMeters()));
		meters.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		meters.addStyleName(MKPK.CSS.mkpkFlexPanelChildMeters());
		container.add(meters);

		InlineLabel blows = new InlineLabel(MKPK.FMT.format(pl.getBlows()));
		blows.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		blows.addStyleName(MKPK.CSS.mkpkFlexPanelChildBlows());
		container.add(blows);

		InlineLabel blowsMinute = new InlineLabel(MKPK.FMT_INT.format(pl.getBlowsMinute()));
		blowsMinute.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		blowsMinute.addStyleName(MKPK.CSS.mkpkFlexPanelChildBlowsMinute());
		container.add(blowsMinute);
		
		InlineLabel hours = new InlineLabel(MKPK.FMT.format(pl.getHours()));
		hours.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		hours.addStyleName(MKPK.CSS.mkpkFlexPanelChildHours());
		container.add(hours);
		
		InlineLabel client = new InlineLabel(pl.getClient() != null ? pl.getClient().getName() : "");
		client.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		client.addStyleName(MKPK.CSS.mkpkFlexPanelChildClient());
		container.add(client);

		InlineLabel comments = new InlineLabel(pl.getComments() != null ? pl.getComments() : "");
		comments.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		comments.addStyleName(MKPK.CSS.mkpkFlexPanelChildComments());
		container.add(comments);

		InlineLabel delete = new InlineLabel();
		delete.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		delete.addStyleName(MKPK.CSS.mkpkFlexPanelChildDelete());
		delete.addStyleName(MKPK.CSS.mkpkIconDelete());
		delete.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
		delete.addStyleName(MKPK.CSS.mkpkPointer());
		if (pl.isDirty()) {
			delete.setText("*");
			delete.addStyleName(MKPK.CSS.mkpkColorRed());
			delete.addStyleName(MKPK.CSS.mkpkBold());
		}
		delete.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				MkpkConfirmDialog cd = new MkpkConfirmDialog();
				cd.confirm(MKPK.MSG.deleteConfirmation(), MKPK.MSG.delete(), new MkpkConfirmDialogCallback() {
					@Override
					public void onCancel() {
					}

					@Override
					public void onAccept() {
						event.preventDefault();
						event.stopPropagation();
						list.remove(pl);
						refreshList();
					}
				});
			}
		});
		container.add(delete);
		return focusContainer;
	}

	private Widget getTotalLine(double minutes) {
		FlowPanel container = new FlowPanel();
		container.setStyleName(MKPK.CSS.mkpkFlexPanel());

		InlineLabel fill1 = new InlineLabel();
		fill1.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		fill1.addStyleName(MKPK.CSS.mkpkFlexPanelChildFill1());
		container.add(fill1);
		
		InlineLabel hours = new InlineLabel(MKPK.FMT.format(minutes / 60));
		hours.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		hours.addStyleName(MKPK.CSS.mkpkFlexPanelChildHours());
		hours.addStyleName(MKPK.CSS.mkpkBold());
		container.add(hours);
		
		InlineLabel fill2 = new InlineLabel();
		fill2.setStyleName(MKPK.CSS.mkpkFlexPanelChild());
		fill2.addStyleName(MKPK.CSS.mkpkFlexPanelChildFill2());
		container.add(fill2);
		
		return container;
	}

	private void refreshList() {
		content.clear();
		
		FlowPanel mainContainer = new FlowPanel();
		mainContainer.setStyleName(MKPK.CSS.mkpkPaddingTop());
		mainContainer.addStyleName(MKPK.CSS.mkpkPaddingLeft());
		mainContainer.addStyleName(MKPK.CSS.mkpkPaddingRight());
		if (list != null && list.size() > 0) {
			Date date = null;
			double minutes = 0;
			for (Planning pl : list) {
				if (GWTDateUtils.compare(pl.getDate(), date) != 0) {
					if ( date != null) {
						mainContainer.add(getTotalLine( minutes ));	
					}
					date = pl.getDate();
					minutes = 0;
				}
				minutes = minutes + pl.getMinutes();
				FocusPanel focusContainer = getLineWidget( pl );
				validateBlowUnitRoll(pl,blowUnits,roll);
				validateMeters(pl,meters);
				mainContainer.add(focusContainer);	
			}
			if (MkpkMathUtils.isNotZero(minutes)) {
				mainContainer.add(getTotalLine( minutes ));				
			}
		} else {
			Label lineLabel = new Label(MKPK.MSG.noData());
			lineLabel.setStyleName(MKPK.CSS.mkpkTextCenter());
			mainContainer.add(lineLabel);
		}
		content.setWidget(mainContainer);
	}

}
