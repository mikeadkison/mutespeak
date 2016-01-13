import java.io.IOException;
import com.melloware.jintellitype.JIntellitype;
import com.melloware.jintellitype.HotkeyListener;
import java.io.File;


public class KeyListener implements HotkeyListener {
	public KeyListener() throws IOException {
		
		JIntellitype.setLibraryLocation(new File("JIntellitype64.dll"));
		JIntellitype.getInstance();
		
		// Assign global hotkeys to Windows+A and ALT+SHIFT+B
		JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_WIN, (int)'A');
		JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_ALT + JIntellitype.MOD_SHIFT, (int)'B');

		//assign this class to be a HotKeyListener
		JIntellitype.getInstance().addHotKeyListener(this);

		//Runtime.getRuntime().exec("cmd /c start tosay.vbs");
	}
	
	// listen for hotkey
	public void onHotKey(int aIdentifier) {
		if (aIdentifier == 2) {
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
		}
	}
}