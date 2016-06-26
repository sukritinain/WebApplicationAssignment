import java.io.StringReader;
import java.io.File;
import java.nio.file.*;
import java.lang.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import java.io.*;
import java.nio.file.Path;

public class Searcher {

    public Searcher() {}
    public static void main(String[] args) throws Exception {
	String usage = "java Searcher";
	search(args[0], "indexes");

    }    
    
    private static TopDocs search(String searchText, String p) {   
		System.out.println("Running search(" + searchText + ")");
	try {  
	    
	    Path path = Paths.get(p);
	    Directory directory = FSDirectory.open(path);       
	    IndexReader indexReader =  DirectoryReader.open(directory);
	    String[] fields = {"item_name", "description","category_name"};
	    Query query = MultiFieldQueryParser.parse(new String[] {searchText, searchText, searchText}, fields, new SimpleAnalyzer());    
	    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
	//if arguments passed are one or two   
	    TopDocs topDocs = indexSearcher.search(query,10000);
	    System.out.println("Number of Hits: " + topDocs.totalHits);
	//Sorting function
		Sort sort = new Sort();  
		String field = "current_price"; 
		Type type = Type.STRING; 
		boolean descending = true; 

		SortField sortField = new SortField(field, type, descending);

		sort.setSort(sortField);

	    for (ScoreDoc scoreDoc: topDocs.scoreDocs) {           
			Document document = indexSearcher.doc(scoreDoc.doc);
			System.out.println("ItemID: " + document.get("item_id") + ", score: "+scoreDoc.score + " [" + document.get("item_name") + "]");

	    }		
	    return topDocs;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }
}