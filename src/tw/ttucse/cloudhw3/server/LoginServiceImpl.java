package tw.ttucse.cloudhw3.server;

import java.util.List;

import javax.jdo.PersistenceManager;

import tw.ttucse.cloudhw3.client.LoginService;
import tw.ttucse.cloudhw3.client.PMF;
import tw.ttucse.cloudhw3.client.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService{
	private static final long serialVersionUID = -384389817920530498L;
	
	public LoginServiceImpl() {
	}
	
	public void init() {
		System.out.println("Checking if default user exitst");		
		checkIfDefaultUserExist();
	}
	
	
	private void checkIfDefaultUserExist(){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		String query = "SELECT FROM " + User.class.getName();
		@SuppressWarnings("unchecked")
		List<User> users = (List<User>) pm.newQuery(query).execute();
		if(users.isEmpty()){
			System.out.println("Default user not exist, add admin/admin....");
			User user = new User("admin", "admin");
			user.setUsername("admin");
			user.setID(new Long(1));
			try{
				pm.makePersistent(user);
			} finally{
				pm.close();
			}
		} else {
			System.out.println("Default user exist");
		}
		System.out.println("Done\n");
	}

	@Override
	public boolean login(String account, String password)
			throws IllegalArgumentException {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		String query = "SELECT FROM " + User.class.getName() + " WHERE account == \'" + account + "\'";
		
		try{
			@SuppressWarnings("unchecked")
			List<User> users = (List<User>) pm.newQuery(query).execute();
			if(users.isEmpty()){
				return false;
			} else {
				String pwd = users.get(0).getPassword();
				return password.equals(pwd);
			}
		} finally{
			pm.close();
		}
		
	}

}
