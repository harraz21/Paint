package Utility;

import _Controllers.Controller;
import __Model.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.awt.*;

/**
 *
 * THIS CLASS WAS MADE TO MAKE SOME STUFF EASIER FOR THE CONTROLLER
 */
public class UTILITY {
    private static UTILITY u=null;
    private static final double RESIZE_SPACE=5.0;
    private final double MINIMUM_RESIZE_WIDTH=15.0;
    private final double MINIMUM_RESIZE_HEIGHT=15.0;
    public static UTILITY getInstance(){
        if(u!=null)return u;
        return new UTILITY();
    }
    private UTILITY(){

    }
    ////////////////////////////////////////
    public boolean shapeInSelection(Shape Parent,Shape s){
        double widthParent=Parent.getProperties().get("Width");
        double heightParent=Parent.getProperties().get("Height");
        double height=s.getProperties().get("Height");
        double width=s.getProperties().get("Width");
        if(s.getPosition().getX()>=Parent.getPosition().getX()&&
           s.getPosition().getX()<=Parent.getPosition().getX()+widthParent&&
           s.getPosition().getY()>=Parent.getPosition().getY()&&
           s.getPosition().getY()<=Parent.getPosition().getY()+heightParent&&
           s.getPosition().getX()+width<=Parent.getPosition().getX()+widthParent&&
           s.getPosition().getY()+height<=Parent.getPosition().getY()+heightParent
           ) {
            return true;
        }

        return false;
    }
//////////////////////////////////////////////
    public void drawDashedRectAroundSelected(Shape s,GraphicsContext canvas){
        canvas.setStroke(Color.BLACK);
        canvas.setFill(Color.TRANSPARENT);
        canvas.setLineDashes(8);
        canvas.setLineWidth(2.5);
        double shapeLineWidth=s.getProperties().get("Linewidth");
        double width=s.getProperties().get("Width");
        double height=s.getProperties().get("Height");
        canvas.strokeRect(s.getPosition().x-shapeLineWidth+(4*shapeLineWidth)/10,
                s.getPosition().y-shapeLineWidth+(4*shapeLineWidth)/10,
                width+2*shapeLineWidth-(2*4*shapeLineWidth)/10,
                height+2*shapeLineWidth-(2*4*shapeLineWidth)/10);
    }

    //////////////////////////COLORS///////////////////////
    private java.awt.Color fxToAwtColor(Color fxColor){
        return  new java.awt.Color((float) fxColor.getRed(),
                (float)  fxColor.getGreen(),
                (float)  fxColor.getBlue(),
                (float)  fxColor.getOpacity());
    }

    public Color awtToFxColor(java.awt.Color awtColor){
        int r = awtColor.getRed();
        int g = awtColor.getGreen();
        int b = awtColor.getBlue();
        int a = awtColor.getAlpha();
        double opacity = a / 255.0 ;
        Color fxColor = Color.rgb(r, g, b, opacity);
        return  fxColor;
    }
   public void PutColorPrimaryFilled(__Model.Shape s){
        s.setColor(fxToAwtColor(Controller.getInstance().get_primary_color_picker().getValue()));
        s.setFillColor(fxToAwtColor(Controller.getInstance().get_secondary_color_picker().getValue()));

    }
    public void PutColorSecondaryFilled(__Model.Shape s){
        s.setColor(fxToAwtColor(Controller.getInstance().get_secondary_color_picker().getValue()));
        s.setFillColor(fxToAwtColor(Controller.getInstance().get_primary_color_picker().getValue()));

    }
    public void PutColorPrimaryNotFilled(__Model.Shape s){
        s.setColor(fxToAwtColor(Controller.getInstance().get_primary_color_picker().getValue()));
        s.setFillColor(fxToAwtColor(javafx.scene.paint.Color.TRANSPARENT));

    }
     public void PutColorSecondaryNotFilled(Shape s){
        s.setColor(fxToAwtColor(Controller.getInstance().get_secondary_color_picker().getValue()));
        s.setFillColor(fxToAwtColor(Color.TRANSPARENT));

    }

