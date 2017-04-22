import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

	/**Takes args "CIDxxxxxxxxx" return a String ATC code like "XXXXXXXX"*/

public abstract class ReadStitch2{

    
    static String line = null;
    static File file = new File("chemical.sources.v5.0.tsv/chemical.sources.v5.0.tsv");
    static FileInputStream stream; 
    static Scanner in;
    public static void  ReadStitch() throws FileNotFoundException{
    	stream=new FileInputStream(file);
        in = new Scanner(stream);

    }

    /**Constructs a new TSVReader which produces values scanned from the specified input stream.*/

    public static boolean hasNextTokens(){

        if(line!=null){
        	return true;
        }
    	//System.out.println(in);
        
        	if(!in.hasNextLine()){
            	return false;
            }
        	String lineThis= in.nextLine().trim();
            if(lineThis.isEmpty()) {
            	return hasNextTokens();
            }
            line = lineThis;
            return true;
        }
        
        
    

    /** Return the wanted ATC ID*/

    public static String stitchCompoundIDToATCID(String stitchCompoundID){
    	 try {
			ReadStitch() ;
			String[] token = null;
	    	boolean fini = false;
	    	while(hasNextTokens() && !fini){
	   			while(token == null || token.length != 4){ //If token is not complete
	   				token = nextTokens();
	   			}if(token[2].equals("BindingDB")){	//If we have done with ATC id
	   				fini = true;
	   			}else if(token[0].equals(transformChemical(stitchCompoundID)) || token[1].equals(transformAlias(stitchCompoundID))){
	  				return token[3];
	   			}
	   			token = nextTokens();
	    	}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    }

    /** Jump to the next line*/

    public static String[] nextTokens(){

        if(!hasNextTokens()){
        	return null;
        }
        String[] tokens = line.split("[\\s\t]+");
        line=null;
        return tokens;
    }

    public static String transformChemical(String CID){
    	String chemical = "";
    	chemical = CID.substring(0, 3) + "m" + CID.substring(4, CID.length());
    	return chemical;
    }

    public static String transformAlias(String CID){
    	String alias = "";
    	alias = CID.substring(0, 3) + "s" + CID.substring(4, CID.length());
    	return alias;
    }

    /* function which convert a stitchID to a medecine Label*/ 
    public static String getATCNameByStitchID(String stitchID){
    	try {
			ReadStitch();
			String Atc_id=ReadStitch2.stitchCompoundIDToATCID(stitchID);
			String drugName=ReadATC.getLabel(Atc_id);
			return drugName;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
		
	}
    
    public void close() throws IOException {
    	in.close();
    }
    
    public static void main(String[] args){
    	long startTime = System.nanoTime();
    	String ATCid=stitchCompoundIDToATCID("CID100002520");
    	System.out.println(ATCid);
    	long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("duree :"+duration/Math.pow(10,9));
    }

}