package body;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 * 
 * use : first launch ReadOmimIndex.java to create the index (once)
 *       then prepareQuery(String field, String query)
 *       then getTI()
 *            getNO()
 * 
 * input : field, the field on witch we want to make the research, such as "CS"
 *         query, a string describing the entry of the user, such as "Hyperreflexia"
 *         
 * output : symptomTI, a table containing the diseases corresponding to the query on the field
 *          symptomNO, a table containing the id corresponding to the query on the field
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
		Query query = parser.createBooleanQuery(field, queryString, BooleanClause.Occur.MUST);
		
	    TopDocs results = searcher.search(query, 100);
	    ScoreDoc[] hits = results.scoreDocs;
	    int numTotalHits = results.totalHits;
	    
	    if (numTotalHits != 0) {
		    hits = searcher.search(query, numTotalHits).scoreDocs;
	    }
	    
	    symptomTI = new ArrayList<String>();
	    symptomNO = new ArrayList<String>();	    
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
	    }
		reader.close();
	}
	
private static void ReadOmimFIX(String field, String queryString) throws IOException, ParseException {
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new KeywordAnalyzer() ;;
		QueryParser parser = new QueryParser(field, analyzer);
		
		//Query query =new PhraseQuery(field,queryString);
		Query query = parser.createBooleanQuery(field, queryString, BooleanClause.Occur.MUST);
		
	    TopDocs results = searcher.search(query, 100);
	    ScoreDoc[] hits = results.scoreDocs;
	    int numTotalHits = results.totalHits;
	    
	    if (numTotalHits != 0) {
		    hits = searcher.search(query, numTotalHits).scoreDocs;
	    }
	    
	    symptomTI = new ArrayList<String>();
	    symptomNO = new ArrayList<String>();	    
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
	    }
		reader.close();
	}
	
	
	public static void prepareQuery(String field, String query) throws IOException, ParseException {
		ReadOmim(field, query);
	}
	
	public static ArrayList<String> getTI(String field,String query) {
		try {
			ReadOmim(field, query);
			return symptomTI;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static ArrayList<String> getTIFIX(String field,String query) {
		try {
			ReadOmimFIX(field, query);
			return symptomTI;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<String> getNO(String field,String query) {
		try {
			ReadOmim(field, query);
			return symptomNO;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	public static ArrayList<String> getNOFIX(String field,String query) {
		try {
			ReadOmimFIX(field, query);
			return symptomNO;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static void main(String[] args){
		
	}
}

