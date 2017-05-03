package body;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract  class ReadOmimOnto {

	/* Take a string like "Cxxxxxxx" and return ArrayList<String> of Preferred Labels and Synonyms like ["Cataract, BLABLA","Blabla, adfksmldfk"] */

	public static ArrayList<String> ClassIDToCUI(String ClassID) {

        String csvFile = "omim_onto.csv";
        BufferedReader br = null;
        String line = "";
        String separator = ",";
        ArrayList<String> listeRetour=new ArrayList();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // Use comma as a separator
                String[] symptom = line.split(separator);
                String [] ID = symptom[0].split("/");
                if(ID[ID.length-1].equals(ClassID)){  //If the IDs are the same
                	//System.out.println(symptom[symptom.length-2]);
                	listeRetour.add(symptom[symptom.length-2]);
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
        if (listeRetour.size()!=0){
        	return listeRetour;
        }
        return null; //No matching ClassID found
    }
	
	
	
	public static ArrayList<String> SymptomToCUI(String stringSymptom) {

        String csvFile = "omim_onto.csv";
        BufferedReader br = null;
        String line = "";
        String separator = ",";
        ArrayList<String> listeRetour=new ArrayList();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // Use comma as a separator
                String[] symptom = line.split(separator);
                String preferedLabel = symptom[1];
                String preferedLabelSynonyms=symptom[2];
                String cuiAjoute="";
                if(preferedLabel.toLowerCase().contains(stringSymptom.toLowerCase()) || preferedLabelSynonyms.toLowerCase().contains(stringSymptom.toLowerCase())){  //If the IDs are the same
                	
                	String classID = (symptom[0].split("/"))[symptom[0].split("/").length-1];
                    //System.out.println(classID);
                	
                    if(classID.startsWith("MTHU")){
                    	/*because we found that cui in sider have
                    	 * their classID in Omim_Onto which begin by MTHU
                    	 */
                    
                    	//cui place can change so we do a if for each case
                    	
                    	if (symptom[symptom.length-3].startsWith("C")){
                    		cuiAjoute=symptom[symptom.length-3];
                    	}
                    	else{
                    		cuiAjoute=symptom[symptom.length-2];
                    		
                    	}
                    	/*some lines contains many cui*/
                    	if (cuiAjoute.contains("|")){
                    		/*we first convert the the "|" by "," because it doesnt work when i split
                    		 * with "|"
                    		 */
                    		String intermediate=cuiAjoute.replace('|',',');
                    		String[]	diffCui=intermediate.split(separator);

                    		for (int i=0;i<diffCui.length;i++){
                    			if (!listeRetour.contains(diffCui[i])){
                    				listeRetour.add(diffCui[i]);
                    			}
                        		
                    		}
                    	}
                    	
                    	else {
                			if (!listeRetour.contains(cuiAjoute)){
                				listeRetour.add(cuiAjoute);
                			}

                    	}
                    }
                	
                }
            }
        }
         catch (FileNotFoundException e) {
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
        if (listeRetour.size()!=0){
        	return listeRetour;
        }
        return null; //No matching ClassID found
    }

	
	
	public static String CUIToClassID(String CUI) {

        String csvFile = "omim_onto.csv";
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
            				return ID[ID.length-1].substring(0,ID[ID.length-1].indexOf(".")); //Return the class ID
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

	public static void main (String[] args) throws Exception{
		long startTime = System.nanoTime();
		String liste=CUIToClassID("C1867385");
		ArrayList<String> liste2=ClassIDToCUI("302800");
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println(duration/Math.pow(10,9));
		ArrayList<String> liste3=SymptomToCUI("cancer");
		
		//System.out.println(liste);
		ArrayList<String> cuiMedecineOmimOnto=ReadOmimOnto.SymptomToCUI("cancer");

		if (cuiMedecineOmimOnto!=null){
			System.out.println("taille"+cuiMedecineOmimOnto.size());
			for (String s:cuiMedecineOmimOnto){
				//System.out.println("ICI");
				ArrayList<String> stitchId=AccesSider.getStitchIdByCUI(s, "name");
				//System.out.println("LA");
				if (stitchId!=null){
					for (String s1:stitchId){
						String label=ReadStitch.getATCNameByStitchID(s1);
						if (label!=null){
							System.out.println(label);
						}
					}
				}
				
			}
		}
	}

}
