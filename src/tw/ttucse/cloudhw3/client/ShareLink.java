package tw.ttucse.cloudhw3.client;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class ShareLink implements Serializable {
	private static final long serialVersionUID = 162117174826921425L;

	@PrimaryKey 
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Long id;

	@Persistent
	String shareName;
	
	@Persistent
	String owner;
	
	@Persistent
	ArrayList<Long> filesIDList;
	
	public ShareLink() {
		filesIDList = new ArrayList<Long>();
	}

	public ShareLink(String name,String owner){
		this();
		shareName = name;
		this.owner=owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShareName() {
		return shareName;
	}

	public void setShareName(String shareName) {
		this.shareName = shareName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public ArrayList<Long> getFilesIDList() {
		return filesIDList;
	}

	public void setFilesIDList(ArrayList<Long> filesID) {
		this.filesIDList = filesID;
	}
}
