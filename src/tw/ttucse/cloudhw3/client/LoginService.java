package tw.ttucse.cloudhw1.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	boolean login(String username, String password) throws IllegalArgumentException;
	void init();
}
