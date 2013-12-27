package tw.ttucse.cloudhw3.client;

import tw.ttucse.cloudhw3.client.FileSystemUserInterfacePane.Mode;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalSplitPanel;

public class ShareWindowPanel extends Composite {
	private static ShareWindowPanel instance;
	private FileSystemUserInterfacePane fileSystemUserInterfacePane = null;
	private TextBox shareNameTextBox;


	/**
	 * @wbp.parser.constructor
	 */
	public ShareWindowPanel() {
		instance = this;

		VerticalPanel vtp = new VerticalPanel();
		vtp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		initWidget(vtp);
		vtp.setSize("800px", "");
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		vtp.add(absolutePanel);
		absolutePanel.setHeight("50px");
		
		Button loginbtn = new Button("Login");
		absolutePanel.add(loginbtn, 709, 4);
		loginbtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				LoginDialogBox loginDialobBox = new LoginDialogBox();
				loginDialobBox.setAnimationEnabled(true);
				loginDialobBox.setText("Login DialogBox");
				
				loginDialobBox.center();
			}
		});
		
		Label lblNewLabel = new Label("~~雲端硬碟~~");
		vtp.add(lblNewLabel);
		lblNewLabel.setHeight("90px");
		
		Label lblNewLabel_1 = new Label("分享名稱");
		vtp.add(lblNewLabel_1);
		
		shareNameTextBox = new TextBox();
		vtp.add(shareNameTextBox);
		
		Button btnNewButton = new Button("搜尋");
		vtp.add(btnNewButton);
		
		Label lblNewLabel_2 = new Label("　　　");
		vtp.add(lblNewLabel_2);
		
		VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel();
		vtp.add(verticalSplitPanel);
		
		Label lblNewLabel_3 = new Label("　　　　");
		vtp.add(lblNewLabel_3);
		btnNewButton.addClickHandler(new SearchClickhander());
	}


	public static ShareWindowPanel getInstance() {
		return instance;
	}


	public static void setInstance(ShareWindowPanel instance) {
		ShareWindowPanel.instance = instance;
	}
	
	class SearchClickhander implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			if(fileSystemUserInterfacePane == null){
				fileSystemUserInterfacePane = new FileSystemUserInterfacePane();
				fileSystemUserInterfacePane.setMode(Mode.Search);
				
				RootPanel.get("mainWin").add(fileSystemUserInterfacePane);
			}
			fileSystemUserInterfacePane.initWithShareName(shareNameTextBox.getText());
		}
	}
}