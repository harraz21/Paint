package IOFile;
/**
 * @author Harraz21 (mohamed harraz21@gmail.com)
 */
import __Model.Shape;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public interface FileExtention  {
    ArrayList<Shape> read(String path) throws IOException, ParseException, InvocationTargetException, IllegalAccessException;
    void write(ArrayList<Shape> data, String path) throws IOException;
}
