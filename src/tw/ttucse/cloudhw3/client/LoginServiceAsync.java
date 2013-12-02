package tw.ttucse.cloudhw1.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {

	void login(String username, String password, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	void init(AsyncCallback<Void> callback);
}
