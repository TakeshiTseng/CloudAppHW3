package tw.ttucse.cloudhw3.client;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class MyFile implements Serializable {
	private static final long serialVersionUID = -1954648572633792095L;
	public static final Integer TYPE_DIR = 0;
	public static final Integer TYPE_FILE = 1;
	
	@PrimaryKey 
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Long id;

	@Persistent
	String fileFolder;

	@Persistent
	String fileKey;

	@Persistent
	String name;

	@Persistent
	Integer fileType;

	public MyFile(String filename, String fileKey, String fileFolder,
			Integer fileType) {
		this.name = filename;
		this.fileKey = fileKey;
		this.fileFolder = fileFolder;
		this.fileType = fileType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileFolder() {
		return fileFolder;
	}
	
	public void setFileFolder(String fileFolder) {
		this.fileFolder = fileFolder;
	}

	public String getFileKey() {
		return fileKey;
	}
	
	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public Integer getFileType() {
		return fileType;
	}

}
