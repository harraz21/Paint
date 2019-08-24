package __Model;

import _Controllers.Controller;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Line extends AbstractShape {

    double x1,x2,y1,y2;
    double xStart,yStart;
    double xEnd,yEnd;
    boolean xLess,yLess;

    @Override
    public void updateInfo(double drawStartX ,double drawStartY, double drawEndX,double drawEndY){
        this.xStart=drawStartX;
        this.yStart=drawStartY;
        this.xEnd=drawEndX;
        this.yEnd=drawEndY;
        xLess=false;
        yLess=false;
        double x =  getMin(drawStartX, drawEndX);
        double y =  getMin(drawStartY , drawEndY);
        double width = calculateAbs(drawStartX, drawEndX);
        double height = calculateAbs(drawStartY , drawEndY);
        double linewidth =  Controller.getInstance().get_line_size().getValue();
        if(this.xPos<xStart ) {
        xLess=true;
        }
        if(this.yPos<yStart ) {
            yLess=true;
        }
        Map<String , Double> m = createMap(width,height,linewidth);
        this.setProperties(m);
        this.setPosition(new Point((int)x,(int)y));

    }
    @Override
    public void draw(GraphicsContext canvas){
      double w=this.properties.get("Width");
      double h=this.properties.get("Height");
      if(this.xLess){
          this.x1=this.xPos+w;
      }else{
          this.x1=this.xPos;
      }
      if(xEnd>xStart){
          this.x2=this.x1+w;
      }else{
          this.x2=this.x1-w;
      }
        if(this.yLess){
            this.y1=this.yPos+h;
        }else{
            this.y1=this.yPos;
        }
        if(yEnd>yStart){
            this.y2=this.y1+h;
        }else{
            this.y2=this.y1-h;
        }
        double xVerticies[] = {this.x1, this.x2};
        double yVerticies[] = {this.y1, this.y2};
        canvas.strokePolygon(xVerticies,yVerticies,2);
    }
    @Override
    public Object Clone() throws CloneNotSupportedException{
        Shape line = new Line();
        line.setColor(color);
        line.setFillColor(fillColor);
        Point position = new Point((int)xPos , (int)yPos);
        line.setPosition(position);
        Map<String, Double> m = new HashMap<String, Double>();
        for (Map.Entry<String , Double> s: properties.entrySet())
            m.put((String)s.getKey(), (Double)s.getValue());
        line.setProperties(m);
        return line;
    }
	@Override
	public String toString() {
		return "Line [xPos=" + xPos + ", yPos=" + yPos + ", color=" + color + ", fillColor=" + fillColor
				+ ", properties=" + properties + "]";
	}
    
}
