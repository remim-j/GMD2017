import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 * use : first "new ATC()" for initializing drugLabels
 * 		 then "getLabel(key)"
 * 
 * input : key, a string describing the id of the drug, such as "S01AA01"
 * output : label, a string describing the name of the drug, such as "Chloramphenicol"
 *
 */


public class ReadATC {
	
	private static String atc_path = "atc_file";
	private static HashMap<String, String> drugLabels = new HashMap<String, String>(); // we want data like id_drug=label_drug inside

	
	private ReadATC() throws IOException {
		
		BufferedReader bf = new BufferedReader(new FileReader(atc_path)); // reading atc_path
		String line = bf.readLine();
		Scanner sc = new Scanner(line);
		
		int n,i;
		String s = "";
		String key = "";
		
		System.out.println("Initialising drugLabels...");
		
		while ((line = bf.readLine()) != null) { // reading each line
			if (line.charAt(0) == 'E') { // this is an interesting line (see the hierarchy of the file for more understanding)
				n = line.length();
				i = 1;
				s = "";
				while (i<n) { // studying each character of the line
					if (line.charAt(i) == ' ' && key == "") { // gets the key, ie the first word after 'E'
						if (s.length() > 1) {
							System.out.print(s);
							key = s;
						}
						s = "";
						i++;
					} else if (line.charAt(i) == '(') { // things in brackets are not interesting for us
						s = s.substring(0,s.length());
						while (line.charAt(i) != ')') {
							i++;
						}
						i++;
					} else if (line.charAt(i) == '[') { // things in brackets are not interesting for us
						s = s.substring(0,s.length());
						while (line.charAt(i) != ']') {
							i++;
						}
						i++;
					} else { // gets the word (key or label) character by character. A label can contain spaces, but a key can't.
						s += line.charAt(i);
						i++;
					}
				}
			}
			if (s != "") { // avoid void labels
				System.out.println(" : "+s);
				drugLabels.put(key.trim(),s.trim());
			}
			s = "";
			key = "";
			sc.close();		
			
		}	
		bf.close();
		
		System.out.println("... drugLabels initialized.\n");
	}
	
	
	public String getLabel(String key) {
		return drugLabels.get(key);
	}
	
	// to delete later
	public static void main (String[] args) throws IOException {
		new ReadATC();
		String key = "S01AA01";
		String key2 = "C07BA07";
		System.out.println("\""+drugLabels.get(key)+"\" corresponds to "+key+".");
		System.out.println("\""+drugLabels.get(key2)+"\" corresponds to "+key2+".");
	}
	
}

