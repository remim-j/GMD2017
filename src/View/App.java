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
	

	@Override
	public void start(Stage primaryStage){
    	this.primaryStage = primaryStage;
    	this.primaryStage.setTitle("GMD 2017");

    	try {
        	// Load overview.
        	FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(App.class.getResource("Interface.fxml"));
        	BorderPane recherche = (BorderPane) loader.load();

        	// Set search bar into the top of root layout.
        	Scene scene = new Scene(recherche);
        	primaryStage.setScene(scene);
        	primaryStage.show();


        	// Give the controller access to the main app.
        	Interface interfaceControler= loader.getController();
        	interfaceControler.setMain(this);

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
}
