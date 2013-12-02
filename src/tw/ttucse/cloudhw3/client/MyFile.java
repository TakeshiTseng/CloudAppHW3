package tw.ttucse.cloudhw3.client;

import java.io.Serializable;

import javax.jdo.annotations.Persistent;
import javax.persistence.Id;

import com.google.appengine.api.blobstore.BlobKey;

public class MyFile implements Serializable{
	private static final long serialVersionUID = -1954648572633792095L;
	
	@Id
	Long id;
	
	@Persistent
	String parent;
	
	@Persistent
	BlobKey fileKey;
	
	@Persistent
	String name;
	
	public MyFile(String filename, BlobKey fileKey) {
		this.name = filename;
		this.fileKey = fileKey;
	}
	
}
