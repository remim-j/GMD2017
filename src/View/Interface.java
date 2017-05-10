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
	private ArrayList<String> nameDisease;
	private ArrayList<ArrayList<String>> origin;
	private ArrayList<String> normOrigin;


	private ArrayList<String> sideEffect;
	private ArrayList<String> nameSideEffect;
	private ArrayList<ArrayList<String>> originSE;
	private ArrayList<String> normOriginSE;

	private ArrayList<String> medecine;
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
    		originMedecine.add(hashMap.get(mapKey));
    		nameMedecine.add(mapKey);
    	}for(String s : nameMedecine){
    		medecine.add(s);
    	}
    	medecine.add("  ");
    	normOriginMed = normalize(originMedecine);
    	for(String s : normOriginMed){
    		medecine.add(s);
    	}
	}


	private void dispSideEffect(ResultsLists possibleOriginOfSideEffect2) {
		HashMap<String,ArrayList<String>> hashMap = possibleOriginOfSideEffect2.hashmap;
    	for (String mapKey : hashMap.keySet()) {
    		originSE.add(hashMap.get(mapKey));
    		nameSideEffect.add(mapKey);
    	}
    	for(String s : nameSideEffect){
    		sideEffect.add(s);
    	}
    	sideEffect.add("  ");
    	normOriginSE = normalize(originSE);
    	for(String s : normOriginSE){
    		sideEffect.add(s);
    	}
	}


	private void dispDiseases(ResultsLists possibleDiseases2) {
		HashMap<String,ArrayList<String>> hashMap = possibleDiseases2.getHashmap();
    	for (String mapKey : hashMap.keySet()) {
    		origin.add(hashMap.get(mapKey));
    		nameDisease.add(mapKey);
    	}
    	for(String s : nameDisease){
    		disease.add(s);
    	}
    	disease.add("  ");
    	normOrigin = normalize(origin);
    	for(String s : normOrigin){
    		disease.add(s);
    	}
	}


	public void initialize(URL arg0, ResourceBundle arg1) {

    }

    public void setMain(App App) {
    	this.App = App;

    	//initialize all variables
    	 nameDisease=new ArrayList<String>();
    	origin =new ArrayList<ArrayList<String>>();
    	normOrigin=new ArrayList<String>();


    	 nameSideEffect=new ArrayList<String>();
    	 originSE=new ArrayList<ArrayList<String>>();;
    	 normOriginSE=new ArrayList<String>();

    	 nameMedecine=new ArrayList<String>();
    	 originMedecine=new ArrayList<ArrayList<String>>();;
    	normOriginMed=new ArrayList<String>();
	}


	public ArrayList<String> normalize(ArrayList<ArrayList<String>> originA){
		ArrayList<String> normOriginA = new ArrayList<String>();
		if(originA != null){
			for(ArrayList<String> list : originA){
				StringBuilder sb = new StringBuilder();
				sb.append("(");
				for (String s : list){
				    sb.append(s);
				    sb.append(", ");
				}
				sb.append(")");
				normOriginA.add(sb.toString());
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