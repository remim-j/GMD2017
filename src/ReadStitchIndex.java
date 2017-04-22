import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class ReadStitchIndex {
  
	private ReadStitchIndex() {
		String indexPath = "index/Stitch";
		String docsPath = "chemical.sources.v5.0.tsv/chemical.sources.v5.0.tsv";
	    final Path docDir = Paths.get(docsPath);

	    try {
	    	System.out.println("Indexing Stitch to directory '" + indexPath + "'..."); // to delete later
	    	Directory dir = FSDirectory.open(Paths.get(indexPath));
	    	Analyzer analyzer = new StandardAnalyzer();
	    	IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
	    	iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
	    	IndexWriter writer = new IndexWriter(dir, iwc);
	    	indexDoc(writer, docDir, Files.getLastModifiedTime(docDir).toMillis());
	    	writer.close();
	    	
	    } catch (IOException e) {
	      System.out.println(" caught a " + e.getClass() +
	       "\n with message: " + e.getMessage());
	    }
	}
	  
	static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
		try {
			InputStream ips = Files.newInputStream(file);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
	    	
			// make a new, empty document
			Document doc = null;
			String line = "";
			boolean first = true, end = false;
			String[] tokens;
			Scanner sc = new Scanner(line);
			
			while ((line=br.readLine()) != null) {
	 
				while (!line.startsWith("CID")) {
					line = br.readLine();
				} 
				if (first) {
					first = false;
				} else {
					//System.out.println("adding " + file); // to delete later
					writer.addDocument(doc);
				}
				doc = new Document();
				sc = new Scanner(line);
				tokens = line.split("[\\s\t]+");
				
				if (tokens[2].equals("BindingDB")) { // end of ATC
					break;
				}
				
				// index compound_id with m
				doc.add(new TextField("compound_id_With_m", tokens[0], Field.Store.NO));
				//System.out.println("compound_id "+ tokens[0]); // to delete later
				
				//index compound_id with s
				doc.add(new TextField("compound_id_With_s", tokens[1], Field.Store.NO));

				// stock ATC_id
				doc.add(new StoredField("ATC_id", tokens[3]));
				//System.out.println("ATC_id "+ tokens[3]); // to delete later
				
			}
			sc.close();

		} catch (Exception e){
	    	System.out.println(e.toString());
	    }
	}
	 
	
	

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		new ReadStitchIndex(); 
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("\nTime needed for indexing Stitch : "+duration/Math.pow(10,9));
	}
}
