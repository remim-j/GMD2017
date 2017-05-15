package body;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * 
 * use : first launch ReadHpOboIndex.java to create the index (once)
 *       then getId(field, query)
 * 
 * input : field, the field on witch we want to make the research, such as "name"
 *         query, a string describing the entry of the user, such as "Failure to thrive"
 * output : symptomeId, a table containing the id of the correspondences to the query on the field
 *
 */

public abstract class ReadHpObo {
	
	private static String index = "index/HpObo";
	private static ArrayList<String> symptomeId = new ArrayList<String>();

	private static void ReadHpObo(String field, String queryString) throws IOException, ParseException {
		
		/* we no longer need field but i keep it */
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser parser = new QueryParser(field, analyzer);
		//TermQuery parser = new TermQuery(new Term(field.toString()));
		//Query query = parser.parse(queryString);
		
		//Query query = parser.createBooleanQuery(field, queryString, BooleanClause.Occur.MUST);
		
		String[] queries={queryString,queryString};
		String[] fields={"name","synonym"};
		BooleanClause.Occur[] occurs={BooleanClause.Occur.SHOULD,BooleanClause.Occur.SHOULD};

		//to remember MultiFieldQueryParser to search on many field
		Query query = MultiFieldQueryParser.parse(queries,fields,occurs, analyzer);
		
		//System.out.println(query);
		//System.out.println("Searching for: " + query.toString(field)); // to delete later
		
	    TopDocs results = searcher.search(query, 100); // hope 100 is enough :)
	    ScoreDoc[] hits = results.scoreDocs;
	    
	    int numTotalHits = results.totalHits;
	    //System.out.println(numTotalHits + " total matching documents"); // to delete later
	    
	    symptomeId = new ArrayList<String>();
	    if(numTotalHits!=0){
		    hits = searcher.search(query, numTotalHits).scoreDocs;

	    }
	    String id = "", name = "";
	    for (int i = 0; i < numTotalHits; i++) {
	    	Document doc = searcher.doc(hits[i].doc);
	    	id = doc.get("id").substring(doc.get("id").indexOf(":")+2);
	    	name = doc.get("name").substring(doc.get("name").indexOf(":")+2);
	    	symptomeId.add(id);
	 	   
	    }
		reader.close();
	}
	
private static void ReadHpOboFIX(String field, String queryString) throws IOException, ParseException {
		
		/* we no longer need field but i keep it */
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new KeywordAnalyzer() ;
		
		//TermQuery parser = new TermQuery(new Term(field.toString()));
		//Query query = parser.parse(queryString);
		
		//Query query = parser.createBooleanQuery(field, queryString, BooleanClause.Occur.MUST);
		
		String[] queries={queryString,queryString};
		String[] fields={"name","synonym"};
		BooleanClause.Occur[] occurs={BooleanClause.Occur.MUST,BooleanClause.Occur.MUST};

		//to remember MultiFieldQueryParser to search on many field
		Query query = MultiFieldQueryParser.parse(queries,fields,occurs,new WhitespaceAnalyzer());
		//Query query = MultiFieldQueryParser.parse(queries,  fields,new SimpleAnalyzer());
		
		//System.out.println(query);
		//System.out.println("Searching for: " + query.toString(field)); // to delete later
		
	    TopDocs results = searcher.search(query, 100); // hope 100 is enough :)
	    ScoreDoc[] hits = results.scoreDocs;
	    
	    int numTotalHits = results.totalHits;
	    //System.out.println(numTotalHits + " total matching documents"); // to delete later
	    
	    symptomeId = new ArrayList<String>();
	    if(numTotalHits!=0){
		    hits = searcher.search(query, numTotalHits).scoreDocs;

	    }
	    String id = "", name = "";
	    for (int i = 0; i < numTotalHits; i++) {
	    	Document doc = searcher.doc(hits[i].doc);
	    	id = doc.get("id").substring(doc.get("id").indexOf(":")+2);
	    	name = doc.get("name").substring(doc.get("name").indexOf(":")+2);
	    	symptomeId.add(id);
	    	
	    	////System.out.println(doc.get("synonym"));
	   
	    }
		reader.close();
	}
	
	
	public static ArrayList<String> getId(String field, String query) throws IOException, ParseException {
		ReadHpObo(field, query);
		return symptomeId;
	}
	
	public static ArrayList<String> getIdFIX(String field, String query) throws IOException, ParseException {
		ReadHpOboFIX(field, query);
		return symptomeId;
	}

	
	// to delete later
	public static void main(String[] args) throws Exception {
		System.out.println("Examples Hp.obo :");
		String field = "name";
		String query = "Anomalies head";
	
		System.out.println("Input \""+query+"\" on field \""+field+"\" corresponds to output : \n");
		long startTime = System.nanoTime();
		ArrayList<String> output = getIdFIX(field, query);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		for (String out : output) {
			System.out.println(out);
		}
		System.out.println(output.size());
	/*	field = "synonym";
		query = "Abnormal growth";
		System.out.println("\nInput \""+query+"\" on field \""+field+"\" corresponds to output : \n");
		output = getId(field, query);
		for (String out : output) {
			System.out.println(out);
		}*/
		System.out.println("\nTime needed for one request Hp.obo : "+duration/Math.pow(10,9));
	}
	
}
