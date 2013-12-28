package tw.ttucse.cloudhw3.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CloudAppHW3 implements EntryPoint {
	
	

	/**
	 * This is the entry point method.
	 */
	
	public void onModuleLoad() {
		ShareWindowPanel shareWindowPanel = new ShareWindowPanel();
		RootPanel.get("mainWin").add(shareWindowPanel);
	}
}
