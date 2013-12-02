package tw.ttucse.cloudhw1.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {
	void addUser(User user, AsyncCallback<Boolean> callBack)throws IllegalArgumentException;
	void editUser(User user, AsyncCallback<Boolean> callBack)throws IllegalArgumentException;
	void getUser(String account, AsyncCallback<User> callBack) throws IllegalArgumentException;
	void deleteUser(User user, AsyncCallback<Boolean> callBack) throws IllegalArgumentException;
	void getUsers(AsyncCallback<User[]> callback)throws IllegalArgumentException;
}
