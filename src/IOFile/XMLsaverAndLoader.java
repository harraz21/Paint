package IOFile;
/**
 * @author Harraz21 (mohamed harraz21@gmail.com)
 */

import __Model.Shape;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;

public class XMLsaverAndLoader  implements FileExtention {
    private ArrayList<Shape> data;

    @Override
    public ArrayList<Shape> read(String path) throws FileNotFoundException {
        FileInputStream fileInputStream=new FileInputStream(new File(path));
        XMLDecoder decoder=new XMLDecoder(fileInputStream);
        this.data=(ArrayList<Shape>)decoder.readObject();
        return this.data;
    }

    @Override
    public void write(ArrayList<Shape> data, String path) throws IOException {
        FileOutputStream fileOutputStream=new FileOutputStream(new File(path));
        XMLEncoder encoder=new XMLEncoder(fileOutputStream);
        encoder.writeObject(data);
        encoder.close();
        fileOutputStream.close();
    }
}
