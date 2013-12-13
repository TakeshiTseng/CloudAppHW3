package tw.ttucse.cloudhw3.server;

import java.util.Collection;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import tw.ttucse.cloudhw3.client.FileService;
import tw.ttucse.cloudhw3.client.MyFile;
import tw.ttucse.cloudhw3.client.PMF;
import tw.ttucse.cloudhw3.client.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FileServiceImpl extends RemoteServiceServlet implements
		FileService {

	private static final long serialVersionUID = 8169459258144511133L;
	PersistenceManager pm = PMF.getInstance().getPersistenceManager();

	@Override
	public List<MyFile> getFilesWithParent(String parent)
			throws IllegalArgumentException {

		String queryString = "select * from " + MyFile.class.getName()
				+ "where fileFolder == \'" + parent + "\'";
		@SuppressWarnings("unchecked")
		List<MyFile> files = (List<MyFile>) pm.newQuery(queryString).execute();

		return files;
	}

	@Override
	public List<MyFile> getParents() throws IllegalArgumentException {
		String queryString = "select * from " + MyFile.class.getName()
				+ "where fileType == \'" + MyFile.TYPE_DIR + "\'";
		@SuppressWarnings("unchecked")
		List<MyFile> dirs = (List<MyFile>) pm.newQuery(queryString).execute();
		return dirs;
	}

	@Override
	public Boolean createFolder(String parent, String name)
			throws IllegalArgumentException {
		try {
			String queryString = "select * from " + MyFile.class.getName()
					+ "where name == \'" + name + "\' AND fileType == \'"
					+ MyFile.TYPE_DIR + "\'";

			@SuppressWarnings("unchecked")
			List<MyFile> dirs = (List<MyFile>) pm.newQuery(queryString)
					.execute();
			if (dirs.size() != 0) {
				return false;
			} else {
				MyFile dir = new MyFile(name, null, parent, MyFile.TYPE_DIR);
				pm.makePersistent(dir);
				pm.flush();
				return true;
			}

		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public Boolean deleteFile(MyFile file) throws IllegalArgumentException {
		Transaction transaction = pm.currentTransaction();
		if (file.getName().equals("/")) {
			return false;
		}
		try {
			transaction.begin();
			Extent<MyFile> ext = pm.getExtent(MyFile.class, false);
			String str = "name==\"" + file.getName() + "\"";
			Query qry = pm.newQuery(ext, str);
			Collection<?> c = (Collection<?>) qry.execute();
			Object obj = c.iterator().next();
			pm.deletePersistent(obj);
			transaction.commit();
			return true;
		} catch (JDOUserException ex) {
			transaction.rollback();
			throw ex;
		}

	}

}
