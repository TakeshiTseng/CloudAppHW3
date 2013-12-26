package tw.ttucse.cloudhw3.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class ShareFilePanelDialogBox extends DialogBox {
	private FlexTable flexTable;
	private ArrayList<MyFile> fileList;
	private ArrayList<CheckBox> checkBoxsLists;
	private TextBox shareNameTextBox;

	public ShareFilePanelDialogBox(ArrayList<MyFile> fileList){
		this();
		this.fileList=fileList;
		for (MyFile myFile : fileList) {
			addRow(myFile);
		}
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public ShareFilePanelDialogBox() {
		setSize("", "");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		setWidget(verticalPanel);
		verticalPanel.setSize("350px", "500px");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(horizontalPanel);
		horizontalPanel.setWidth("349px");
		
		Label shareNameLabel = new Label("Share Name");
		horizontalPanel.add(shareNameLabel);
		
		shareNameTextBox = new TextBox();
		horizontalPanel.add(shareNameTextBox);
		
		ScrollPanel scrollPanel = new ScrollPanel();
		verticalPanel.add(scrollPanel);
		scrollPanel.setSize("350px", "530px");
		
		flexTable = new FlexTable();
		scrollPanel.setWidget(flexTable);
		flexTable.setSize("100%", "100%");
		
		Label label = new Label();
		flexTable.setWidget(0, 0, label);
		
		Label label_1 = new Label("File Name");
		flexTable.setWidget(0, 1, label_1);
		
		Label label_2 = new Label("File Type");
		flexTable.setWidget(0, 2, label_2);
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		verticalPanel.add(absolutePanel);
		absolutePanel.setHeight("30px");
		
		Button btnNewButton = new Button("Share");
		absolutePanel.add(btnNewButton, 65, 0);
		btnNewButton.addClickHandler(new ShareButtonClickHander());
		
		Button btnNewButton_1 = new Button("Close");
		absolutePanel.add(btnNewButton_1, 200, 0);
		btnNewButton_1.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});

		checkBoxsLists = new ArrayList<CheckBox>();
	}

	public void addRow(MyFile myFile) {
		int row = flexTable.getRowCount();
		Label filebtn = new Label(myFile.getName());
		
		CheckBox checkBox = new CheckBox();
		checkBox.setName((row-1)+"");
		checkBoxsLists.add(checkBox);

		flexTable.setWidget(row, 0, checkBox);
		flexTable.setWidget(row, 1, filebtn);
		flexTable.setWidget(row, 2, new Label(myFile.getTypeName()));
	}
	
	class ShareButtonClickHander implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			String owner = MainWindowPanel.getInstance().getLoginUser().getAccount();
			ShareLink shareLink = new ShareLink(shareNameTextBox.getText(),owner);
			ArrayList<Long> sharefilesList = shareLink.getFilesIDList();
			ArrayList<Integer> checkIDList = new ArrayList<Integer>();
			for (CheckBox checkBox : checkBoxsLists) {
				if(checkBox.getValue()){
					checkIDList.add(Integer.parseInt(checkBox.getName()));
				}
			}
			for (Integer index : checkIDList) {
				Long ID = fileList.get(index).getId();
				sharefilesList.add(ID);
			}
			System.out.println(sharefilesList);
			hide();
		}
		
	}
}
