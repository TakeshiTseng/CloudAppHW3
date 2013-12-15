package tw.ttucse.cloudhw3.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

public class UploadPanel extends DialogBox {

	final FormPanel form;
	final String path;
	private final FileServiceAsync fileServiceAsync = GWT
			.create(FileService.class);

	public UploadPanel(String path) {
		this.path = path;
		
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

		VerticalPanel panel = new VerticalPanel();
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
				System.out.println("Submit!");
				form.submit();
			}
		});
		
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				hide();
			}
		});
		panel.add(submitButton);
		flexTable.setWidget(0, 0, form);
	}

}
