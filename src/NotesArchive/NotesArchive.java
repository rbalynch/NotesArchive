package NotesArchive;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NoLockFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Scanner;
import java.util.TimeZone;

public class NotesArchive {
    StandardAnalyzer analyzer = new StandardAnalyzer(); //Handles tokens
    FSDirectory index = FSDirectory.open(Paths.get("C:\\Users\\rbaly\\OneDrive\\Desktop\\INDEX"), NoLockFactory.INSTANCE); //Directory for indexed items
    IndexWriterConfig config = new IndexWriterConfig(analyzer); //Configuration for IndexWriter
    IndexWriter iw = new IndexWriter(index, config); //Creates and maintains index
    static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago")); //Calendar for timestamps

    //INDEXER CONSTRUCTOR
    public NotesArchive() throws IOException, org.json.simple.parser.ParseException {
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    }

    //HELPER METHODS
    @SuppressWarnings("unchecked")
    public static void createJSON(File f) throws IOException {
        JSONObject obj = new JSONObject(); //Creates new JSON object

        //Grabs text from text file
        StringBuilder s = new StringBuilder();
        Scanner sc = new Scanner(f);
        while (sc.hasNextLine()) {
            s.append(sc.nextLine()).append(" ");
        }

        //Grabs necessary timestamps using BasicFileAttributes
        BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
        long fileMade = attr.creationTime().toMillis();
        long lastEdited = attr.lastModifiedTime().toMillis();
        long fileSize = attr.size();

        //Puts all details of file into JSON object
        obj.put("fileName", f.getName());
        obj.put("directory", f.getAbsolutePath());
        obj.put("fileMade", fileMade);
        obj.put("lastEdited", lastEdited);
        obj.put("lastInd", calendar.getTimeInMillis());
        obj.put("fileSize", fileSize);
        obj.put("fileText", s.toString());

        //Writes the json object to .json file
        String dir = "C:\\Users\\rbaly\\IdeaProjects\\NotesArchive_3\\jsons\\" + getFileWithoutExtension(f) + ".json";
        FileWriter writer = new FileWriter(dir);
        writer.write(obj.toString());
        writer.close();
    } //Creates new JSON file based on text file
    public static String getFileWithoutExtension(File file) {
        return file.getName().substring(0, file.getName().indexOf(FilenameUtils.getExtension(file.getName())) - 1);
    } //Gets the file name without the extension
    public static String getFileText(File file) throws FileNotFoundException {
        StringBuilder s = new StringBuilder();
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            s.append(sc.nextLine()).append(" ");
        }
        return s.toString();
    } //Gets text from .txt file
    public static String getJSONText(File master) {
        JSONParser parser = new JSONParser();
        String fileText;
        try {
            Object obj = parser.parse(new FileReader(master));
            JSONObject jsonObject = (JSONObject) obj;
            fileText = (String) jsonObject.get("fileText");
        }
        catch (IOException | org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }
        return fileText;
    }//Gets text from .json file
    public static String getJSONDirectory(File master) {
        JSONParser parser = new JSONParser();
        String directory;
        try {
            Object obj = parser.parse(new FileReader(master));
            JSONObject jo = (JSONObject) obj;
            directory = (String) jo.get("directory");
        }
        catch (org.json.simple.parser.ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        return directory;
    }//Gets directory from .json file
    public static boolean isBlank(File file) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        return attr.size() <= 0;
    }//Checks if file is empty

    //SEARCHING AND INDEXING
    public ArrayList<Document> searchAfter(String s, int id, int count) throws IOException, ParseException {
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        StoredFields storedFields = searcher.storedFields();

        Query q = switch (id) {
            case 0 -> new QueryParser("fileText", analyzer).parse(s);
            case 1 -> new QueryParser("fileName", analyzer).parse(s);
            case 2 -> new QueryParser("serial", analyzer).parse(s);
            case 3 -> new QueryParser("directory", analyzer).parse(s);
            default -> new QueryParser("fileText", analyzer).parse(s);
        };

        TopDocs docs = searcher.search(q, 10);
        for (int i = 0; i < count; i++) {
            docs = searcher.searchAfter(docs.scoreDocs[docs.scoreDocs.length - 1], q, 10);
        }

        ScoreDoc[] hits = docs.scoreDocs;
        ArrayList<Document> list = new ArrayList<>();
        for (ScoreDoc sd : hits) {
            int hitId = sd.doc;
            Document d = storedFields.document(hitId);
            list.add(d);
        }

        reader.close();
        return list;
    }
    public ArrayList<Document> search(String s, int id) throws IOException, ParseException {
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        StoredFields storedFields = searcher.storedFields();

        Query q = switch (id) {
            case 0 -> new QueryParser("fileText", analyzer).parse(s);
            case 1 -> new QueryParser("fileName", analyzer).parse(s);
            case 2 -> new QueryParser("serial", analyzer).parse(s);
            case 3 -> new QueryParser("directory", analyzer).parse(s);
            default -> new QueryParser("fileText", analyzer).parse(s);
        };

        TopDocs docs = searcher.search(q, 10);
        ScoreDoc[] hits = docs.scoreDocs;
        ArrayList<Document> list = new ArrayList<>();
        for (ScoreDoc sd : hits) {
            int hitId = sd.doc;
            Document d = storedFields.document(hitId);
            list.add(d);
        }

        reader.close();
        return list;
    } //Searches through index
    public void addDoc(IndexWriter w, String dir) throws IOException, org.json.simple.parser.ParseException, ParseException {
        Object o = new JSONParser().parse(new FileReader(dir));
        JSONObject jsonObject = (JSONObject) o;

        String fileName = (String) jsonObject.get("fileName");
        String directory = (String) jsonObject.get("directory");
        String fileText = (String) jsonObject.get("fileText");

        Document doc = new Document();
        doc.add(new TextField("fileName", fileName, Field.Store.YES));
        doc.add(new StringField("directory", directory, Field.Store.YES));
        doc.add(new TextField("fileText", fileText, Field.Store.YES));
        doc.add(new StringField("jsonDir", dir, Field.Store.YES));
        w.addDocument(doc);
    }

    //STARTUP/SHUTDOWN METHODS
    public void refreshJSONS() throws IOException, org.json.simple.parser.ParseException, ParseException {
        File file = new File("C:\\Users\\rbaly\\IdeaProjects\\NotesArchive_3\\jsons"); //Directory of all .json files
        File[] list = file.listFiles();

        if (list != null) {
            for (File json : list) {
                if (!isBlank(json)) { //Ensures the file isn't blank before continuing
                    File realFile = new File(getJSONDirectory(json)); //Grabs the file the .json file is pointing to
                    if (!realFile.exists()) { //Checks if the real file is no longer in this directory
                        iw.deleteDocuments(new Term("directory", getJSONDirectory(json))); //Removes it from the index
                        FileWriter writer = new FileWriter(json); //Replaces the .json file with blank text
                        writer.write("");
                        writer.close();
                    }
                    else {
                        String fileText = getJSONText(json); //Gets both the text from the .txt file and the text stored in the .json file
                        String realText = getFileText(realFile);

                        if (realText.compareTo(fileText) != 0) { //Changes stored text on json if text file was edited
                            createJSON(realFile);
                            iw.deleteDocuments(new Term("fileName", realFile.getName()));
                        }

                        if (search(realFile.getName(), 1).isEmpty()) { //Checks if file name is found in index
                            addDoc(iw, json.getAbsolutePath()); //Indexes if it hasn't
                        }
                        else if (!search(realFile.getName(), 1).isEmpty()) { //If the name has been found in the index
                            ArrayList<Document> docs = search(realFile.getName(), 1); //List of results from the file name
                            boolean indexed = false;
                            for (Document d : docs) { //Checks every result to see if the result's directory matches the file
                                if (Objects.equals(d.get("directory"), realFile.getAbsolutePath())) {
                                    indexed = true;
                                }
                            }
                            if (!indexed) { //If directory was never found
                                addDoc(iw, json.getAbsolutePath()); //Indexes new file
                            }
                        }
                    }
                }
            }
        }
    } //Refreshes all .json files in jsons folder and fixes any issues
    public void cleanJSONFolder() throws IOException {
        File file = new File("C:\\Users\\rbaly\\IdeaProjects\\NotesArchive_3\\jsons"); //.json files directory
        File[] list = file.listFiles();
        if (list != null) {
            for (File json : list) {
                BasicFileAttributes attr = Files.readAttributes(json.toPath(), BasicFileAttributes.class);
                if (attr.size() <= 0) { //If the .json file being checked is empty
                    json.delete(); //Delete the .json file from the folder
                }
            }
        }
    } //Removes all blank .json files
}