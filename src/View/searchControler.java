package View;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class searchControler {
	
	
private App App;

@FXML Button search;
@FXML TextField searchField;
@FXML ImageView image;

	 public void setMain(App App) {
	    	this.App = App;
	    	Image image1 = new Image("file:image.jpg");
			image.setImage(image1);

	 }
	 
	 public void goSearch(){
		
		 App.setUserInput(searchField.getText());
		 App.changeScene(1);
		 
	 }
	 
	 
	 
}
