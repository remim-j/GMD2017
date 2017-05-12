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

public class ReadOminIndex {
  
	private ReadOminIndex() {
		String indexPath = "index/Omim";
		String docsPath = "omim.txt";
	    final Path docDir = Paths.get(docsPath);

	    try {
	    	System.out.println("Indexing Omim to directory '" + indexPath + "'..."); // to delete later
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
			String line = "", no = "", temp = "";
			boolean first = true;
			int i = 0, n = 0;
			
			while ((line=br.readLine()) != null) {
	 
				// initialize and save
				if (line.startsWith("*RECORD*")) {
					if (first) {
						first = false;
					} else {
						writer.addDocument(doc);
					}
					doc = new Document();
				}
				
				// store TI
				if (line.startsWith("*FIELD* TI")) {
					n = no.length();
					line = br.readLine().substring(n+2);
					temp = "";
					for (i = 0;i < line.length();i++) {
						if (line.charAt(i) == ';') {
							break;
						} else {
							temp = temp + line.charAt(i);
						}
					}
					temp=temp.toUpperCase();
					doc.add(new StoredField("TI", temp));
					System.out.println("TI "+ temp); // to delete later
				}
				
				// index and store CS
				if (line.startsWith("*FIELD* CS")) {
					temp = "";
					line = br.readLine();
					while (!(line.contains("*FIELD*"))) {
						if (line.endsWith(":") && temp.equals("")==false) {
							temp = temp + ";";
						} else if (!line.contains(":") && !line.trim().equals("")) {
							line = line.trim();
							temp = temp + line;
						}
						line = br.readLine();
					}
					doc.add(new TextField("CS", temp, Field.Store.YES));
				}
				
				// index and store NO
				if (line.startsWith("*FIELD* NO")) {
					no = br.readLine();
					doc.add(new TextField("NO", no, Field.Store.YES));
				}
	      }

		} catch (Exception e){
	    	System.out.println(e.toString());
	    }
	}

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		new ReadOminIndex(); 
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("\nTime needed for indexing Omim : "+duration/Math.pow(10,9));
	}
}

