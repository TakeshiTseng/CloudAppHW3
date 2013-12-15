package tw.ttucse.cloudhw3.server;

import java.util.Collection;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import tw.ttucse.cloudhw3.client.PMF;
import tw.ttucse.cloudhw3.client.User;
import tw.ttucse.cloudhw3.client.UserService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {
	private static final long serialVersionUID = -2554257850851305233L;

	public UserServiceImpl() {
		System.out.println("userserviceImpl loading scuess");
	}

	@Override
	public boolean addUser(User user) throws IllegalArgumentException {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		String query = "SELECT FROM " + User.class.getName() + " WHERE account == \'" + user.getAccount() + "\'";
		
		try{
			@SuppressWarnings("unchecked")
			List<User> users = (List<User>) pm.newQuery(query).execute();
			if(users.isEmpty()){
				String query2 = "SELECT MAX(ID) FROM " + User.class.getName();
				Long ID = (Long) pm.newQuery(query2).execute();
				if(ID==null){
					ID=new Long(1);
				}
				user.setID(ID+1);
				pm.makePersistent(user);
				pm.flush();
			} else {
				return false;
			}
		} finally{
			pm.close();
		}
		System.out.println("add user service success");
		return true;
	}

	@Override
	public boolean editUser(User user) throws IllegalArgumentException {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction transaction = pm.currentTransaction();
		if(user.getAccount().equals("admin")){
			return false;
		}
		try {
			transaction.begin();
			Extent<User> ext = pm.getExtent(User.class, false);
			String str ="account==\""+user.getAccount()+"\"";
			Query qry= pm.newQuery(ext, str); 
			Collection<?> c = (Collection<?>) qry.execute();
			Object obj = c.iterator().next();
			((User)obj).setPassword(user.getPassword());
			((User)obj).setUsername(user.getUsername());
			pm.makePersistent(obj);
			transaction.commit();
		} catch(JDOUserException ex){
			transaction.rollback();
			throw ex;
		}finally {
			pm.close();
		}
		System.out.println("Modify user success.");
		return true;
	}

	@Override
	public User getUser(String account) throws IllegalArgumentException {
		Object obj;
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction transaction = pm.currentTransaction();
		try {
			transaction.begin();
			Extent<User> ext = pm.getExtent(User.class, false);
			String str ="account==\""+account+"\"";
			Query qry= pm.newQuery(ext, str); 
			Collection<?> c = (Collection<?>) qry.execute();
			obj = c.iterator().next();
			transaction.commit();
		} catch(JDOUserException ex){
			transaction.rollback();
			throw ex;
		}finally {
			pm.close();
		}
		return (User)obj;
	}

	@Override
	public boolean deleteUser(User user) throws IllegalArgumentException {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction transaction = pm.currentTransaction();
		if(user.getAccount().equals("admin")){
			return false;
		}
		try {
			transaction.begin();
			Extent<User> ext = pm.getExtent(User.class, false);
			String str ="account==\""+user.getAccount()+"\"";
			Query qry= pm.newQuery(ext, str); 
			Collection<?> c = (Collection<?>) qry.execute();
			Object obj = c.iterator().next();
			pm.deletePersistent (obj);
			transaction.commit();
		} catch(JDOUserException ex){
			transaction.rollback();
			throw ex;
		}finally {
			pm.close();
		}
		System.out.println("Remove user success.");
		return true;
	}

	@Override
	public User[] getUsers() throws IllegalArgumentException {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		String query = "SELECT FROM " + User.class.getName();

		try {
			@SuppressWarnings("unchecked")
			List<User> users = (List<User>) pm.newQuery(query).execute();
			return users.toArray(new User[users.size()]);
		} finally {
			pm.close();
		}
	}

}
