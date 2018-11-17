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
import com.merkapack.watson.util.MkpkStringUtils;

public class MkpkMessageDialog extends MkpkCustomDialog {
    
	public static interface MkpkMessageDialogCallback {
		void onClose();
	}

	
	private static MkpkMessageDialog INSTANCE;
	 
	private SimpleLayoutPanel root;
	private MkpkMessageDialog() {
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
	private SimpleLayoutPanel getRoot() {
		return root;
	}
	private static MkpkMessageDialog getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MkpkMessageDialog();
		}
		return INSTANCE;
	}
	
	public static void error(String msg) {
		show("ERROR",msg);
	}
	public static void warning (final String msg) {
		show("AVISO",msg);
	}
	
    public static void show(String msg) {
    	show(msg, (String) null);
    }
	
    public static void show(String header,String msg) {
    	show(header,msg, null);
    }

    public static void show(String msg, final MkpkMessageDialogCallback callback) {
    	show(null, msg, callback);
    }
    
	public static void show(String header,String msg, final MkpkMessageDialogCallback callback) {
		final MkpkMessageDialog md = getInstance();
		md.setCaption( MkpkStringUtils.defaultIfBlank(header, "Mensaje"));
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
					md.hide();
					if (callback != null) callback.onClose();	
				}
			}
		});
    	okButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				okButton.setEnabled(false);
				md.hide();
				if (callback != null) callback.onClose();
			}
		});
    	buttons.add(okButton);
    	
    	md.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				if (callback != null) callback.onClose();
			}
		});
    	panel.add(buttons);
    	md.getRoot().setWidget(panel);
    	
    	md.center();
    	md.show();
    	
	    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	        public void execute() {
	        	okButton.setFocus(true);        	
	        }
	    });
    }
	
}
