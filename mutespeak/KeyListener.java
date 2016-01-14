package mutespeak;

import java.io.IOException;
import com.melloware.jintellitype.JIntellitype;
import com.melloware.jintellitype.HotkeyListener;
import java.io.File;

import java.util.Map;
import java.util.HashMap;
import java.io.PrintWriter;

import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;

import java.util.List;


public class KeyListener implements HotkeyListener {
	private final String SCRIPT_PART0 = "' \nset speech = Wscript.CreateObject(\"SAPI.spVoice\")\n speech.speak ";
	protected String saying;
	
	protected int hotKeyIndex;
	private static int hotKeyIndexIncrementer = 0; //identifier for registered hot keys
	private boolean bindEnabled;
	private String bind;
	
	public KeyListener() {
		hotKeyIndex = hotKeyIndexIncrementer;
		hotKeyIndexIncrementer++;
		
		JIntellitype.setLibraryLocation(new File("JIntellitype64.dll"));
		
		JIntellitype.getInstance();

		//assign this class to be a HotKeyListener
		JIntellitype.getInstance().addHotKeyListener(this);

	}
	
	protected void changeBind(String character) {
		JIntellitype.getInstance().unregisterHotKey(hotKeyIndex);
		this.bind = character;
		JIntellitype.getInstance().registerHotKey(hotKeyIndex, bind); //rebind to a new character
	}
	
	protected String getBind() {
		return bind;
	}
	
	protected void changeSaying(String saying) {
		this.saying = saying;
	}
	
	protected void disableBind() {
		System.out.println("disablin bind: " + hotKeyIndex);
		JIntellitype.getInstance().unregisterHotKey(hotKeyIndex);
	}
	
	protected void enableBind() {
		if (bind != null) {
			JIntellitype.getInstance().registerHotKey(hotKeyIndex, bind);
		}
	}
	
	protected void removeBind() {
		disableBind();
		bind = null;
	}
	
	// listen for hotkey
	public void onHotKey(int aIdentifier) {
		if (saying != null && aIdentifier == hotKeyIndex) {
			System.out.println("id: " + hotKeyIndex);
			String script = SCRIPT_PART0 + "\"" + saying + "\"";
			
			try {
				PrintWriter writer = new PrintWriter("talking_file.vbs", "UTF-8");
				writer.println(script);
				writer.close();
				Runtime.getRuntime().exec("cmd /c start talking_file.vbs");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}