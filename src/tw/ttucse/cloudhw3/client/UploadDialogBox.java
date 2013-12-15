package tw.ttucse.cloudhw3.client;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;

public class UploadDialogBox extends DialogBox{
	public UploadDialogBox() {

		String url = "127.0.0.1:8888/cloudapphw3/upload";
		String htmlString ="<html><body>\n"
				+ "<form method=\"POST\" enctype=\"multipart/form-data\" action=\""+url+"\">\n"
				+ "File to upload: <input type=\"file\" name=\"myFile\"><br>\n"
				+ "Parent: <input type=\"text\" name=\"parent\" value=\"/\"><br>\n"
				+ "<br>\n"
				+ "<input type=\"submit\" value=\"Press\"> to upload the file!\n"
				+ "</form></body></html>";
		System.out.println(htmlString);
		HTMLPanel panel = new HTMLPanel(htmlString);
		setWidget(panel);
		panel.setSize("100%", "100%");
	}

}
