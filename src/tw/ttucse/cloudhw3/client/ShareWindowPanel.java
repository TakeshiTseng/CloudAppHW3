package tw.ttucse.cloudhw3.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class ShareWindowPanel extends Composite {
	private static ShareWindowPanel instance;


	/**
	 * @wbp.parser.constructor
	 */
	public ShareWindowPanel() {
		instance = this;

		VerticalPanel vtp = new VerticalPanel();
		vtp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		initWidget(vtp);
		vtp.setWidth("800px");
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		vtp.add(absolutePanel);
		absolutePanel.setHeight("39px");
		
		Button loginbtn = new Button("Login");
		absolutePanel.add(loginbtn, 709, 4);
		
		Label lblNewLabel = new Label("~~雲端硬碟~~");
		vtp.add(lblNewLabel);
		lblNewLabel.setHeight("90px");
		
		Label lblNewLabel_1 = new Label("分享名稱");
		vtp.add(lblNewLabel_1);
		
		TextBox txtbx = new TextBox();
		vtp.add(txtbx);
		
		Button btnNewButton = new Button("搜尋");
		vtp.add(btnNewButton);
	}


	public static ShareWindowPanel getInstance() {
		return instance;
	}


	public static void setInstance(ShareWindowPanel instance) {
		ShareWindowPanel.instance = instance;
	}
}