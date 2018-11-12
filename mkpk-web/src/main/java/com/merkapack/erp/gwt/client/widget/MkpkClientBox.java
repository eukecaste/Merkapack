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
import com.merkapack.erp.core.model.Client;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.rpc.ClientService;
import com.merkapack.erp.gwt.client.rpc.ClientServiceAsync;
import com.merkapack.erp.gwt.client.rpc.ClientServiceAsyncDecorator;
import com.merkapack.watson.util.MkpkStringUtils;

public class MkpkClientBox extends ResizeComposite implements HasValue<String>
	,Focusable, HasSelectionHandlers<Client>, HasAllFocusHandlers
	,HasAllKeyHandlers {
	
	protected static final String BEGIN_STRONG = "<strong>";
	protected static final String END_STRONG = "</strong>";
	
	private static final int MIN_CHARACTERS = 2;
	private static final int MAX_CHARACTERS = 8;

	private ClientServiceAsync service;

	private Client selected;
	
	private SuggestBox client;
	private TextBox clientTextBox;
	
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
	
	private static class ClientSuggestion extends MultiWordSuggestion {
		
		private Client client;
		
		private ClientSuggestion(Client client, String replacementString, String displayString) {
			super( replacementString, displayString );
			this.client = client;
		}
		
		public Client getClient() {
			return client;
		}
		
	}
	
	public MkpkClientBox() {
		ClientServiceAsync commonServiceRaw = GWT.create(ClientService.class);
		service = new ClientServiceAsyncDecorator(commonServiceRaw);
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle() {
			@Override
			public void requestSuggestions(final Request request,final Callback callback) {
				suggestionDisplay.hideSuggestions();
				if (MkpkStringUtils.length(request.getQuery()) >= MIN_CHARACTERS
				 && MkpkStringUtils.length(request.getQuery()) <= MAX_CHARACTERS) {
					reset();
					service.getClients(request.getQuery() ,new AsyncCallback<LinkedList<Client>>() {
		
								public void onFailure(Throwable caught) {
									callback.onSuggestionsReady(request, new Response());
								}
		
								public void onSuccess(LinkedList<Client> result) {
									LinkedList<Suggestion> suggestions = new LinkedList<Suggestion>();
									if (result != null) {
										
										for (final Client client : result) {
											SafeHtmlBuilder bld = new SafeHtmlBuilder();
											String ds = client.getName();
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
									        ClientSuggestion as = new ClientSuggestion(client, client.getName(), bld.toSafeHtml().asString());
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
		clientTextBox = new TextBox();
		suggestionDisplay =  new AccountSuggestionDisplay();
		client = new SuggestBox(oracle,clientTextBox,suggestionDisplay);
		clientTextBox.setStyleName(MKPK.CSS.mkpkInputText());
		clientTextBox.setVisibleLength(20);
		clientTextBox.setMaxLength(32);
		
		client.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				ClientSuggestion selected = (ClientSuggestion) event.getSelectedItem();
				select( selected.getClient() );
			}
		});
		initWidget(client);
	}
	
	public void setVisibleLength(int i) {
		clientTextBox.setVisibleLength(i);		
	}

	private void select(Client result) {
		clientTextBox.removeStyleName(MKPK.CSS.mkpkTextBoxError() );
		selected = result;
		SelectionEvent.fire(MkpkClientBox.this, result );
	}
	
	private void reset() {
		clientTextBox.removeStyleName(MKPK.CSS.mkpkTextBoxError() );
		selected = null;
	}

	public Client getSelected() {
		return selected;
	}

	@Override
	public String getValue() {
		return client.getValue();
	}
	public void setValue(Client client, boolean fire) {
		this.selected = client;
		setValue(client==null?null:client.getName(),fire);
	}

	@Override
	public void setValue(String value) {
		client.setValue(value);
		if (MkpkStringUtils.isEmpty(value)) {
			reset();
		}
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		client.setValue(value,fireEvents);
	}

	// --------------------------------------------------------- HANDLERS
	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return clientTextBox.addBlurHandler(handler);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return clientTextBox.addFocusHandler(handler);
	}

	@Override
	public int getTabIndex() {
		return clientTextBox.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		clientTextBox.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		clientTextBox.selectAll();
		clientTextBox.setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		clientTextBox.setTabIndex(index);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return client.addValueChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<Client> handler) {
		return super.addHandler(handler, SelectionEvent.getType());
	}

	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return clientTextBox.addKeyUpHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return clientTextBox.addKeyDownHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return clientTextBox.addKeyPressHandler(handler);
	}
	public void setEnabled(boolean enabled) {
		clientTextBox.setEnabled(enabled);
	}

}
