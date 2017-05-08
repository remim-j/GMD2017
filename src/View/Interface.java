package View;

import java.io.Closeable;
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
import javafx.scene.Node;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import body.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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

    @FXML
    public ProgressIndicator pi = new ProgressIndicator(0);

    @FXML
    public VBox hb = new VBox();


	ArrayList<String> disease=new ArrayList<String>();

	body.GlobalClass globalClass=new GlobalClass();

	public App App;


	public void bouton(ActionEvent e){	//Get the input of the user, and launch the research

		/*clear last search*/
		clearItems();
		userInput = UserInput.getText();
		//body.GlobalClass.userInput = userInput;

		globalClass.doSearch(userInput);
		int n = disease.size();		//Initializing the arrayList
		for ( int i = 0; i < n; i++){
			disease.remove(i);
		}

		possibleDiseases = globalClass.getPossibleDiseases();
		possibleOriginOfSideEffect = globalClass.getPossibleOriginOfSideEffect();
		usefulMedecines = globalClass.getUsefulMedecines();

		dispDiseases(possibleDiseases);
		dispSideEffect(possibleOriginOfSideEffect);
		dispMedecine(usefulMedecines);

		normOriginMed = normalize(originMedecine);
		normOriginSE = normalize(originSE);
		normOrigin = normalize(origin);
		hb.getChildren().remove(pi);
		hb.setVisible(false);
		hb.managedProperty().bind(hb.visibleProperty());
		/*hb.setManaged(false);
		pi.setVisible(false);
		pi.managedProperty().bind(hb.visibleProperty());
		//pi.setManaged(false);
		slider.setVisible(false);
		slider.managedProperty().bind(hb.visibleProperty());
		//slider.setManaged(false);*/
		setItems();
		System.out.println("J'ai fini");

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
		HashMap<String,ArrayList<String>> hashMap = possibleDiseases2.getHashmap();
    	for (String mapKey : hashMap.keySet()) {
    		origin.add(hashMap.get(mapKey));
    		nameDisease.add(mapKey);
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
				for (String s : list){
				    sb.append(s);
				    sb.append(", ");
				}
				normOriginA.add(sb.toString());
			}
		}

		return normOriginA;
	}

	public void setItems(){
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


	public void clearItems(){

		globalClass.clearList();
		Diseases.getItems().clear();
		Diseases.refresh();
		SideEffect.getItems().clear();
		SideEffect.refresh();
		Medecine.getItems().clear();
		Medecine.refresh();
		OriginOfSideEffect.getItems().clear();
		OriginOfMedecine.getItems().clear();
		OriginOfDisease.getItems().clear();
	}
}