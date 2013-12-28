package tw.ttucse.cloudhw3.client;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class MyFile extends File implements Serializable {
	private static final long serialVersionUID = -1954648572633792095L;
	
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
	FileType filetype;
	
	public MyFile() {
		// TODO Auto-generated constructor stub
	}

	public MyFile(String filename, String fileKey, String fileFolder,
			FileType fileType) {
		this.name = filename;
		this.fileKey = fileKey;
		this.fileFolder = fileFolder;
		setType(fileType);
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
	
	public static String  getTypeString(MyFile myFile) {;
		if (myFile.getType() ==FileType.DIR) {
			return "Floder";
		}else if (myFile.getType() == FileType.FILE) {
			return "File";
		}
		return null;
	}
	
	public String getTypeName() {
		return getTypeString(this);
	}

	@Override
	public FileType getType() {
		return filetype;
	}

	@Override
	public void setType(FileType type) {
		filetype=type;
		super.setType(type);
	}
}
