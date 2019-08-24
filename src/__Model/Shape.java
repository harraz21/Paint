package __Model;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;


public interface Shape{

    public void setPosition(java.awt.Point position);
    public java.awt.Point getPosition();
    /* update shape specific properties (e.g., radius) */
    public void setProperties(java.util.Map<String, Double> properties);
    public java.util.Map<String, Double> getProperties();
    public void setColor(java.awt.Color color);
    public java.awt.Color getColor();
    public void setFillColor(java.awt.Color color);
    public java.awt.Color getFillColor();
    /* redraw the shape on the canvas,
    for swing, you will cast canvas to java.awt.Graphics */
    public void draw(GraphicsContext canvas);
    /* create a deep clone of the shape */
    public Object Clone() throws CloneNotSupportedException;
    //OUR FUNCTIONS
    public void updateInfo(double drawStartX ,double drawStartY, double drawEndX,double drawEndY);
    public Point getSelectOffset();
    public void setSelectOffset(double select_xOffset,double select_yOffset);
}