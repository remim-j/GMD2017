import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;


public class Test {

	public static void main(String[] args) throws IOException, SQLException{

		/*String ATCID;
		ReadOmimOnto.CUIToPreferredLabel("C3549655");
		ReadStitch TSV = new ReadStitch();
		ATCID = TSV.stitchCompoundIDToATCID("CID00003393");
		System.out.println(ATCID);
		try {
			TSV.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		ReadHpoAnnotations BD = new ReadHpoAnnotations("C:/Users/user/Desktop/2A/GMD/projet/projet_2016-17/hpo/hpo_annotations.sqlite");
		BD.connect();
		ResultSet statement = BD.getResultOf("SELECT disease_id AND disease_db FROM phenotype_annotation WHERE sign_id = 'HP:00002890';");*/
		
		
		/*An example of test*/
		ReadHpObo.ReadHpObo();
		String userInput="hand";
		
		try {
			ArrayList<String> nameAndSynonyms=ReadHpObo.getSynonyms(userInput);
		System.out.println(nameAndSynonyms.size());
			for (String s:nameAndSynonyms){
				System.out.println(s);
				ArrayList<String> cui=AccesSider.cuiToCure(s);
				for (String s1:cui){
					ArrayList<String> preferedLabel=ReadOmimOnto.CUIToPreferredLabel(s1);
					for (String s2: preferedLabel){
						System.out.println(s2);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}