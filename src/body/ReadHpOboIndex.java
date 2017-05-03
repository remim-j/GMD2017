package body;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

public class ReadHpOboIndex {
  
	private ReadHpOboIndex() {
		String indexPath = "index/HpObo";
		String docsPath = "hp_obo_file";
	    final Path docDir = Paths.get(docsPath);

	    try {
	    	System.out.println("Indexing Hp.obo to directory '" + indexPath + "'..."); // to delete later
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
			String line = "", string = "";
			boolean first = true;
			int i = 0;
			
			while ((line=br.readLine()) != null) {
	 
				// initialize and save
				if (line.startsWith("[Term]")) {
					if (first) {
						first = false;
					} else {
						//System.out.println("adding " + file); // to delete later
						writer.addDocument(doc);
					}
					doc = new Document();
				}
				
				// stocker and indexer name
				if (line.startsWith("name:")) {
					string = line.substring(6);
					doc.add(new TextField("name", line,Field.Store.YES));
					//System.out.println("name "+ string); // to delete later
				}
				
				// indexer synonym
				if (line.startsWith("synonym:")) {
					line = line.substring(10);
					i = 0;
					string = "";					
					while (line.charAt(i) != '"') {
						string = string + line.substring(i,i+1);
						i++;
					}
					doc.add(new TextField("synonym", string, Field.Store.NO));
					//System.out.println("synonym "+ string); // to delete later
				}
			      
				// indexer and stocker id
				if (line.startsWith("id:")) {
					string = line.substring(4);
					doc.add(new TextField("id", line, Field.Store.YES));
					//System.out.println("id "+ string); // to delete later
				}
			      
				// stocker def
				if (line.startsWith("def:")) {
					line = line.substring(6);
					i = 0;
					string = "";					
					while (line.charAt(i) != '"') {
						string = string + line.substring(i,i+1);
						i++;
					}
					doc.add(new StoredField("def", string));
					//System.out.println("def "+ string); // to delete later
				}
				
				// stocker is_a
				if (line.startsWith("is_a:")) {
					line = line.substring(6);
					i = 0;
					string = "";					
					while (line.charAt(i) != '!') {
						string = string + line.substring(i,i+1);
						i++;
					}
					doc.add(new StoredField("is_a", string.trim()));
					//System.out.println("is_a "+ string.trim()); // to delete later
				}
	      }

		} catch (Exception e){
	    	System.out.println(e.toString());
	    }
	}

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		new ReadHpOboIndex(); 
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("\nTime needed for indexing Hp.obo : "+duration/Math.pow(10,9));
	}
}
