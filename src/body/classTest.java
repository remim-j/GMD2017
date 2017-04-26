package body;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class classTest {

	public static void main(String[] args){
		// Map de test
		  final Map<String, Integer> map = new HashMap<String, Integer>();
		  map.put("toto", 1);
		  map.put("tata", 3);
		  map.put("tutu", 2);
		  map.put("titi", 3);
		  map.put("mama", 4);
		 
		  // Ajout des entrées de la map à une liste
		  final List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(map.entrySet());
		 
		  // Tri de la liste sur la valeur de l'entrée
		  Collections.sort(entries, new Comparator<Entry<String, Integer>>() {
		    public int compare(final Entry<String, Integer> e1, final Entry<String, Integer> e2) {
			    	if(e1.getValue().compareTo(e2.getValue())==-1){
			    		return 1;
			    	}
			    	else{ if (e1.getValue().compareTo(e2.getValue())==1){
			    		return -1;
			    	}
			    	else{
			    		return  e1.getValue().compareTo(e2.getValue());
			    	}
		    	}
		     
		    }
		  });
		 
		  // Affichage du résultat
		  for (final Entry<String, Integer> entry : entries) {
		    System.out.println(entry.getKey() + " " + entry.getValue());
		  }		
		
		
	}
	
	public  List<Entry<String, ArrayList<String>>> sortHashMap(HashMap<String,ArrayList<String>> hashmap){
		 final List<Entry<String, ArrayList<String>>> entries = new ArrayList<Entry<String, ArrayList<String>>>(hashmap.entrySet());
		 
		  // Tri de la liste sur la valeur de l'entrée
		  Collections.sort(entries, new Comparator<Entry<String, ArrayList<String>>>() {
		    public int compare(final Entry<String, ArrayList<String>> e1, final Entry<String, ArrayList<String>> e2) {
		    	
		    	/*here i inverse results in ortder to make order entries from the the highest to the lowest*/
			    	if(((Integer)e1.getValue().size()).compareTo((Integer)(e2.getValue().size()))==-1){
			    		return 1;
			    	}
			    	else if (((Integer)e1.getValue().size()).compareTo((Integer)(e2.getValue().size()))==1){
			    		return -1;
			    	}
			    	else{
			    		return  ((Integer)e1.getValue().size()).compareTo((Integer)(e2.getValue().size()));
			    	}
		    	}
		     
		    });
		  return entries;
	}
		  
	

}
