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
import com.merkapack.erp.core.model.Product;
import com.merkapack.erp.gwt.client.common.MKPK;
import com.merkapack.erp.gwt.client.rpc.ProductService;
import com.merkapack.erp.gwt.client.rpc.ProductServiceAsync;
import com.merkapack.erp.gwt.client.rpc.ProductServiceAsyncDecorator;
import com.merkapack.watson.util.MkpkStringUtils;

public class MkpkProductBox extends ResizeComposite implements HasValue<String>
	,Focusable, HasSelectionHandlers<Product>, HasAllFocusHandlers
	,HasAllKeyHandlers {
	
	protected static final String BEGIN_STRONG = "<strong>";
	protected static final String END_STRONG = "</strong>";
	
	private static final int MIN_CHARACTERS = 2;
	private static final int MAX_CHARACTERS = 8;

	private ProductServiceAsync service;

	private Product selected;
	
	private SuggestBox product;
	private TextBox productTextBox;
	
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
	
	private static class ProductSuggestion extends MultiWordSuggestion {
		
		private Product product;
		
		private ProductSuggestion(Product product, String replacementString, String displayString) {
			super( replacementString, displayString );
			this.product = product;
		}
		
		public Product getProduct() {
			return product;
		}
		
	}
	
	public MkpkProductBox() {
		ProductServiceAsync commonServiceRaw = GWT.create(ProductService.class);
		service = new ProductServiceAsyncDecorator(commonServiceRaw);
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle() {
			@Override
			public void requestSuggestions(final Request request,final Callback callback) {
				suggestionDisplay.hideSuggestions();
				if (MkpkStringUtils.length(request.getQuery()) >= MIN_CHARACTERS
				 && MkpkStringUtils.length(request.getQuery()) <= MAX_CHARACTERS) {
					reset();
					service.getProducts(request.getQuery() ,new AsyncCallback<LinkedList<Product>>() {
		
								public void onFailure(Throwable caught) {
									callback.onSuggestionsReady(request, new Response());
								}
		
								public void onSuccess(LinkedList<Product> result) {
									LinkedList<Suggestion> suggestions = new LinkedList<Suggestion>();
									if (result != null) {
										
										for (final Product product : result) {
											SafeHtmlBuilder bld = new SafeHtmlBuilder();
											String ds = product.getName();
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
									        ProductSuggestion as = new ProductSuggestion(product, product.getName(), bld.toSafeHtml().asString());
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
		productTextBox = new TextBox();
		suggestionDisplay =  new AccountSuggestionDisplay();
		product = new SuggestBox(oracle,productTextBox,suggestionDisplay);
		productTextBox.setStyleName(MKPK.CSS.mkpkInputText());
		productTextBox.setVisibleLength(20);
		productTextBox.setMaxLength(32);
		
		product.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				ProductSuggestion selected = (ProductSuggestion) event.getSelectedItem();
				select( selected.getProduct() );
			}
		});
		initWidget(product);
	}
	
	public void setVisibleLength(int i) {
		productTextBox.setVisibleLength(i);		
	}

	private void select(Product result) {
		productTextBox.removeStyleName(MKPK.CSS.mkpkTextBoxError() );
		selected = result;
		SelectionEvent.fire(MkpkProductBox.this, result );
	}
	
	private void reset() {
		productTextBox.removeStyleName(MKPK.CSS.mkpkTextBoxError() );
		selected = null;
	}

	public Product getSelected() {
		return selected;
	}

	@Override
	public String getValue() {
		return product.getValue();
	}
	public void setValue(Product product, boolean fire) {
		this.selected = product;
		setValue(product==null?null:product.getName(),fire);
	}

	@Override
	public void setValue(String value) {
		product.setValue(value);
		if (MkpkStringUtils.isEmpty(value)) {
			reset();
		}
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		product.setValue(value,fireEvents);
	}

	// --------------------------------------------------------- HANDLERS
	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return productTextBox.addBlurHandler(handler);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return productTextBox.addFocusHandler(handler);
	}

	@Override
	public int getTabIndex() {
		return productTextBox.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		productTextBox.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		productTextBox.selectAll();
		productTextBox.setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		productTextBox.setTabIndex(index);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return product.addValueChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<Product> handler) {
		return super.addHandler(handler, SelectionEvent.getType());
	}

	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return productTextBox.addKeyUpHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return productTextBox.addKeyDownHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return productTextBox.addKeyPressHandler(handler);
	}
	public void setEnabled(boolean enabled) {
		productTextBox.setEnabled(enabled);
	}

}
