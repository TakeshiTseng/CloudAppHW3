package tw.ttucse.cloudhw3.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UploadPanelDialogBox extends DialogBox {

	final FormPanel form;
	final MyFile folder;
	final VerticalPanel panel;
	private Long ID;
	private final FileServiceAsync fileServiceAsync = GWT
			.create(FileService.class);

	public UploadPanelDialogBox(MyFile dir) {
		folder = dir;
		String path = folder.getFileFolder()+"/"+folder.getName();
		
		FlexTable flexTable = new FlexTable();
		setWidget(flexTable);
		flexTable.setSize("592px", "444px");

		// 建立上傳的 Form
		form = new FormPanel();
		
		// 取得上傳路徑
		fileServiceAsync.getUploadURL(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {}

			@Override
			public void onSuccess(String result) {
				form.setAction(result);
				form.setEncoding(FormPanel.ENCODING_MULTIPART);
				form.setMethod(FormPanel.METHOD_POST);
			}
		});

		panel = new VerticalPanel();
		form.setWidget(panel);

		// 之後把它改成 hidden 屬性，再選擇樹狀目錄時去設定他
		Hidden parent = new Hidden("parent", path);
		panel.add(parent);

		// 檔案選擇的元件
		FileUpload upload = new FileUpload();
		upload.setName("myFile");
		panel.add(upload);

		// 送出按鈕
		Button submitButton = new Button("Submit");

		// 設定按下後的動作
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fileServiceAsync.getNewID(new AsyncCallback<Long>() {

					@Override
					public void onFailure(Throwable caught) {
						System.out.println("Submit failure,get new id failure");
						hide();
					}

					@Override
					public void onSuccess(Long result) {
						System.out.println("Submit!");
						Hidden parent = new Hidden("ID", ""+result);
						ID=result;
						panel.add(parent);
						form.submit();
					}
				});
			}
		});
		
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				hide();
				fileServiceAsync.getFileWithID(ID,true, new AsyncCallback<MyFile>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(MyFile result) {
						FileSystemUserInterfacePane fileSystemUserInterfacePane = FileSystemUserInterfacePane.getInstance();
						fileSystemUserInterfacePane.addRow(result);
						fileSystemUserInterfacePane.getFileList().add(result);
					}
				});
			}
		});
		panel.add(submitButton);
		
		Button btnNewButton = new Button("Close");
		btnNewButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		panel.add(btnNewButton);
		flexTable.setWidget(0, 0, form);
	}

}
