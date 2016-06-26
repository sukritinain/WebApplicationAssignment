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
import java.sql.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Indexer {

    public Indexer() {}
    public static IndexWriter indexWriter;

    public static void main(String args[]) {
    String usage = "java Indexer";
    rebuildIndexes("indexes");
    }

    public static void insertDoc(IndexWriter i, String item_id, String item_name, String description, String current_price, String category_name){

 
        Document doc = new Document();
        doc.add(new TextField("item_id", item_id, Field.Store.YES));
        doc.add(new TextField("item_name", item_name, Field.Store.YES));
        doc.add(new TextField("description", description, Field.Store.YES));
        doc.add(new TextField("current_price", current_price, Field.Store.YES));
        doc.add(new TextField("category_name", category_name, Field.Store.YES));

        try{
            i.addDocument(doc); } catch (Exception e) {e.printStackTrace(); }
}
    
    public static void rebuildIndexes(String indexPath) {    
    try {

    Path path = Paths.get(indexPath);   
    System.out.println("Indexing to directory '" + indexPath + "'...\n");
    Directory directory = FSDirectory.open(path);
    IndexWriterConfig config = new IndexWriterConfig(new SimpleAnalyzer());
    IndexWriter i  = new IndexWriter(directory, config);
    i.deleteAll();


    Connection conn = null;
    Statement stmt = null;

    String sql = "SELECT i.item_id, i.item_name, i.description, a.current_price, GROUP_CONCAT(c.category_name separator ',') as category_name FROM has_category c INNER JOIN item i on c.item_id = i.item_id INNER JOIN auction a on i.item_id = a.item_id GROUP BY c.item_id"; 

    conn = DbManager.getConnection(true);
    stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sql);
    
    while(rs.next()){
        String item_id = "" + rs.getString("item_id"); 
        insertDoc(i,rs.getString("item_id"), rs.getString("item_name"), rs.getString("description"),rs.getString("current_price"),rs.getString("category_name")); 
       
}

    i.close();
    directory.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}


    