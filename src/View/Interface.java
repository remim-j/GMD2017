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

	private HashMap<String,Integer> provokedDiseases=new HashMap<String,Integer>();
	private HashMap<String,Integer> originOfSideEffect=new  HashMap<String,Integer>();
	private HashMap<String,Integer>  usefulMedecine=new HashMap<String,Integer>();
	private ArrayList<String> suggestedEntry=new ArrayList<String>();
	private ResultsLists possibleDiseases=new  ResultsLists();
	private ResultsLists possibleOriginOfSideEffect=new  ResultsLists();
	private ResultsLists usefulMedecines=new  ResultsLists();
    private String userInput;

    @FXML
    private TextField UserInput;

    @FXML
    public ListView<String> ProvokedDiseases;

    @FXML
    public ListView<String> UsefulMedecine;

    @FXML
    public ListView<String> OriginOfSideEffect;

    @FXML
    public ListView<String> SuggestedEntry;

    @FXML
    private WebView webviewVideo;

    @FXML
    private Label labelTitre;


	ObservableMap<String,Integer> disease = FXCollections.observableMap (provokedDiseases);
	ObservableMap<String,Integer> sideEffect = FXCollections.observableMap (originOfSideEffect);
	ObservableMap<String,Integer> medecine = FXCollections.observableMap (usefulMedecine);
	ObservableList<String> suggestion = FXCollections.observableList(suggestedEntry);


	private App App;


	public void bouton(ActionEvent e){	//Get the input of the user, and launch the search
		userInput = UserInput.getText();
		body.GlobalClass.userInput = userInput;
		int n = items.size();
		for ( int i = 0; i < n; i++){
			items.remove(0);
		}
		provokedDiseases = body.GlobalClass.provokedDiseases;
		originOfSideEffect = body.GlobalClass.originOfSideEffect;
		usefulMedecine = body.GlobalClass.usefulMedecine;
		possibleDiseases = body.GlobalClass.possibleDiseases;
		possibleOriginOfSideEffect = body.GlobalClass.possibleOriginOfSideEffect;
		usefulMedecines = body.GlobalClass.usefulMedecines;

		Iterator<provokedDiseases> iteratorResultatTest = videos.iterator();
		if (provokedDiseases.size() == 0) {
			System.out.println("Il n'y a pas de résultat");
		} else {
			while (iteratorResultatTest.hasNext()) {
				Video video = iteratorResultatTest.next();
				items.add(video.getTitle());
			}
		}
	    ProvokedDiseases.setItems(items);
	}


    public void initialize(URL arg0, ResourceBundle arg1) {
	    ProvokedDiseases.setItems(items);
	    OriginOfSideEffect.setItems(itemsSE);
	    App.getUsefulMedecine().loadVideos(App.getPathUsefulMedecine());
	    for ( Video fav : App.getUsefulMedecine() ){
	    	itemsMedecine.add(fav.getTitle());
	    }
	    UsefulMedecine.setItems(itemsMedecine);
    }

    public void setMain(App App) {
    	this.App = App;
	}

    public void play(ActionEvent e){
    	int select;
    	select = ProvokedDiseases.getSelectionModel().getSelectedIndex();
    	if ( select >=0 ){
        	webviewVideo.getEngine().load((videos.getVideo(select).getUrlVideo()));
        	labelTitre.setText(items.get(select));
    	}
    }

//////////////////////////////////////////////////////////////// Gestion des favoris ////////////////////////////////////////////////////////////

    public void playFav(ActionEvent e){
    	int select;
    	select = UsefulMedecine.getSelectionModel().getSelectedIndex();
    	if ( select >=0 ){
        	webviewVideo.getEngine().load(App.getUsefulMedecine().getVideo(select).getUrlVideo());
        	labelTitre.setText(itemsMedecine.get(select));
    	}
    }


    public void ajouterFav(ActionEvent m){
    	int select;
    	select = ProvokedDiseases.getSelectionModel().getSelectedIndex();
    	if ( select >= 0 ){
    		itemsMedecine.add(videos.getVideo(select).getTitle());
    		App.getUsefulMedecine().addVideo(videos.getVideo(select));
    	}
    	UsefulMedecine.setItems(itemsMedecine);
		App.getUsefulMedecine().saveUsefulMedecine(App.getPathUsefulMedecine());
    }

    public void removeFav(ActionEvent m){
    	int select;
    	select = UsefulMedecine.getSelectionModel().getSelectedIndex();
    	if ( select >= 0 ){
    		itemsMedecine.remove(select);
    		App.getUsefulMedecine().removeVideo(App.getUsefulMedecine().getVideo(select));
    	}
    	UsefulMedecine.setItems(itemsMedecine);
		App.getUsefulMedecine().saveUsefulMedecine(App.getPathUsefulMedecine());
    }

