import java.io.IOException;
import com.melloware.jintellitype.JIntellitype;
import com.melloware.jintellitype.HotkeyListener;
import java.io.File;

import java.util.Map;
import java.util.HashMap;
import java.io.PrintWriter;

public class KeyListener implements HotkeyListener {
	private final String SCRIPT_PART0 = "' \nset speech = Wscript.CreateObject(\"SAPI.spVoice\")\n speech.speak ";
	private static int hotKeyIndex = 0; //identifier for registered hot keys
	private Map<Integer, String> indexToSayingMap;
	
	public KeyListener() throws IOException {
		indexToSayingMap = new HashMap<>();
		
		JIntellitype.setLibraryLocation(new File("JIntellitype64.dll"));
		JIntellitype.getInstance();
		
		// Assign global hotkeys to Windows+A and ALT+SHIFT+B
		//JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_WIN, (int)'A');
		//JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_ALT + JIntellitype.MOD_SHIFT, (int)'B');

		//assign this class to be a HotKeyListener
		JIntellitype.getInstance().addHotKeyListener(this);

		//Runtime.getRuntime().exec("cmd /c start tosay.vbs");
	}
	
	public void bindToSaying(String character, String saying) {
		JIntellitype.getInstance().registerHotKey(hotKeyIndex, character);
		indexToSayingMap.put(hotKeyIndex, saying);
		hotKeyIndex++; //increment index so it is unique
	}
	
	// listen for hotkey
	public void onHotKey(int aIdentifier) {
		String saying = indexToSayingMap.get(aIdentifier);
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