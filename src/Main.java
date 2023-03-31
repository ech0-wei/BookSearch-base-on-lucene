import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.lucene.queryparser.classic.ParseException;

public class Main {
	// delete previous index files
	public static void deleteFile(File dirFile) {
		if(!dirFile.exists()) {
			return;
		}
		if(dirFile.isFile()) {
			dirFile.delete();
		}
		else {
			for(File file : dirFile.listFiles()) {
				deleteFile(file);
			}
		}
	}
	
	// read stop words
	public static Set<String> readStopWords() {
		Set<String> stopWords = new HashSet<>();
		String fileName = "stopwords.txt";
		try (BufferedReader rd = new BufferedReader(new FileReader(fileName))){
			String word;
			while((word = rd.readLine()) != null) {
				word = word.trim();
				if(!word.isEmpty()) {
					stopWords.add(word);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return stopWords;
	}
	
	public static void main(String[] args){
		if(args.length != 2){
			System.out.println("Usage: java Indexer <DocsPath> <IndexPath>");
			System.exit(1);
		}
		// build the indexer
		System.out.println("Building the indexes...");
		System.out.println("Please wait for a momnet.");
		
		Set<String> stopWords = readStopWords();
		try {
			File dirFile = new File(args[1]);
			deleteFile(dirFile);
			Indexer indexer = new Indexer(args[0], args[1], stopWords);
			indexer.buildIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Build the indexes successfully.");
		try {
			Search search = new Search(args[1], stopWords);
			
			// prompt the user to search the books
			Scanner sc = new Scanner(System.in);
			String input;
			while(true) {
				System.out.println("\n============Search the books============");
				System.out.print("Enter the book id: ");
				String bookId = sc.nextLine().trim();
				if(bookId.equals("exit")) {
					break;
				}
				System.out.print("Enter the keywords: ");
				input = sc.nextLine().trim();
				System.out.print("Enter the number of results: ");
				int number = sc.nextInt();
				sc.nextLine();
				
				// search the books according to keywords
				search.searchBook(input, bookId, number);
			}
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
