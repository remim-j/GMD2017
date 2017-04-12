import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is the interface with the database.
 * Take "HP:xxxxxxx" String as an argument
 * Return disease_id (for ex : 215510)
 **/

public class ReadHpoAnnotations{

	private String dbName;
	public Connection connection;
	private Statement requete;

	/**
     * dbName is the name of the database
     **/

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
     * true = database opened
     *
     */

	public boolean connect (){
		try{
			// Establish the connection
			connection = DriverManager.getConnection("jdbc:sqlite:"+this.dbName);
			// Declaring the object wich allows to do statements
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
     * true = connection closed
     *
     */

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
     * The SQL statement
     * @param is the SQL statement which ends with a ";"
     * @return a ResultSet containing the result of the statement
     */

	public ResultSet getResultOf (String requete){
		try{
			return this.requete.executeQuery(requete);
		}catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}

}