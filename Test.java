import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException{

		String ATCID;
		ReadOmimOnto.CUIToPreferredLabel("C3549655");
		ReadStitch TSV = new ReadStitch();
		ATCID = TSV.stitchCompoundIDToATCID("CID00003393");
		System.out.println(ATCID);
		try {
			TSV.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}