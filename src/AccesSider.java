
import java.sql.*;

public class AccesSider {
	
	static  String DB_SERVER="jdbc:mysql://neptune.telecomnancy.univ-lorraine.fr:3306/";
	static String DB="gmd";
	static String DRIVER="com.mysql.jdbc.Driver";
	static String USER_NAME="gmd-read";
	static String USER_PSWD="esial";
	public AccesSider() {
		// TODO Auto-generated constructor stub	
	}
	
	
	public static void main(String[] args){
		try{
			//activer la connexion
			Class.forName(DRIVER);
			Connection con=DriverManager.getConnection(DB_SERVER+DB,USER_NAME,USER_PSWD);
			System.out.println("here");
			//Requette SQL
			String myQuery="SELECT MAI.stitch_compound_id,MSE.stitch_compound_id1,MSE.stitch_compound_id2,MAI.cui,MSE.side_effect_name "+
							"FROM meddra_all_indications MAI,meddra_all_se MSE "+
							"WHERE MAI.stitch_compound_id=MSE.stitch_compound_id1 ;";
			String myQ="SELECT * "+
						"FROM meddra_all_indications ;";
			System.out.println("here1");
			Statement st =con.createStatement();
			System.out.println("here12");
			ResultSet res=st.executeQuery(myQ);
			System.out.println("here2");
			
			while (res.next()){
				String stitch_id =res.getString("stitch_compound_id1");
				//String stitch_id2=res.getString("stitch_compound_id2");
				
				String cui=res.getString("cui");
				//String sideEffect=res.getString(("side_effect_name"));
				/*int age=res.getInt("age");
				String telephone=res.getString(("telephone"));
				String ville=res.getString(("ville"));
				String email=res.getString(("email"));*/
				
				//String ligne=id+","+nom+","+prenom+","+age+","+telephone+","+ville+","+email;
				//System.out.println(ligne);
				/*il faut juste reecrire cette sortie dans 
				 * un fichier .csv
				 */
				System.out.println("stitch_id : "+stitch_id+" cui: "+ cui+" side Effect: "+sideEffect);
			}
			res.close();
			st.close();
			con.close();
		}
		//on gere 2 erreurs
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
