package __Model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractShape implements Shape,Cloneable {
    //UTILITY FUNCTIONS
    protected double getMin(double x, double y){
        return  Math.min(x,y);
    }

    protected double getMax(double x , double y) {
        return Math.max(x, y);
    }
    protected double calculateAbs(double x, double y){
        return Math.abs(x - y);
    }
    /////////////
    public java.util.Map<String, Double> createMap( double width,double height,double linewidth){
        Map<String, Double> m = new HashMap<String, Double>();
        m.put("Width",width);
        m.put("Height",height);
        m.put("Linewidth", linewidth);
        return m;
    }
    protected double xPos,yPos;

    protected java.awt.Color color=null;
    protected java.awt.Color fillColor=null;
    private double select_xOffset;
    private double select_yOffset;
    protected Map<String,Double> properties=new HashMap<>();
    private static ArrayList<Shape> shapesList = new ArrayList<Shape>();
    private static ArrayList<Shape> selectedShapes = new ArrayList<Shape>();
   // public ShapeFactory(){}

    @Override
    public void setPosition(java.awt.Point position){
        xPos=position.getX();
        yPos=position.getY();
    }

    public static ArrayList<Shape> getShapesList() {
        return shapesList;
    }

    public static ArrayList<Shape> getSelectedShapes() { return selectedShapes; }

    @Override

    public java.awt.Point getPosition(){

        return new Point((int)xPos,(int)yPos);
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }
    public double getxPos() {
        return xPos;
    }
    public void setyPos(double yPos) {
        this.yPos = yPos;
    }
    public double getyPos() {
        return yPos;
    }
    @Override
    public void setColor(java.awt.Color color){
        this.color=color;
    }
    @Override
    public java.awt.Color getColor(){
        return this.color;
    }
    @Override
    public void setFillColor(java.awt.Color color){
        this.fillColor = color;
    }
    @Override
    public java.awt.Color getFillColor(){
        return this.fillColor;
    }
    public void printData(){
        for(Shape shape:shapesList)
        	System.out.println(shape);
    }


    @Override
    public Point getSelectOffset(){
        return new Point((int)select_xOffset,(int)select_yOffset);
    }

    @Override
    public Map<String, Double> getProperties() {
        return properties;
    }
    
    @Override
    public void setProperties(Map<String, Double> properties) {
        this.properties = properties;
    }

    public static void setShapesList(ArrayList<Shape> shapesList) {
        AbstractShape.shapesList = shapesList;
    }

    public static void setSelectedShapes(ArrayList<Shape> selectedShapes) { AbstractShape.selectedShapes = selectedShapes; }

    @Override
    public void setSelectOffset(double select_xOffset,double select_yOffset) {
        this.select_xOffset = select_xOffset;
        this.select_yOffset = select_yOffset;
    }
}
