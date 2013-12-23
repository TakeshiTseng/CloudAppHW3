package tw.ttucse.cloudhw3.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class UserFormDialogBox extends DialogBox implements ClickHandler {
	private UserFormDialogBox thisPanel;
	private PasswordTextBox password;
	private TextBox accountTextBox;
	private TextBox usernameTextBox;
	private Label errorMesssage;
	private Button registerButton;
	private Type type;

	enum Type {
		Register, Modify;
	}

	public UserFormDialogBox(Type type,User user) {

		FlexTable flexTable = new FlexTable();
		setWidget(flexTable);
		flexTable.setSize("371px", "240px");

		Label label = new Label("請輸入帳號及密碼");
		flexTable.setWidget(0, 0, label);

		errorMesssage = new Label("");
		flexTable.setWidget(0, 1, errorMesssage);

		Label label_2 = new Label("帳號");
		flexTable.setWidget(1, 0, label_2);

		accountTextBox = new TextBox();
		flexTable.setWidget(1, 1, accountTextBox);
		accountTextBox.setWidth("225px");

		Label label_3 = new Label("密碼");
		flexTable.setWidget(2, 0, label_3);

		password = new PasswordTextBox();
		flexTable.setWidget(2, 1, password);
		password.setWidth("225px");

		Label label_1 = new Label("使用者姓名");
		flexTable.setWidget(3, 0, label_1);

		usernameTextBox = new TextBox();
		flexTable.setWidget(3, 1, usernameTextBox);
		usernameTextBox.setWidth("225px");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		flexTable.setWidget(4, 1, horizontalPanel);

		registerButton = new Button("Register");
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
		this.type=type;

		if (type == Type.Modify) {
			registerButton.setHTML("Done");
			accountTextBox.setText(user.getAccount());
			password.setText(user.getPassword());
			usernameTextBox.setText(user.getUsername());
			accountTextBox.setEnabled(false);
			label.setText("請修改資料");
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		UserServiceAsync userServiceAsync = GWT.create(UserService.class);
		FileServiceAsync fileServiceAsync = GWT.create(FileService.class);
		String account = accountTextBox.getText();
		final String pwd = password.getText();
		final String username = usernameTextBox.getText();
		final User user = new User(account, pwd);
		user.setUsername(username);
		registerButton.setEnabled(false);

		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				registerButton.setEnabled(true);
				errorMesssage.setText("伺服器連線錯誤!");
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					closePanel();
					if(type==Type.Register){
						MainWindowPanel.getInstance().userlistAddusers(new User[] { user });
					}else{
						List<User> list = MainWindowPanel.getInstance().getUserlist();
						User u=list.get(list.indexOf(user));
						u.setPassword(pwd);
						u.setUsername(username);
					}
					MainWindowPanel.getInstance().updatadisplay();
				} else {
					errorMesssage.setText("失敗");
					registerButton.setEnabled(true);
				}
			}
		};

		if (type == Type.Register) {
			userServiceAsync.addUser(user, callback);
			fileServiceAsync.createFolder(".", user.getAccount(), new AsyncCallback<MyFile>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(MyFile result) {
					System.out.println("Create User subFolder success");
				}
			});
		} else {
			userServiceAsync.editUser(user, callback);
		}
	}

	public void closePanel() {
		thisPanel.hide();
	}

}
