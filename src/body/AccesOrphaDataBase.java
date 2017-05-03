package body;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;



/*function getDiseaseByClinicalSign :  take as input the name of the symptom
 * and return a list of disease which can provoke this symptom.
 * Here we have to enter the RIGHT symptom or it does not work
 */

/*function getDiseaseById : take an id number and return the disease with this id*/


public class AccesOrphaDataBase {
	//faut  first se connecter a la base
	//curl -X GET http://mea3u:CouchDB2A@couchdb.telecomnancy.univ-lorraine.fr/orphadatabase/_design/clinicalsigns/_view/GetDiseaseByClinicalSign
	//http://couchdb.telecomnancy.univ-lorraine.fr/orphadatabase/_design/clinicalsigns/_view/GetDiseaseByClinicalSign?startkey=%22Hypo%22&endkey=%22Hypq%22
	//http://couchdb.telecomnancy.univ-lorraine.fr/orphadatabase/_design/diseases/_view/GetDiseasesNumber?key=512
	
	public static ArrayList<String> GetDeseaseByClinicalSign(String symptom){
		symptom=symptom.replace(" ","%20");

		String StringUrl="http://couchdb.telecomnancy.univ-lorraine.fr/orphadatabase/_design/clinicalsigns/_view/GetDiseaseByClinicalSign?key=%22"+symptom+"%22";
		long duration=0;
		ArrayList<String> diseases= new ArrayList<String>();
		try {			
			URL url = new URL(StringUrl);
	
			long startTime = System.nanoTime();
			Reader in = new InputStreamReader(url.openStream());

			JSONObject json = (JSONObject) JSONValue.parse(in);
			 JSONArray Array =((JSONArray)json.get("rows"));
			 
			 for (int i=0;i<Array.size();i++){
				 /*we parse the Json to obtain the name of te disease*/
				 JSONObject object=(JSONObject) Array.get(i);
				 JSONObject objectValue =((JSONObject)object.get("value"));
				 JSONObject objectDisease=((JSONObject)objectValue.get("disease"));
				 JSONObject objectName=((JSONObject)objectDisease.get("Name"));
				 String diseaseName=(String) objectName.get("text");
				 
				 /*add the disease to the the list*/
				 diseases.add(diseaseName);
			 }
			 
			long endTime = System.nanoTime();
			duration = (endTime - startTime);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//System.out.println(duration/Math.pow(10, 9));
		if (diseases.size()==0){
			return null;		
			}
		return diseases;
	}
	
public static ArrayList<String> GetDeseaseIdByClinicalSign(String symptom){
		
	symptom=symptom.replace(" ","%20");
	//System.out.println(symptom);
		String StringUrl="http://couchdb.telecomnancy.univ-lorraine.fr/orphadatabase/_design/clinicalsigns/_view/GetDiseaseByClinicalSign?key=%22"+symptom+"%22";
		long duration=0;
		ArrayList<String> diseaseIdList= new ArrayList<String>();
		try {			
			URL url = new URL(StringUrl);
	
			long startTime = System.nanoTime();
			Reader in = new InputStreamReader(url.openStream());

			JSONObject json = (JSONObject) JSONValue.parse(in);
			 JSONArray Array =((JSONArray)json.get("rows"));
			 
			 for (int i=0;i<Array.size();i++){
				 /*we parse the Json to obtain the name of te disease*/
				 JSONObject object=(JSONObject) Array.get(i);
				 JSONObject objectValue =((JSONObject)object.get("value"));
				 JSONObject objectDisease=((JSONObject)objectValue.get("disease"));
				 Long diseaseIdLong=((Long)objectDisease.get("OrphaNumber"));
				 String diseaseId=diseaseIdLong.toString();
				 
				 /*add the disease to the the list*/
				 diseaseIdList.add(diseaseId);
			 }
			 
			long endTime = System.nanoTime();
			duration = (endTime - startTime);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//System.out.println(duration/Math.pow(10, 9));
		if (diseaseIdList.size()==0){
			return null;
		}
		return diseaseIdList;
	}
	
public static ArrayList<String> GetDeseaseByDiseaseId(int num){
		
		String StringUrl="http://couchdb.telecomnancy.univ-lorraine.fr/orphadatabase/_design/diseases/_view/GetDiseasesNumber?key="+num;
		long duration=0;
		ArrayList<String> diseases= new ArrayList<String>();
		try {			
			URL url = new URL(StringUrl);
	
			long startTime = System.nanoTime();
			Reader in = new InputStreamReader(url.openStream());

			JSONObject json = (JSONObject) JSONValue.parse(in);
			 JSONArray Array =((JSONArray)json.get("rows"));
			 
			 for (int i=0;i<Array.size();i++){
				 /*we parse the Json to obtain the name of te disease*/
				 JSONObject object=(JSONObject) Array.get(i);
				 JSONObject objectValue =((JSONObject)object.get("value"));
				 String diseaseName=(String) objectValue.get("Name");
				 

				 /*add the disease to the the list*/
				 diseases.add(diseaseName);
				 
				 //I HAVE TO MAKE A FUNCTION WHICH WILL BRING OUT THE DISORDER SIGNLIST FOR THIS DISEASE
			 }
			 
			long endTime = System.nanoTime();
			duration = (endTime - startTime);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//System.out.println(duration/Math.pow(10, 9));
		if (diseases.size()==0){
			diseases.add(num+"  is not found as a disease id ");
		}
		return diseases;
	}
	
	public static void main(String[] args) {
		
		ArrayList<String>liste =GetDeseaseIdByClinicalSign("Anomalies of ear and hearing");
		for (String s : liste){
			System.out.println(s);
		}
		//System.out.println("taille "+liste.size());
	}

}
