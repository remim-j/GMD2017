package View;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import body.ResultsLists;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import body.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import View.App;

public class Interface implements Initializable{

	private ResultsLists possibleDiseases = new  ResultsLists();
	private ResultsLists possibleOriginOfSideEffect = new  ResultsLists();
	private ResultsLists usefulMedecines = new  ResultsLists();

	private ArrayList<String> disease;

	private ArrayList<String> sideEffect;

	private ArrayList<String> medecine;

    private String userInput;

    @FXML
    private TextField UserInput;

    @FXML
    public ListView<String> Diseases;

    @FXML
    public ListView<String> Medecine;

    @FXML
    public ListView<String> SideEffect;

    @FXML
    public ProgressIndicator pi = new ProgressIndicator(0);

    @FXML
    public VBox hb = new VBox();

	body.GlobalClass globalClass=new GlobalClass();

	public App App;

	ObservableList<String> oDisease = FXCollections.observableArrayList();
	ObservableList<String> oSideEffect = FXCollections.observableArrayList();
	ObservableList<String> oMedecine = FXCollections.observableArrayList();

	public void bouton(ActionEvent e){	//Get the input of the user, and launch the research

		int n = oDisease.size();
		for ( int i = 0; i < n; i++){
			oDisease.remove(0);
		}
		n = oSideEffect.size();
		for ( int i = 0; i < n; i++){
			oSideEffect.remove(0);
		}
		n = oMedecine.size();
		for ( int i = 0; i < n; i++){
			oMedecine.remove(0);
		}

		/*clear last search*/
		clearItems();
		userInput = UserInput.getText();
		//body.GlobalClass.userInput = userInput;

		globalClass.doSearch(userInput);

		possibleDiseases = globalClass.getPossibleDiseases();
		possibleOriginOfSideEffect = globalClass.getPossibleOriginOfSideEffect();
		usefulMedecines = globalClass.getUsefulMedecines();

		dispDiseases(possibleDiseases);
		dispSideEffect(possibleOriginOfSideEffect);
		dispMedecine(usefulMedecines);

		//Hiding loading pane
		hb.getChildren().remove(pi);
		hb.setVisible(false);
		hb.managedProperty().bind(hb.visibleProperty());

		setItems();
		System.out.println("J'ai fini");

	}

////////////////////////////////////////////////////////////////Displaying the results ////////////////////////////////////////////////////////////

    private void dispMedecine(ResultsLists usefulMedecines2) {
    	HashMap<String,ArrayList<String>> hashMap = usefulMedecines2.hashmap;
    	for (String mapKey : hashMap.keySet()) {
    		medecine.add(mapKey + normalize(hashMap.get(mapKey)));
    	}
	}


	private void dispSideEffect(ResultsLists possibleOriginOfSideEffect2) {
		HashMap<String,ArrayList<String>> hashMap = possibleOriginOfSideEffect2.hashmap;
    	for (String mapKey : hashMap.keySet()) {
    		sideEffect.add(mapKey + normalize(hashMap.get(mapKey)));
    	}
	}


	private void dispDiseases(ResultsLists possibleDiseases2) {
		HashMap<String,ArrayList<String>> hashMap = possibleDiseases2.getHashmap();
    	for (String mapKey : hashMap.keySet()) {
    		disease.add(mapKey + normalize(hashMap.get(mapKey)));
    	}
	}


	public void initialize(URL arg0, ResourceBundle arg1) {

    }

    public void setMain(App App) {
    	this.App = App;

    	//initialize all variables

    	disease = new ArrayList<String>();

    	sideEffect = new ArrayList<String>();

    	medecine = new ArrayList<String>();
	}


	public String normalize(ArrayList<String> originA){
		String normOriginA = "";
		if(!originA.equals("")){
			for(String s : originA){
				System.out.println(s);
				normOriginA = normOriginA + ", " + s;
			}
		}
		return normOriginA;
	}


	public void setItems(){

		oDisease = FXCollections.observableArrayList(disease);
		oSideEffect = FXCollections.observableArrayList(sideEffect);
		oMedecine = FXCollections.observableArrayList(medecine);

		Diseases.setItems(oDisease);
	    SideEffect.setItems(oSideEffect);
	    Medecine.setItems(oMedecine);
	}


	public void clearItems(){

		globalClass.clearList();
		Diseases.getItems().clear();
		Diseases.refresh();
		SideEffect.getItems().clear();
		SideEffect.refresh();
		Medecine.getItems().clear();
		Medecine.refresh();
	}
}
