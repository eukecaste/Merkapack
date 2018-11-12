package com.merkapack.erp.gwt.client.widget;

import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle.MultiWordSuggestion;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.merkapack.erp.core.model.Machine;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.rpc.MachineService;
import com.merkapack.erp.gwt.client.rpc.MachineServiceAsync;
import com.merkapack.erp.gwt.client.rpc.MachineServiceAsyncDecorator;
import com.merkapack.watson.util.MkpkStringUtils;

public class MkpkMachineBox extends ResizeComposite implements HasValue<String>
	,Focusable, HasSelectionHandlers<Machine>, HasAllFocusHandlers
	,HasAllKeyHandlers {
	
	protected static final String BEGIN_STRONG = "<strong>";
	protected static final String END_STRONG = "</strong>";
	
	private static final int MIN_CHARACTERS = 2;
	private static final int MAX_CHARACTERS = 8;

	private MachineServiceAsync service;

	private Machine selected;
	
	private SuggestBox machine;
	private TextBox machineTextBox;
	
	private AccountSuggestionDisplay suggestionDisplay;
	
	private static class AccountSuggestionDisplay extends DefaultSuggestionDisplay {
	    private Widget suggestionMenu;

	    @Override
	    protected Widget decorateSuggestionList(Widget suggestionList) {
	        suggestionMenu = suggestionList;
	        return suggestionList;
	    }
	    
	    @Override
		protected void moveSelectionDown() {
			super.moveSelectionDown();
			scrollSelectedItemIntoView();
		}

		@Override
		protected void moveSelectionUp() {
			super.moveSelectionUp();
			scrollSelectedItemIntoView();
		}
		
	    private void scrollSelectedItemIntoView() {
	        NodeList<Node> trList = suggestionMenu.getElement().getChild(1).getChild(0).getChildNodes();
	        for (int trIndex = 0; trIndex < trList.getLength(); ++trIndex) {
	            Element trElement = (Element)trList.getItem(trIndex);
	            if (((Element)trElement.getChild(0)).getClassName().contains("selected")) {
	                trElement.scrollIntoView();
	                break;
	            }
	        }
	    }
	}
	
	private static class MachineSuggestion extends MultiWordSuggestion {
		
		private Machine machine;
		
		private MachineSuggestion(Machine machine, String replacementString, String displayString) {
			super( replacementString, displayString );
			this.machine = machine;
		}
		
		public Machine getMachine() {
			return machine;
		}
		
	}
	
	public MkpkMachineBox() {
		MachineServiceAsync commonServiceRaw = GWT.create(MachineService.class);
		service = new MachineServiceAsyncDecorator(commonServiceRaw);
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle() {
			@Override
			public void requestSuggestions(final Request request,final Callback callback) {
				suggestionDisplay.hideSuggestions();
				if (MkpkStringUtils.length(request.getQuery()) >= MIN_CHARACTERS
				 && MkpkStringUtils.length(request.getQuery()) <= MAX_CHARACTERS) {
					reset();
					service.getMachines(request.getQuery() ,new AsyncCallback<LinkedList<Machine>>() {
		
								public void onFailure(Throwable caught) {
									callback.onSuggestionsReady(request, new Response());
								}
		
								public void onSuccess(LinkedList<Machine> result) {
									LinkedList<Suggestion> suggestions = new LinkedList<Suggestion>();
									if (result != null) {
										
										for (final Machine machine : result) {
											SafeHtmlBuilder bld = new SafeHtmlBuilder();
											String ds = machine.getName();
											int i = MkpkStringUtils.indexOfIgnoreCase(ds, request.getQuery());
											bld.appendHtmlConstant("<span class=\"" 
													+ MKPK.CSS.mkpkIconBullet()
													+ MkpkStringUtils.SPACE
													+ MKPK.CSS.mkpkIconPaddingLeft()
													+ "\" >");
											bld.appendEscaped(MkpkStringUtils.substring(ds, 0, i));
											bld.appendHtmlConstant(BEGIN_STRONG);
											bld.appendEscaped(MkpkStringUtils.substring(ds, i, (i + MkpkStringUtils.length(request.getQuery()) )));
									        bld.appendHtmlConstant(END_STRONG);
									        bld.appendEscaped(MkpkStringUtils.substring(ds, (i + MkpkStringUtils.length(request.getQuery()) )));
									        bld.appendHtmlConstant("</span>");
									        MachineSuggestion as = new MachineSuggestion(machine, machine.getName(), bld.toSafeHtml().asString());
											suggestions.add(as);
										}
									}
									Response resp = new Response(suggestions);
									callback.onSuggestionsReady(request, resp);
									
								}
							});
				}
			}
		};
		machineTextBox = new TextBox();
		suggestionDisplay =  new AccountSuggestionDisplay();
		machine = new SuggestBox(oracle,machineTextBox,suggestionDisplay);
		machineTextBox.setStyleName(MKPK.CSS.mkpkInputText());
		machineTextBox.setVisibleLength(20);
		machineTextBox.setMaxLength(32);
		
		machine.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				MachineSuggestion selected = (MachineSuggestion) event.getSelectedItem();
				select( selected.getMachine() );
			}
		});
		initWidget(machine);
	}
	
	public void setVisibleLength(int i) {
		machineTextBox.setVisibleLength(i);		
	}

	private void select(Machine result) {
		machineTextBox.removeStyleName(MKPK.CSS.mkpkTextBoxError() );
		selected = result;
		SelectionEvent.fire(MkpkMachineBox.this, result );
	}
	
	private void reset() {
		machineTextBox.removeStyleName(MKPK.CSS.mkpkTextBoxError() );
		selected = null;
	}

	public Machine getSelected() {
		return selected;
	}

	@Override
	public String getValue() {
		return machine.getValue();
	}
	public void setValue(Machine machine, boolean fire) {
		this.selected = machine;
		setValue(machine==null?null:machine.getName(),fire);
	}

	@Override
	public void setValue(String value) {
		machine.setValue(value);
		if (MkpkStringUtils.isEmpty(value)) {
			reset();
		}
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		machine.setValue(value,fireEvents);
	}

	// --------------------------------------------------------- HANDLERS
	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return machineTextBox.addBlurHandler(handler);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return machineTextBox.addFocusHandler(handler);
	}

	@Override
	public int getTabIndex() {
		return machineTextBox.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		machineTextBox.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		machineTextBox.selectAll();
		machineTextBox.setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		machineTextBox.setTabIndex(index);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return machine.addValueChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<Machine> handler) {
		return super.addHandler(handler, SelectionEvent.getType());
	}

	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return machineTextBox.addKeyUpHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return machineTextBox.addKeyDownHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return machineTextBox.addKeyPressHandler(handler);
	}
	public void setEnabled(boolean enabled) {
		machineTextBox.setEnabled(enabled);
	}

}
