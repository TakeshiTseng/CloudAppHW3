package tw.ttucse.cloudhw3.client;

import java.util.ArrayList;
import java.util.Stack;

import tw.ttucse.cloudhw3.client.FloderFormDialogBox.Type;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.*;

public class FileSystemUserInterfacePane extends Composite {
	private static FileSystemUserInterfacePane instance;
	private String path = "";
	private MyFile myFile;
	private Stack<MyFile> myfileStack = new Stack<MyFile>();
	private Button backButton;
	private ArrayList<MyFile> fileList;

	public MyFile getMyFile() {
		return myFile;
	}

	public void setMyFile(MyFile myFile) {
		this.myFile = myFile;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private final FileServiceAsync fileServiceAsync = GWT
			.create(FileService.class);

	public ArrayList<MyFile> getFileList() {
		return fileList;
	}

	public void setFileList(ArrayList<MyFile> fileList) {
		this.fileList = fileList;
	}

	private Label usernameLabel;
	private Label pathLabel;
	private FlexTable flexTable;

	public static FileSystemUserInterfacePane getInstance() {
		return instance;
	}

	public FileSystemUserInterfacePane() {
		instance = this;
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		initWidget(verticalPanel);
		verticalPanel.setSize("800px", "84px");

		AbsolutePanel absolutePanel = new AbsolutePanel();
		verticalPanel.add(absolutePanel);
		absolutePanel.setHeight("27px");

		Button button = new Button("X");
		absolutePanel.add(button, 0, 0);
		button.setSize("29px", "27px");
		button.addClickHandler(new closeButtonClickHander());

		Label label = new Label("CloudApp  -  File Systems");
		label.setStyleName("FileSystemUI_Title");
		absolutePanel.add(label, 324, 0);

		Button btnNewButton = new Button("Print All File to Console");
		btnNewButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fileServiceAsync.getParents(new AsyncCallback<MyFile[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(MyFile[] result) {
						System.out.println("\n*****\nAll file:");
						for (MyFile file : result) {
							System.out.println("ID:"+file.getId()+"\t\t"+file.getFileFolder() + "/"
									+ file.getName());
						}
						System.out.println("*****\n");
					}
				});
			}
		});
		absolutePanel.add(btnNewButton, 35, -3);
		
		Button btnNewButton_1 = new Button("New button");
		absolutePanel.add(btnNewButton_1, 202, 0);
		btnNewButton_1.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fileServiceAsync.getShareLinks(new AsyncCallback<ShareLink[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ShareLink[] result) {
						System.out.println("\n*****\nAll ShareLink:");
						for (ShareLink shareLink : result) {
							System.out.println("ID:"+shareLink.id+"\t\tOwner:"+shareLink.owner);
							System.out.println(shareLink);
						}
						System.out.println("*****\n");
					}
				});
			}
		});

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);

		Label label_1 = new Label("User : ");
		horizontalPanel.add(label_1);

		usernameLabel = new Label();
		horizontalPanel.add(usernameLabel);

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		verticalPanel.add(horizontalPanel_1);

		Button button_1 = new Button("Creat Floder");
		horizontalPanel_1.add(button_1);
		button_1.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				FloderFormDialogBox creatFloderDialogBox = new FloderFormDialogBox(
						Type.Create, null, null);
				creatFloderDialogBox.initFloder(getMyFile());
				creatFloderDialogBox.setAnimationEnabled(true);
				creatFloderDialogBox.setText("Create Folder DialogBox");

				creatFloderDialogBox.center();
			}
		});

		Label label_3 = new Label("　　　　");
		horizontalPanel_1.add(label_3);

		Button button_2 = new Button("Upload File");
		horizontalPanel_1.add(button_2);

		button_2.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UploadPanelDialogBox uploadPanel = new UploadPanelDialogBox(
						myFile);
				uploadPanel.center();
			}
		});
		
		Label label_2 = new Label("　　　　");
		horizontalPanel_1.add(label_2);
		
		Button button_3 = new Button("Share Files");
		horizontalPanel_1.add(button_3);
		button_3.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ShareFilePanelDialogBox shareFilePanelDialogBox = new ShareFilePanelDialogBox(
						fileList);
				shareFilePanelDialogBox.center();
			}
		}); 
		
		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel_2
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(horizontalPanel_2);
		horizontalPanel_2.setSize("800px", "600px");

		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel_1
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel_2.add(verticalPanel_1);
		verticalPanel_1.setHeight("502px");

		AbsolutePanel absolutePanel_1 = new AbsolutePanel();
		verticalPanel_1.add(absolutePanel_1);
		absolutePanel_1.setSize("798px", "32px");

		backButton = new Button("回上一層");
		absolutePanel_1.add(backButton, 0, 0);
		backButton.addClickHandler(new BackButtonClickHander());

		pathLabel = new Label();
		absolutePanel_1.add(pathLabel, 74, 5);
		pathLabel.setSize("37px", "20px");

		ScrollPanel scrollPanel = new ScrollPanel();
		verticalPanel_1.add(scrollPanel);
		scrollPanel.setHeight("456px");
		flexTable = new FlexTable();
		scrollPanel.setWidget(flexTable);
		flexTable.setSize("100%", "100%");
		flexTable.setCellSpacing(5);
		flexTable.setCellPadding(5);
		flexTable.setBorderWidth(1);
		initFlexTable();
	}

	public void checkIfDefaultFileExist(User user) {
		fileServiceAsync.checkIfDefaultFileExist(user,
				new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Void result) {
					}
				});
	}

	public void initWithUser(User user) {
		initWithUserName(user.getUsername());
	}

	public void initWithUserName(final String username) {
		usernameLabel.setText(username);
		fileServiceAsync.getFilesWithParent(".", new AsyncCallback<MyFile[]>() {

			@Override
			public void onFailure(Throwable caught) {
				System.out.println("failure");
			}

			@Override
			public void onSuccess(MyFile[] result) {
				MyFile[] userFiles = result;
				for (MyFile userfile : userFiles) {
					System.out.println("check" + userfile.getName() + " "
							+ username);
					if (userfile.getName().equals(username)) {
						initWithMyfile(userfile);
						break;
					}
				}
			}
		});
	}

	public void initWithMyfile(MyFile myFile) {
		setMyFile(myFile);
		System.out.println("set file :" + getMyFile().getName());
		String path = myFile.getFileFolder() + "/" + myFile.getName();
		initWithPath(path);
	}

	public void initWithPath(String path) {
		this.path = path;
		pathLabel.setText(path);
		fileServiceAsync.getFilesWithParent(path,
				new AsyncCallback<MyFile[]>() {

					@Override
					public void onSuccess(MyFile[] result) {
						fileList = new ArrayList<MyFile>();
						for (MyFile myFile : result) {
							fileList.add(myFile);
						}
						updateUI();
					}

					@Override
					public void onFailure(Throwable caught) {
						System.err.println("Get File Failure!");
					}
				});
	}

	public void updateUI() {
		initFlexTable();
		String[] str = path.split("/");
		backButton.setEnabled(str.length > 2);
		for (MyFile myFile : fileList) {
			addRow(myFile);
		}
	}

	public void addRow(MyFile myFile) {
		// System.out.println("add :" + myFile.name + " type:"
		// + myFile.getTypeName());
		int row = flexTable.getRowCount();
		Button filebtn = new Button(myFile.getName());
		filebtn.addClickHandler(new MyfileClickHandler());
		if (myFile.getFileType() == MyFile.TYPE_DIR) {
			filebtn.setStyleName("fileButton");
		}

		Button modiftbtn = new Button("Modify");
		modiftbtn.addClickHandler(new ModifyButtonClickHandler());
		modiftbtn.setTitle(myFile.getName());
		Button removrbtn = new Button("Remove");
		removrbtn.setTitle(myFile.getName());
		removrbtn.addClickHandler(new RemoveButtonClickHandler());
		flexTable.setWidget(row, 0, filebtn);
		flexTable.setWidget(row, 1, new Label(myFile.getTypeName()));
		flexTable.setWidget(row, 2, modiftbtn);
		flexTable.setWidget(row, 3, removrbtn);
	}

	public void initFlexTable() {
		flexTable.removeAllRows();
		Label label_5 = new Label("File Name");
		flexTable.setWidget(0, 0, label_5);
		label_5.setWidth("100px");

		Label label_6 = new Label("Type");
		flexTable.setWidget(0, 1, label_6);
		label_6.setWidth("60px");

		Label label_7 = new Label("Modify");
		flexTable.setWidget(0, 2, label_7);
		label_7.setWidth("60px");

		Label label_8 = new Label("Remove");
		flexTable.setWidget(0, 3, label_8);
		label_8.setWidth("60px");
	}

	class closeButtonClickHander implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			MainWindowPanel mainWindowPanel = MainWindowPanel.getInstance();
			RootPanel.get("mainWin").remove(
					mainWindowPanel.getFileSystemUserInterfacePane());
			RootPanel.get("mainWin").add(mainWindowPanel);
		}
	}

	class ModifyButtonClickHandler implements ClickHandler {

		private int selectIndex;

		@Override
		public void onClick(ClickEvent event) {
			String name = ((Button) event.getSource()).getTitle();
			MyFile selectFile = null;
			for (int i = 0; i < fileList.size(); i++) {
				MyFile file = fileList.get(i);
				if (file.getName().equals(name)) {
					selectFile = file;
					selectIndex = i;
				}
			}
			FloderFormDialogBox floderFormDialogBox = new FloderFormDialogBox(
					Type.Modify, selectFile, selectIndex);
			floderFormDialogBox.initFloder(getMyFile());
			floderFormDialogBox.setAnimationEnabled(true);
			floderFormDialogBox.setText("Modify Folder DialogBox");

			floderFormDialogBox.center();
		}
	}

	class RemoveButtonClickHandler implements ClickHandler {

		private int selectIndex;

		@Override
		public void onClick(ClickEvent event) {
			String name = ((Button) event.getSource()).getTitle();
			MyFile selectFile = null;
			for (int i = 0; i < fileList.size(); i++) {
				MyFile file = fileList.get(i);
				if (file.getName().equals(name)) {
					selectFile = file;
					selectIndex = i;
				}
			}

			if (selectFile.getFileType() == MyFile.TYPE_DIR) {
				fileServiceAsync.deleteFloder(selectFile,
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								System.out.println("File delete failure");
							}

							@Override
							public void onSuccess(Boolean result) {
								fileList.remove(selectIndex);
								flexTable.removeRow(selectIndex + 1);
							}
						});
			} else {
				fileServiceAsync.deleteFile(selectFile,
						new AsyncCallback<MyFile>() {

							@Override
							public void onFailure(Throwable caught) {
								System.out.println("File delete failure");
							}

							@Override
							public void onSuccess(MyFile result) {
								fileList.remove(selectIndex);
								flexTable.removeRow(selectIndex + 1);
							}
						});
			}

			/*
			 * fileServiceAsync.deleteSubFloder(selectFile.getFileFolder() + "/"
			 * + selectFile.getName() + "/", new AsyncCallback<Void>() {
			 * 
			 * @Override public void onFailure(Throwable caught) {
			 * System.out.println("File delete failure"); }
			 * 
			 * @Override public void onSuccess(Void result) {
			 * fileList.remove(selectIndex); flexTable.removeRow(selectIndex +
			 * 1);
			 * 
			 * } });
			 */
		}
	}

	class MyfileClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			String name = ((Button) event.getSource()).getText();
			MyFile selectFile = null;
			for (MyFile myfile : fileList) {
				if (myfile.getName().equals(name)) {
					selectFile = myfile;
				}
			}
			if (selectFile.fileType == MyFile.TYPE_DIR) {
				myfileStack.push(getMyFile());
				initWithMyfile(selectFile);
				updateUI();
			} else {
				String blobKeyString = selectFile.getFileKey();
				String fileName = selectFile.getName();
				
				String downloadURL = "/cloudapphw3/download?blob-key="
						+ blobKeyString + "&fileName=" + fileName;
				Window.Location.assign(downloadURL);
			}
		}
	}

	class BackButtonClickHander implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			MyFile m = myfileStack.pop();
			System.out.println(m.getName());
			initWithMyfile(m);
			updateUI();
		}
	}
}
