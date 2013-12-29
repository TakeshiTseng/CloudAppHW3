package tw.ttucse.cloudhw3.client;

import java.io.Serializable;

public class File implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5559011176579547752L;
	private FileType type;
	
	public FileType getType() {
		return type;
	}

	public void setType(FileType type) {
		this.type = type;
	}

	public File() {
	}
	
	public File(FileType type) {
		setType(type);
	}
	public enum FileType {
		SHARELINK,DIR,FILE
	}
}