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
	public String userInput;


	@Override
	public void start(Stage primaryStage) {
    	this.primaryStage = primaryStage;
    	this.primaryStage.setTitle("GMD 2017");


    	showPersonOverview();
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
 	* Load the scene.
 	*/
	public void showPersonOverview() {
    	try {
        	// Load person overview.
        	FXMLLoader loaderRecherche = new FXMLLoader();
        	loaderRecherche.setLocation(App.class.getResource("Interface.fxml"));
        	BorderPane rechercheOverview = (BorderPane) loaderRecherche.load();

        	// Set search bar into the top of root layout.
        	Scene scene = new Scene(rechercheOverview);
        	primaryStage.setScene(scene);
        	primaryStage.show();


        	// Give the controller access to the main app.
        	Interface recherche = loaderRecherche.getController();
        	recherche.setMain(this);

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