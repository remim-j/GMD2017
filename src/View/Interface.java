package View;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import body.GlobalClass;
import body.ResultsLists;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import body.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import View.App;

public class Interface {

	private ResultsLists possibleDiseases = new  ResultsLists();
	private ResultsLists possibleOriginOfSideEffect = new  ResultsLists();
	private ResultsLists usefulMedecines = new  ResultsLists();

	private ArrayList<String> disease;

	private ArrayList<String> sideEffect;

	private ArrayList<String> medecine;

@FXML Label tailleDisease;
@FXML Label tailleMedecine;
@FXML Label tailleSideEffect;

   

    @FXML
    public ListView<String> Diseases;

    @FXML
    public ListView<String> Medecine;

    @FXML
    public ListView<String> SideEffect;

    @FXML Label result;
    
    @FXML Label resultName;
    

   @FXML Label title;
   @FXML Label title2;

	body.GlobalClass globalClass=new GlobalClass();

	public App App;

	ObservableList<String> oDisease = FXCollections.observableArrayList();
	ObservableList<String> oSideEffect = FXCollections.observableArrayList();
	ObservableList<String> oMedecine = FXCollections.observableArrayList();

	/******** MAIN *************************************************/
	 public void setMain(App App) {
	    	this.App = App;
	    	
	    	//initialize all variables
	    	disease = new ArrayList<String>();
	    	sideEffect = new ArrayList<String>();
	    	medecine = new ArrayList<String>();
	    	
	    	String input=App.getUserInput();
	    	if (input.endsWith("*") ){
	    		input=input.substring(0,input.length()-2);
	    		globalClass.doSearch(input);
	    	}
	    	else{
	    		System.out.println("ici");
	    		globalClass.doSearchFIX(input);

	    	}
	    	
	    	
	    	//get disease from globlaClass//
	    	possibleDiseases = globalClass.getPossibleDiseases();
			possibleOriginOfSideEffect = globalClass.getpossibleOriginSideEffect();
			usefulMedecines = globalClass.getUsefulMedecines();

			//set items in different listView//
			dispDiseases(possibleDiseases);
			dispSideEffect(possibleOriginOfSideEffect);
			dispMedecine(usefulMedecines);
			setItems();
			
			tailleDisease.setText(""+possibleDiseases.hashmap.size()+ " results");
			tailleSideEffect.setText(""+possibleOriginOfSideEffect.hashmap.size() +" results");
			tailleMedecine.setText(""+usefulMedecines.hashmap.size()+ " results");
			
			
			resultName.setText(App.getUserInput());
			
		}


////////////////////////////////////////////////////////////////Displaying the results ////////////////////////////////////////////////////////////

    private void dispMedecine(ResultsLists usefulMedecines2) {
    	HashMap<String,ArrayList<String>> hashMap = usefulMedecines2.sort();
    	for (String mapKey : hashMap.keySet()) {
    		medecine.add(mapKey + normalize(hashMap.get(mapKey)));
    	}
	}


	private void dispSideEffect(ResultsLists possibleOriginOfSideEffect2) {
		HashMap<String,ArrayList<String>> hashMap = possibleOriginOfSideEffect2.sort();
    	for (String mapKey : hashMap.keySet()) {
    		sideEffect.add(mapKey + normalize(hashMap.get(mapKey)));
    	}
	}


	private void dispDiseases(ResultsLists possibleDiseases2) {
		HashMap<String,ArrayList<String>> hashMap = possibleDiseases2.sort();
    	for (String mapKey : hashMap.keySet()) {
    		disease.add(mapKey + normalize(hashMap.get(mapKey)));
    	}
	}


	
   

	public String normalize(ArrayList<String> originA){
		String normOriginA = "";
		if(!originA.equals("")){
			String origin="";
			for(String s : originA){
				if (origin.equals((""))){
					origin=origin+s;

				}
				else{
					origin=origin+" || "+s;
				}
				
			}
			normOriginA = normOriginA + "    (" + origin+")";
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
	
	public void research(){
		clearItems();
		App.changeScene(0);
	}


	public void clearItems(){

		globalClass.clearList();
		Diseases.getItems().clear();
		Diseases.refresh();
		SideEffect.getItems().clear();
		SideEffect.refresh();
		Medecine.getItems().clear();
		Medecine.refresh();
		
		
		for ( int i = 0; i < oDisease.size(); i++){
			oDisease.remove(0);
		}
		
		for ( int i = 0; i < oSideEffect.size(); i++){
			oSideEffect.remove(0);
		}
		
		for ( int i = 0; i < oMedecine.size(); i++){
			oMedecine.remove(0);
		}
	}
}
