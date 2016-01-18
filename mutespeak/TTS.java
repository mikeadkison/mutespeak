package mutespeak;


import java.io.IOException;
import com.melloware.jintellitype.JIntellitype;
import com.melloware.jintellitype.HotkeyListener;
import java.io.File;
import java.util.Map;
import java.util.HashMap;

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

import java.util.prefs.Preferences;

public class TTS extends Application {
	private List<BindHBox> bindHBoxes;
	private KeyListener listener;
	private static final int NUM_FIELDS = 20;

	public static void main(String[] args) {
		
		launch(args);
		while(true) {
			
		}
		
	}
	
	@Override
    public void start(Stage stage) throws IOException {
		Preferences preferences = Preferences.userRoot().node(this.getClass().getName()); //used to save/load fields
		listener = new KeyListener();
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent e) {
				
				for (int i = 0; i < bindHBoxes.size(); i++) {
					preferences.put("textField" + i, bindHBoxes.get(i).sayingTextField.getText());
					preferences.putBoolean("ctrlCB" + i, bindHBoxes.get(i).cntrlCB.isSelected());
					preferences.putBoolean("altCB" + i, bindHBoxes.get(i).altCB.isSelected());
				}
				
				try {
					Platform.exit();
					System.exit(0);
				} 
				catch (Exception e1) {
					e1.printStackTrace();
				}
				
				
			}
	    });
		
		GridPane gridPane = new GridPane();
		
		CheckBox bindToggle = new CheckBox("binds enabled");
		bindToggle.setSelected(true);
		
		bindHBoxes = new ArrayList<>();
		for (int i = 0; i < NUM_FIELDS; i++) {
			BindHBox bindHBox = new BindHBox(listener, bindHBoxes, bindToggle);
			gridPane.add(bindHBox, 0, i);
			bindHBoxes.add(bindHBox);
			bindHBox.sayingTextField.setText(preferences.get("textField" + i, ""));
			bindHBox.cntrlCB.setSelected((preferences.getBoolean("ctrlCB" + i, false)));
			bindHBox.altCB.setSelected((preferences.getBoolean("altCB" + i, false)));
		}
		
		listener.bindHBoxes = bindHBoxes;
		
		
		bindToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableVal, Boolean oldVal, Boolean newVal) {
				if (true == newVal) { //turn binds on
					listener.bindsEnabled = true;
				} else { //turn binds off
					listener.bindsEnabled = false;
				}
			}
		});

		
		VBox rootVBox = new VBox();
		rootVBox.getChildren().add(gridPane);
		rootVBox.getChildren().add(bindToggle);
		
		Scene scene = new Scene(rootVBox, 500, 600, Color.BLACK);
		stage.setScene(scene);
		stage.show();
	}

}