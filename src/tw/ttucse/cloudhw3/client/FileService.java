package tw.ttucse.cloudhw3.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("file")
public interface FileService extends RemoteService {
	MyFile[] getFilesWithParent(String parent)throws IllegalArgumentException;

	MyFile[] getParents() throws IllegalArgumentException;

	MyFile createFolder(String parent, String name)throws IllegalArgumentException;

	MyFile deleteFile(MyFile file) throws IllegalArgumentException;
	
	void checkIfDefaultFileExist(User user) throws IllegalArgumentException;

	MyFile editMyFile(MyFile myFile) throws IllegalArgumentException;
	
	void deleteSubFloder(String path) throws IllegalArgumentException;
	
	String getUploadURL() throws IllegalArgumentException;
}
