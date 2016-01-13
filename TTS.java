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

public class TTS extends Application {
	private KeyListener listener;
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
   
		GridPane rootGridPane = new GridPane();
		ToggleGroup toggleGroup = new ToggleGroup();
		
		listener = new KeyListener();
		
		for (int i = 0; i < 20; i++) {
			Label sayingLabel = new Label("voice saying: ");
			rootGridPane.add(sayingLabel, 0, i);
			
			final TextField sayingTextField = new TextField ();
			rootGridPane.add(sayingTextField, 1, i);
			
			Label bindLabel = new Label("bind: ");
			rootGridPane.add(bindLabel, 2, i);
			
			final RadioButton radioButton = new RadioButton();
			radioButton.setToggleGroup(toggleGroup);
			rootGridPane.add(radioButton, 3, i);

			final Label bindCharactersLabel = new Label("");
			rootGridPane.add(bindCharactersLabel, 4, i);
			
			radioButton.setOnKeyTyped(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent ke) {
					bindCharactersLabel.setText(ke.getCharacter());
					listener.bindToSaying(ke.getCharacter(), sayingTextField);
				}
			});
			
			sayingTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent ke) {
					if (bindCharactersLabel.getText().length() > 0) {
						listener.bindToSaying(bindCharactersLabel.getText(), sayingTextField);
					}
				}
			});
			
			
		}
		
		
		
		
		Scene scene = new Scene(rootGridPane, 500, 500, Color.BLACK);
		stage.setScene(scene);
		stage.show();
	}
}