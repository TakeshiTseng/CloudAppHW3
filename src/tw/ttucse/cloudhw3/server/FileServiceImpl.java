package tw.ttucse.cloudhw3.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.Extent;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.w3c.css.sac.SiblingSelector;

import tw.ttucse.cloudhw3.client.FileService;
import tw.ttucse.cloudhw3.client.MyFile;
import tw.ttucse.cloudhw3.client.PMF;
import tw.ttucse.cloudhw3.client.User;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FileServiceImpl extends RemoteServiceServlet implements
		FileService {
	BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	public FileServiceImpl() {
		System.out.println("fileserviceImpl loading scuess");
	}

	private static final long serialVersionUID = 8169459258144511133L;

	@Override
	public MyFile[] getFilesWithParent(String parent)
			throws IllegalArgumentException {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();

		String queryString = "fileFolder == \'" + parent + "\'";
		@SuppressWarnings("unchecked")
		List<MyFile> files = (List<MyFile>) pm.newQuery(MyFile.class,
				queryString).execute();

		return files.toArray(new MyFile[files.size()]);
	}

	@Override
	public MyFile[] getParents() throws IllegalArgumentException {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		String queryString = "SELECT FROM " + MyFile.class.getName();
		try {
			@SuppressWarnings("unchecked")
			List<MyFile> dirs = (List<MyFile>) pm.newQuery(queryString)
					.execute();
			return dirs.toArray(new MyFile[dirs.size()]);
		} finally {
			pm.close();
		}
	}

	@Override
	public MyFile createFolder(String parent, String name)
			throws IllegalArgumentException {
		if (name.equals("")) {
			System.out.println("Create not OK!");
			return null;
		}
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		try {
			String query = "name == \'" + name + "\' && fileFolder == \'"
					+ parent + "\' && fileType == " + MyFile.TYPE_DIR;

			@SuppressWarnings("unchecked")
			List<MyFile> dirs = (List<MyFile>) pm.newQuery(MyFile.class, query)
					.execute();
			if (dirs.size() != 0) {
				System.out.println("Create not OK!");
				return null;
			} else {
				String query2 = "SELECT MAX(id) FROM " + MyFile.class.getName();
				Long ID = (Long) pm.newQuery(query2).execute();
				if (ID == null) {
					ID = new Long(1);
				}
				MyFile dir = new MyFile(name, null, parent, MyFile.TYPE_DIR);
				dir.setId(ID + 1);
				pm.makePersistent(dir);
				pm.flush();
				System.out.println("Create folder OK!");
				return dir;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			pm.close();
		}

	}

	@Override
	public MyFile deleteFile(MyFile file) throws IllegalArgumentException {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction transaction = pm.currentTransaction();
		Query query;
		List<MyFile> files;
		
		if (file.getName().equals("/") || file.getName().equals(".")) {
			return null;
		}
		
		query = pm.newQuery("select from " + MyFile.class.getName() + " where id == " + file.getId());
		files = (List<MyFile>) query.execute();
		if(files.size() == 1){
			file = files.get(0);
		}
		
		if(file.getFileType() == MyFile.TYPE_DIR){
			String queryStatement = "SELECT FROM " + MyFile.class.getName();
			query = pm.newQuery(queryStatement);
			
			files = (List<MyFile>) query.execute();
			String path = file.getFileFolder() + "/" + file.getName();
			System.out.println("Delete Path : " + path);
			transaction.begin();
			for(MyFile myFile : files){
				if (myFile.getFileFolder().startsWith(path)) {
					pm.deletePersistent(myFile);
				}
			}
			pm.deletePersistent(file);
			transaction.commit();
		} else {
			transaction.begin();
			pm.deletePersistent(file);
			transaction.commit();
		}

		return file;
	}

	@Override
	public void checkIfDefaultFileExist(User user) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		System.out.println("Check If Default File Exist");
		String query = "name==\'" + user.getAccount() + "\' && fileFolder==\'"
				+ "." + "\'";
		@SuppressWarnings("unchecked")
		List<MyFile> myFiles = (List<MyFile>) pm.newQuery(MyFile.class, query)
				.execute();
		if (myFiles.isEmpty()) {
			System.out.println("Default myFiles not exist, add folder....");
			MyFile newFile = new MyFile(user.getAccount(), null, ".",
					MyFile.TYPE_DIR);
			String query2 = "SELECT MAX(id) FROM " + MyFile.class.getName();
			Long ID = (Long) pm.newQuery(query2).execute();
			if (ID == null) {
				ID = new Long(1);
			}
			newFile.setId(ID);
			try {
				pm.makePersistent(newFile);
				pm.flush();
			} finally {
				pm.close();
			}
		} else {
			System.out.println("Default file exist");
		}
		System.out.println("Done\n");
	}

	@Override
	public MyFile editMyFile(MyFile newfile) throws IllegalArgumentException {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction transaction = pm.currentTransaction();
		MyFile oldfile = null;
		String oldParentString = null;
		String newParentString = newfile.getFileFolder() + "/"
				+ newfile.getName()+"/";

		try {
			Extent<MyFile> ext = pm.getExtent(MyFile.class, false);
			String str = "id==" + newfile.getId();
			Query qry = pm.newQuery(ext, str);
			Collection<?> c = (Collection<?>) qry.execute();
			Object obj = c.iterator().next();
			oldfile = (MyFile) obj;
			oldParentString = oldfile.getFileFolder() + "/" + oldfile.getName()+"/";

			oldfile.setName(newfile.getName());
			oldfile.setFileFolder(newfile.getFileFolder());

			transaction.begin();
			pm.makePersistent(obj);
			transaction.commit();
			System.out.println("Modify user success.");
		} catch (JDOUserException ex) {
			ex.printStackTrace();
			transaction.rollback();

		} finally {
			pm.close();
		}
		
		modifySubFloder(oldParentString,newParentString);
		return oldfile;
	}
	
	@Override
	public void deleteSubFloder(String path) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		MyFile[] myFilesattay;
		String queryString = "SELECT FROM " + MyFile.class.getName();
		try {
			@SuppressWarnings("unchecked")
			List<MyFile> dirs = (List<MyFile>) pm.newQuery(queryString)
					.execute();
			myFilesattay = dirs.toArray(new MyFile[dirs.size()]);
			ArrayList<MyFile> myFileslist = new ArrayList<MyFile>();
			System.out.println(1);
			for (MyFile myFile : myFilesattay) {
				System.err.println(path+" "+myFile.getFileFolder()+"/");
				if ((myFile.getFileFolder()+"/"+myFile.getName()+"/").startsWith(path)) {
					myFileslist.add(myFile);
				}
			}
			for (MyFile myFile : myFileslist) {
				deleteFile(myFile);
			}
		} finally {
			pm.close();
		}
	}

	public void modifySubFloder(String oldFolderString, String newFolderString) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		MyFile[] myFilesattay;
		String queryString = "SELECT FROM " + MyFile.class.getName();
		try {
			@SuppressWarnings("unchecked")
			List<MyFile> dirs = (List<MyFile>) pm.newQuery(queryString)
					.execute();
			myFilesattay = dirs.toArray(new MyFile[dirs.size()]);
			ArrayList<MyFile> myFileslist = new ArrayList<MyFile>();
			int oldFolderStringLen = oldFolderString.length();
			for (MyFile myFile : myFilesattay) {
				if ((myFile.getFileFolder()+"/").startsWith(oldFolderString)) {
					myFileslist.add(myFile);
				}
			}
			Transaction transaction = pm.currentTransaction();

			for (MyFile myFile : myFileslist) {
				String folderString = myFile.getFileFolder()+"/";
				folderString = newFolderString
						+ folderString.substring(oldFolderStringLen);
				folderString=folderString.substring(0,folderString.length()-1);
				myFile.setFileFolder(folderString);

				try {
					transaction.begin();
					pm.makePersistent(myFile);
					transaction.commit();
				} catch (Exception ex) {
					ex.printStackTrace();
					transaction.rollback();
				}
			}
		} finally {
			pm.close();
		}
	}

	@Override
	public String getUploadURL() throws IllegalArgumentException {
		String url = blobstoreService.createUploadUrl("/cloudapphw3/upload");
		return url;
	}

}
