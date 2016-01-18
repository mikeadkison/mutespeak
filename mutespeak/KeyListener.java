package mutespeak;

import java.io.IOException;
import com.melloware.jintellitype.JIntellitype;
import com.melloware.jintellitype.HotkeyListener;
import java.io.File;

import java.util.Map;
import java.util.HashMap;


import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;

import java.util.List;
import java.util.ArrayList;

import javafx.scene.control.TextField;

public class KeyListener implements HotkeyListener {
	
	
	private List<String> nonModifiers; //a-z 0-9 keys which are non modifiers
	private List<String> modifiers; //ctrl, alt keys which are modifiers
	
	private Map<String, List<String>> triggeringComboToKeysMap; //maps combos to the keys they trigger
	private Map<Integer, String> hotKeyIndexToComboMap; // maps hotKeyUids to combos that they represent
	
	private static int hotKeyIndexIncrementer = 0;
	
	private Map<String, Long> keyToTimeLastPressedMap;
	private static final long MAX_DELAY = 30;
	
	protected List<BindHBox> bindHBoxes;
	protected boolean bindsEnabled;
	
	private long bindLastActivated;
	
	private static long BIND_COOLDOWN = 500;
	
	public KeyListener() {
		bindLastActivated = 0;
		bindsEnabled = true;
		//assign this class to be a HotKeyListener
		JIntellitype.setLibraryLocation(new File("JIntellitype64.dll"));
		JIntellitype.getInstance().addHotKeyListener(this);
		
		nonModifiers = new ArrayList<>();
		modifiers = new ArrayList<>();
		triggeringComboToKeysMap = new HashMap<>();
		hotKeyIndexToComboMap = new HashMap<>();
		keyToTimeLastPressedMap = new HashMap<>();
		
	}
	
	protected void startListeningFor(String key) {
		if (key.equals("ALT") || key.equals("CTRL")) {
			if (!modifiers.contains(key)) {
				modifiers.add(key);
			}
		} else {
			if (!nonModifiers.contains(key)) {
				nonModifiers.add(key);
			}
		}
		unRegisterBinds();
		doBinds();
	}
	
	/**
	 * @param from the BindHBox which made the request to stop listeningg to key
	 */
	protected void stopListeningFor(String key, BindHBox from) {
		if (bindHBoxes != null) {
			boolean actuallyStopListening = true;
			for (BindHBox hbox: bindHBoxes) {
				if (hbox != from && hbox.getBindKeys().contains(key)) {
					actuallyStopListening = false;
				}
			}
			
			if (actuallyStopListening) {
				modifiers.remove(key);
				nonModifiers.remove(key);
			}
		}
		unRegisterBinds();
		doBinds();
	}
	
	
	private void unRegisterBinds() {
		for (Integer unBindIndex: hotKeyIndexToComboMap.keySet()) { //deregister all of the current hotkeys
			JIntellitype.getInstance().unregisterHotKey(unBindIndex);
		}
	}
	
