package tw.ttucse.cloudhw3.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.BlobstoreService;

public class DownloadServiceImpl extends HttpServlet {
	private static final long serialVersionUID = -6127281556275619023L;
	private final BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	private final BlobInfoFactory bif = new BlobInfoFactory();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String blobKeyString = req.getParameter("blob-key");
		String fileName = req.getParameter("fileName");
		BlobKey blobKey = new BlobKey(blobKeyString);
		BlobInfo blobInfo = bif.loadBlobInfo(blobKey);
		fileName = blobInfo.getFilename();
		String contentType = blobInfo.getContentType();
		// resp.setContentType( "application/octet-stream" );
		resp.setContentType(contentType);
		resp.addHeader("Content-Disposition", "filename=\"" + fileName + "\"");

		blobstoreService.serve(blobKey, resp);
	}

}
