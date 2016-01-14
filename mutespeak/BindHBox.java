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

public class BindHBox extends HBox {
	protected RadioButton radioButton;
	protected Label bindCharactersLabel;
	protected final KeyListener listener = new KeyListener();
	private List<BindHBox> allHBoxes;
	private CheckBox cntrlCB;
	private CheckBox altCB;
	
	private static final List<String> ALLOWED_BINDS = new ArrayList<>();
	
	public BindHBox(ToggleGroup toggleGroup, List<BindHBox> allHBoxes, CheckBox enabledToggle) {
		this.allHBoxes = allHBoxes;
		
		Label sayingLabel = new Label("voice saying: ");
		this.getChildren().add(sayingLabel);
		
		TextField sayingTextField = new TextField ();
		this.getChildren().add(sayingTextField);
		
		Label bindLabel = new Label("bind: ");
		this.getChildren().add(bindLabel);
		
		radioButton = new RadioButton();
		radioButton.setToggleGroup(toggleGroup);
		this.getChildren().add(radioButton);

		bindCharactersLabel = new Label("");
		this.getChildren().add(bindCharactersLabel);
		
		cntrlCB = new CheckBox("ctrl");
		this.getChildren().add(cntrlCB);
		
		altCB = new CheckBox("alt");
		this.getChildren().add(altCB);
		
		cntrlCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableVal, Boolean oldVal, Boolean newVal) {
				changeBinding(bindCharactersLabel.getText());
			}
		});
		
		altCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableVal, Boolean oldVal, Boolean newVal) {
				changeBinding(bindCharactersLabel.getText());
			}
		});
		
		radioButton.setOnKeyTyped(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				bindCharactersLabel.setText(ke.getCharacter());
				String bindString = ke.getCharacter();
				
				changeBinding(bindString);
				
				for (BindHBox hbox: allHBoxes) {
					if (hbox != BindHBox.this) {
						KeyListener listener = hbox.listener;
						if (listener.getBind() != null && listener.getBind().equals(BindHBox.this.listener.getBind())) {
							listener.removeBind();
							hbox.bindCharactersLabel.setText("");
						}
					}
					
				}
				
				if (!enabledToggle.isSelected()) {
					listener.disableBind();
				}
			}
		});
		
		sayingTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				listener.changeSaying(sayingTextField.getText() + ke.getCharacter());
			}
		});
	}
	
	/**
	 * @param baseBind - the base key before alt and ctrl key modifiers added
	 */
	private void changeBinding(String baseBind) {
		System.out.println("change binding");
		if (cntrlCB.isSelected()) {
			System.out.println("cntrl");
			baseBind = "CTRL+" + baseBind;
		}
				
		if (altCB.isSelected()) {
			baseBind = "ALT+" + baseBind;
		}
		System.out.println(baseBind);
		listener.changeBind(baseBind);
	}
}