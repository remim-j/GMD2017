package View;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import body.ResultsLists;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

public class Interface implements Initializable{

	private ResultsLists possibleDiseases =new  ResultsLists();
	private ResultsLists possibleOriginOfSideEffect =new  ResultsLists();
	private ResultsLists usefulMedecines =new  ResultsLists();

	private ArrayList<String> nameDisease;
	private ArrayList<ArrayList<String>> origin;

	private HashMap<String,ArrayList<String>> results;

	private ArrayList<String> nameSideEffect;
	private ArrayList<ArrayList<String>> originSE;

	private ArrayList<String> nameMedecine;
	private ArrayList<ArrayList<String>> originMedecine;

    private String userInput;

    @FXML
    private TextField UserInput;

    @FXML
    public ListView<String> Diseases;

    @FXML
    public ListView<String> Medecine;

    @FXML
    public ListView<String> SideEffect;


	ArrayList<String> disease;

	private App App;


	public void bouton(ActionEvent e){	//Get the input of the user, and launch the research
		userInput = UserInput.getText();
		body.GlobalClass.userInput = userInput;
		int n = disease.size();		//Initializing the arrayList
		for ( int i = 0; i < n; i++){
			disease.remove(0);
		}

		possibleDiseases = body.GlobalClass.possibleDiseases;
		possibleOriginOfSideEffect = body.GlobalClass.possibleOriginOfSideEffect;
		usefulMedecines = body.GlobalClass.usefulMedecines;

		dispDiseases(possibleDiseases);
		dispSideEffect(possibleOriginOfSideEffect);
		dispMedecine(usefulMedecines);

	}

////////////////////////////////////////////////////////////////Displaying the results ////////////////////////////////////////////////////////////

    private void dispMedecine(ResultsLists usefulMedecines2) {
    	HashMap<String,ArrayList<String>> hashMap = usefulMedecines2.hashmap;
    	for (String mapKey : hashMap.keySet()) {
    		originMedecine.add(hashMap.get(mapKey));
    		nameMedecine.add(mapKey);
    	}

	}


	private void dispSideEffect(ResultsLists possibleOriginOfSideEffect2) {
		HashMap<String,ArrayList<String>> hashMap = possibleOriginOfSideEffect2.hashmap;
    	for (String mapKey : hashMap.keySet()) {
    		originSE.add(hashMap.get(mapKey));
    		nameSideEffect.add(mapKey);
    	}
	}


	private void dispDiseases(ResultsLists possibleDiseases2) {
		HashMap<String,ArrayList<String>> hashMap = possibleDiseases2.hashmap;
    	for (String mapKey : hashMap.keySet()) {
    		origin.add(hashMap.get(mapKey));
    		nameDisease.add(mapKey);
    	}
	}


	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> oNameDisease = FXCollections.observableArrayList(nameDisease);
		ObservableList<String> oNameSideEffect = FXCollections.observableArrayList(nameSideEffect);
		ObservableList<String> oNameMedecine = FXCollections.observableArrayList(nameMedecine);
		Diseases.setItems(oNameDisease);
	    SideEffect.setItems(oNameSideEffect);
	    Medecine.setItems(oNameMedecine);
    }

    public void setMain(App App) {
    	this.App = App;
	}

}