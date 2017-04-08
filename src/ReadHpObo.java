import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * 
 * use : first "new ReadHpObo()" to initialize drugIds
 * 		 then "getSynonyms(name)"
 * 			  "getId(name)"
 * 
 * input : symptom, a String describing a request of the user, such as "Abnormality of the toenails"
 * output : a array containing the symptom and the synonyms of the symptom, such as "[Abnormality of the toenail, Abnormality of the toenails]"
 *
 * input : symptom, a String describing a request of the user, such as "Abnormality of the toenails"
 * output : the id of this symptom, such as "HP:0008388"
 *
 */

public abstract class ReadHpObo {

	private static String hpobo_path = "hp_obo_file";
	private static HashMap<String, String> drugIds = new HashMap<String, String>(); // we want data like id_drug=name_drug inside
	private static HashMap<String,  ArrayList<String>> synonymsList = new HashMap<String, ArrayList<String>>();
	
	public static void ReadHpObo() throws IOException {
		
		int i,n;
		String s = "", key = "", name = "";
		ArrayList<String> temp = new ArrayList<String>();
		BufferedReader bf = new BufferedReader(new FileReader(hpobo_path)); // reading hpobo_path
		String line = bf.readLine();
		Scanner sc = new Scanner(line);
		
		//System.out.println("Initialising drugIds..."); // to delete later
		
		while ((line = bf.readLine()) != null) { // reading each line
			if (line.length() > 7) {
				if (line.substring(0, 2).equals("id")) {
					//System.out.print(line.substring(4)+" : "); // to delete later
					key = line.substring(4);
					
				} else if (line.substring(0,4).equals("name")) {
					if (!temp.isEmpty()) {
						synonymsList.put(name,temp);
						temp = new ArrayList<String>();
					}
					name = line.substring(6);
					//System.out.println(name); // to delete later
					drugIds.put(key,name);
					
				} else if (line.substring(0,7).equals("synonym")) {
					line = line.substring(10);
					sc = new Scanner(line);
					n = line.length();
					s = "";
					
					for (i=0; i<n; i++) { // studying each character of the line
						if (line.charAt(i) == '"') {
						//	System.out.println(key+" : "+s); // to delete later
							drugIds.put(key,s);
							temp.add(s);
							break;
						} else {
							s = s + line.substring(i,i+1);
						}
					}
				}
			}			
		}
		bf.close();
		
		//System.out.println("... drugIds initialized.\n"); // to delete later
	}
	
	
	// used to get the id corresponding to the name
	public static String getId(String value) {
		for (Entry<String,String> entry : drugIds.entrySet()) { //studying each entry of the hashmap
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return "none";
	}
	
	
	// used to get the synonyms corresponding to the name
	public static ArrayList<String> getSynonyms(String name) {
		boolean add = true;
		ArrayList<String> name_synonyms = synonymsList.get(name);
		if (name_synonyms != null) {
			for (String synonym : name_synonyms) {
				if (synonym.equals(name)) {
					add = false;
				}
			} if (add) {
				name_synonyms.add(0,name);
			}
		} else {
			name_synonyms = new ArrayList<String>();
			name_synonyms.add(name);
		}
		return name_synonyms;
	}
	
	
	// to delete later
	public static void main (String[] args) throws IOException {
		long startTime = System.nanoTime();
		ReadHpObo();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		//System.out.println(duration/Math.pow(10,9));		String value = "Annular pancreas"; //"HP:0001734"
		String value2 = "Abnormality of the toenails"; //"HP:0008388"
		String value3 = "Supernumerary metacarpal bones";
		String value4 = "Aplasia/Hypoplasia of the nails";
		/*System.out.println("Output \""+getId(value)+"\" corresponds to input \""+value+"\".");
		*///System.out.println("Output \""+getId(value2)+"\" corresponds to input \""+value2+"\".");
		System.out.println("Output \""+getSynonyms(value3)+"\" is the array of name and synonyms corresponding to input \""+value3+"\".");
		/*System.out.println("Output \""+getSynonyms(value4)+"\" is the array of name and synonyms corresponding to input \""+value4+"\".");*/
		
		
		//An example of req
		
		
	}
}

