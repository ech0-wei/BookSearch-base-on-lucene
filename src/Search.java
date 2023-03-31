import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class Search {
	/**
	 * Query parser
	 */
	private QueryParser parser;
	/**
	 * Index searcher
	 */
	private IndexSearcher searcher;
	/**
	 * Stop words
	 */
	private Set<String> stopWords;
	// Constructor
	public Search(String indexPath, Set<String> stopWords) throws IOException {
		this.stopWords = stopWords;
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
	    searcher = new IndexSearcher(reader);
	    Analyzer analyzer = new StandardAnalyzer();
	    parser = new QueryParser(LuceneConstants.KEYWORDS, analyzer);
	}
	
	// search the books
	public void searchBook(String input, String bookId, int number) throws ParseException, IOException {
		
		input = input.replaceAll("[^A-Za-z0-9]", " ");
		// remove stop words
		String[] split = input.split("\\s+");
		StringBuilder builder = new StringBuilder();
		for(String word : split) {
			if(!stopWords.contains(word)) {
				builder.append(word + " ");
			}
		}
		
		// create the query
		Query query = parser.parse(builder.toString());
		System.out.println(query);
		// search the top 5 documents
		TopDocs results = searcher.search(query, number);
		ScoreDoc[] hits = results.scoreDocs;
		for(int i = 0; i < hits.length; i++) {
			Document doc = searcher.doc(hits[i].doc);
	        String docNo = doc.get(LuceneConstants.DOCNO);
	        float score = hits[i].score;
	        System.out.printf("%s Q0 %-12s %d %.15f GRP3\n", bookId, docNo, i+1, score);
		}
	}
}
