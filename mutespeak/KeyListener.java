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
import java.util.ArrayList;

public class KeyListener implements HotkeyListener {
	private final String SCRIPT_PART0 = "' \nset speech = Wscript.CreateObject(\"SAPI.spVoice\")\n speech.speak ";
	protected String saying;
	
	private static int hotKeyIndexIncrementer = 0; //identifier for registered hot keys
	private List<String> nonModifiers; //a-z 0-9 keys which are non modifiers
	private List<String> modifiers; //ctrl, alt keys which are modifiers
	private List<Integer> hotKeyIndices;
	private Map<String, Long> keyToTimeLastPressedMap;
	private Map<String, Integer> comboToHotKeyIndexMap;
	private Map<Integer, String> hotKeyIndexToComboMap;
	private Map<String, List<String>> triggeringComboToKeysMap; //maps combos to the keys they trigger
	
	private final long MAX_BIND_DELAY = 1000;
	
	public KeyListener() {
		nonModifiers = new ArrayList<>();
		modifiers = new ArrayList<>();
		hotKeyIndices = new ArrayList<>();
		keyToTimeLastPressedMap = new HashMap<>();
		comboToHotKeyIndexMap = new HashMap<>();
		hotKeyIndexToComboMap = new HashMap<>();
		triggeringComboToKeysMap = new HashMap<>();
		
		JIntellitype.setLibraryLocation(new File("JIntellitype64.dll"));
		
		JIntellitype.getInstance();

		//assign this class to be a HotKeyListener
		JIntellitype.getInstance().addHotKeyListener(this);

	}
	
	protected void addBind(String character) {
		System.out.println("adding bind: " + character);
		if ("ALT".equals(character) || "CTRL".equals(character)) {
			if (!modifiers.contains(character)) {
				modifiers.add(character);
			}
		} else if (!nonModifiers.contains(character)) {
			nonModifiers.add(character);
		}
		System.out.println(modifiers);
		System.out.println(nonModifiers);
		redoBinds();
	}
	
	private void redoBinds() {
		unbindBinds(); //unbind current binds
		hotKeyIndices.clear(); //remove references to current binds
		keyToTimeLastPressedMap.clear();
		comboToHotKeyIndexMap.clear();
		hotKeyIndexToComboMap.clear();
		triggeringComboToKeysMap.clear();
		
		//register a bind for every other character that needs bound
		if (nonModifiers.size() > 0) {
			//make a main bind
			List<String> modifierPrefixes = new ArrayList<>();
			String basePrefix = "";
			for (String modifier: modifiers) { //generate all possible modifer combinations
				basePrefix = basePrefix + modifier + "+";
				modifierPrefixes.add(basePrefix);
			}
			
			List<String> completeCombos = new ArrayList<>();
			for (String nonModifier: nonModifiers) { //combine prefixes with binds
				List<String> triggeringCombos = new ArrayList<>();
				completeCombos.add(nonModifier);
				triggeringCombos.add(nonModifier);
				for (String prefix: modifierPrefixes) {
					completeCombos.add(prefix + nonModifier);
					triggeringCombos.add(prefix + nonModifier);
				}
				
				for (String combo: triggeringCombos) {
					if (!triggeringComboToKeysMap.containsKey(combo)) {
						triggeringComboToKeysMap.put(combo, new ArrayList<String>());
					}
					triggeringComboToKeysMap.get(combo).add(nonModifier);
					for (String modifier: modifiers) { //map this combo to the modifiers whose keypresses it satisfies
						if (combo.contains(modifier)) {
							triggeringComboToKeysMap.get(combo).add(modifier);
						}
					}
				}
			}
			
			for (String modifier: modifiers) { //single modifiers may also be pressed without additional keys, so add them to the list of copmlete binds
				completeCombos.add(modifier);
				List<String> keys = new ArrayList<>();
				keys.add(modifier);
				triggeringComboToKeysMap.put(modifier, keys); // ex) "CTRL" -> ("CTRL");
			}
			
			//bind(register) every key combination in the completeCombos list -- 
			//   at this point, the completeCombos list contains a list of every key combination
			//   that could be pressed when the user is trying to trigger the key combination they selected in the gui
			//   Also, the triggeringComboToKeysMap contains a map of the key combinations that satisfy some key's press-> thekeys which are satisfied
			for (String combo: completeCombos) {
				JIntellitype.getInstance().registerHotKey(hotKeyIndexIncrementer, combo);
				hotKeyIndices.add(hotKeyIndexIncrementer);
				comboToHotKeyIndexMap.put(combo, hotKeyIndexIncrementer);
				hotKeyIndexToComboMap.put(hotKeyIndexIncrementer, combo);
				hotKeyIndexIncrementer++;
			}
			System.out.println("complete binds: " + completeCombos);
		}
	}
	
	
	protected void changeSaying(String saying) {
		this.saying = saying;
	}
	
	private void unbindBinds() {
		for (Integer hotKeyIndexToUnBind: hotKeyIndices) {
			JIntellitype.getInstance().unregisterHotKey(hotKeyIndexToUnBind);
		}
	}
	
	protected void disableBinds() {
		unbindBinds();
	}
	
	protected void enableBinds() {
		redoBinds();
	}
	
	
	protected void removeBind(String toRemove) {
		System.out.println("removing bind: " + toRemove);
		modifiers.remove(toRemove);
		nonModifiers.remove(toRemove);
		//hotKeyIndices.remove(comboToHotKeyIndexMap.get(toRemove));
		redoBinds();
	}
	
	
	// listen for hotkey
	public void onHotKey(int aIdentifier) {
		System.out.println("------------------------");
		System.out.println("pressed " + aIdentifier);
		//get the combo associated with the aIdentifier
		String comboPressed = hotKeyIndexToComboMap.get(aIdentifier);
		System.out.println("combo: " + comboPressed);
		List<String> keysSatisfied = triggeringComboToKeysMap.get(comboPressed); //a list of the keys selected in the gui that are satisfied by this key combo
		System.out.println("keys satisfied: " + keysSatisfied);
		//record that each key satisfied by this particular combination was pressed recently
		for (String key: keysSatisfied) {
			keyToTimeLastPressedMap.put(key, System.currentTimeMillis());
		}
		
		
		//keyToTimeLastPressedMap.put(aIdentifier, System.currentTimeMillis());
		System.out.println("keyToTimeLastPressedMap keys: " + keyToTimeLastPressedMap.keySet());
		
		

		
		boolean say = true;
		List<String> keys = new ArrayList<>();
		keys.addAll(modifiers);
		keys.addAll(nonModifiers);
		for (String key: keys) {
			if (keyToTimeLastPressedMap.get(key) == null || System.currentTimeMillis() - keyToTimeLastPressedMap.get(key) > MAX_BIND_DELAY) {
				say = false;
			}
		}
	
		if (say && saying != null) {
			say();
		}


	}
	
	private void say() {
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