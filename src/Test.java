import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public class Test {

	public static void main(String[] args) throws IOException, SQLException{

		String ATCID;
		ReadStitch TSV = new ReadStitch();
		ATCID = TSV.stitchCompoundIDToATCID("CID00003393");
		System.out.println(ATCID);
		try {
			TSV.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String NO;
		ReadHpoAnnotations BD = new ReadHpoAnnotations("C:/Users/user/Desktop/2A/GMD/projet/projet_2016-17/hpo/hpo_annotations.sqlite");
		BD.connect();
		ResultSet1 statement = BD.getResultOf("SELECT disease_id AND disease_db FROM phenotype_annotation WHERE sign_id = '"+id1+"';");
		ResultSet2 statement = BD.getResultOf("SELECT disease_id AND disease_db FROM phenotype_annotation WHERE disease_id = '"+NO+"';");
		ResultSet3 statement = BD.getResultOf("SELECT disease_id AND disease_db FROM phenotype_annotation WHERE disease_id = '"+id2+"';");
		ResultSetMetaData statementmd = statement.getMetaData();
		   int columnsNumber = statementmd.getColumnCount();
		   System.out.println(statement == null);
		   System.out.println(columnsNumber);
		   while(statement.next()){
		       statement.getString(1);
		   }
		   while (statement.next()) {
			   for (int i = 1; i <= columnsNumber; i++) {
				   if (i > 1){
					   System.out.print(",  ");
				   }
		           String columnValue = statement.getString(i);
		           System.out.print(columnValue + " " + statementmd.getColumnName(i));
		       }
		   }
		System.out.println(statement);
	}

}