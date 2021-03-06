package body;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class is the interface with the database.
 * Take "HP:xxxxxxx" String as an argument
 * Return disease_id (for ex : 215510)
 **/

public abstract class ReadHpoAnnotations{

	private static String dbName;
	public static Connection connection;
	//private static PreparedStatement statement;

	/**
    * dbName is the name of the database
    **/

	public static void ReadHpoAnnotations (){

		// Loading the sqlite driver JDBC, the class loader

		try{
			Class.forName("org.sqlite.JDBC");
		}catch (ClassNotFoundException e1){
			System.err.println(e1.getMessage());
		}
		dbName ="hpo_annotations.sqlite";
;
		connection = null;
	}

	/**
    * true = database opened
    *
    */

	public static boolean connect (){
		try{
			ReadHpoAnnotations();
			// Establish the connection
			connection = DriverManager.getConnection("jdbc:sqlite:"+dbName);
			// Declaring the object wich allows to do statements
			
			//statement.executeUpdate("PRAGMA synchronous = OFF;");
			//statement.setQueryTimeout(30);
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
			}
		}

	/**
    * true = connection closed
    *
    */

	public static boolean disconnect (){

		try{
			if(connection != null){
				connection.close();
			}
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;}
		}

	/**
    * The SQL statement
    * @param is the SQL statement which ends with a ";"
    * @return a ResultSet containing the result of the statement
    */

	
	
	
	public static ArrayList<String> getDiseaseLabelBySignId(String signID) throws Exception {
		connect();
		
		String myQuery="SELECT disease_label "
						+ "FROM phenotype_annotation "
						+ "WHERE sign_id = ? ;";
		PreparedStatement statement =connection.prepareStatement(myQuery);
		
		statement.setString(1,signID);
		ResultSet res=statement.executeQuery();
				
		ArrayList<String> listeDiseaseLabel=new ArrayList<String>();
		while (res.next()){
			String label =res.getString("disease_label");
			int  count=0;
			if ((int)label.charAt(0) <65){
				for (int i = 0;i < label.length();i++) {
					if (label.charAt(i) == ' ') {
						break;
					} else {
						count = count + 1;
					}
				}
			}
			label = label.substring((count));
			String temp="";
			for (int i = 0;i < label.length();i++) {
				if (label.charAt(i) == ';') {
					break;
				} else {
					temp = temp + label.charAt(i);
				}
			}
			temp=temp.trim();
			temp=temp.toUpperCase();
			listeDiseaseLabel.add(temp);
		}
		return listeDiseaseLabel;
	}
	
	public static String getDiseaseLabelByDiseaseId(String deseaseID) {
		
		try {
			connect();
			String myQuery="SELECT disease_label "
							+ "FROM phenotype_annotation "
							+ "WHERE disease_id =? ;";
			
			PreparedStatement statement =connection.prepareStatement(myQuery);
			statement.setString(1,deseaseID);
			ResultSet res=statement.executeQuery();
					
			while (res.next()){
				String label =res.getString("disease_label");
				int  count=0;
				if ((int)label.charAt(0) <65){
					for (int i = 0;i < label.length();i++) {
						if (label.charAt(i) == ' ') {
							break;
						} else {
							count = count + 1;
						}
					}
				}
				label = label.substring((count));
				String temp="";
				for (int i = 0;i < label.length();i++) {
					if (label.charAt(i) == ';') {
						break;
					} else {
						temp = temp + label.charAt(i);
					}
				}
				temp=temp.trim();
				temp=temp.toUpperCase();
				return temp;
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getDiseaseIdByLabel(String diseaseLabel){
		
		try {
			connect();
			String myQuery="SELECT disease_id "
							+ "FROM phenotype_annotation "
							+ "WHERE disease_label = ?;";
			
			PreparedStatement statement =connection.prepareStatement(myQuery);
			statement.setString(1,diseaseLabel);
			ResultSet res=statement.executeQuery();
					
			
			while (res.next()){
				String label =res.getString("disease_id");
				
				return label;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	public static void main (String[] args){
		
		try {
			
			/*ArrayList<String> liste=getDiseaseLabelByDiseaseId("603629");
			for (String s : liste){
				System.out.println(s);
			}*/
			ArrayList<String> symptomIdFromHpObo=ReadHpObo.getId("name","Small for gestational age");
			if (symptomIdFromHpObo !=null){
				//On fait tous le trajet suivant le mapping
				
				for (String s : symptomIdFromHpObo){
					ArrayList<String> diseaseLabel=ReadHpoAnnotations.getDiseaseLabelBySignId(s);
					//System.out.println(diseaseLabel.size());
					for(String s1:diseaseLabel){
						System.out.println(s1);
					}
				}
			}
			/*ArrayList<String> diseaseIdFromOrpha=AccesOrphaDataBase.GetDeseaseIdByClinicalSign("Anomalies of ear and hearing");

			if (diseaseIdFromOrpha != null){
				/*On fait tous le trajet suivant le mapping
				for (String s:diseaseIdFromOrpha ){
					//System.out.println(s);
					String diseaseLabelFromHpoAnnotation=ReadHpoAnnotations.getDiseaseLabelByDiseaseId(s);
					//for (String s2 : diseaseLabelFromHpoAnnotation){
						System.out.println(s+ "  :  "+diseaseLabelFromHpoAnnotation);
					//}
				}
			}*/

		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}