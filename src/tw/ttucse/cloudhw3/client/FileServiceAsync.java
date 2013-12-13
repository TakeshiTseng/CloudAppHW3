package tw.ttucse.cloudhw3.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FileServiceAsync {
	void getFilesWithParent(String parent, AsyncCallback<List<MyFile>> callback);

	void getParents(AsyncCallback<List<MyFile>> callback);

	void createFolder(String parent, String name, AsyncCallback<Boolean> callback);

	void deleteFile(MyFile file, AsyncCallback<Boolean> callback);

}
