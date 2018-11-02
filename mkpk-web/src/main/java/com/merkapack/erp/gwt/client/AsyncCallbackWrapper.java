package com.merkapack.erp.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.merkapack.erp.gwt.client.common.MKPK;

public class AsyncCallbackWrapper<T> implements AsyncCallback<T> {

	private AsyncCallback<T> asyncCallback;

	public AsyncCallbackWrapper(AsyncCallback<T> asyncCallback) {
		this.asyncCallback = asyncCallback;
	}

	@Override
	public void onSuccess(T result) {
		asyncCallback.onSuccess(result);
		MKPK.stop();
	}

	@Override
	public void onFailure(Throwable caught) {
		asyncCallback.onFailure(caught);
		MKPK.fail();
	}
}