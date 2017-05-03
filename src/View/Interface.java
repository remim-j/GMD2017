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

import javafx.scene.control.TextField;

import View.App;

public class Interface implements Initializable{

	private ResultsLists possibleDiseases = new  ResultsLists();
	private ResultsLists possibleOriginOfSideEffect = new  ResultsLists();
	private ResultsLists usefulMedecines = new  ResultsLists();

	private ArrayList<String> nameDisease;
	private ArrayList<ArrayList<String>> origin;
	private ArrayList<String> normOrigin;


	private ArrayList<String> nameSideEffect;
	private ArrayList<ArrayList<String>> originSE;
	private ArrayList<String> normOriginSE;

	private ArrayList<String> nameMedecine;
	private ArrayList<ArrayList<String>> originMedecine;
	private ArrayList<String> normOriginMed;

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
    public ListView<String> OriginOfSideEffect;

    @FXML
    public ListView<String> OriginOfMedecine;

    @FXML
    public ListView<String> OriginOfDisease;

	ArrayList<String> disease;

	public App App;


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

		normOriginMed = normalize(originMedecine);
		normOriginSE = normalize(originSE);
		normOrigin = normalize(origin);


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

    }

    public void setMain(App App) {
    	this.App = App;
	}


	public ArrayList<String> normalize(ArrayList<ArrayList<String>> originA){
		ArrayList<String> normOriginA = new ArrayList<String>();
		for(ArrayList<String> list : originA){
			StringBuilder sb = new StringBuilder();
			for (String s : list){
			    sb.append(s);
			    sb.append(", ");
			}
			normOriginA.add(sb.toString());
		}
		return normOriginA;
	}

	public void Recherche(){
		ObservableList<String> oNameDisease = FXCollections.observableArrayList(nameDisease);
		ObservableList<String> oNameSideEffect = FXCollections.observableArrayList(nameSideEffect);
		ObservableList<String> oNameMedecine = FXCollections.observableArrayList(nameMedecine);
		ObservableList<String> oOriginSE = FXCollections.observableArrayList(normOriginSE);
		ObservableList<String> oOrigin = FXCollections.observableArrayList(normOrigin);
		ObservableList<String> oOringinMedecine = FXCollections.observableArrayList(normOriginMed);
		Diseases.setItems(oNameDisease);
	    SideEffect.setItems(oNameSideEffect);
	    Medecine.setItems(oNameMedecine);
	    OriginOfSideEffect.setItems(oOriginSE);
	    OriginOfMedecine.setItems(oOringinMedecine);
	    OriginOfDisease.setItems(oOrigin);
	}
}