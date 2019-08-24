package __Model;

import _Controllers.Controller;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Triangle extends AbstractShape  {
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
    	double xVerticies[] = {this.xPos , this.xPos+this.properties.get("Width") 
    	, this.xPos+(this.properties.get("Width")/2)};
    	double yVerticies[] = {this.yPos+this.getProperties().get("Height") 
    			, this.yPos+this.getProperties().get("Height")  ,this.yPos};
    	canvas.fillPolygon(xVerticies, yVerticies, 3);
    	canvas.strokePolygon(xVerticies, yVerticies, 3);
    }
    @Override
    public Object Clone() throws CloneNotSupportedException{
		Shape triangle = new Triangle();
		triangle.setColor(color);
		triangle.setFillColor(fillColor);
		Point position = new Point((int)xPos , (int)yPos);
		triangle.setPosition(position);
		Map<String, Double> m = new HashMap<String, Double>();
		for (Map.Entry<String , Double> s: properties.entrySet())
			m.put((String)s.getKey(), (Double)s.getValue());
		triangle.setProperties(m);
		return triangle;
    	}
	@Override
	public String toString() {
		return "Triangle [xPos=" + xPos + ", yPos=" + yPos + ", color=" + color + ", fillColor=" + fillColor
				+ ", properties=" + properties + "]";
	}


}
