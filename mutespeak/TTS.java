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

public class TTS extends Application {
	List<BindHBox> bindHBoxes;
	
	public static void main(String[] args) {
		launch(args);
		while(true) {
			
		}
		
	}
	
	@Override
    public void start(Stage stage) throws IOException {
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
		{
			public void handle(WindowEvent e){
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
		for (int i = 0; i < 20; i++) {
			BindHBox bindHBox = new BindHBox(bindHBoxes, bindToggle);
			gridPane.add(bindHBox, 0, i);
			bindHBoxes.add(bindHBox);
			
			
		}
		
		
		bindToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableVal, Boolean oldVal, Boolean newVal) {
				if (true == newVal) { //turn binds on
					enableAllHBoxBinds();
				} else { //turn binds off
					for (BindHBox hbox: bindHBoxes) {
						hbox.listener.disableBinds();
					}
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
	
	private void enableAllHBoxBinds() {
		for (BindHBox hbox: bindHBoxes) {
			hbox.listener.enableBinds();
		}
	}
}