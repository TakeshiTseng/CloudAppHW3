package tw.ttucse.cloudhw3.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class LoginDialogBox extends DialogBox implements ClickHandler{
	private final LoginServiceAsync loginServiceAsync = GWT.create(LoginService.class);
	LoginDialogBox thisPanel;
	private PasswordTextBox password;
	private TextBox accountTextBox;
	Label errorMesssage;
	public LoginDialogBox() {
		loginServiceAsync.init(new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Void result) {
			}
		});
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
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		flexTable.setWidget(3, 1, horizontalPanel);
		
		Button button = new Button("登入");
		horizontalPanel.add(button);
		button.addClickHandler(this);
		
		thisPanel = this;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		String account = accountTextBox.getText();
		String pwd = password.getText();
		loginServiceAsync.login(account, pwd, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if(!result){
					errorMesssage.setText("帳號或密碼有誤");
				} else {
					RootPanel.get("mainWin").add(new MainWindowPanel());
					final DialogBox dialogBox = new DialogBox();
					dialogBox.setText("Remote Message.");
					dialogBox.setAnimationEnabled(true);
					Button btn = new Button("Close.");
					btn.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();							
						}
					});
					VerticalPanel verticalPanel = new VerticalPanel();
					verticalPanel.add(new HTML("<br><b>Login Success.</b><br>"));
					verticalPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
					verticalPanel.add(btn);
					dialogBox.setWidget(verticalPanel);
					thisPanel.hide();
					dialogBox.center();
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				errorMesssage.setText("伺服器連線錯誤");
				
			}
		});
		
	}

}
