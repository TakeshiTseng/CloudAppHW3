package tw.ttucse.cloudhw3.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("file")
public interface FileService extends RemoteService {
	MyFile[] getFilesWithParent(String parent) throws IllegalArgumentException;

	MyFile[] getParents() throws IllegalArgumentException;

	MyFile createFolder(String parent, String name)
			throws IllegalArgumentException;

	MyFile deleteFile(MyFile file) throws IllegalArgumentException;

	boolean deleteFloder(MyFile file) throws IllegalArgumentException;

	void checkIfDefaultFileExist(User user) throws IllegalArgumentException;

	MyFile editMyFile(MyFile myFile) throws IllegalArgumentException;

	String getUploadURL() throws IllegalArgumentException;

	Long getNewID() throws IllegalArgumentException;

	MyFile getFileWithID(Long ID) throws IllegalArgumentException;

	MyFile[] getFileWithID(Long[] IDs) throws IllegalArgumentException;

	MyFile getFileWithID(Long ID, boolean isSure)
			throws IllegalArgumentException;

	MyFile getFileWithName(String name, String parent)
			throws IllegalArgumentException;

	MyFile getFileWithName(String name, String parent, boolean isSure)
			throws IllegalArgumentException;

	ShareLink addFileToShareLink(Long[] fileId, String shareLinkName,
			String owner) throws IllegalArgumentException;

	ShareLink[] getShareLinks() throws IllegalArgumentException;

	ShareLink[] getShareLinksWithOwner(String owner)
			throws IllegalArgumentException;

	ShareLink[] getShareLinksWithShareName(String keyword)
			throws IllegalArgumentException;
}
