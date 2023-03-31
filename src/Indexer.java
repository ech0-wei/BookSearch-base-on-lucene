import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
	/**
	 * The path of raw documents
	 */
	private String docsPath;
	/**
	 * The path of index files
	 */
	private String indexPath;
	/**
	 * Stop words
	 */
	private Set<String> stopWords;
	
	// Constructor
	public Indexer(String docsPath, String indexPath, Set<String> stopWords) throws IOException {
		this.docsPath = docsPath;
		this.indexPath = indexPath;
		this.stopWords = stopWords;
	}
	
	// build the index
	public void buildIndex() throws IOException {
		// initiate
		Path docsDir = Paths.get(docsPath);
		Directory indexDirectory = FSDirectory.open(Paths.get(indexPath));
		
		Analyzer analyzer = new StandardAnalyzer();
	    IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
	    iwc.setOpenMode(OpenMode.CREATE);
	    
	    // create the indexWriter
	    IndexWriter writer = new IndexWriter(indexDirectory, iwc);
	    indexDocs(writer, docsDir);
	    System.out.println(writer.toString());
	    writer.close();
	}
	
	// Build the index files
	public void indexDocs(IndexWriter writer, Path path) throws IOException {
		if(Files.isDirectory(path)) {
			// the path is a directory
			Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				  indexDoc(writer, file);
		          return FileVisitResult.CONTINUE;
		        }
			});
		}
		else {
			// the path is a file
			indexDoc(writer, path);
		}
	}
	
	// Index a single document
	public void indexDoc(IndexWriter writer, Path file) throws IOException {
		try (BufferedReader rd = new BufferedReader(new FileReader(file.toString()))){
			String line;
			Document doc = null;
			String docNo = null;
			StringBuilder builder = new StringBuilder();
			boolean isInHtml = false;
			while((line = rd.readLine()) != null) {
				if(!line.isEmpty()) {
					line = line.trim();
					String mLine = line.toUpperCase();
					
					// read the DOCNO
					if(mLine.startsWith("<DOCNO>") && mLine.endsWith("</DOCNO>")) {
						docNo = line.substring(7, line.length()-8).trim();
						doc = new Document();
						Field docNoField = new StringField(LuceneConstants.DOCNO, docNo, Field.Store.YES);
						doc.add(docNoField);
					}
					
					if(doc != null && mLine.startsWith("<HTML>")) {
						isInHtml = true;
					}
					
					if(doc != null && mLine.startsWith("</HTML>")) {
						// add a document
						Field keywordsField = new TextField(LuceneConstants.KEYWORDS, builder.toString(), Field.Store.NO);
						doc.add(keywordsField);
						writer.addDocument(doc);
						doc = null;
						isInHtml = false;
						builder = new StringBuilder();
					}
					
					if(isInHtml) {
						// add other keywords
						line = line.replaceAll("[^A-Za-z0-9]", " ");
						String[] split = line.split("\\s+");
						StringBuilder mBuilder = new StringBuilder();
						for(String word : split) {
							if(!stopWords.contains(word)) {
								mBuilder.append(word + " ");
							}
						}
						builder.append(" ").append(mBuilder.toString());
					}
				}
			}
		}
	}
}
