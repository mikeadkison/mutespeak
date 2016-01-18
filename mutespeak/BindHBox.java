package mutespeak;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.concurrent.Task;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.WindowEvent;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.ArrayList;

import javafx.scene.control.ComboBox;
import java.io.PrintWriter;


public class BindHBox extends HBox {
	protected Label bindCharactersLabel;
	private KeyListener listener;
	private List<BindHBox> allHBoxes;
	protected CheckBox cntrlCB;
	protected CheckBox altCB;
	protected ComboBox<String> bindsComboBox0;
	protected ComboBox<String> bindsComboBox1;
	protected TextField sayingTextField;
	
	private static final List<String> ALLOWED_BINDS = new ArrayList<>();
	
	
	private final static String SCRIPT_PART0 = "' \nset speech = Wscript.CreateObject(\"SAPI.spVoice\")\n speech.speak ";
	
	public BindHBox(KeyListener listener, List<BindHBox> allHBoxes, CheckBox enabledToggle) {
		this.listener = listener;
		
		
		this.allHBoxes = allHBoxes;
		
		Label sayingLabel = new Label("voice saying: ");
		this.getChildren().add(sayingLabel);
		
		sayingTextField = new TextField ();
		this.getChildren().add(sayingTextField);
		
		Label bindLabel = new Label("bind: ");
		this.getChildren().add(bindLabel);
		
		bindsComboBox0 = new ComboBox<>();
		bindsComboBox0.getItems().addAll("", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
		bindsComboBox0.setValue("");
		this.getChildren().add(bindsComboBox0);
		
		bindsComboBox1 = new ComboBox<>();
		bindsComboBox1.getItems().addAll("", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
		bindsComboBox1.setValue("");
		this.getChildren().add(bindsComboBox1);

		bindCharactersLabel = new Label("");
		this.getChildren().add(bindCharactersLabel);
		
		cntrlCB = new CheckBox("ctrl");
		this.getChildren().add(cntrlCB);
		
		altCB = new CheckBox("alt");
		this.getChildren().add(altCB);
		
		cntrlCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableVal, Boolean oldVal, Boolean newVal) {
				if (newVal) {
					listener.startListeningFor("CTRL");
				} else {
					listener.stopListeningFor("CTRL", BindHBox.this);
				}
			}
		});
		
		altCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableVal, Boolean oldVal, Boolean newVal) {
				if (newVal) {
					listener.startListeningFor("ALT");
				} else {
					listener.stopListeningFor("ALT", BindHBox.this);
				}
			}
		});
		
		bindsComboBox0.valueProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue ov, String oldStr, String newStr) {
				listener.stopListeningFor(oldStr, BindHBox.this);
				if (!newStr.equals("")) {	
					testAndClearOtherHBoxes(bindsComboBox0);
					listener.startListeningFor(newStr);
				}
			}    
		});
		
		bindsComboBox1.valueProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue ov, String oldStr, String newStr) {
				listener.stopListeningFor(oldStr, BindHBox.this);
				if (!newStr.equals("")) {
					testAndClearOtherHBoxes(bindsComboBox1); //order of these two lines matters
					listener.startListeningFor(newStr);
				}
			}    
		});
	}
	
	private void testAndClearOtherHBoxes(ComboBox changedCB) {
		for (BindHBox hbox: allHBoxes) {
			if (!hbox.equals(BindHBox.this)) {
				testAndClearHBox(hbox, changedCB);
			}
		}
	}
	
	private void testAndClearHBox(BindHBox hbox, ComboBox changedCB) { //check if hbox has the same bind as this and clear hbox fields appropriately if it does
		ComboBox otherCbox0 = hbox.bindsComboBox0;
		ComboBox otherCbox1 = hbox.bindsComboBox1;
		CheckBox otherCntrlCB = hbox.cntrlCB;
		CheckBox otherAltCB = hbox.altCB;
		
		//check if binds are the same between hboxes and clear the appropriate fields if so
		if (otherCntrlCB.isSelected() == this.cntrlCB.isSelected() && otherAltCB.isSelected() == this.altCB.isSelected()) {
			if (otherCbox0.getValue().equals(bindsComboBox0.getValue()) && otherCbox1.getValue().equals(bindsComboBox1.getValue())) {
				if (otherCbox0.getValue().equals(changedCB.getValue())) {
					otherCbox0.setValue("");
				} else if (otherCbox1.getValue().equals(changedCB.getValue())) {
					otherCbox0.setValue("");
				}
			}
		}
	}
	
	protected List<String> getBindKeys() {
		List<String> binds = new ArrayList<>();
		if (!bindsComboBox0.getValue().equals("")) {
			binds.add(bindsComboBox0.getValue());
		}
		
		if (!bindsComboBox1.getValue().equals("")) {
			binds.add(bindsComboBox1.getValue());
		}
		
		if (cntrlCB.isSelected()) {
			binds.add("CTRL");
		}
		
		if (altCB.isSelected()) {
			binds.add("ALT");
		}
		return binds;
	}
	
	protected void onBindSatisfied() {
		System.out.println("bind satisfied");
		say();
	}
	
	private void say() {
		String script = SCRIPT_PART0 + "\"" + sayingTextField.getText() + "\"";
		
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