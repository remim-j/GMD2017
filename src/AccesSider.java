
import java.sql.*;
import java.util.ArrayList;

public abstract class AccesSider {
	
	static  String DB_SERVER="jdbc:mysql://neptune.telecomnancy.univ-lorraine.fr:3306/";
	static String DB="gmd";
	static String DRIVER="com.mysql.jdbc.Driver";
	static String USER_NAME="gmd-read";
	static String USER_PSWD="esial";
	public AccesSider() {
		// TODO Auto-generated constructor stub	
	}
	
	public static ArrayList<String> idMedocCauseEffetSecondaire(String side_effect) throws Exception{
		
		//activer la connexion
		Class.forName(DRIVER);
		Connection con=DriverManager.getConnection(DB_SERVER+DB,USER_NAME,USER_PSWD);
		
		//Requette SQL
		String myQuery="SELECT * "+
				" FROM meddra_all_se "+
				" Where lower(side_effect_name) = ? ; ";
		PreparedStatement st=con.prepareStatement(myQuery);
		st.setString(1,(side_effect).toLowerCase()); //I take all diseases which contains the word
		ResultSet res=st.executeQuery();
		
		ArrayList<String> listeIdMedicaments=new ArrayList<String>();
		while (res.next()){
			String stitch_id =res.getString("stitch_compound_id1");
			String stitch_id2=res.getString("stitch_compound_id2");
			
			
			if (!listeIdMedicaments.contains(stitch_id)){
				listeIdMedicaments.add(stitch_id);
			}
			if (!listeIdMedicaments.contains(stitch_id2)){
				listeIdMedicaments.add(stitch_id2);
			}
		}
		res.close();
		st.close();
		con.close();
		
		return listeIdMedicaments;
	}
	
public static ArrayList<String> getStitchIDByConceptName(String concept_name) throws Exception{
		
		//activer la connexion
		Class.forName(DRIVER);
		Connection con=DriverManager.getConnection(DB_SERVER+DB,USER_NAME,USER_PSWD);
		
		//Requette SQL
		String myQuery="SELECT * "+
				" FROM meddra_all_indications "+
				" Where lower(concept_name)= ? ; ";
		PreparedStatement st=con.prepareStatement(myQuery);
		st.setString(1,(concept_name).toLowerCase()); //I take all diseases which contains the word
		ResultSet res=st.executeQuery();
		
		ArrayList<String> listeIdMedicaments=new ArrayList<String>();
		while (res.next()){
			String stitch_id =res.getString("stitch_compound_id");
			//String stitch_id2=res.getString("stitch_compound_id2");
			
			
			if (!listeIdMedicaments.contains(stitch_id)){
				listeIdMedicaments.add(stitch_id);
			}
			
		}
		res.close();
		st.close();
		con.close();
		
		return listeIdMedicaments;
	}
	
public static ArrayList<String> cuiToCure(String concept_name) throws Exception{
	
	//activer la connexion
	Class.forName(DRIVER);
	Connection con=DriverManager.getConnection(DB_SERVER+DB,USER_NAME,USER_PSWD);
	
	//Requette SQL
	String myQuery="SELECT * "+
			" FROM meddra_all_indications "+
			" Where lower(concept_name)= ? ; ";
	PreparedStatement st=con.prepareStatement(myQuery);
	st.setString(1,(concept_name).toLowerCase()); //I take all diseases which contains the word
	ResultSet res=st.executeQuery();
	
	ArrayList<String> listeCui=new ArrayList<String>();
	while (res.next()){
		String cui =res.getString("cui");
		//String stitch_id2=res.getString("stitch_compound_id2");
		
		
		if (!listeCui.contains(cui)){
			listeCui.add(cui);
		}
		
	}
	res.close();
	st.close();
	con.close();
	
	return listeCui;
}

public static ArrayList<String> getStitchIdByCUI(String cui,String name) throws Exception{
	
	//activer la connexion
			Class.forName(DRIVER);
			Connection con=DriverManager.getConnection(DB_SERVER+DB,USER_NAME,USER_PSWD);
			
			//Requette SQL
			String myQuery="SELECT * "+
					" FROM meddra_all_indications "+
					" Where cui = ?  OR CUI_of_meddra_term = ?; ";
			PreparedStatement st=con.prepareStatement(myQuery);
			st.setString(1,(cui)); 
			st.setString(2,(cui));

			ResultSet res=st.executeQuery();
			
			ArrayList<String> listeIdMedicaments=new ArrayList<String>();
			while (res.next()){
				String stitch_id =res.getString("stitch_compound_id");
				
				
				if (!listeIdMedicaments.contains(stitch_id)){
					listeIdMedicaments.add(stitch_id);
				}
			
			}
			res.close();
			st.close();
			con.close();
			if (listeIdMedicaments.size()!=0){
				return listeIdMedicaments;

			}
			return null;
}


	

	
	public static void main(String[] args){
		try{
			/*ArrayList<String> liste=cuiToCure("Failure");//
			for (String s : liste){
				System.out.println(s.toString());
			}*/
			ArrayList<String> idStitch=AccesSider.getStitchIdByCUI("C0393808","name");

			if (idStitch !=null){
			
				for(String s : idStitch){
					System.out.println(s);
					String label=ReadStitch.getATCNameByStitchID(s);
					/*ADD TO ORIGIN OF SIDE EFFECT LIST*/
					System.out.println("idMedoc:"+s);
				}
			}
		//on gere 2 erreurs
		}
		catch(ClassNotFoundException e){
			System.err.println("le driver n'a pu etre trouve");
			System.out.println("Exception "+e);
			e.printStackTrace();
		}
		catch(SQLException ex){
			System.err.println("SQL info");
			while (ex!=null){
				System.err.println("Error msg: "+ex.getMessage());
				System.err.println("SQLSTATE: "+ex.getSQLState());
				System.err.println("Error code: "+ex.getErrorCode());
				ex.printStackTrace();
				ex=ex.getNextException();//pour les drivers qui supportent des chaines enchainees
			
			}
		}
		catch(Exception ex){
			System.err.println("autre erreur");
			while (ex!=null){
				System.err.println("Error msg: "+ex.getMessage());
				ex.printStackTrace();			
			}
		}
	
	}
	
	

}
