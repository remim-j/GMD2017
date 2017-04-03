
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadOmimOnto {


	public static ArrayList<String> CUIToPreferredLabel(String CUI) {

        String csvFile = "C:/Users/user/Desktop/2A/GMD/projet/projet_2016-17/omim/omim_onto.csv";
        BufferedReader br = null;
        String line = "";
        String separator = ",";
        ArrayList<String> label = new ArrayList<String>();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] symptom = line.split(separator);
            	for(int i = 0; i < symptom.length; i++){
            		if(symptom[i].equals(CUI)){
            			System.out.println("Symptom [CUI = " + symptom[i] + " , Preferred Label =" + symptom[1] + "]");
            			for(int j = 1; j < i-2; j++){
            				label.add(symptom[j]);
            			}
            			System.out.println(label);
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

        System.out.println("No CUI does match");
        return null;

    }
}
