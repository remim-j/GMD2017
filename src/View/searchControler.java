package View;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class searchControler {
	
	
private App App;

@FXML Button search;
@FXML TextField searchField;

	 public void setMain(App App) {
	    	this.App = App;

	 }
	 
	 public void goSearch(){
		
		 App.setUserInput(searchField.getText());
		 App.changeScene(1);
		 this.notifyAll();
	 }
	 
	 
	 
}
