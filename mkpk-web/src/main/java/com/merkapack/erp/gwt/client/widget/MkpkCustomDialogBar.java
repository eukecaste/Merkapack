package com.merkapack.erp.gwt.client.widget;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.merkapack.erp.gwt.client.common.MKPK;

public class MkpkCustomDialogBar extends Composite {
	
	public static interface Listener {
		void onClose();
	}

	private Label captionLabel;
	private Button closeButton;
	private List<Listener> listeners;
	
	public MkpkCustomDialogBar() {
		FlowPanel p1 = new FlowPanel();
		p1.setStyleName(MKPK.CSS.mkpkCDPanel());
		FlowPanel p2 = new FlowPanel();
		p2.setStyleName(MKPK.CSS.mkpkCDHeader());
		p1.add(p2);
		FlowPanel p3 = new FlowPanel();
		p3.setStyleName(MKPK.CSS.mkpkCDTitle());
		captionLabel = new Label();
		p3.add(captionLabel);
		
		closeButton = new Button();
		closeButton.setStyleName(MKPK.CSS.mkpkCDClose());
		p3.add(closeButton);
		p2.add(p3);
		initWidget(p1);
		listeners = new LinkedList<Listener>();
		closeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireOnClose();
			}
		});
	}
	
	public String getCaption(){
		return captionLabel.getText();
	}

	public void setCaption(String caption) {
		captionLabel.setText(caption);
	}
	
	public void addCloseHandler(Listener listener){
		listeners.add(listener);
	}

	public void removeCloseHandler(Listener listener){
		listeners.remove(listener);
	}
	

	
	
	
	private void fireOnClose(){
		for (Listener listener : listeners) {
			listener.onClose();
		}
	}

}
