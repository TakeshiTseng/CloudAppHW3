package tw.ttucse.cloudhw3.client;

import java.util.ArrayList;

import tw.ttucse.cloudhw3.client.File.FileType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class FloderFormDialogBox extends DialogBox implements ClickHandler {
	private FloderFormDialogBox thisPanel;
	private TextBox myfilename;
	private Label errorMesssage;
	private Button registerButton;
	private Type type;
	private MyFile floder;
	private MyFile modifyfileFile=null;
	private Integer modifyIndex=null;
	
	enum Type {
		Create, Modify;
	}

	public FloderFormDialogBox(Type type, MyFile myFile,Integer index) {

		FlexTable flexTable = new FlexTable();
		setWidget(flexTable);
		flexTable.setSize("371px", "240px");

		Label label = new Label("請輸入名稱");
		flexTable.setWidget(0, 0, label);

		errorMesssage = new Label("");
		flexTable.setWidget(0, 1, errorMesssage);

		Label label_1 = new Label("名稱");
		flexTable.setWidget(1, 0, label_1);

		myfilename = new TextBox();
		flexTable.setWidget(1, 1, myfilename);
		myfilename.setWidth("225px");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		flexTable.setWidget(2, 1, horizontalPanel);

		registerButton = new Button("Create");
		horizontalPanel.add(registerButton);

		Label lblNewLabel = new Label("　　　　");
		horizontalPanel.add(lblNewLabel);

		Button closeButton = new Button("Close");
		horizontalPanel.add(closeButton);

		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				closePanel();
			}
		});

		registerButton.addClickHandler(this);

		thisPanel = this;
		this.type = type;

		if (type == Type.Modify) {
			registerButton.setHTML("Done");
			myfilename.setText(myFile.getName());
			label.setText("請修改名稱");
			modifyfileFile=myFile;
			modifyIndex=index;
		}
		
	}

	public void initFloder(MyFile floder) {
		this.floder = floder;
	}

	@Override
	public void onClick(ClickEvent event) {
		FileServiceAsync fileServiceAsync = GWT.create(FileService.class);
		final String myfilenameString = myfilename.getText();
		registerButton.setEnabled(false);
		final MyFile myFile = new MyFile(myfilenameString, null,
				floder.getFileFolder()+"/"+floder.getName(), FileType.DIR);
		System.out.println("path: "+floder.getFileFolder()+"/"+floder.getName());

		AsyncCallback<MyFile> callback = new AsyncCallback<MyFile>() {

			@Override
			public void onFailure(Throwable caught) {
				registerButton.setEnabled(true);
				errorMesssage.setText("伺服器連線錯誤!");
			}

			@Override
			public void onSuccess(MyFile result) {
				if (result != null) {
					closePanel();
					if (type == Type.Create) {
						FileSystemUserInterfacePane.getInstance().getFileList().add(result);
						FileSystemUserInterfacePane.getInstance()
								.addRow(result);
					} else {
						ArrayList<File> filelist = FileSystemUserInterfacePane.getInstance().getFileList();
						filelist.remove((int)modifyIndex);
						filelist.add(modifyIndex,result);		
						FileSystemUserInterfacePane.getInstance().updateUI();
					}
				} else {
					errorMesssage.setText("失敗");
					registerButton.setEnabled(true);
				}
			}
		};

		if (type == Type.Create) {
			String path = floder.getFileFolder()+"/"+floder.getName();
			fileServiceAsync.createFolder(path, myfilenameString, callback);
		} else {
			modifyfileFile.setName(myfilenameString);
			fileServiceAsync.editMyFile(modifyfileFile, callback);
		}
	}

	public void closePanel() {
		thisPanel.hide();
	}

}