	/**
	 * map key combinations to what key presses they satisfy
	 * important: cant have two+ non modifiers in a combo
	 */
	public void doBinds() {
		clearMaps();
		bindsEnabled = true;
		
		
		//map a combo composed only of a single modifier to the single modifier (and nothing else) which that combo satisfies
		for (String modifier: modifiers) {
			List<String> keys = new ArrayList<>();// a combo composed of a single key can only trigger that key
			keys.add(modifier);
			triggeringComboToKeysMap.put(modifier, keys);
		}
		
		
		//generate all possible modifer combinations
		List<String> modifierPrefixes =  new ArrayList<>();
		
		for(int combinationSize = 0; combinationSize < modifiers.size(); combinationSize++) {
			modifierPrefixes.addAll(combinate(modifiers, combinationSize));
		}
		
		
		
		
		//now combine them with the non modifiers
		List<String> modsPlusNonMods = new ArrayList<>();
		for (String nonModifier: nonModifiers) {
			
			//map the combo composed of this single key to this single key which it triggers
			triggeringComboToKeysMap.put(nonModifier, new ArrayList<String>());
			triggeringComboToKeysMap.get(nonModifier).add(nonModifier);
			
			for (String modifierPrefix: modifierPrefixes) {
				String currentCombo = modifierPrefix + "+" + nonModifier;
				List<String> modifiersInPrefix = new ArrayList<>();
				
				//determine which modifiers are in prefix
				for (String modifier: modifiers) {
					if (modifierPrefix.contains(modifier)) {
						modifiersInPrefix.add(modifier);
					}
				}
				
				//map the current combo to all of the keys it includes
				triggeringComboToKeysMap.put(currentCombo, new ArrayList<String>());
				triggeringComboToKeysMap.get(currentCombo).add(nonModifier);
				for (String modifierInPrefix: modifiersInPrefix) {
					triggeringComboToKeysMap.get(currentCombo).add(modifierInPrefix);
				}
				
			}
		}
		
		//actually register every combo
		for (String combo: triggeringComboToKeysMap.keySet()) {
			hotKeyIndexToComboMap.put(hotKeyIndexIncrementer, combo);
			JIntellitype.getInstance().registerHotKey(hotKeyIndexIncrementer, combo);
			hotKeyIndexIncrementer++;
		}
		
		System.out.println("triggeringComboToKeysMap: " + triggeringComboToKeysMap.keySet());
		
		
	}
	
	private void clearMaps() {
		triggeringComboToKeysMap.clear();
		hotKeyIndexToComboMap.clear();
		keyToTimeLastPressedMap.clear();
	}
	
	private List<String> combinate(List<String> theList, int depth) {
		
		if (depth == 1 || theList.size() == 1) { //base case
			return theList;
		}
		
		
		List<String> basePlusResults = new ArrayList<>();
		for (int i = 0; i < theList.size(); i++) {
			String base = theList.get(i);
			List<String> addTheseToBase = combinate(theList.subList(i + 1, theList.size()), depth - 1);

			for (String toAdd: addTheseToBase) {
				String combined = base + "+" + toAdd;
				basePlusResults.add(combined);
			}
			
		}
		return basePlusResults;
	}
	
	protected void disableBinds() {
		unRegisterBinds();
		bindsEnabled = false;
	}
	
	protected void enableBinds() {
		unRegisterBinds();
		bindsEnabled = true;
		doBinds();
	}
	
	@Override
	public void onHotKey(int aIdentifier) {
		if (bindsEnabled) {
			String combo = hotKeyIndexToComboMap.get(aIdentifier);
			List<String> keysSatisfied = triggeringComboToKeysMap.get(combo); // get the keys which are included in this combo
			for (String key: keysSatisfied) {
				keyToTimeLastPressedMap.put(key, System.currentTimeMillis());
			}
			
			List<String> keys = new ArrayList<>();
			keys.addAll(modifiers);
			keys.addAll(nonModifiers);
			
			List<String> recentlyPressed = new ArrayList<>();
			
			for (String key: keys) {

				if (keyToTimeLastPressedMap.get(key) != null && System.currentTimeMillis() - keyToTimeLastPressedMap.get(key) < MAX_DELAY) {
					recentlyPressed.add(key);
				}
			}
			
			//need: a system to wait a bit before calling onBindSatisfied to see if any additional keys are pressed that could result in a more complicated combo
			for (BindHBox hbox: bindHBoxes) {
				if (recentlyPressed.containsAll(hbox.getBindKeys()) && !hbox.getBindKeys().isEmpty()) {
					System.out.println("currtime: " + System.currentTimeMillis());
					System.out.println("lsdtct: " + bindLastActivated);
					if (System.currentTimeMillis() - bindLastActivated > BIND_COOLDOWN) {
						hbox.onBindSatisfied();
						bindLastActivated = System.currentTimeMillis();
					}
				}
			}
			
		}
		
	}
}