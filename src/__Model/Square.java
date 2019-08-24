package __Model;

import _Controllers.Controller;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Square extends AbstractShape {
    /**
     * d :draw start
     *              |
     *              |
    *       --------d-------
     *              |
     *              |
     *
     */
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
        canvas.strokeRect(this.xPos,this.yPos,this.properties.get("Width"),this.properties.get("Height"));
        canvas.fillRect(this.xPos,this.yPos,this.properties.get("Width"),this.properties.get("Height"));
    }
    @Override
    public Object Clone() throws CloneNotSupportedException{
        Shape square = new Square();
        square.setColor(color);
        square.setFillColor(fillColor);
        Point position = new Point((int)xPos , (int)yPos);
        square.setPosition(position);
        Map<String, Double> m = new HashMap<String, Double>();
        for (Map.Entry<String , Double> s: properties.entrySet())
            m.put((String)s.getKey(), (Double)s.getValue());
        square.setProperties(m);
        return square;
    }
	@Override
	public String toString() {
		return "Square [xPos=" + xPos + ", yPos=" + yPos + ", color=" + color + ", fillColor=" + fillColor
				+ ", properties=" + properties + "]";
	}

}
