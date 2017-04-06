import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
�* This class is the interface with the database.
�**/

public class ReadHpoAnnotations{

	private String dbName;
	public Connection connection;
	private Statement requete;

	/**
�����* dbName is the name of the database
�����**/

	public ReadHpoAnnotations (String dbName){

		// Loading the sqlite driver JDBC, the class loader

		try{
			Class.forName("org.sqlite.JDBC");
		}catch (ClassNotFoundException e1){
			System.err.println(e1.getMessage());
		}
		this.dbName = dbName;
		this.connection = null;
	}

	/**
�����* true = database opened
�����*
�����*/
	public boolean connect (){
		try{
			// Etablit la connection
			System.out.println("dfg");
			connection = DriverManager.getConnection("jdbc:sqlite:"+this.dbName);
			// D�clare l'objet qui permet de faire les requ�tes
			requete = connection.createStatement();
			requete.executeUpdate("PRAGMA synchronous = OFF;");
			requete.setQueryTimeout(30);
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
			}
		}

	/**
�����* true = connection closed
�����*
�����*/

	public boolean disconnect (){

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
�����* Permet de faire une requ�te SQL
�����* @param requete La requ�te SQL (avec un ";" � la fin)
�����* @return Un ResultSet contenant le r�sultat de la requ�te
�����*/

	public ResultSet getResultOf (String requete){
		try{
			return this.requete.executeQuery(requete);
		}catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
�����* Permet de modifier une entr�e de la base de donn�es.</br>
�����* @param requete La requete SQL de modification
�����*/
	public void updateValue (String requete){
		try{
			this.requete.executeUpdate(requete);
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
}