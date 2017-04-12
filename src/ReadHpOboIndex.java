import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

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
		String indexPath = "/home/aurore/workspace/GMD/index/HpObo";
		String docsPath = "hp_obo_file";
	    final Path docDir = Paths.get(docsPath);
	    boolean create = true;
	    Date start = new Date();

	    try {
	    	System.out.println("Indexing to directory '" + indexPath + "'...");
	    	Directory dir = FSDirectory.open(Paths.get(indexPath));
	    	Analyzer analyzer = new StandardAnalyzer();
	    	IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

	    	if (create) {
	    		iwc.setOpenMode(OpenMode.CREATE);
	    	} else {
	    		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
	    	}

	    	IndexWriter writer = new IndexWriter(dir, iwc);
	    	indexDoc(writer, docDir, Files.getLastModifiedTime(docDir).toMillis());
	    	writer.close();
	
	    	Date end = new Date();
	    	System.out.println(end.getTime() - start.getTime() + " total milliseconds");

		// si erreur
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
			String line;
			String string = "";
			boolean first = true;
			while ((line=br.readLine()) != null) {
	 
				// initialize and save
				if (line.startsWith("[Term]")) {
					if (first) {
						first = false;
					} else {
						System.out.println("adding " + file);
						writer.addDocument(doc);
					}
					doc = new Document();
				}
				
				// stocker et indexer name
				if (line.startsWith("name:")) {
					string = line.substring(7);
					doc.add(new TextField("name ", line,Field.Store.YES));
					System.out.println("name "+ string);
				}
				
				// stocker synonym
				if (line.startsWith("synonym:")) {
					string = line.substring(10);
					doc.add(new StoredField("synonym ", string));
					System.out.println("synonym "+ string);
				}
			      
				// indexer et stocker id
				if (line.startsWith("id:")) {
					string = line.substring(5);
					doc.add(new TextField("id ", line,Field.Store.YES));
					System.out.println("id "+ string);
				}
			      
				// indexer def
				if (line.startsWith("def:")) {
					string = line.substring(6);
					doc.add(new TextField("def ", line,Field.Store.NO));
					System.out.println("def "+ string);
				}
				
				// indexer is_a
				if (line.startsWith("is_a:")) {
					string = line.substring(7);
					doc.add(new TextField("is_a ", line,Field.Store.NO));
					System.out.println("is_a "+ string);
				}
	    	  
	      }

		} catch (Exception e){
	    	System.out.println(e.toString());
	    }
	}

	public static void main(String[] args) {
		new ReadHpOboIndex(); 
	}
}
