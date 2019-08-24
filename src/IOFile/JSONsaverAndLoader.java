package IOFile;
/**
 * @author Harraz21 (mohamed harraz21@gmail.com)
 */
import __Model.*;
import __Model.Shape;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

public class JSONsaverAndLoader implements FileExtention {
    private ArrayList<Shape> data;

    @Override
    public ArrayList<Shape> read(String path) throws IOException, ParseException, InvocationTargetException, IllegalAccessException {
        FileReader reader = new FileReader(new File(path));
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        Object parsedObject = parser.parse(reader);
        JSONObject jsonObject = (JSONObject) parsedObject;
        data=new ArrayList<>();
        JSONArray jsonArray = (JSONArray) jsonObject.get("ShapesList");
        for (Object jsnO : jsonArray) {
            JSONObject shape = (JSONObject) jsnO;
            Shape shapeMade;
            ShapeFactory shapeFactory = new ShapeFactory();
            switch (shape.get("Class").toString()) {
                case "class __Model.Rectangle":
                    shapeMade = shapeFactory.createShape(SHAPES.RECTANGLE);
                    break;
                case "class __Model.Circle":
                    shapeMade = shapeFactory.createShape(SHAPES.CIRCLE);
                    break;
                case "class __Model.Line":
                    shapeMade = shapeFactory.createShape(SHAPES.LINE);
                    break;
                case "class __Model.Square":
                    shapeMade = shapeFactory.createShape(SHAPES.SQUARE);
                    break;
                case "class __Model.Ellipse":
                    shapeMade = shapeFactory.createShape(SHAPES.ELLIPSE);
                    break;
                case "class __Model.Triangle":
                    shapeMade = shapeFactory.createShape(SHAPES.TRIANGLE);
                    break;
                default:
                    shapeMade = shapeFactory.createShape(SHAPES.TRIANGLE);
                    break;
            }
            int r, b, g;
            JSONObject fillColor = (JSONObject) shape.get("fillColor");
            int a=Math.toIntExact(Long.valueOf((long)fillColor.get("a")));
            r = Math.toIntExact(Long.valueOf((Long) fillColor.get("r")));
            b = Math.toIntExact(Long.valueOf((Long) fillColor.get("b")));
            g = Math.toIntExact(Long.valueOf((Long) fillColor.get("g")));
            java.awt.Color c = new Color(r, g, b,a);
            if (shapeMade != null) {
                shapeMade.setFillColor(c);
            }
            JSONObject color = (JSONObject) shape.get("color");
            r = Math.toIntExact(Long.valueOf((Long) color.get("r")));
            b = Math.toIntExact(Long.valueOf((Long) color.get("b")));
            g = Math.toIntExact(Long.valueOf((Long) color.get("g")));
            c = new Color(r, g, b);
            if (shapeMade != null) {
                shapeMade.setColor(c);
            }
            int x = Integer.valueOf(Long.toString((Long) shape.get("xPos")));
            int y = Integer.valueOf(Long.toString((Long) shape.get("yPos")));
            if (shapeMade != null) {
                shapeMade.setPosition(new Point(x, y));
            }
            if (shapeMade != null) {
                Map<String, Double> props;
                JSONObject PROPERTIES = (JSONObject) shape.get("properties");
                props = (Map<String, Double>) PROPERTIES;
                shapeMade.setProperties(props);
            }
            data.add(shapeMade);
        }
        return this.data;
    }

    @Override
    public void write(ArrayList<Shape> data, String path) throws IOException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Shape shape : data) {
            JSONObject shapeToBeAdded = new JSONObject();
            JSONObject fillColor = new JSONObject();
            fillColor.put("a",shape.getFillColor().getAlpha());
            fillColor.put("r", shape.getFillColor().getRed());
            fillColor.put("b", shape.getFillColor().getBlue());
            fillColor.put("g", shape.getFillColor().getGreen());
            shapeToBeAdded.put("fillColor", fillColor);
            JSONObject color = new JSONObject();
            color.put("r", shape.getColor().getRed());
            color.put("b", shape.getColor().getBlue());
            color.put("g", shape.getColor().getGreen());
            shapeToBeAdded.put("color", color);
            int x = shape.getPosition().x;
            int y = shape.getPosition().y;
            shapeToBeAdded.put("xPos", x);
            shapeToBeAdded.put("yPos", y);
            shapeToBeAdded.put("properties", shape.getProperties());
            shapeToBeAdded.put("Class", shape.getClass().toString());
            jsonArray.add(shapeToBeAdded);
        }
        jsonObject.put("ShapesList", jsonArray);
        FileWriter fileWriter = new FileWriter(new File(path));
        fileWriter.write(jsonObject.toJSONString());
        fileWriter.flush();
        fileWriter.close();
    }
}
