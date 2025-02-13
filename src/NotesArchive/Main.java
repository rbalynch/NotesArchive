/**
 * @author  Riley Lynch
 * @version 0.8.3-alpha
 * @since   2/13/2025
 */

package NotesArchive;
import org.json.simple.parser.ParseException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {
        new AppWindow("Files");
    }
}
