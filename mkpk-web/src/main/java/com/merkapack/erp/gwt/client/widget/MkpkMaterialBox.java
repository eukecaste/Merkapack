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
import com.merkapack.erp.core.model.Material;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.rpc.MaterialService;
import com.merkapack.erp.gwt.client.rpc.MaterialServiceAsync;
import com.merkapack.erp.gwt.client.rpc.MaterialServiceAsyncDecorator;
import com.merkapack.watson.util.MkpkStringUtils;

public class MkpkMaterialBox extends ResizeComposite implements HasValue<String>
	,Focusable, HasSelectionHandlers<Material>, HasAllFocusHandlers
	,HasAllKeyHandlers {
	
	protected static final String BEGIN_STRONG = "<strong>";
	protected static final String END_STRONG = "</strong>";
	
	private static final int MIN_CHARACTERS = 2;
	private static final int MAX_CHARACTERS = 8;

	private MaterialServiceAsync service;

	private Material selected;
	
	private SuggestBox material;
	private TextBox materialTextBox;
	
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
	
	private static class MaterialSuggestion extends MultiWordSuggestion {
		
		private Material material;
		
		private MaterialSuggestion(Material material, String replacementString, String displayString) {
			super( replacementString, displayString );
			this.material = material;
		}
		
		public Material getMaterial() {
			return material;
		}
		
	}
	
	public MkpkMaterialBox() {
		MaterialServiceAsync commonServiceRaw = GWT.create(MaterialService.class);
		service = new MaterialServiceAsyncDecorator(commonServiceRaw);
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle() {
			@Override
			public void requestSuggestions(final Request request,final Callback callback) {
				suggestionDisplay.hideSuggestions();
				if (MkpkStringUtils.length(request.getQuery()) >= MIN_CHARACTERS
				 && MkpkStringUtils.length(request.getQuery()) <= MAX_CHARACTERS) {
					reset();
					service.getMaterials(request.getQuery() ,new AsyncCallback<LinkedList<Material>>() {
		
								public void onFailure(Throwable caught) {
									callback.onSuggestionsReady(request, new Response());
								}
		
								public void onSuccess(LinkedList<Material> result) {
									LinkedList<Suggestion> suggestions = new LinkedList<Suggestion>();
									if (result != null) {
										
										for (final Material material : result) {
											SafeHtmlBuilder bld = new SafeHtmlBuilder();
											String ds = material.getName();
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
									        MaterialSuggestion as = new MaterialSuggestion(material, material.getName(), bld.toSafeHtml().asString());
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
		materialTextBox = new TextBox();
		suggestionDisplay =  new AccountSuggestionDisplay();
		material = new SuggestBox(oracle,materialTextBox,suggestionDisplay);
		materialTextBox.setStyleName(MKPK.CSS.mkpkInputText());
		materialTextBox.setVisibleLength(20);
		materialTextBox.setMaxLength(32);
		
		material.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				MaterialSuggestion selected = (MaterialSuggestion) event.getSelectedItem();
				select( selected.getMaterial() );
			}
		});
		initWidget(material);
	}
	
	private void select(Material result) {
		materialTextBox.removeStyleName(MKPK.CSS.mkpkTextBoxError() );
		selected = result;
		SelectionEvent.fire(MkpkMaterialBox.this, result );
	}
	
	private void reset() {
		materialTextBox.removeStyleName(MKPK.CSS.mkpkTextBoxError() );
		selected = null;
	}

	public Material getSelected() {
		return selected;
	}

	@Override
	public String getValue() {
		return material.getValue();
	}
	public void setValue(Material material, boolean fire) {
		this.selected = material;
		setValue(material==null?null:material.getName(),fire);
	}

	@Override
	public void setValue(String value) {
		material.setValue(value);
		if (MkpkStringUtils.isEmpty(value)) {
			reset();
		}
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		material.setValue(value,fireEvents);
	}

	// --------------------------------------------------------- HANDLERS
	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return materialTextBox.addBlurHandler(handler);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return materialTextBox.addFocusHandler(handler);
	}

	@Override
	public int getTabIndex() {
		return materialTextBox.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		materialTextBox.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		materialTextBox.selectAll();
		materialTextBox.setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		materialTextBox.setTabIndex(index);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return material.addValueChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<Material> handler) {
		return super.addHandler(handler, SelectionEvent.getType());
	}

	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return materialTextBox.addKeyUpHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return materialTextBox.addKeyDownHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return materialTextBox.addKeyPressHandler(handler);
	}
	public void setEnabled(boolean enabled) {
		materialTextBox.setEnabled(enabled);
	}
}