////////////////////////////////////////////////////// Gestion de la playlist ////////////////////////////////////////////////////////////

    public void playOriginOfSideEffect(ActionEvent e){
    	int select;
    	select = OriginOfSideEffect.getSelectionModel().getSelectedIndex();
    	if (select >=0){
        	webviewVideo.getEngine().load(App.getOriginOfSideEffect().getVideo(select).getUrlVideo());
        	labelTitre.setText(itemsPlay.get(select));
    	}else{
    		webviewVideo.getEngine().load(App.getOriginOfSideEffect().getVideo(0).getUrlVideo());
        	labelTitre.setText(itemsPlay.get(0));
    	}
    }

    public void ajoutPlay(ActionEvent m){
    	int select;
    	select = ProvokedDiseases.getSelectionModel().getSelectedIndex();
    	if(select >= 0){
    		itemsPlay.add(videos.getVideo(select).getTitle());
    		App.getOriginOfSideEffect().addVideo(videos.getVideo(select));
    	}
    	OriginOfSideEffect.setItems(itemsPlay);
    }

    public void removePlay(ActionEvent m){
    	int select;
    	select = OriginOfSideEffect.getSelectionModel().getSelectedIndex();
    	if (select >= 0){
    		itemsPlay.remove(select);
    		App.getOriginOfSideEffect().removeVideo(videos.getVideo(select));
    	}
    	OriginOfSideEffect.setItems(itemsPlay);
    }

    public void ajoutPlayFromFav(ActionEvent m){
    	int select;
    	select = UsefulMedecine.getSelectionModel().getSelectedIndex();
    	if(select >= 0){
    		itemsPlay.add(App.getUsefulMedecine().getVideo(select).getTitle());
    		App.getOriginOfSideEffect().addVideo(App.getUsefulMedecine().getVideo(select));
    	}
    	OriginOfSideEffect.setItems(itemsPlay);
    }

    public void suivPlay(ActionEvent m){
    	int select;
    	select = OriginOfSideEffect.getSelectionModel().getSelectedIndex();
    	if (select >=0 && select < itemsPlay.size()-1 && itemsPlay.size()-1 >= 0){ //If the selected vid is not the last vid
        	// Remove the selected video
        	itemsPlay.remove(0);
    		App.getOriginOfSideEffect().removeVideo(App.getOriginOfSideEffect().getVideo(0));
    		// Play the next video
        	webviewVideo.getEngine().load(App.getOriginOfSideEffect().getVideo(0).getUrlVideo());
        	labelTitre.setText(itemsPlay.get(0));
        	OriginOfSideEffect.setItems(itemsPlay);
    	} else if (select >= 0 && itemsPlay.size()-1 == 0 && select <= itemsPlay.size()-1){
        	itemsPlay.remove(0);
    		App.getOriginOfSideEffect().removeVideo(App.getOriginOfSideEffect().getVideo(0));
    		webviewVideo.getEngine().load(null);
    		labelTitre.setText("");
    	}
    	OriginOfSideEffect.setItems(itemsPlay);
    }

    ////////////////////////////////////////////////////////// Téléchargements //////////////////////////////////////////////////////////////////

    public void telechargerFav(ActionEvent m){
    	int select;
    	select = UsefulMedecine.getSelectionModel().getSelectedIndex();
    	if (select >=0){
        	webviewVideo.getEngine().load(App.getUsefulMedecine().getVideo(select).getUrlVideo());
        	labelTitre.setText(itemsMedecine.get(select));
    	}
    }

    public void telechargerRes(ActionEvent m){
    	int select;
    	select = ProvokedDiseases.getSelectionModel().getSelectedIndex();
    	if(select >= 0){
    		itemsPlay.add(videos.getVideo(select).getTitle());
    		App.getOriginOfSideEffect().addVideo(videos.getVideo(select));
    	}
    	OriginOfSideEffect.setItems(itemsPlay);
    }

    public void telechargerPlay(ActionEvent m){
    	int select;
    	select = OriginOfSideEffect.getSelectionModel().getSelectedIndex();
    	if(select >= 0){
    		itemsPlay.add(videos.getVideo(select).getTitle());
    		App.getOriginOfSideEffect().addVideo(videos.getVideo(select));
    	}
    	OriginOfSideEffect.setItems(itemsPlay);
    }
}