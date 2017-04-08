import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract  class ReadOmimOnto {

	/* Take a string like "Cxxxxxxx" and return ArrayList<String> of Preferred Labels and Synonyms like ["Cataract, BLABLA","Blabla, adfksmldfk"] */


	public static ArrayList<String> CUIToPreferredLabel(String CUI) {

        String csvFile = "omim_onto.csv";
        BufferedReader br = null;
        String line ;
        String separator = ",";
        ArrayList<String> label = new ArrayList<String>();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as a separator
                String[] symptom = line.split(separator);
            	for(int i = 0; i < symptom.length; i++){
            		if(symptom[i].equals(CUI)){
            			//System.out.println("Symptom [CUI = " + symptom[i] + " , Preferred Label =" + symptom[1] + "]");
            			for(int j = 1; j < i-3; j++){
            				/*Remi l'affichage des "" n'etait pas du a ton print
            				 * Dans le fichier les synonymes sont delimité par des "
            				 * du coup le dernier que tu prenais n'étais pas un synonyme mais
            				 * la definition
            				 */
            				symptom[j]=symptom[j].replace("\"","");
            				symptom[j]=symptom[j].replace(" ","");
            				//System.out.println("word "+symptom[j]);
            				label.add(symptom[j]);
            			}
            			//System.out.println(label);
                    	return label;
            		}
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //System.out.println("No CUI does match");
        return null;

    }
	
	public static void main (String[] args){
		long startTime = System.nanoTime();
		ArrayList<String> liste=CUIToPreferredLabel("C1418359");
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println(duration/Math.pow(10,9));
		for(String s : liste){
			System.out.println(s);
		}
		
	}
	
}
