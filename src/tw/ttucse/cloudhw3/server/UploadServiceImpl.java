package tw.ttucse.cloudhw3.server;

import java.io.IOException;
import java.util.*;

import javax.jdo.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.google.appengine.api.blobstore.*;
import tw.ttucse.cloudhw3.client.*;

public class UploadServiceImpl extends HttpServlet {
	private static final long serialVersionUID = 624911565554458385L;
	BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	PersistenceManager pm = PMF.getInstance().getPersistenceManager();
	
	@Override
	public void init() throws ServletException {
		checkIfDefaultFileExist();
	}
	private void checkIfDefaultFileExist(){
		System.out.println("Check If Default File Exist");
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		String query = "SELECT FROM " + MyFile.class.getName();
		@SuppressWarnings("unchecked")
		List<MyFile> myFiles = (List<MyFile>) pm.newQuery(query).execute();
		if(myFiles.isEmpty()){
			System.out.println("Default myFiles not exist, add root....");
			MyFile newFile = new MyFile("/", null, null, MyFile.TYPE_DIR);
			
			try{
				pm.makePersistent(newFile);
				pm.flush();
			} finally{
				pm.close();
			}
		} else {
			System.out.println("Default file exist");
		}
		System.out.println("Done");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String url = blobstoreService.createUploadUrl("/cloudapphw3/upload");
		resp.setContentType("text/html");
		resp.getWriter().println("<html><body>");
		resp.getWriter().println("<form method=\"POST\" enctype=\"multipart/form-data\" action=\""+url+"\">");
		resp.getWriter().println("File to upload: <input type=\"file\" name=\"myFile\"><br>");
		resp.getWriter().println("Parent: <input type=\"text\" name=\"parent\" value=\"/\"><br>");
		resp.getWriter().println("<br>");
		resp.getWriter().println("<input type=\"submit\" value=\"Press\"> to upload the file!");
		resp.getWriter().println("</form></body></html>");
		
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Map<String, List<BlobInfo>> blobInfoMap = blobstoreService.getBlobInfos(req);
		
		System.out.println(blobInfoMap);
		List<BlobInfo> infos = blobInfoMap.get("myFile");
		
		if(infos.size() > 0){
			BlobInfo blobInfo = infos.get(0);
			
			String filename = blobInfo.getFilename();
			System.out.println("Upload : " + filename);
			String fileKey = blobInfo.getBlobKey().getKeyString();
			String fileParent = req.getParameter("parent");
			MyFile myFile = new MyFile(filename, fileKey, fileParent, MyFile.TYPE_FILE);
			String queryStatment = "SELECT FROM " + MyFile.class.getName() + " WHERE name == \'"+filename+"\'";//"\' AND fileFolder == \'" + fileParent + "\';";
			Query query = pm.newQuery(queryStatment);
			
			@SuppressWarnings("unchecked")
			List<MyFile> queryResult = (List<MyFile>) query.execute();
			
			if(queryResult.size() == 0){
				queryStatment = "SELECT FROM " + MyFile.class.getName() ;
				query = pm.newQuery(queryStatment);
				
				queryResult = (List<MyFile>) query.execute();
				myFile.setId((long) (queryResult.size() +1));
				
				pm.makePersistent(myFile);
				pm.flush();
			}
			resp.setContentType("text/html");
			resp.getWriter().println("Upload OK");
		} else {
			resp.setContentType("text/html");
			resp.getWriter().println("File Error!");
		}
		
	}

}
