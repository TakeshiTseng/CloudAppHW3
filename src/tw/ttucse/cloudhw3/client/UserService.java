package tw.ttucse.cloudhw3.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService{
	boolean addUser(User user) throws IllegalArgumentException;
	boolean editUser(User user) throws IllegalArgumentException;
	User getUser(String account) throws IllegalArgumentException;
	boolean deleteUser(User user) throws IllegalArgumentException;
	User[] getUsers() throws IllegalArgumentException;
}