    //////////RESIZING///////
    /**
     *   * x : Mouse Click
     * p : Shape Position
     *
     * coord. system is from left top to right bottom
     *  (0%,0%)
     <-  Width     ->
     *   p------------------+
     *   | +--------------+ | ^
     *   | |            x | | |
     *   | |     MOVE     | | Height
     *   | |              | | |
     *   | +--------------+ | .
     *   +------------------+
     *                     (100%,100%)
     */
    public boolean checkIfHoverMove(double xPos, double yPos , double width , double height , Point position){
        if(xPos >= position.x+RESIZE_SPACE
                && xPos <= position.x + width-RESIZE_SPACE
                && yPos<=position.y+height-RESIZE_SPACE && yPos>=position.y+RESIZE_SPACE)
            return true;
        return false;
    }
    public void resizeFromNorth(Shape shape , MouseEvent e) {
        double yPos = e.getY();
        double x = shape.getPosition().x;
        double newheight = Math.abs(yPos - shape.getPosition().y);
        double sHeight=shape.getProperties().get("Height");
        double nHeight=MINIMUM_RESIZE_HEIGHT;
        if(yPos < shape.getPosition().y) {
            Point position = new Point( (int) x , (int) yPos );
            shape.setPosition(position);
            nHeight=sHeight  + newheight;
            shape.getProperties().put("Height",nHeight);
        }
        else {
            if(sHeight-newheight>MINIMUM_RESIZE_HEIGHT){
                nHeight=sHeight-newheight;
                Point position = new Point( (int) x , (int) yPos);
                shape.setPosition(position);
            }
            shape.getProperties().put("Height", nHeight);
        }
    }
    public void resizeFromWest(Shape shape , MouseEvent e) {
        double xPos = e.getX();
        double y = shape.getPosition().y;
        double newwidth = Math.abs(xPos - shape.getPosition().x);
        double sWidth=shape.getProperties().get("Width");
        double nWidth=MINIMUM_RESIZE_WIDTH;
        if(xPos < shape.getPosition().x) {
            Point position = new Point( (int) xPos , (int) y);
            shape.setPosition(position);
            nWidth=sWidth+newwidth;
            shape.getProperties().put("Width",nWidth);
        }
        else {
            if(sWidth-newwidth>MINIMUM_RESIZE_WIDTH) {
                nWidth=sWidth-newwidth;
                Point position = new Point((int) xPos, (int) y);
                shape.setPosition(position);
            }
            shape.getProperties().put("Width", nWidth);
        }
    }
   public void resizeFromEast(Shape shape , MouseEvent e) {
        double xPos = e.getX();
        double shapeWidth = shape.getProperties().get("Width");
        double newwidth = Math.abs(xPos - (shape.getPosition().x+shapeWidth));
        double sWidth=shape.getProperties().get("Width");
        double nWidth=MINIMUM_RESIZE_WIDTH;
        if(xPos > shapeWidth + shape.getPosition().x) {
            nWidth=sWidth+newwidth;
            shape.getProperties().put("Width",nWidth);
        }else {
            if(sWidth-newwidth>MINIMUM_RESIZE_WIDTH){
                nWidth=sWidth-newwidth;
            }
            shape.getProperties().put("Width",nWidth);
        }
    }
    public void resizeFromSouth(Shape shape , MouseEvent e) {
        double yPos = e.getY();
        double shapeHeight = shape.getProperties().get("Height");
        double newheight = Math.abs(yPos - (shape.getPosition().y + shapeHeight));
        double sHeight=shape.getProperties().get("Height");
        double nHeight=MINIMUM_RESIZE_HEIGHT;

        if(yPos > shapeHeight + shape.getPosition().y) {
            nHeight=sHeight + newheight;
            shape.getProperties().put("Height",nHeight );
        }
        else {
            if(sHeight-newheight>MINIMUM_RESIZE_HEIGHT){
                nHeight=sHeight-newheight;
            }
            shape.getProperties().put("Height", nHeight);
        }
    }
    public   boolean checkIfHoverNorth(double xPos , double yPos ,double width , Point position){
        if(xPos>=position.x && xPos<=position.x+width && yPos>=position.y -RESIZE_SPACE && yPos<=position.y +RESIZE_SPACE)
            return true;
        return false;
    }

   public boolean checkIfHoverEast(double xPos , double yPos ,double width ,double height, Point position){
        if(xPos<=position.x+width+RESIZE_SPACE && xPos>=position.x+width-RESIZE_SPACE&& yPos>position.y && yPos<position.y + height)
            return true;
        return false;
    }
    public boolean checkIfHoverWest(double xPos , double yPos , double height , Point position){
        if(xPos>=position.x-RESIZE_SPACE&& xPos<=position.x+RESIZE_SPACE && yPos>position.y && yPos<position.y+height)
            return true;
        return false;
    }
   public boolean checkIfHoverSouth(double xPos, double yPos , double width , double height , Point position){
        if(xPos >= position.x && xPos <= position.x + width && yPos<=position.y+height+RESIZE_SPACE && yPos>=position.y+height-RESIZE_SPACE)
            return true;
        return false;
    }
}
