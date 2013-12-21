package tw.ttucse.cloudhw3.client;

import java.util.ArrayList;
import java.util.List;

import tw.ttucse.cloudhw3.client.UserFormDialogBox.Type;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.*;
import com.google.gwt.cell.client.NumberCell;

public class MainWindowPanel extends Composite {
	private static MainWindowPanel instance;
	private int start;
	private HasData<User> display;
	private AsyncDataProvider<User> provider;
	private CellTable<User> cellTable;
	private SimplePager pager;
	private SingleSelectionModel<User> selectionModel;
	private FileSystemUserInterfacePane fileSystemUserInterfacePane;
	private User loginUser;
	private MyFile myFile;

	public MyFile getMyFile() {
		return myFile;
	}

	public void setMyFile(MyFile myFile) {
		this.myFile = myFile;
	}

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
		fileServiceAsync.getFileWithName(loginUser.getAccount(), ".", true,
				new AsyncCallback<MyFile>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(MyFile result) {
						System.out.println("Get Mydile: "
								+ result.getFileFolder() + "/"
								+ result.getName());
						setMyFile(result);
					}
				});
	}

	public static MainWindowPanel getInstance() {
		return instance;
	}

	public FileSystemUserInterfacePane getFileSystemUserInterfacePane() {
		return fileSystemUserInterfacePane;
	}

	private static List<User> userlist;
	private final UserServiceAsync userServiceAsync = GWT
			.create(UserService.class);

	private final FileServiceAsync fileServiceAsync = GWT
			.create(FileService.class);

	public MainWindowPanel(User user) {
		this();
		setLoginUser(user);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public MainWindowPanel() {
		instance = this;
		userlist = new ArrayList<User>();

		cellTable = new CellTable<User>();
		cellTable.setSize("800px", "600px");
		cellTable.setPageSize(3);
		cellTable.setSelectionModel(selectionModel);
		User user = new User("test", "test");
		user.setUsername("test");

		provider = new AsyncDataProvider<User>() {
			@Override
			protected void onRangeChanged(HasData<User> Display) {
				display = Display;
				start = Display.getVisibleRange().getStart();
				int end = start + Display.getVisibleRange().getLength();
				end = end >= userlist.size() ? userlist.size() : end;
				List<User> sub = userlist.subList(start, end);
				updateRowData(start, sub);
			}
		};
		AsyncCallback<User[]> getUsersCallback = new AsyncCallback<User[]>() {

			@Override
			public void onFailure(Throwable caught) {
				System.out.println("getUsers failure");
			}

			@Override
			public void onSuccess(User[] result) {
				userlistAddusers(result);
				List<User> sub = userlist.subList(
						0,
						cellTable.getPageSize() >= userlist.size() ? userlist
								.size() : cellTable.getPageSize());
				provider.updateRowData(0, sub);
				provider.updateRowCount(userlist.size(), true);
			}

		};
		userServiceAsync.getUsers(getUsersCallback);
		VerticalPanel vtp = new VerticalPanel();

		initWidget(vtp);

		FlexTable flexTable = new FlexTable();
		vtp.add(flexTable);

		Button createButton = new Button("Create");
		flexTable.setWidget(0, 0, createButton);
		createButton.addClickHandler(new createButtonClickHandler());

		Label label = new Label("");
		flexTable.setWidget(0, 1, label);

		Button filesystemButton = new Button("File System");
		flexTable.setWidget(0, 2, filesystemButton);
		filesystemButton.addClickHandler(new filesystemButtonClickHander());

		Column<User, Number> column = new Column<User, Number>(new NumberCell()) {
			@Override
			public Number getValue(User object) {
				return (Number) userlist.indexOf(object);
			}
		};
		cellTable.addColumn(column, "NO.");

		TextColumn<User> textColumn = new TextColumn<User>() {
			@Override
			public String getValue(User object) {
				return object.getAccount();
			}
		};
		cellTable.addColumn(textColumn, "Account");

		TextColumn<User> textColumn_1 = new TextColumn<User>() {
			@Override
			public String getValue(User object) {
				return object.getPassword();
			}
		};
		cellTable.addColumn(textColumn_1, "Password");

		TextColumn<User> textColumn_2 = new TextColumn<User>() {
			@Override
			public String getValue(User object) {
				return object.getUsername();
			}
		};
		cellTable.addColumn(textColumn_2, "User Name");

		provider.addDataDisplay(cellTable);
		provider.updateRowCount(userlist.size(), true);

		pager = new SimplePager();
		pager.setDisplay(cellTable);

		vtp.add(cellTable);

		Column<User, String> modifyAction = new Column<User, String>(
				new ButtonCell()) {
			@Override
			public String getValue(User object) {
				return "modify";
			}
		};
		modifyAction.setFieldUpdater(new ModifyFieldUpdater());
		cellTable.addColumn(modifyAction, "Modify");

		Column<User, String> removeAction = new Column<User, String>(
				new ButtonCell()) {
			@Override
			public String getValue(User object) {
				return "remove";
			}
		};
		removeAction.setFieldUpdater(new RemoveFieldUpdater());
		cellTable.addColumn(removeAction, "Remove");
		vtp.add(pager);
	}

	public void userlistAddusers(User[] users) {
		for (User user : users) {
			userlist.add(user);
		}
	}

	private void updatadisplay(int start, int end) {
		List<User> sub = userlist.subList(start, end);
		provider.updateRowData(start, sub);
		provider.updateRowCount(userlist.size(), true);
	}

	public void updatadisplay() {
		int end = start + display.getVisibleRange().getLength();
		end = end >= userlist.size() ? userlist.size() : end;
		updatadisplay(start, end);
		System.out.println("updatadisplay finish");
	}

	public void updatadisplaytoEnd() {
		int end = userlist.size();
		start = end - cellTable.getPageSize() + 1;
		updatadisplay(start, end);
		pager.setPage(pager.getPageCount());
		System.out.println("updatadisplaytoEnd finish");
	}

	public CellTable<User> getCellTable() {
		return cellTable;
	}

	class ModifyFieldUpdater implements FieldUpdater<User, String> {
		@Override
		public void update(int index, User object, String value) {
			UserFormDialogBox registerDialogBox = new UserFormDialogBox(
					Type.Modify, object);
			registerDialogBox.setAnimationEnabled(true);
			registerDialogBox.setText("Modify Information DialogBox");
			registerDialogBox.center();
		}
	}

	class RemoveFieldUpdater implements FieldUpdater<User, String> {
		@Override
		public void update(int index, final User object, String value) {

			if (loginUser.getAccount().equals(object.getAccount())) {
				userServiceAsync.deleteUser(object,
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Connent failure !");
							}

							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									userlist.remove(object);
									updatadisplay();
								} else {
									Window.alert("Remove failure");
								}
							}
						});
				System.out.println(myFile);
				fileServiceAsync.deleteFloder(myFile,
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								System.out
										.println("MainWindowPanel.RemoveFieldUpdater.update(...).new AsyncCallback() {...}.onFailure()");
							}

							@Override
							public void onSuccess(Boolean result) {
								if (result) {									
								System.out
										.println("Delete User subFolder success");
								}
							}
						});
			} else {
				Window.alert("非帳號本人，處裡失敗!");
			}

		}
	}

	public List<User> getUserlist() {
		return userlist;
	}

	class filesystemButtonClickHander implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			fileSystemUserInterfacePane = new FileSystemUserInterfacePane();
			RootPanel.get("mainWin").remove(MainWindowPanel.getInstance());
			RootPanel.get("mainWin").add(fileSystemUserInterfacePane);
			fileSystemUserInterfacePane.checkIfDefaultFileExist(loginUser);
			fileSystemUserInterfacePane.initWithUser(loginUser);
		}
	}

	class createButtonClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			UserFormDialogBox registerDialogBox = new UserFormDialogBox(
					Type.Register, null);
			registerDialogBox.setAnimationEnabled(true);
			registerDialogBox.setText("Register DialogBox");
			registerDialogBox.center();
		}

	}
}