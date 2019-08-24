package __Model;


import _Controllers.Controller;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class Rectangle extends AbstractShape  {
    @Override
    public void updateInfo(double drawStartX ,double drawStartY, double drawEndX,double drawEndY){
        double x =  getMin(drawStartX, drawEndX);
        double y =  getMin(drawStartY , drawEndY);
        double width = calculateAbs(drawStartX, drawEndX);
        double height = calculateAbs(drawStartY , drawEndY);
        double linewidth =  Controller.getInstance().get_line_size().getValue();
        Map<String , Double> m = createMap(width,height,linewidth);
        this.setPosition(new Point((int)x,(int)y));
        this.setProperties(m);
    }
    
    @Override
    public void draw(GraphicsContext canvas){
        canvas.strokeRect(this.xPos,this.yPos,this.properties.get("Width"),this.properties.get("Height"));
        canvas.fillRect(this.xPos,this.yPos,this.properties.get("Width"),this.properties.get("Height"));
    }
    
	@Override
    public Object Clone() throws CloneNotSupportedException{
        Shape rect = new Rectangle();
        rect.setColor(color);
        rect.setFillColor(fillColor);
        Point position = new Point((int)xPos , (int)yPos);
        rect.setPosition(position);
        Map<String, Double> m = new HashMap<String, Double>();
        for (Map.Entry<String , Double> s: properties.entrySet())
            m.put((String)s.getKey(), (Double)s.getValue());
        rect.setProperties(m);
        return rect;
    }
	@Override
	public String toString() {
		return "Rectangle [xPos=" + xPos + ", yPos=" + yPos + ", color=" + color + ", fillColor=" + fillColor
				+ ", properties=" + properties + "]";
	}
    

}
