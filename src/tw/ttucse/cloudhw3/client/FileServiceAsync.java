package tw.ttucse.cloudhw3.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FileServiceAsync {
	void getFilesWithParent(String parent, AsyncCallback<MyFile[]> callback)
			throws IllegalArgumentException;

	void getParents(AsyncCallback<MyFile[]> callback)
			throws IllegalArgumentException;

	void createFolder(String parent, String name, AsyncCallback<MyFile> callback)
			throws IllegalArgumentException;

	void deleteFile(MyFile file, AsyncCallback<MyFile> callback)
			throws IllegalArgumentException;

	void deleteFloder(MyFile file, AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;

	void checkIfDefaultFileExist(User user, AsyncCallback<Void> callback)
			throws IllegalArgumentException;

	void editMyFile(MyFile myFile, AsyncCallback<MyFile> callback)
			throws IllegalArgumentException;

	void getUploadURL(AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void getNewID(AsyncCallback<Long> callback) throws IllegalArgumentException;

	void getFileWithID(Long ID, AsyncCallback<MyFile> callback)
			throws IllegalArgumentException;

	void getFileWithID(Long ID, boolean isSure, AsyncCallback<MyFile> callback)
			throws IllegalArgumentException;

	void getFileWithName(String name, String parent,
			AsyncCallback<MyFile> callback) throws IllegalArgumentException;

	void getFileWithName(String name, String parent, boolean isSure,
			AsyncCallback<MyFile> callback) throws IllegalArgumentException;
}