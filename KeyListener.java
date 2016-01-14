import java.io.IOException;
import com.melloware.jintellitype.JIntellitype;
import com.melloware.jintellitype.HotkeyListener;
import java.io.File;

import java.util.Map;
import java.util.HashMap;
import java.io.PrintWriter;

import javafx.scene.control.TextField;


public class KeyListener implements HotkeyListener {
	private final String SCRIPT_PART0 = "' \nset speech = Wscript.CreateObject(\"SAPI.spVoice\")\n speech.speak ";
	private static int hotKeyIndex = 0; //identifier for registered hot keys

	private Map<Integer, TextField> hotKeyIndexToSayingFieldMap;
	private Map<TextField, Integer> sayingFieldToHotKeyIndexMap;
	private Map<Integer, String> hotKeyIndexToBindMap;
	
	public KeyListener() throws IOException {
		hotKeyIndexToSayingFieldMap = new HashMap<>();
		sayingFieldToHotKeyIndexMap = new HashMap<>();
		hotKeyIndexToBindMap = new HashMap<>();
		
		JIntellitype.setLibraryLocation(new File("JIntellitype64.dll"));
		JIntellitype.getInstance();
		
		// Assign global hotkeys to Windows+A and ALT+SHIFT+B
		//JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_WIN, (int)'A');
		//JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_ALT + JIntellitype.MOD_SHIFT, (int)'B');

		//assign this class to be a HotKeyListener
		JIntellitype.getInstance().addHotKeyListener(this);

	}
	
	public void bindToSaying(String character, TextField sayingTextField) {
		Integer indexToRebind = sayingFieldToHotKeyIndexMap.get(sayingTextField); //get the uid of the bind coupled with this text field if the text field has been bound before
		if (null != indexToRebind) {
			JIntellitype.getInstance().unregisterHotKey(indexToRebind);
			JIntellitype.getInstance().registerHotKey(indexToRebind, character); //rebind to a new character
			hotKeyIndexToBindMap.put(indexToRebind, character);
			
		} else { //no bind exists yet for this text field, so associate the field with a bind (meaning the bind's unique id)
			JIntellitype.getInstance().registerHotKey(hotKeyIndex, character);
			sayingFieldToHotKeyIndexMap.put(sayingTextField, hotKeyIndex);
			hotKeyIndexToSayingFieldMap.put(hotKeyIndex, sayingTextField);
			hotKeyIndexToBindMap.put(hotKeyIndex, character);
			hotKeyIndex++; //increment index so it is unique
		}
	}
	
	public void disableBinds() {
		for (Integer indexToUnbind: hotKeyIndexToSayingFieldMap.keySet()) {
			JIntellitype.getInstance().unregisterHotKey(indexToUnbind);
		}
	}
	
	public void enableBinds() {
		for (Integer indexToRebind: hotKeyIndexToSayingFieldMap.keySet()) {
			JIntellitype.getInstance().registerHotKey(indexToRebind, hotKeyIndexToBindMap.get(indexToRebind));
		}
	}
	
	// listen for hotkey
	public void onHotKey(int aIdentifier) {
		String saying = hotKeyIndexToSayingFieldMap.get(aIdentifier).getText();
		if (saying != null) {
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
		
		/* if (aIdentifier == 2) {
			System.out.println("WINDOWS+A hotkey pressed");
			try {
				Runtime.getRuntime().exec("cmd /c start tosay.vbs");
			} catch (IOException e) {
				e.printStackTrace();
				JIntellitype.getInstance().cleanUp();
				System.exit(-1);
			}
		}
		if (aIdentifier == 1) {
			System.out.println("Alt shift b prsesed, terminating");
			// Termination, make sure to call before exiting
			JIntellitype.getInstance().cleanUp();
			System.exit(0);
		} */
	}
}