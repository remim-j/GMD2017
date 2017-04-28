package View;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import body.ResultsLists;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
	private ArrayList<String> normOrigin;

	private HashMap<String,ArrayList<String>> results;

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
    public TableView<String> Diseases;

    @FXML
    public TableView<String> Medecine;

    @FXML
    public TableView<String> SideEffect;

    @FXML
    public TableView<String> OriginOfSideEffect;

    @FXML
    public TableView<String> OriginOfMedecine;

    @FXML
    public TableView<String> OriginOfDisease;

    @FXML
    Tab Dis = new Tab();

    @FXML
    Tab Med = new Tab();

    @FXML
    Tab SE = new Tab();


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
	    TableColumn<String,String> firstNameColDis = new TableColumn<String,String>("Name");
	    firstNameColDis.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
	    TableColumn<String,String> secNameColDis = new TableColumn<String,String>("Origin of the data");
	    secNameColDis.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
	    Diseases.getColumns().addAll(firstNameColDis);
	    OriginOfDisease.getColumns().addAll(secNameColDis);
	    TableColumn<String,String> firstNameColSE = new TableColumn<String,String>("Name");
	    firstNameColSE.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
	    TableColumn<String,String> secNameColSE = new TableColumn<String,String>("Origin of the data");
	    secNameColSE.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
	    SideEffect.getColumns().addAll(firstNameColSE);
	    OriginOfSideEffect.getColumns().addAll(secNameColSE);
	    TableColumn<String,String> firstNameColMed = new TableColumn<String,String>("Name");
	    firstNameColMed.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
	    TableColumn<String,String> secNameColMed = new TableColumn<String,String>("Origin of the data");
	    secNameColMed.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
	    Medecine.getColumns().addAll(firstNameColDis);
	    OriginOfMedecine.getColumns().addAll(secNameColDis);
	    Med.getColumns().addAll(firstNameColMed, secNameColMed);
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

    public void setMain(App App) {
    	this.App = App;
	}

}