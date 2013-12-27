package tw.ttucse.cloudhw3.client;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import tw.ttucse.cloudhw3.client.File.FileType;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class UploadPanel extends Composite {
	final Tree folderTree;
	final FormPanel form;
	private final BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	private final FileServiceAsync fileServiceAsync = GWT
			.create(FileService.class);

	public UploadPanel() {

		ArrayList<MyFile> folders = new ArrayList<>();
		MyFile rootFile = new MyFile(".", null, null, FileType.DIR);
		folders.add(rootFile);

		FlexTable flexTable = new FlexTable();
		initWidget(flexTable);
		flexTable.setSize("592px", "444px");

		Label label_1 = new Label("選擇目錄");
		flexTable.setWidget(0, 0, label_1);

		// 建立樹狀圖，讓使用者選擇上傳目錄
		folderTree = new Tree();
		flexTable.setWidget(0, 1, folderTree);

		TreeItem root = new TreeItem();
		root.setText("檔案系統");

		folderTree.addItem(root);
		updateTree(root, folders.toArray(new MyFile[folders.size()]));
		// 建立樹狀圖，讓使用者選擇上傳目錄

		// 建立上傳的 Form
		form = new FormPanel();

		// 取得上傳路徑
		String url = blobstoreService.createUploadUrl("/cloudapphw3/upload");
		form.setAction(url);
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		// 之後把它改成 hidden 屬性，再選擇樹狀目錄時去設定他
		final TextBox tb = new TextBox();
		tb.setName("parent");
		panel.add(tb);

		// 檔案選擇的元件
		FileUpload upload = new FileUpload();
		upload.setName("uploadFormElement");
		panel.add(upload);

		// 送出按鈕
		Button submitButton = new Button("Submit");

		// 設定按下後的動作
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				form.submit();
			}
		});
		panel.add(submitButton);

	}

	/**
	 * 用遞迴的方式建立樹狀圖
	 * 
	 * @param treeItem
	 * @param folders
	 */
	private void updateTree(TreeItem treeItem, MyFile[] folders) {
		for (MyFile folder : folders) {
			if (folder.getType() == FileType.FILE) {
				continue;
			}
			final TreeItem item = new TreeItem();
			item.setText(folder.getName());
			treeItem.addItem(item);

			fileServiceAsync.getFilesWithParent(folder.getName(),
					new AsyncCallback<MyFile[]>() {

						@Override
						public void onSuccess(MyFile[] result) {
							updateTree(item, result);
						}

						@Override
						public void onFailure(Throwable caught) {
							Logger.getLogger("Error").log(
									Level.WARNING,
									"Update Tree Failure, reason : "
											+ caught.getMessage());
							caught.printStackTrace();
						}
					}
			);
		}
	}
}
