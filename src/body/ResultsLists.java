package body;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


public class ResultsLists {


	public HashMap<String,ArrayList<String>> hashmap;

	public ResultsLists(){
		hashmap=new HashMap<String,ArrayList<String>>();
	}

	/*we add a results to the result list*/
	public void add(String name,String origin){
		if (hashmap.containsKey(name)){
			ArrayList<String> originList=hashmap.get(name);
			if (!originList.contains(origin)){
				originList.add(origin);
			}
		}else{
			ArrayList<String> originList=new ArrayList<String>();
			originList.add(origin);
			hashmap.put(name,originList);
		}
	}


	public HashMap<String,ArrayList<String>> getHashmap(){
		return hashmap;
	}

	public  List<Entry<String, ArrayList<String>>> sortHashMap(){
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


	public String toStringArrayList(ArrayList<String> array){
		String s="";
		for (String s1:array){
			s=s+s1+"\n";
		}
		return s;
	}

	public String toString(){
		String returnedString="";
		List<Entry<String, ArrayList<String>>> entries=sortHashMap();
		int i=1;
		 for ( Entry<String, ArrayList<String>> entry : entries) {
			 returnedString= returnedString+ i+"- : "+entry.getKey() + " : " + entry.getValue().size()+"\n"
					 		+toStringArrayList(entry.getValue());
			   i++;
			  }
		return returnedString;

	}

}


