package com.merkapack.erp.gwt.client.widget;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.merkapack.erp.gwt.client.common.MKPK;

public class MkpkConfirmDialog extends MkpkCustomDialog {
    
	public static interface MkpkConfirmDialogCallback {
		void onAccept();
		void onCancel();
		default void onClose() {
			this.onCancel();
		}
	}

	private SimpleLayoutPanel root;
	
	public MkpkConfirmDialog() {
		setVisible(false);
		setAnimationEnabled(true);
		setGlassEnabled(true);
		setModal(true);
		setStyleName(MKPK.CSS.mkpkConfirmDialog());
		root = new SimpleLayoutPanel();
		root.setWidth("500px");
		root.setHeight("90px");
		root.setStyleName(MKPK.CSS.mkpkPadding());
		this.setWidget(root);
	}
    public void confirm(String msg, final MkpkConfirmDialogCallback callback) {
    	confirm("Pregunta", msg, callback);
    }
    
	public void confirm(String header,String msg, final MkpkConfirmDialogCallback callback) {
    	setCaption(header);
    	FlowPanel panel = new FlowPanel();
    	Label label = new Label(msg);
    	label.setStyleName(MKPK.CSS.mkpkConfirmDialogMsg());
    	panel.add(label);
    	FlowPanel buttons = new FlowPanel();
    	buttons.setStyleName(MKPK.CSS.mkpkTextCenter());
    	
    	final Button okButton = new Button();
    	okButton.setStyleName(MKPK.CSS.mkpkConfirmDialogOkButton());
    	okButton.setText( MKPK.MSG.accept());
    	okButton.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
					hide();
					callback.onCancel();	
				}
			}
		});
    	okButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				okButton.setEnabled(false);
				hide();
				callback.onAccept();
			}
		});
    	buttons.add(okButton);
    	
    	final Button cancelButton = new Button();
    	cancelButton.setStyleName(MKPK.CSS.mkpkConfirmDialogCancelButton());
    	cancelButton.addStyleName(MKPK.CSS.mkpkMarginLeft());
    	cancelButton.setText( MKPK.MSG.cancel());
    	cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cancelButton.setEnabled(false);
				hide();
				callback.onCancel();
			}
		});
    	cancelButton.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
					hide();
					callback.onCancel();	
				}
			}
		});
    	addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				callback.onClose();
			}
		});
    	buttons.add(cancelButton);
    	panel.add(buttons);
    	root.setWidget(panel);
    	
    	center();
    	show();
    	
	    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	        public void execute() {
	        	okButton.setFocus(true);        	
	        }
	    });
    }
	
}
