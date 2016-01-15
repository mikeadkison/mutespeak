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



public class BindHBox extends HBox {
	protected Label bindCharactersLabel;
	protected final KeyListener listener = new KeyListener();
	private List<BindHBox> allHBoxes;
	private CheckBox cntrlCB;
	private CheckBox altCB;
	
	private static final List<String> ALLOWED_BINDS = new ArrayList<>();
	
	public BindHBox(List<BindHBox> allHBoxes, CheckBox enabledToggle) {
		
		
		
		this.allHBoxes = allHBoxes;
		
		Label sayingLabel = new Label("voice saying: ");
		this.getChildren().add(sayingLabel);
		
		TextField sayingTextField = new TextField ();
		this.getChildren().add(sayingTextField);
		
		Label bindLabel = new Label("bind: ");
		this.getChildren().add(bindLabel);
		
		ComboBox<String> bindsComboBox0 = new ComboBox<>();
		bindsComboBox0.getItems().addAll("", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
		this.getChildren().add(bindsComboBox0);
		
		ComboBox<String> bindsComboBox1 = new ComboBox<>();
		bindsComboBox1.getItems().addAll("", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
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
					listener.addBind("CTRL");
				} else {
					listener.removeBind("CTRL");
				}
			}
		});
		
		altCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableVal, Boolean oldVal, Boolean newVal) {
				if (newVal) {
					listener.addBind("ALT");
				} else {
					listener.removeBind("ALT");
				}
			}
		});
		
		bindsComboBox0.valueProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue ov, String oldStr, String newStr) {
				listener.removeBind(oldStr);
				if (!newStr.equals("")) {
					listener.addBind(newStr);
				}
			}    
		});
		
		bindsComboBox1.valueProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue ov, String oldStr, String newStr) {
				listener.removeBind(oldStr);
				if (!newStr.equals("")) {
					listener.addBind(newStr);
				}
			}    
		});

		
		sayingTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				listener.changeSaying(sayingTextField.getText() + ke.getCharacter());
			}
		});
	}
}