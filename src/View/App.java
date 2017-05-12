package View;

import java.io.IOException;




import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class App extends Application {


	private Stage primaryStage;
	private BorderPane rootLayout;
	String userInput="";
	AnchorPane recherche;
	
	
	
	
	@Override
	public void start(Stage primaryStage){
    	this.primaryStage = primaryStage;
    	this.primaryStage.setTitle("GMD 2017");
    	initRootLayout();
    	showScene();

	}
	private void showScene() {
    	try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("SearchView.fxml"));
            recherche = (AnchorPane) loader.load();
            rootLayout.setCenter(recherche);
            searchControler control = loader.getController();
            control.setMain(this);

        } catch (IOException e) {
            	e.printStackTrace();
		}
	}



	/**
 	* Initializes the root layout.
 	*/
	public void initRootLayout() {
    	try {
        	// Load root layout from fxml file.
        	FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(App.class.getResource("RootLayout.fxml"));
        	rootLayout = (BorderPane) loader.load();
        	// Show the scene containing the root layout.
        	Scene scene = new Scene(rootLayout);
        	primaryStage.setScene(scene);
        	primaryStage.show();
        	 primaryStage.show();
             primaryStage.setResizable(false);
             rootControler rootCont= loader.getController();
             //rootCont.setMain(this);   	
    	} catch (IOException e) {
        	e.printStackTrace();
    	}
	}
	


	/**
 	* Returns the main stage.
 	* @return
 	*/
	public Stage getPrimaryStage() {
    	return primaryStage;
	}


	public static void main(String[] args) {
    	launch(args);
	}
	
	 public void changeScene(int x){
	    	if(x==1){
	    		try {
	    			FXMLLoader loader = new FXMLLoader();
	            	loader.setLocation(App.class.getResource("interface.fxml"));
	            	recherche = (AnchorPane) loader.load();
		            rootLayout.setCenter(recherche);
	            	Interface interfaceControler= loader.getController();
	            	interfaceControler.setMain(this);
	            } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	    	}
	    	else if (x==0){
	    		try {
	    			FXMLLoader loader = new FXMLLoader();
	            	loader.setLocation(App.class.getResource("SearchView.fxml"));
	            	recherche = (AnchorPane) loader.load();
		            rootLayout.setCenter(recherche);
		            searchControler control = loader.getController();
		            control.setMain(this);
	            } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	 }
	 
	 public void setUserInput(String s){
			userInput=s;
		}
		public String getUserInput(){
			return userInput;
		}
}
