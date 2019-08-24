package __Model;

import _Controllers.Controller;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Circle extends AbstractShape  {
	
    @Override
    public void updateInfo(double drawStartX ,double drawStartY, double drawEndX,double drawEndY){
        double x=0,y=0;
        double width = calculateAbs(drawStartX, drawEndX);
        double height = calculateAbs(drawStartY, drawEndY);
        double minLen=getMin(width,height);
        if(drawEndX<=drawStartX) {
            x = drawStartX - minLen;
        }else if(drawEndX>drawStartX) {
            x = drawStartX;
        }
        if(drawEndY<=drawStartY) {
            y = drawStartY - minLen;
        }else if(drawEndY>drawStartY) {
            y = drawStartY;
        }
        double linewidth =  Controller.getInstance().get_line_size().getValue();
        Map<String , Double> m = createMap(minLen,minLen,linewidth);
        this.setPosition(new Point((int)x,(int)y));
        this.setProperties(m);
    }
    @Override
    public void draw(GraphicsContext canvas){
        canvas.strokeOval(this.xPos,this.yPos,this.properties.get("Width"),this.properties.get("Height"));
        canvas.fillOval(this.xPos,this.yPos,this.properties.get("Width"),this.properties.get("Height"));
    }
    @Override
    public Object Clone() throws CloneNotSupportedException{
        Shape circle = new Circle();
        circle.setColor(color);
        circle.setFillColor(fillColor);
        Point position = new Point((int)xPos , (int)yPos);
        circle.setPosition(position);
        Map<String, Double> m = new HashMap<String, Double>();
        for (Map.Entry<String , Double> s: properties.entrySet())
            m.put((String)s.getKey(), (Double)s.getValue());
        circle.setProperties(m);
        return circle;
    }
	@Override
	public String toString() {
		return "Circle [xPos=" + xPos + ", yPos=" + yPos + ", color=" + color + ", fillColor=" + fillColor
				+ ", properties=" + properties + "]";
	}

}
