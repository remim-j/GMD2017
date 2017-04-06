import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

	/**Takes args "CIDxxxxxxxxx" return a String ATC code like "XXXXXXXX"*/

public class ReadStitch implements Closeable{

    final Scanner in;
    String line = null;
    File file = new File("C:/Users/user/Desktop/2A/GMD/projet/projet_2016-17/chemical.sources.v5.0.tsv");
    InputStream stream = new FileInputStream(file);

    public ReadStitch() throws FileNotFoundException{

        in = new Scanner(stream);

    }

    /**Constructs a new TSVReader which produces values scanned from the specified input stream.*/

    public boolean hasNextTokens(){

        if(line!=null){
        	return true;
        }
        if(!in.hasNextLine()){
        	return false;
        	}
        String line = in.nextLine().trim();
        if(line.isEmpty()) {
        	return hasNextTokens();
        }
        this.line = line;
        return true;
    }

    /** Return the wanted ATC ID*/

    public String stitchCompoundIDToATCID(String stitchCompoundID){
    	String[] token = null;
    	while(hasNextTokens()){
    		while(token == null){
    			token = nextTokens();
    		}
    		if(token.length == 4 && token[2].equals("ATC")){
    			System.out.println(token[1]);
    			if(token[0].equals(transformChemical(stitchCompoundID)) || token[1].equals(transformAlias(stitchCompoundID))){
    				return token[3];
    			}
    		}
    		token = nextTokens();
    	}
    	System.out.println("No StitchCompoundID does match");
    	return null;
    }

    /** Jump to the next line*/

    public String[] nextTokens(){

        if(!hasNextTokens()){
        	return null;
        }
        String[] tokens = line.split("[\\s\t]+");
        line=null;
        return tokens;
    }

    public String transformChemical(String CID){
    	String chemical = "";
    	chemical = CID.substring(0, 3) + "m" + CID.substring(3, CID.length());
    	return chemical;
    }

    public String transformAlias(String CID){
    	String alias = "";
    	alias = CID.substring(0, 3) + "s" + CID.substring(3, CID.length());
    	return alias;
    }

    @Override public void close() throws IOException {
    	in.close();
    	}

}