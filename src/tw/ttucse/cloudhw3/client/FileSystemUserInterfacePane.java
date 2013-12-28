package tw.ttucse.cloudhw3.client;

import java.util.ArrayList;
import java.util.Stack;

import tw.ttucse.cloudhw3.client.File.FileType;
import tw.ttucse.cloudhw3.client.FloderFormDialogBox.Type;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class FileSystemUserInterfacePane extends Composite {
	enum Mode {
		Controll, Search
	}

	private static FileSystemUserInterfacePane instance;
	private String path = "";
	private String pathstr = "";
	private File file;
	private Stack<File> fileStack = new Stack<File>();
	private Button backButton;
	private ArrayList<File> fileList;
	// private ArrayList<MyFile> fileList;
	// private ArrayList<ShareLink> shareLinksList;
	private Mode mode = Mode.Controll;

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		if (mode == Mode.Search) {
			disableItem();
		} else {

		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private final FileServiceAsync fileServiceAsync = GWT
			.create(FileService.class);

	public ArrayList<File> getFileList() {
		return fileList;
	}

	public void setFileList(ArrayList<File> fileList) {
		this.fileList = fileList;
	}

	private Label usernameLabel;
	private Label pathLabel;
	private FlexTable flexTable;
	private Button closebutton;
	private Button printAllFileButton;
	private Button printAllShareButton;
	private Button Creatbutton;
	private Button Uploadbutton;
	private Button Sharebutton;
	private Label userlabel;

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

		closebutton = new Button("X");
		absolutePanel.add(closebutton, 0, 0);
		closebutton.setSize("29px", "27px");
		closebutton.addClickHandler(new closeButtonClickHander());

		Label label = new Label("CloudApp  -  File Systems");
		label.setStyleName("FileSystemUI_Title");
		absolutePanel.add(label, 324, 0);

		printAllFileButton = new Button("Print All File to Console");
		printAllFileButton.addClickHandler(new ClickHandler() {

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
							System.out.println("ID:" + file.getId() + "\t\t"
									+ file.getType() + "\t"
									+ file.getFileFolder() + "/"
									+ file.getName());
						}
						System.out.println("*****\n");
					}
				});
			}
		});
		absolutePanel.add(printAllFileButton, 35, -3);

		printAllShareButton = new Button("printAllShareButton");
		absolutePanel.add(printAllShareButton, 202, 0);
		printAllShareButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fileServiceAsync
						.getShareLinks(new AsyncCallback<ShareLink[]>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(ShareLink[] result) {
								System.out.println("\n*****\nAll ShareLink:");
								for (ShareLink shareLink : result) {
									System.out.println("ShareName:"
											+ shareLink.shareName
											+ "\t\tOwner:" + shareLink.owner);
									System.out.println(shareLink);
								}
								System.out.println("*****\n");
							}
						});
			}
		});

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);

		userlabel = new Label("User : ");
		horizontalPanel.add(userlabel);

		usernameLabel = new Label();
		horizontalPanel.add(usernameLabel);

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		verticalPanel.add(horizontalPanel_1);

		Creatbutton = new Button("Creat Floder");
		horizontalPanel_1.add(Creatbutton);
		Creatbutton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				FloderFormDialogBox creatFloderDialogBox = new FloderFormDialogBox(
						Type.Create, null, null);
				creatFloderDialogBox.initFloder((MyFile) getFile());
				creatFloderDialogBox.setAnimationEnabled(true);
				creatFloderDialogBox.setText("Create Folder DialogBox");

				creatFloderDialogBox.center();
			}
		});

		Label label_3 = new Label("　　　　");
		horizontalPanel_1.add(label_3);

		Uploadbutton = new Button("Upload File");
		horizontalPanel_1.add(Uploadbutton);

		Uploadbutton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UploadPanelDialogBox uploadPanel = new UploadPanelDialogBox(
						(MyFile) file);
				uploadPanel.center();
			}
		});

		Label label_2 = new Label("　　　　");
		horizontalPanel_1.add(label_2);

		Sharebutton = new Button("Share Files");
		horizontalPanel_1.add(Sharebutton);
		Sharebutton.addClickHandler(new ClickHandler() {

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
		setFile(myFile);
		System.out.println("set file :" + ((MyFile) getFile()).getName());
		String path = myFile.getFileFolder() + "/" + myFile.getName();
		initWithPath(path);
	}

	public void initWithPath(String path) {
		this.path = path;
		if (mode != Mode.Search) {
			pathLabel.setText(getPath());
		} else {
			pathLabel.setText(pathstr);
		}
		fileServiceAsync.getFilesWithParent(path,
				new AsyncCallback<MyFile[]>() {

					@Override
					public void onSuccess(MyFile[] result) {
						fileList = new ArrayList<File>();
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

	public void initWithShareName(String ShareName) {
		pathstr = ShareName + "/";
		usernameLabel.setText(ShareName);
		setPath(pathstr);
		pathLabel.setText(getPath());
		fileServiceAsync.getShareLinksWithShareName(ShareName,
				new AsyncCallback<ShareLink[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ShareLink[] result) {
						if (result.length == 0) {
							Window.alert("This keyword no file to share.");
						}
						fileList = new ArrayList<File>();
						for (ShareLink shareLink : result) {
							fileList.add(shareLink);
						}
						updateUI();
					}
				});
	}

	public void initWithShareLink(ShareLink shareLink) {
		setFile(shareLink);

		pathLabel.setText(pathstr);
		ArrayList<Long> IDs = new ArrayList<Long>();
		String[] IDsString = shareLink.idstr.split(",");
		for (String string : IDsString) {
			IDs.add(Long.parseLong(string));
		}
		fileServiceAsync.getFileWithID(IDs.toArray(new Long[IDs.size()]),
				new AsyncCallback<MyFile[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(MyFile[] result) {
						fileList = new ArrayList<File>();
						for (MyFile file : result) {
							fileList.add(file);
						}
						updateUI();
					}
				});
	}

	public void updateUI() {
		initFlexTable();
		String[] str = pathLabel.getText().split("/");
		if (mode != Mode.Search) {
			backButton.setEnabled(str.length > 2);
		} else {
			backButton.setEnabled(str.length > 1);
		}
		for (File file : fileList) {
			if (file.getType() != FileType.SHARELINK) {
				addRow((MyFile) file);
			} else {
				addRow((ShareLink) file);
			}
		}
	}

	public void addRow(MyFile myFile) {
		// System.out.println("add :" + myFile.name + " type:"
		// + myFile.getTypeName());
		int row = flexTable.getRowCount();
		Button filebtn = new Button(myFile.getName());
		filebtn.addClickHandler(new MyfileClickHandler());
		if (myFile.getType() == FileType.DIR) {
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

	public void addRow(ShareLink shareLink) {
		// System.out.println("add :" + myFile.name + " type:"
		// + myFile.getTypeName());
		int row = flexTable.getRowCount();
		Button sharebtn = new Button(shareLink.getShareName());
		sharebtn.setTitle(shareLink.getId().toString());
		sharebtn.addClickHandler(new ShareLinkClickHander());
		// if (myFile.getFileType() == MyFile.TYPE_DIR) {
		sharebtn.setStyleName("fileButton");
		// }

		flexTable.setWidget(row, 0, sharebtn);
		flexTable.setWidget(row, 1, new Label("ShareLink"));
		flexTable.setWidget(row, 2, new Label(shareLink.getOwner()));
	}

	public void initFlexTable() {
		flexTable.removeAllRows();
		Label label_5 = new Label("File Name");
		flexTable.setWidget(0, 0, label_5);
		label_5.setWidth("100px");

		Label label_6 = new Label("Type");
		flexTable.setWidget(0, 1, label_6);
		label_6.setWidth("60px");

		if (mode == Mode.Controll) {
			Label label_7 = new Label("Modify");
			flexTable.setWidget(0, 2, label_7);
			label_7.setWidth("60px");

			Label label_8 = new Label("Remove");
			flexTable.setWidget(0, 3, label_8);
			label_8.setWidth("60px");
		} else {
			Label label_7 = new Label("Owner");
			flexTable.setWidget(0, 2, label_7);
			label_7.setWidth("60px");
		}
	}

	public void disableItem() {
		closebutton.setVisible(false);
		printAllFileButton.setVisible(false);
		printAllShareButton.setVisible(false);
		Creatbutton.setVisible(false);
		Uploadbutton.setVisible(false);
		Sharebutton.setVisible(false);
		userlabel.setText("KeyWord : ");
	}

	public void enableItem() {
		closebutton.setVisible(true);
		printAllFileButton.setVisible(true);
		printAllShareButton.setVisible(true);
		Creatbutton.setVisible(true);
		Uploadbutton.setVisible(true);
		Sharebutton.setVisible(true);
		userlabel.setText("User : ");
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
				MyFile file = (MyFile) fileList.get(i);
				if (file.getName().equals(name)) {
					selectFile = file;
					selectIndex = i;
				}
			}
			FloderFormDialogBox floderFormDialogBox = new FloderFormDialogBox(
					Type.Modify, selectFile, selectIndex);
			floderFormDialogBox.initFloder((MyFile) getFile());
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
				MyFile file = (MyFile) fileList.get(i);
				if (file.getName().equals(name)) {
					selectFile = file;
					selectIndex = i;
				}
			}

			if (selectFile.getType() == FileType.DIR) {
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
		}
	}

	class MyfileClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			String name = ((Button) event.getSource()).getText();
			MyFile selectFile = null;
			for (File file : fileList) {
				MyFile myFile = (MyFile) file;
				if (myFile.getName().equals(name)) {
					selectFile = myFile;
				}
			}
			if (selectFile.getType() == FileType.DIR) {
				fileStack.push(getFile());
				pathstr = pathstr + selectFile.name + "/";
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

	class ShareLinkClickHander implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			String name = ((Button) event.getSource()).getTitle();
			ShareLink selectShareLink = null;
			for (File file : fileList) {
				ShareLink shareLink = (ShareLink) file;
				if (shareLink.getId().toString().equals(name)) {
					selectShareLink = shareLink;
				}
			}
			pathstr = pathstr + selectShareLink.owner + "/";
			initWithShareLink(selectShareLink);
			updateUI();

		}

	}

	class BackButtonClickHander implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			if (mode == Mode.Search) {
				int i = pathstr.substring(0, pathstr.length() - 1).lastIndexOf(
						"/");
				pathstr = pathstr.substring(0, i);
			}
			if (!fileStack.isEmpty()) {
				File file = fileStack.pop();
				if (file instanceof MyFile) {
					initWithMyfile((MyFile) file);
				} else {
					System.out.println("sh " + (ShareLink) file);
					initWithShareLink((ShareLink) file);
				}
			} else {
				String keyWord = pathstr;
				initWithShareName(keyWord);
			}
			updateUI();
		}
	}
}
