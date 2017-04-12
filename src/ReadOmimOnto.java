import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract  class ReadOmimOnto {

	/* Take a string like "Cxxxxxxx" and return ArrayList<String> of Preferred Labels and Synonyms like ["Cataract, BLABLA","Blabla, adfksmldfk"] */


	public static String CUIToClassID(String CUI) {

        String csvFile = "C:/Users/user/Desktop/2A/GMD/projet/projet_2016-17/omim/omim_onto.csv";
        BufferedReader br = null;
        String line ;
        String separator = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // Use comma as a separator
                String[] symptom = line.split(separator);
                String [] ID;
            	for(int i = 0; i < symptom.length; i++){
            		if(symptom[i].equals(CUI)){
            			ID = symptom[0].split("/");
            			if(ID[ID.length-1].substring(0, 1).equals("M")){  //If the first letter of the ID is "M" for "MTHUC"
            				return null;
            			}else{
            				return ID[ID.length-1]; //Return the class ID
            			}
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
        return null; //No matching CUI found
    }

	public static void main (String[] args){
		long startTime = System.nanoTime();
		String liste=CUIToClassID("C1846800");
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println(duration/Math.pow(10,9));
		System.out.println(liste);
	}

}
