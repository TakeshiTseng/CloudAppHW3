package tw.ttucse.cloudhw3.client;

import java.io.Serializable;

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
	String idstr;
	
	public ShareLink() {
		idstr="";
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

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}
	
	public void addID(Long ID) {
		idstr=idstr+ID+",";
	}
	
	public void remove(Long ID) {
		if(idstr.startsWith(ID+",")){
			idstr=idstr.split(ID+",")[1];
			return;
		}
		String[] IDs = idstr.split(","+ID+",");
		if (IDs.length==1) {
			idstr=IDs[0];
		}else{
			idstr=IDs[0]+","+IDs[1];
		}
		
		return;
	}
	
	@Override
	public String toString() {
		return "{"+idstr.substring(0, idstr.length()-1)+"}";
	}
}
