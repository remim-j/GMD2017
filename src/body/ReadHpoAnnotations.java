package body;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.sqlite.*;

/**
 * This class is the interface with the database.
 * Take "HP:xxxxxxx" String as an argument
 * Return disease_id (for ex : 215510)
 **/

public abstract class ReadHpoAnnotations{

	private static String dbName;
	public static Connection connection;
	private static PreparedStatement statement;

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

	public ResultSet getResultOf (String requete){
		try{
			//statement=this.connection.prepareStatement(requette)
			return this.statement.executeQuery(requete);
		}catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static ArrayList<String> getDiseaseLabelBySignId(String signID ) throws Exception{
		connect();
		String myQuery="SELECT disease_label "
						+ "FROM phenotype_annotation "
						+ "WHERE sign_id = ? ;";
		statement =connection.prepareStatement(myQuery);
		
		statement.setString(1,signID);
		ResultSet res=statement.executeQuery();
				
		ArrayList<String> listeDiseaseLabel=new ArrayList<String>();
		while (res.next()){
			String label =res.getString("disease_label");
			listeDiseaseLabel.add(label);
		}
		return listeDiseaseLabel;
	}
	
	public static ArrayList<String> getDiseaseLabelByDiseaseId(String deseaseID ) throws Exception{
		connect();
		String myQuery="SELECT disease_label "
						+ "FROM phenotype_annotation "
						+ "WHERE disease_id LIKE ? ;";
		statement =connection.prepareStatement(myQuery);
		
		statement.setString(1,"%"+deseaseID+"%");
		ResultSet res=statement.executeQuery();
				
		ArrayList<String> listeDiseaseLabel=new ArrayList<String>();
		while (res.next()){
			String label =res.getString("disease_label");
			
			if (!listeDiseaseLabel.contains(label)){/* we avoid to add the same name many times because a 
			/*desease_id will always match with the same desease_label*/
				listeDiseaseLabel.add(label);

			}
		}
		return listeDiseaseLabel;
	}
	
	
	public static void main (String[] args){
		
		try {
			/*ArrayList<String> liste=getDiseaseLabelByDiseaseId("603629");
			for (String s : liste){
				System.out.println(s);
			}*/
			/*ArrayList<String> symptomIdFromHpObo=ReadHpObo.getId("name","Small for gestational age");
			if (symptomIdFromHpObo !=null){
				//On fait tous le trajet suivant le mapping
				
				for (String s : symptomIdFromHpObo){
					ArrayList<String> diseaseLabel=ReadHpoAnnotations.getDiseaseLabelBySignId(s);
					System.out.println(diseaseLabel.size());
					for(String s1:diseaseLabel){
						System.out.println(s1);
					}
				}
			}*/
			ArrayList<String> diseaseIdFromOrpha=AccesOrphaDataBase.GetDeseaseIdByClinicalSign("Anomalies of ear and hearing");

			if (diseaseIdFromOrpha != null){
				/*On fait tous le trajet suivant le mapping*/
				for (String s:diseaseIdFromOrpha ){
					//System.out.println(s);
					ArrayList<String> diseaseLabelFromHpoAnnotation=ReadHpoAnnotations.getDiseaseLabelByDiseaseId(s);
					for (String s2 : diseaseLabelFromHpoAnnotation){
						System.out.println(s+ "  :  "+s2);
					}
				}
			}

		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		
}

