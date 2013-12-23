package tw.ttucse.cloudhw3.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.geronimo.mail.util.Base64;

import tw.ttucse.cloudhw3.client.MyFile;
import tw.ttucse.cloudhw3.client.PMF;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

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
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Map<String, List<BlobInfo>> blobInfoMap = blobstoreService.getBlobInfos(req);
		
		System.out.println(blobInfoMap);
		List<BlobInfo> infos = blobInfoMap.get("myFile");
		
		if(infos.size() > 0){
			BlobInfo blobInfo = infos.get(0);
			
			String filename = blobInfo.getFilename();
			
			if(filename.startsWith("=?UTF-8?B?")){
				filename = filename.substring(10, filename.length() - 2);
				byte[] decodedData = Base64.decode(filename);
				filename = new String(decodedData);
			}
			
			System.out.println("Upload : " + filename);
			String fileKey = blobInfo.getBlobKey().getKeyString();
			String fileParent = req.getParameter("parent");
			Long ID = Long.parseLong(req.getParameter("ID"));
			MyFile myFile = new MyFile(filename, fileKey, fileParent, MyFile.TYPE_FILE);
			myFile.setId(ID);
			String queryStatment = "SELECT FROM " + MyFile.class.getName() + " WHERE name == \'"+filename+"\' && fileFolder == \'" + fileParent + "\' && id=="+ID;
			System.out.println("queryStatment : " + queryStatment);
			Query query = pm.newQuery(queryStatment);
			
			List<MyFile> queryResult = (List<MyFile>) query.execute();
			System.out.println("Result size : " + queryResult.size());
			if(queryResult.size() == 0){				
				pm.makePersistent(myFile);
				pm.flush();
				resp.setContentType("text/html");
				resp.getWriter().println("Upload OK");
			} else {
				resp.setContentType("text/html");
				resp.getWriter().println("Error!");
			}
			
		} else {
			resp.setContentType("text/html");
			resp.getWriter().println("File Error!");
		}
		
		resp.flushBuffer();
	}

}
