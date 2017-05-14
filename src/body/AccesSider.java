package body;

import java.sql.*;
import java.util.ArrayList;

public abstract class AccesSider {
	
	static String DB_SERVER = "jdbc:mysql://neptune.telecomnancy.univ-lorraine.fr:3306/";
	static String DB = "gmd";
	static String DRIVER = "com.mysql.jdbc.Driver";
	static String USER_NAME = "gmd-read";
	static String USER_PSWD = "esial";
	static Connection con; /*data to initialize sider*/
	
	public static void  InitSider() {		
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(DB_SERVER+DB,USER_NAME,USER_PSWD);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeSider() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static ArrayList<String> idMedicineSideEffect(String side_effect) throws Exception {
		
		// activate connection
		Class.forName(DRIVER);
		Connection con = DriverManager.getConnection(DB_SERVER+DB,USER_NAME,USER_PSWD);
		
		// SQL request
		String myQuery = "SELECT * "+
				" FROM meddra_all_se "+
				" Where lower(side_effect_name) LIKE ? ; ";
		
		PreparedStatement st = con.prepareStatement(myQuery);
		st.setString(1,"%"+(side_effect).toLowerCase()+"%"); // take all diseases which contains the word
		ResultSet res = st.executeQuery();
		
		ArrayList<String> idMedicinesList = new ArrayList<String>();
		while (res.next()){
			String stitch_id = res.getString("stitch_compound_id1");
			String stitch_id2 = res.getString("stitch_compound_id2");
			
			if (!idMedicinesList.contains(stitch_id)) {
				idMedicinesList.add(stitch_id);
			}
			if (!idMedicinesList.contains(stitch_id2)) {
				idMedicinesList.add(stitch_id2);
			}
		}
		res.close();
		st.close();
		con.close();
		return idMedicinesList;
	}
	
public static ArrayList<String> idMedicineSideEffectFIX(String side_effect) throws Exception {
		
		// activate connection
		Class.forName(DRIVER);
		Connection con = DriverManager.getConnection(DB_SERVER+DB,USER_NAME,USER_PSWD);
		
		// SQL request
		String myQuery = "SELECT * "+
				" FROM meddra_all_se "+
				" Where lower(side_effect_name)= ?; ";
		
		PreparedStatement st = con.prepareStatement(myQuery);
		st.setString(1,(side_effect).toLowerCase()); 
		ResultSet res = st.executeQuery();
		
		ArrayList<String> idMedicinesList = new ArrayList<String>();
		while (res.next()){
			String stitch_id = res.getString("stitch_compound_id1");
			String stitch_id2 = res.getString("stitch_compound_id2");
			
			if (!idMedicinesList.contains(stitch_id)) {
				idMedicinesList.add(stitch_id);
			}
			if (!idMedicinesList.contains(stitch_id2)) {
				idMedicinesList.add(stitch_id2);
			}
		}
		res.close();
		st.close();
		con.close();
		return idMedicinesList;
	}
	
	public static ArrayList<String> getStitchIDByConceptName(String concept_name) throws Exception {
		
		// activate connection
		Class.forName(DRIVER);
		Connection con = DriverManager.getConnection(DB_SERVER+DB,USER_NAME,USER_PSWD);
		
		// SQL request
		String myQuery = "SELECT * "+
				" FROM meddra_all_indications "+
				" Where lower(concept_name) LIKE ? ; ";
		
		PreparedStatement st = con.prepareStatement(myQuery);
		st.setString(1,"%"+(concept_name).toLowerCase()+"%"); // take all diseases which contains the word
		ResultSet res = st.executeQuery();
		
		ArrayList<String> idMedicinesList=new ArrayList<String>();
		while (res.next()) {
			String stitch_id =res.getString("stitch_compound_id");
			if (!idMedicinesList.contains(stitch_id)) {
				idMedicinesList.add(stitch_id);
			}
		}
		res.close();
		st.close();
		con.close();
		return idMedicinesList;
	}
	
public static ArrayList<String> getStitchIDByConceptNameFIX(String concept_name) throws Exception {
		
		// activate connection
		Class.forName(DRIVER);
		Connection con = DriverManager.getConnection(DB_SERVER+DB,USER_NAME,USER_PSWD);
		
		// SQL request
		String myQuery = "SELECT * "+
				" FROM meddra_all_indications "+
				" Where lower(concept_name)=?; ";
		
		PreparedStatement st = con.prepareStatement(myQuery);
		st.setString(1,(concept_name).toLowerCase()); 
		ResultSet res = st.executeQuery();
		
		ArrayList<String> idMedicinesList=new ArrayList<String>();
		while (res.next()) {
			String stitch_id =res.getString("stitch_compound_id");
			if (!idMedicinesList.contains(stitch_id)) {
				idMedicinesList.add(stitch_id);
			}
		}
		res.close();
		st.close();
		con.close();
		return idMedicinesList;
	}
	
	public static ArrayList<String> cuiToCure(String concept_name) throws Exception {
	
		// activate connection
		Class.forName(DRIVER);
		Connection con = DriverManager.getConnection(DB_SERVER+DB,USER_NAME,USER_PSWD);
	
		// SQL request
		String myQuery = "SELECT * "+
				" FROM meddra_all_indications "+
				" Where lower(concept_name) LIKE  ? ; ";
		
		PreparedStatement st = con.prepareStatement(myQuery);
		st.setString(1,"%"+(concept_name).toLowerCase()+"%"); // take all diseases which contains the word
		ResultSet res = st.executeQuery();
	
		ArrayList<String> listeCui = new ArrayList<String>();
		while (res.next()) {
			String cui =res.getString("cui");
			if (!listeCui.contains(cui)){
				listeCui.add(cui);
			}
		}
		res.close();
		st.close();
		con.close();
		return listeCui;
	}
	
	public static ArrayList<String> cuiToCureFIX(String concept_name) throws Exception {
		
		// activate connection
		Class.forName(DRIVER);
		Connection con = DriverManager.getConnection(DB_SERVER+DB,USER_NAME,USER_PSWD);
	
		// SQL request
		String myQuery = "SELECT * "+
				" FROM meddra_all_indications "+
				" Where lower(concept_name) =? ; ";
		
		PreparedStatement st = con.prepareStatement(myQuery);
		st.setString(1,(concept_name).toLowerCase());
		ResultSet res = st.executeQuery();
	
		ArrayList<String> listeCui = new ArrayList<String>();
		while (res.next()) {
			String cui =res.getString("cui");
			if (!listeCui.contains(cui)){
				listeCui.add(cui);
			}
		}
		res.close();
		st.close();
		con.close();
		return listeCui;
	}

	
	public static ArrayList<String> getStitchIdByCUI(String cui,String name) throws Exception {
	
		// activate connection
		Class.forName(DRIVER);
		Connection con = DriverManager.getConnection(DB_SERVER+DB,USER_NAME,USER_PSWD);
			
		// SQL request
		String myQuery = "SELECT * "+
					" FROM meddra_all_indications "+
					" Where cui = ?  OR cui_of_meddra_term = ?; ";
		
		PreparedStatement st = con.prepareStatement(myQuery);
		st.setString(1,(cui)); 
		st.setString(2,(cui));
		ResultSet res = st.executeQuery();
		
		ArrayList<String> idMedicinesList = new ArrayList<String>();
		while (res.next()) {
			String stitch_id = res.getString("stitch_compound_id");
			if (!idMedicinesList.contains(stitch_id)){
				idMedicinesList.add(stitch_id);
			}
		}
		res.close();
		st.close();
		con.close();
		if (idMedicinesList.size()!= 0) {
			return idMedicinesList;
		} else {
			return null;
		}
	}

	
	public static void main(String[] args) {
		try {
			long startTime = System.nanoTime();
			InitSider();
			ArrayList<String> liste = cuiToCure("Failure");
			long endTime = System.nanoTime();
       		double duration = (endTime - startTime)/Math.pow(10,9);
       		System.out.println("\n Time needed : "+duration);
			for (String s : liste) {
				//System.out.println(s.toString());
			}
			 
			ArrayList<String> cuiMedecineOmimOnto=ReadOmimOnto.SymptomToCUI("cancer");

			for (String s:cuiMedecineOmimOnto){ 
				ArrayList<String> stitchId=AccesSider.getStitchIdByCUI(s, "name");
				if (stitchId != null){
					for (String s1 : stitchId){
						String label = ReadStitch.getATCNameByStitchID(s1);
						System.out.println(label);
					}
				}
				ArrayList<String> sideEffectStitchID = AccesSider.idMedicineSideEffect(s);
				if (sideEffectStitchID != null) {
					for (String s1 : sideEffectStitchID){
						String label = ReadStitch.getATCNameByStitchID(s1);
						//System.out.println(label);
					}
				}
			}
				
			//on gere 2 erreurs
		}
		catch(ClassNotFoundException e) {
			System.err.println("le driver n'a pu etre trouve");
			System.out.println("Exception "+e);
			e.printStackTrace();
		}
		catch(SQLException ex) {
			System.err.println("SQL info");
			while (ex != null) {
				System.err.println("Error msg: "+ex.getMessage());
				System.err.println("SQLSTATE: "+ex.getSQLState());
				System.err.println("Error code: "+ex.getErrorCode());
				ex.printStackTrace();
				ex = ex.getNextException();//pour les drivers qui supportent des chaines enchainees
			}
		}
		catch(Exception ex) {
			System.err.println("autre erreur");
			while (ex != null) {
				System.err.println("Error msg: "+ex.getMessage());
				ex.printStackTrace();			
			}
		}
	}
	
}
