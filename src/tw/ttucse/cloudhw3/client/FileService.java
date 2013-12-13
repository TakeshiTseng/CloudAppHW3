package tw.ttucse.cloudhw3.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface FileService extends RemoteService{
	List<MyFile> getFilesWithParent(String parent) throws IllegalArgumentException;
	List<MyFile> getParents() throws IllegalArgumentException;
	Boolean createFolder(String name) throws IllegalArgumentException;
	Boolean deleteFile(MyFile file) throws IllegalArgumentException;
}
