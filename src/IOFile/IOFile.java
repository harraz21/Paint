package IOFile;
/**
 * @author Harraz21 (mohamed harraz21@gmail.com)
 */
import __Model.Shape;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class IOFile {
    private ArrayList<Shape> data;
    private FileExtention extentionType;


    public void Save(String path) throws IOException, InvocationTargetException, IllegalAccessException {
        this.ExtentionTypeDetermination(path);
        extentionType.write(this.data ,path);
    }
    public void load(String path) throws IOException, ParseException, InvocationTargetException, IllegalAccessException {
        this.ExtentionTypeDetermination(path);
        this.data=extentionType.read(path);
    }
    public void ExtentionTypeDetermination(String fileName)
    {
        if(fileName.endsWith(".xml"))
        {
            extentionType=new XMLsaverAndLoader();
        }else if(fileName.endsWith(".json"))
        {
            extentionType=new JSONsaverAndLoader();
        }
    }

    public ArrayList<Shape> getData() {
        return data;
    }

    public void setData(ArrayList<Shape> data) {
        this.data = data;
    }
}
