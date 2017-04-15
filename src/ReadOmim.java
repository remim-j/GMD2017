import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 * 
 * use : first launch ReadOmimIndex.java to create the index (once)
 *       then getTI(field, query)
 * 
 * input : field, the field on witch we want to make the research, such as "CS"
 *         query, a string describing the entry of the user, such as "Hyperreflexia"
 * output : symptomTI, a table containing the title of the diseases of the correspondences to the query on the field
 *
 */

public abstract class ReadOmim {
	
	private static String index = "index/Omim";
	private static ArrayList<String> symptomTI = new ArrayList<String>();
	private static ArrayList<String> symptomNO = new ArrayList<String>();

	private static void ReadOmim(String field, String queryString) throws IOException, ParseException {
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser parser = new QueryParser(field, analyzer);
		//TermQuery parser = new TermQuery(new Term(field.toString()));
		//Query query = parser.parse(queryString);
		Query query = parser.createBooleanQuery(field, queryString, BooleanClause.Occur.MUST);
		
		//System.out.println("Searching for: " + query.toString(field)); // to delete later
		
	    TopDocs results = searcher.search(query, 100); // hope 100 is enough :)
	    ScoreDoc[] hits = results.scoreDocs;
	    
	    int numTotalHits = results.totalHits;
	    //System.out.println(numTotalHits + " total matching documents"); // to delete later
	    
	    symptomTI = new ArrayList<String>();
	    symptomNO = new ArrayList<String>();
	    if(numTotalHits!=0){
		    hits = searcher.search(query, numTotalHits).scoreDocs;

	    }
	    String TI = "", NO = "";
	    for (int i = 0; i < numTotalHits; i++) {
	    	Document doc = searcher.doc(hits[i].doc);
	    	if (doc.get("TI") != null) {
	    		TI = doc.get("TI");
	    	}
	    	symptomTI.add(TI);
	    	if (doc.get("NO") != null) {
	    		NO = doc.get("NO");
	    	}
	    	symptomNO.add(NO);
	    	//System.out.println(TI); // to delete later
	    }
		reader.close();
	}
	
	
	public static ArrayList<String> getTI(String field, String query) throws IOException, ParseException {
		ReadOmim(field, query);
		return symptomTI;
	}

	public static ArrayList<String> getNO(String field, String query) throws IOException, ParseException {
		ReadOmim(field, query);
		return symptomNO;
	}
	
	// to delete later
	public static void main(String[] args) throws Exception {
		System.out.println("Examples Omim :");
		String field = "CS";
		String query = "Hyperreflexia of m";
		System.out.println("Input \""+query+"\" on field \""+field+"\" corresponds to output : \n");
		long startTime = System.nanoTime();
		ArrayList<String> output = getTI(field, query);
		for (String out : output) {
			System.out.println(out);
		}
		output = getNO(field,query);
		for (String out : output) {
			System.out.println(out);
		}
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		field = "NO";
		query = "100050";
		System.out.println("\nInput \""+query+"\" on field \""+field+"\" corresponds to output : \n");
		output = getTI(field, query);
		for (String out : output) {
			System.out.println(out);
		}
		System.out.println("\nTime needed for one request Hp.obo : "+duration/Math.pow(10,9));
	}
	
}

