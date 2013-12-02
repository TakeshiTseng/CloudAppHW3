package tw.ttucse.cloudhw3.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CloudAppHW3 implements EntryPoint {
	
	

	/**
	 * This is the entry point method.
	 */
	
	public void onModuleLoad() {
		LoginDialogBox loginDialobBox = new LoginDialogBox();
		loginDialobBox.setAnimationEnabled(true);
		loginDialobBox.setText("Login DialogBox");
		
		loginDialobBox.center();
	}
}
