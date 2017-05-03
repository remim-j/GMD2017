package body;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;


public abstract class ReadStitch {
	
	static String ATC_id;
	private static String index = "index/Stitch";
	private static void ReadStitchIndexBis( String queryString) throws IOException, ParseException {
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		//QueryParser parser = new QueryParser(field, analyzer);

		//TermQuery parser = new TermQuery(new Term(field.toString()));
		//Query query = parser.parse(queryString);
		//Query query = parser.createBooleanQuery(field, queryString, BooleanClause.Occur.MUST);
		String[] queries={transformChemical(queryString),transformAlias(queryString)};
		String[] fields={"compound_id_With_m","compound_id_With_s"};
		BooleanClause.Occur[] occurs={BooleanClause.Occur.SHOULD,BooleanClause.Occur.SHOULD};

		/**to remember MultiFieldQueryParser to search on many field**/
		Query query = MultiFieldQueryParser.parse(queries,fields,occurs, analyzer);
		
		//System.out.println(query);
		
	    TopDocs results = searcher.search(query, 100); // hope 100 is enough :)
	    ScoreDoc[] hits = results.scoreDocs;
	    
	    int numTotalHits = results.totalHits;
	    
	   
	    if(numTotalHits!=0){
		    hits = searcher.search(query, numTotalHits).scoreDocs;

	    }
	    
	    for (int i = 0; i < numTotalHits; i++) {
	    	Document doc = searcher.doc(hits[i].doc);
	    	if (doc.get("ATC_id") != null) {
	    		ATC_id=doc.get("ATC_id");
	    		break;
	    	}
	    }
		reader.close();
		
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
	
	public static String stitchCompoundIDToATCID(String stitchID) throws IOException, ParseException{
		ReadStitchIndexBis(stitchID);
		return ATC_id;
	}
	
	 public static String getATCNameByStitchID(String stitchID){
	    	try {
				
				String Atc_id;
					Atc_id = ReadStitch.stitchCompoundIDToATCID(stitchID);
					String drugName=ReadATC.getLabel(Atc_id);
					return drugName;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	
	    	return null;
	
		}
	
	public static void  main (String[] args){
		try {
			System.out.println(stitchCompoundIDToATCID("CID100000085"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
