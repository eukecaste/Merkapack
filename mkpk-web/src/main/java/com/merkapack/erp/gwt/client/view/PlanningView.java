package com.merkapack.erp.gwt.client.view;


import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
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
import com.google.gwt.resources.client.CssResource.ClassName;
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
					checkButton.setEnabled(true);
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
					checkButton.setEnabled(true);
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
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					public void execute() {
						product.setFocus(true);
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
		list.add(planning);
		planningRow.setPlanning( planning );
		planningRow.refresh();
		refreshList();
		content.scrollToBottom();
		return planning;
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
				.setClient(jsPlanning.getClient()==null?null
					:new Client()
					.setId(jsPlanning.getClient().getId())
					.setDomain(jsPlanning.getClient().getDomain())
					.setName(jsPlanning.getClient().getName()))
				.setComments(jsPlanning.getComments())
			);
		}
		refreshList();
	}

	protected void checkList() {
		LinkedList<Planning> ret = PlanningRowCalculator.calculate(list);
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
		tab.getColumnFormatter().setWidth( 7, "80px");
		tab.getColumnFormatter().setWidth( 8, "80px");
		tab.getColumnFormatter().setWidth( 9, "80px");
		tab.getColumnFormatter().setWidth(10, "80px");
		tab.getColumnFormatter().setWidth(11, "80px");
		tab.getColumnFormatter().setWidth(12, "100px");
		tab.getColumnFormatter().setWidth(13, "auto");
		tab.getColumnFormatter().setWidth(14, "20px");
		paintHeader( tab );
		return tab;
	}

	private void paintHeader(FlexTable tab) {
		String[] labels = new String[] { ".", MKPK.MSG.date()
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

			// ORDER
			tab.setWidget(row, col, new Label("+"));
			tab.getCellFormatter().addStyleName(row, col, MKPK.CSS.mkpkTextCenter());
			col++;
			
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
			validate();
		}
		public void validate() {
			if ( MkpkMathUtils.isNotZero(getPlanning().getRollWidth())) {
				if ( MkpkMathUtils.isNotZero(getPlanning().getWidth())) {
					if (MkpkMathUtils.isNotZero(MkpkMathUtils.round(getPlanning().getRollWidth() % getPlanning().getWidth()))) {
						blowUnits.addStyleName(MKPK.CSS.mkpkColorRed());
						roll.addStyleName(MKPK.CSS.mkpkColorRed());
					} else {
						blowUnits.removeStyleName(MKPK.CSS.mkpkColorRed());
						roll.removeStyleName(MKPK.CSS.mkpkColorRed());
					}
				}
			}
			if ( MkpkMathUtils.isNotZero(getPlanning().getRollLength())) {
				if ( MkpkMathUtils.isNotZero(getPlanning().getMeters())) {
					if ( getPlanning().getMeters() > getPlanning().getRollLength()) {
						meters.addStyleName(MKPK.CSS.mkpkColorRed());
					} else {
						meters.removeStyleName(MKPK.CSS.mkpkColorRed());
					}
					
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
	
	
	
//	private void refreshList() {
//		content.clear();
//		FlowPanel container = new FlowPanel("pre");
//		container.setStyleName(MKPK.CSS.mkpkFlexContainer());
//		container.getElement().getStyle().setFontSize(0.95, Unit.EM);
//		if (list != null && list.size() > 0) {
//			String line0 = 
//				  '\u250c' + MkpkStringUtils.repeat('\u2500', 12)
//				+ '\u252c' + MkpkStringUtils.repeat('\u2500', 3)
//				+ '\u252c' + MkpkStringUtils.repeat('\u2500', 14)
//				+ '\u252c' + MkpkStringUtils.repeat('\u2500', 13)
//				+ '\u252c' + MkpkStringUtils.repeat('\u2500', 14)
//				+ '\u252c' + MkpkStringUtils.repeat('\u2500', 10)
//				+ '\u252c' + MkpkStringUtils.repeat('\u2500', 11)
//				+ '\u252c' + MkpkStringUtils.repeat('\u2500', 10)
//				+ '\u252c' + MkpkStringUtils.repeat('\u2500', 10)
//				+ '\u252c' + MkpkStringUtils.repeat('\u2500', 5)
//				+ '\u252c' + MkpkStringUtils.repeat('\u2500', 10)
//				+ '\u252c' + MkpkStringUtils.repeat('\u2500', 20)
//				+ '\u252c' + MkpkStringUtils.repeat('\u2500', 21)
//				+ '\u2510';
//			String line1 = 
//				  '\u2502' + MkpkStringUtils.center(MKPK.MSG.date(), 12)
//				+ '\u2502' + MkpkStringUtils.center( "#" , 3)
//				+ '\u2502' + MkpkStringUtils.rightPad( MKPK.MSG.measure() ,14)
//				+ '\u2502' + MkpkStringUtils.rightPad( MKPK.MSG.material() ,13)
//				+ '\u2502' + MkpkStringUtils.rightPad( MKPK.MSG.roll() ,14)
//				+ '\u2502' + MkpkStringUtils.leftPad ( MKPK.MSG.unit(), 10)
//				+ '\u2502' + MkpkStringUtils.leftPad ( MKPK.MSG.blowUnits(), 11)
//				+ '\u2502' + MkpkStringUtils.leftPad ( MKPK.MSG.meters(), 10)
//				+ '\u2502' + MkpkStringUtils.leftPad ( MKPK.MSG.blows(), 10)
//				+ '\u2502' + MkpkStringUtils.leftPad ( MKPK.MSG.blowsMinutesAbbrv(), 5)
//				+ '\u2502' + MkpkStringUtils.leftPad ( MKPK.MSG.time(), 10)
//				+ '\u2502' + MkpkStringUtils.rightPad( MKPK.MSG.client(),20)
//				+ '\u2502' + MkpkStringUtils.rightPad( MKPK.MSG.comments(),21)
//				+ '\u2502';
//				;
//			String line2 = 
//					  '\u251c' + MkpkStringUtils.repeat('\u2500', 12)
//					+ '\u253c' + MkpkStringUtils.repeat('\u2500', 3)
//					+ '\u253c' + MkpkStringUtils.repeat('\u2500', 14)
//					+ '\u253c' + MkpkStringUtils.repeat('\u2500', 13)
//					+ '\u253c' + MkpkStringUtils.repeat('\u2500', 14)
//					+ '\u253c' + MkpkStringUtils.repeat('\u2500', 10)
//					+ '\u253c' + MkpkStringUtils.repeat('\u2500', 11)
//					+ '\u253c' + MkpkStringUtils.repeat('\u2500', 10)
//					+ '\u253c' + MkpkStringUtils.repeat('\u2500', 10)
//					+ '\u253c' + MkpkStringUtils.repeat('\u2500', 5)
//					+ '\u253c' + MkpkStringUtils.repeat('\u2500', 10)
//					+ '\u253c' + MkpkStringUtils.repeat('\u2500', 20)
//					+ '\u253c' + MkpkStringUtils.repeat('\u2500', 21)
//					+ '\u2524';
//			InlineLabel line0Label = new InlineLabel(line0);
//			line0Label.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//			InlineLabel line1Label = new InlineLabel(line1);
//			line1Label.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//			InlineLabel line2Label = new InlineLabel(line2);
//			line2Label.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//			FlowPanel f0 = new FlowPanel(); f0.add(line0Label); container.add(f0);
//			FlowPanel f1 = new FlowPanel(); f1.add(line1Label); container.add(f1);
//			FlowPanel f2 = new FlowPanel(); f2.add(line2Label); container.add(f2);

//			String evenColor = MKPK.CSS.mkpkEvenBackgroundColor();
//			String oddColor = MKPK.CSS.mkpkOddBackgroundColor();
//			String color = oddColor;
//			Date date = null;
//			double minutes = 0;
//			for (Planning pl : list) {
//				if (GWTDateUtils.compare(pl.getDate(), date) != 0) {
//					if ( date != null) {
//						String line40 = 
//								  '\u2514' + MkpkStringUtils.repeat('\u2500', 12)
//								+ '\u2534' + MkpkStringUtils.repeat('\u2500', 3)
//								+ '\u2534' + MkpkStringUtils.repeat('\u2500', 14)
//								+ '\u2534' + MkpkStringUtils.repeat('\u2500', 13)
//								+ '\u2534' + MkpkStringUtils.repeat('\u2500', 14)
//								+ '\u2534' + MkpkStringUtils.repeat('\u2500', 10)
//								+ '\u2534' + MkpkStringUtils.repeat('\u2500', 11)
//								+ '\u2534' + MkpkStringUtils.repeat('\u2500', 10)
//								+ '\u2534' + MkpkStringUtils.repeat('\u2500', 10)
//								+ '\u2534' + MkpkStringUtils.repeat('\u2500', 5)
//								+ '\u2534' + MkpkStringUtils.repeat('\u2500', 10)
//								+ '\u2534' + MkpkStringUtils.repeat('\u2500', 20)
//								+ '\u2534' + MkpkStringUtils.repeat('\u2500', 21)
//								+ '\u2518';
//						InlineLabel line40Label = new InlineLabel(line40);
//						line40Label.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//						FlowPanel f60 = new FlowPanel(); f60.add(line40Label); container.add(f60);
//						
//						
//						String lineTotalMinutes = MkpkStringUtils.leftPad("Total horas dia (" + MKPK.DATE_FORMAT.format( date ) + ")", 113)
//							+ MkpkStringUtils.leftPad(MKPK.FMT.format( minutes / 60 ), 10);
//						InlineLabel lineTotalMinutesLabel = new InlineLabel(lineTotalMinutes);
//						lineTotalMinutesLabel.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//						lineTotalMinutesLabel.addStyleName(MKPK.CSS.mkpkBold());
////						lineTotalMinutesLabel.addStyleName( color );
//						FlowPanel f3 = new FlowPanel(); f3.add(lineTotalMinutesLabel); container.add(f3);
//
////						color = color.equals(oddColor)?evenColor:oddColor;
//						
//						String line022 = 
//								  '\u250c' + MkpkStringUtils.repeat('\u2500', 12)
//								+ '\u252c' + MkpkStringUtils.repeat('\u2500', 3)
//								+ '\u252c' + MkpkStringUtils.repeat('\u2500', 14)
//								+ '\u252c' + MkpkStringUtils.repeat('\u2500', 13)
//								+ '\u252c' + MkpkStringUtils.repeat('\u2500', 14)
//								+ '\u252c' + MkpkStringUtils.repeat('\u2500', 10)
//								+ '\u252c' + MkpkStringUtils.repeat('\u2500', 11)
//								+ '\u252c' + MkpkStringUtils.repeat('\u2500', 10)
//								+ '\u252c' + MkpkStringUtils.repeat('\u2500', 10)
//								+ '\u252c' + MkpkStringUtils.repeat('\u2500', 5)
//								+ '\u252c' + MkpkStringUtils.repeat('\u2500', 10)
//								+ '\u252c' + MkpkStringUtils.repeat('\u2500', 20)
//								+ '\u252c' + MkpkStringUtils.repeat('\u2500', 21)
//								+ '\u2510';
//						InlineLabel line222Label = new InlineLabel(line022);
//						line222Label.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//						FlowPanel f022 = new FlowPanel(); f022.add(line222Label); container.add(f022);
//						
//					}
//					date = pl.getDate();
//					minutes = 0;
//				}
//				minutes = minutes + pl.getMinutes();
//				
//				String line = 
//					  '\u2502' + MkpkStringUtils.center(MKPK.DATE_FORMAT.format( pl.getDate()), 12)
//					+ '\u2502' + MkpkStringUtils.center( MkpkNumberUtils.toString( pl.getOrder()), 3)
//					+ '\u2502' + MkpkStringUtils.rightPad( MkpkStringUtils.abbreviate(pl.getProduct()!=null?pl.getProduct().getName():"", 13),14)
//					+ '\u2502' + MkpkStringUtils.rightPad( MkpkStringUtils.abbreviate(pl.getMaterial()!=null?pl.getMaterial().getName():"", 12),13)
//					+ '\u2502' + MkpkStringUtils.rightPad( MkpkStringUtils.abbreviate(pl.getRoll()!=null?pl.getRoll().getName():"", 13),14)
//					+ '\u2502' + MkpkStringUtils.leftPad(MKPK.FMT_INT.format( pl.getAmount()), 10)
//					+ '\u2502' + MkpkStringUtils.leftPad(MKPK.FMT_INT.format( pl.getBlowUnits()), 11)
//					+ '\u2502' + MkpkStringUtils.leftPad(MKPK.FMT.format( pl.getMeters()), 10)
//					+ '\u2502' + MkpkStringUtils.leftPad(MKPK.FMT.format( pl.getBlows()), 10)
//					+ '\u2502' + MkpkStringUtils.leftPad(MKPK.FMT_INT.format( pl.getBlowsMinute()), 5)
//					+ '\u2502' + MkpkStringUtils.leftPad(MKPK.FMT.format( pl.getHours()), 10)
//					+ '\u2502' + MkpkStringUtils.rightPad( MkpkStringUtils.abbreviate(pl.getClient()!=null?pl.getClient().getName():"", 19),20)
//					+ '\u2502' + MkpkStringUtils.rightPad( MkpkStringUtils.abbreviate(pl.getComments()!=null?pl.getComments():".....", 20),21)
//					+ '\u2502';
//				InlineLabel lineLabel = new InlineLabel(line);
//				lineLabel.setStyleName(MKPK.CSS.mkpkIconPaddingLeft());
////				lineLabel.addStyleName( color );
//				lineLabel.addStyleName(MKPK.CSS.mkpkClickableLabel());
//				lineLabel.addClickHandler( new ClickHandler() {
//					
//					@Override
//					public void onClick(ClickEvent event) {
//						planningRow.setPlanning(pl);
//						planningRow.refresh();
//					}
//				});
//				FlowPanel f4 = new FlowPanel(); f4.add(lineLabel); container.add(f4);
//			}
//			String line400 = 
//					  '\u2514' + MkpkStringUtils.repeat('\u2500', 12)
//					+ '\u2534' + MkpkStringUtils.repeat('\u2500', 3)
//					+ '\u2534' + MkpkStringUtils.repeat('\u2500', 14)
//					+ '\u2534' + MkpkStringUtils.repeat('\u2500', 13)
//					+ '\u2534' + MkpkStringUtils.repeat('\u2500', 14)
//					+ '\u2534' + MkpkStringUtils.repeat('\u2500', 10)
//					+ '\u2534' + MkpkStringUtils.repeat('\u2500', 11)
//					+ '\u2534' + MkpkStringUtils.repeat('\u2500', 10)
//					+ '\u2534' + MkpkStringUtils.repeat('\u2500', 10)
//					+ '\u2534' + MkpkStringUtils.repeat('\u2500', 5)
//					+ '\u2534' + MkpkStringUtils.repeat('\u2500', 10)
//					+ '\u2534' + MkpkStringUtils.repeat('\u2500', 20)
//					+ '\u2534' + MkpkStringUtils.repeat('\u2500', 21)
//					+ '\u2518';
//			InlineLabel line400Label = new InlineLabel(line400);
//			line400Label.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//			FlowPanel f600 = new FlowPanel(); f600.add(line400Label); container.add(f600);
//				
//			if ( minutes != 0) {
//				String lineTotalMinutes = MkpkStringUtils.leftPad("Total horas dia (" + MKPK.DATE_FORMAT.format( date ) + ")", 113)
//						+ MkpkStringUtils.leftPad(MKPK.FMT.format( minutes / 60 ), 10);
//				InlineLabel lineTotalMinutesLabel = new InlineLabel(lineTotalMinutes);
//				lineTotalMinutesLabel.addStyleName(MKPK.CSS.mkpkIconPaddingLeft());
//				lineTotalMinutesLabel.addStyleName(MKPK.CSS.mkpkBold());
//				FlowPanel f5 = new FlowPanel(); f5.add(lineTotalMinutesLabel); container.add(f5);
//			}
//		} else {
//			Label lineLabel = new Label( MKPK.MSG.noData() );
//			lineLabel.setStyleName(MKPK.CSS.mkpkTextCenter());
//			container.add(lineLabel);
//		}
	private void refreshList() {
		content.clear();
		FlowPanel container = new FlowPanel("pre");
		container.setStyleName(MKPK.CSS.mkpkFlexContainer());
		container.getElement().getStyle().setFontSize(0.95, Unit.EM);
		if (list != null && list.size() > 0) {
			for (Planning pl : list) {
				InlineLabel selector = new InlineLabel();
				selector.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				selector.addStyleName(MKPK.CSS.mkpkIconBullet());
				selector.addStyleName(MKPK.CSS.mkpkFlexContainerChildSelector());
				container.add(selector);
				
				InlineLabel date = new InlineLabel(MKPK.DATE_FORMAT.format(pl.getDate()));
				date.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				date.addStyleName(MKPK.CSS.mkpkFlexContainerChildDate());
				container.add(date);

				InlineLabel order = new InlineLabel(MkpkNumberUtils.toString(pl.getOrder()));
				order.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				order.addStyleName(MKPK.CSS.mkpkFlexContainerChildOrder());
				container.add(order);

				InlineLabel product = new InlineLabel(MkpkStringUtils.abbreviate(pl.getProduct() != null ? pl.getProduct().getName() : "" , 13));
				product.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				product.addStyleName(MKPK.CSS.mkpkFlexContainerChildProduct());
				container.add(product);
				
				InlineLabel material = new InlineLabel(MkpkStringUtils.abbreviate(pl.getMaterial() != null ? pl.getMaterial().getName() : "",13));
				material.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				material.addStyleName(MKPK.CSS.mkpkFlexContainerChildMaterial());
				container.add(material);

				InlineLabel roll = new InlineLabel(MkpkStringUtils.abbreviate(pl.getRoll() != null ? pl.getRoll().getName() : "", 13));
				roll.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				roll.addStyleName(MKPK.CSS.mkpkFlexContainerChildRoll());
				container.add(roll);

				InlineLabel amount = new InlineLabel(MKPK.FMT_INT.format(pl.getAmount()));
				amount.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				amount.addStyleName(MKPK.CSS.mkpkFlexContainerChildAmount());
				container.add(amount);
				
				InlineLabel blowUnits = new InlineLabel(MKPK.FMT_INT.format(pl.getBlowUnits()));
				blowUnits.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				blowUnits.addStyleName(MKPK.CSS.mkpkFlexContainerChildBlowUnits());
				container.add(blowUnits);
				
				InlineLabel meters = new InlineLabel(MKPK.FMT.format(pl.getMeters()));
				meters.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				meters.addStyleName(MKPK.CSS.mkpkFlexContainerChildMeters());
				container.add(meters);

				InlineLabel blows = new InlineLabel(MKPK.FMT.format(pl.getBlows()));
				blows.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				blows.addStyleName(MKPK.CSS.mkpkFlexContainerChildBlows());
				container.add(blows);

				InlineLabel blowsMinute = new InlineLabel(MKPK.FMT_INT.format(pl.getBlowsMinute()));
				blowsMinute.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				blowsMinute.addStyleName(MKPK.CSS.mkpkFlexContainerChildBlowsMinute());
				container.add(blowsMinute);
				
				InlineLabel hours = new InlineLabel(MKPK.FMT.format(pl.getHours()));
				hours.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				hours.addStyleName(MKPK.CSS.mkpkFlexContainerChildHours());
				container.add(hours);
				
				InlineLabel client = new InlineLabel(MkpkStringUtils.abbreviate(pl.getClient() != null ? pl.getClient().getName() : "", 19));
				client.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				client.addStyleName(MKPK.CSS.mkpkFlexContainerChildClient());
				container.add(client);

				InlineLabel comments = new InlineLabel(MkpkStringUtils.abbreviate(pl.getComments() != null ? pl.getComments() : ".....", 20));
				comments.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				comments.addStyleName(MKPK.CSS.mkpkFlexContainerChildComments());
				container.add(comments);

				InlineLabel delete = new InlineLabel();
				delete.setStyleName(MKPK.CSS.mkpkFlexContainerChild());
				delete.addStyleName(MKPK.CSS.mkpkIconDelete());
				delete.addStyleName(MKPK.CSS.mkpkFlexContainerChildDelete());
				container.add(delete);
				
			}
		} else {
			Label lineLabel = new Label(MKPK.MSG.noData());
			lineLabel.setStyleName(MKPK.CSS.mkpkTextCenter());
			container.add(lineLabel);
		}
		content.setWidget(container);
	}
	
}
