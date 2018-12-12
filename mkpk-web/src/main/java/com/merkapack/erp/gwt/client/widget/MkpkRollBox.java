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
import com.google.gwt.user.client.Timer;
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
import com.merkapack.erp.core.model.Roll;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.rpc.RollService;
import com.merkapack.erp.gwt.client.rpc.RollServiceAsync;
import com.merkapack.erp.gwt.client.rpc.RollServiceAsyncDecorator;
import com.merkapack.watson.util.MkpkStringUtils;

public class MkpkRollBox extends ResizeComposite implements HasValue<String>
	,Focusable, HasSelectionHandlers<Roll>, HasAllFocusHandlers
	,HasAllKeyHandlers {
	
	private static final int CHANGE_DISPLAY_MILLIS = 4000;
	protected static final String BEGIN_STRONG = "<strong>";
	protected static final String END_STRONG = "</strong>";
	
	private static final int MIN_CHARACTERS = 2;
	private static final int MAX_CHARACTERS = 8;

	private static final RollServiceAsync commonServiceRaw = GWT.create(RollService.class);
	private static final RollServiceAsync SERVICE = new RollServiceAsyncDecorator(commonServiceRaw);

	private Roll selected;
	
	private SuggestBox roll;
	private TextBox rollTextBox;
	
	private AccountSuggestionDisplay suggestionDisplay;
	
	public static interface IMaterialCallback {
		Integer getMaterial();
	}
	
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
	
	private static class RollSuggestion extends MultiWordSuggestion {
		
		private Roll roll;
		
		private RollSuggestion(Roll roll, String replacementString, String displayString) {
			super( replacementString, displayString );
			this.roll = roll;
		}
		
		public Roll getRoll() {
			return roll;
		}
		
	}
	
	public MkpkRollBox(IMaterialCallback materialCallBack) {
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle() {
			@Override
			public void requestSuggestions(final Request request,final Callback callback) {
				suggestionDisplay.hideSuggestions();
				if (MkpkStringUtils.length(request.getQuery()) >= MIN_CHARACTERS
				 && MkpkStringUtils.length(request.getQuery()) <= MAX_CHARACTERS) {
					reset();
					SERVICE.getRolls(request.getQuery(),(materialCallBack==null?null:materialCallBack.getMaterial())
							,new AsyncCallback<LinkedList<Roll>>() {
								public void onFailure(Throwable caught) {
									callback.onSuggestionsReady(request, new Response());
								}
		
								public void onSuccess(LinkedList<Roll> result) {
									LinkedList<Suggestion> suggestions = new LinkedList<Suggestion>();
									if (result != null) {
										
										for (final Roll roll : result) {
											SafeHtmlBuilder bld = new SafeHtmlBuilder();
											String ds = roll.getName() 
												+ (roll.getMaterial()!=null?" ("+ roll.getMaterial().getName()+")":"");
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
									        RollSuggestion as = new RollSuggestion(roll, roll.getName(), bld.toSafeHtml().asString());
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
		rollTextBox = new TextBox();
		suggestionDisplay =  new AccountSuggestionDisplay();
		roll = new SuggestBox(oracle,rollTextBox,suggestionDisplay);
		rollTextBox.setStyleName(MKPK.CSS.mkpkInputText());
		rollTextBox.setVisibleLength(20);
		rollTextBox.setMaxLength(32);
		
		roll.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				RollSuggestion selected = (RollSuggestion) event.getSelectedItem();
				select( selected.getRoll() );
			}
		});
		initWidget(roll);
	}
	
	public void setVisibleLength(int i) {
		rollTextBox.setVisibleLength(i);		
	}

	private void select(Roll result) {
		rollTextBox.removeStyleName(MKPK.CSS.mkpkTextBoxError() );
		selected = result;
		SelectionEvent.fire(MkpkRollBox.this, result );
	}
	
	private void reset() {
		rollTextBox.removeStyleName(MKPK.CSS.mkpkTextBoxError() );
		selected = null;
	}

	public Roll getSelected() {
		return selected;
	}

	@Override
	public String getValue() {
		return roll.getValue();
	}
	public void setValue(Roll roll, boolean fire) {
		this.selected = roll;
		setValue(roll==null?null:roll.getName(),fire);
	}
	public void setValue(Roll value, boolean fireEvents, boolean shouldDisplayChange) {
		setValue(value, fireEvents);
		if (shouldDisplayChange) {
			addStyleName(MKPK.CSS.mkpkValueChanged());
			if (CHANGE_DISPLAY_MILLIS > 0)
				new Timer() {
					@Override
					public void run() {
						removeStyleName(MKPK.CSS.mkpkValueChanged());
					}
				}.schedule(CHANGE_DISPLAY_MILLIS);
		}
	}

	@Override
	public void setValue(String value) {
		roll.setValue(value);
		if (MkpkStringUtils.isEmpty(value)) {
			reset();
		}
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		roll.setValue(value,fireEvents);
	}

	// --------------------------------------------------------- HANDLERS
	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return rollTextBox.addBlurHandler(handler);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return rollTextBox.addFocusHandler(handler);
	}

	@Override
	public int getTabIndex() {
		return rollTextBox.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		rollTextBox.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		rollTextBox.selectAll();
		rollTextBox.setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		rollTextBox.setTabIndex(index);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return roll.addValueChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<Roll> handler) {
		return super.addHandler(handler, SelectionEvent.getType());
	}

	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return rollTextBox.addKeyUpHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return rollTextBox.addKeyDownHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return rollTextBox.addKeyPressHandler(handler);
	}
	public void setEnabled(boolean enabled) {
		rollTextBox.setEnabled(enabled);
	}

}
