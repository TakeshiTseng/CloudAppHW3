package tw.ttucse.cloudhw3.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FileServiceAsync {
	void getFilesWithParent(String parent, AsyncCallback<MyFile[]> callback) throws IllegalArgumentException;

	void getParents(AsyncCallback<MyFile[]> callback) throws IllegalArgumentException;

	void createFolder(String parent, String name, AsyncCallback<MyFile> callback)throws IllegalArgumentException;

	void deleteFile(MyFile file, AsyncCallback<MyFile> callback)throws IllegalArgumentException;

	void checkIfDefaultFileExist(User user, AsyncCallback<Void> callback)throws IllegalArgumentException;

	void editMyFile(MyFile myFile, AsyncCallback<MyFile> callback)throws IllegalArgumentException;

	void deleteSubFloder(String path, AsyncCallback<Void> callback) throws IllegalArgumentException;

	void getUploadURL(AsyncCallback<String> callback) throws IllegalArgumentException;

}