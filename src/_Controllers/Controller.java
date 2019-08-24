package _Controllers;

import FileChooserView.FileChooserView;
import IOFile.IOFile;
import Utility.ContainerResizer;
import Utility.UTILITY;
import __Memento.CareTaker;
import __Memento.Memento;
import __Memento.Originator;
import __Model.*;
import __Model.Shape;
import command.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;


public class Controller implements Initializable,DrawingEngine  {
    //Singleton DP
    private static Controller sceneController;

    public static Controller getInstance(){
       return sceneController;
    }
    public static void setSceneController(Controller Cont){
        sceneController=Cont;
    }

    @FXML
    private AnchorPane _main_container;
    //Menu Stuff
    @FXML
    private MenuItem m_new;
    @FXML
    private MenuItem m_save_as;
    @FXML
    private MenuItem m_open;

    //CANVAS THINGS
    @FXML
    private AnchorPane _canvas_container;

    @FXML
    private Canvas _canvas;


    //TOP LAYER Stuff

    @FXML
    private ColorPicker _primary_color_picker;
    @FXML
    private ColorPicker _secondary_color_picker;
    @FXML
    private Slider _line_size;

    //TOOLS BUTTONS
    @FXML
    private ToggleGroup SelectedTool;
    @FXML
    private Button _copy;
    @FXML
    private Button _delete;
    @FXML
    private Button _undo;
    @FXML
    private Button _redo;
    @FXML
    private ToggleButton _select;
    @FXML
    private ToggleButton _cursor;
    @FXML
    private ToggleButton _rectangle;
    @FXML
    private ToggleButton _circle;
    @FXML
    private ToggleButton _line;
    @FXML
    private ToggleButton _triangle;
    @FXML
    private CheckBox _fill_shape;
    @FXML
    private CheckBox _uniform_shape;

    //PLUGIN
    @FXML
    private ToggleButton _plugin_shape;

    //UTILITY
    private CareTaker careTaker;
    private Originator originator;
    private String lastPath;


    //IMPORTANT VARs
    private final UTILITY Utility=UTILITY.getInstance();//have some utility functions
    private double drawStartX;
    private double drawStartY;
    private double drawEndX;
    private double drawEndY;
    private boolean draggableEast,draggableWest,draggableNorth,draggableSouth;
    private boolean movable;
    private Shape newshape=null;
    private Shape selectedShape=null;
    private Shape oShape=null;
    private Shape nShape=null;
    private boolean SHIFT_PRESSED=false;
    private double select_xOffset=0;
    private double select_yOffset=0;
    private ShapeFactory shapeFactory = new ShapeFactory();
    List<Class<? extends Shape>> supportedShapes;
	Plugin plugin = Plugin.getInstance();

    private Shape tmpShape=null;

    private GraphicsContext g;


    KeyCombination ctrlAndY = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_ANY);
    KeyCombination ctrlAndZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_ANY);
    KeyCombination ctrlAndS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_ANY);
    ///////////////////////////////////
    ///////////////////////////////////
    /////////////FUNCTIONS/////////////
    ///////////////////////////////////
    ///////////////////////////////////

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        originator = new Originator();
        careTaker = new CareTaker();
        _redo.setDisable(true);
        _undo.setDisable(true);
        //we init Anchor with 800,600 and default minimum of 400,300
        ContainerResizer.makeResizable(_canvas_container);

        menuNewOnAction();//Call for new On Action method to start the canvas
        g=_canvas.getGraphicsContext2D();
        //CHECK IF SHIFT IS PRESSED OR NOT
        _main_container.setOnKeyPressed(e->{
            if(e.getCode()==KeyCode.SHIFT){
                SHIFT_PRESSED=true;
            }
            if(e.getCode()==KeyCode.DELETE){
                deleteOnAction();
            }
            if(e.getCode()==KeyCode.Q){
                _triangle.setSelected(true);
            }
            if(e.getCode()==KeyCode.W){
                _circle.setSelected(true);
            }
            if(e.getCode()==KeyCode.E){
                _rectangle.setSelected(true);
            }
            if(e.getCode()==KeyCode.R){
                _line.setSelected(true);
            }
            if(e.getCode()==KeyCode.C){
               _cursor.setSelected(true);
            }
            if(ctrlAndY.match(e)){
              redoOnAction();
            }
            if(ctrlAndZ.match(e)){
               undoOnAction();
            }
            if(ctrlAndS.match(e)){
                saveOnAction();
            }

        });

        _main_container.setOnKeyReleased(e->{
            if(e.getCode()==KeyCode.SHIFT){
                SHIFT_PRESSED=false;
            }
        });
        _main_container.setOnMouseDragged(e->{
          refresh(g);

        });
        _canvas.setOnMousePressed(event -> {
            drawStartX=drawEndX=event.getX();
            drawStartY=drawEndY=event.getY();

            g.setLineWidth(_line_size.getValue());//set width of the line
            if(!_cursor.isSelected()){
                selectedShape=null;
            }
            if(!_select.isSelected()){
                AbstractShape.setSelectedShapes(new ArrayList<>());//Deselect shapes
            }
            if(_cursor.isSelected()) {
                if (!checkSelectedShape(AbstractShape.getShapesList(), event)) {
                    if (!(draggableWest || draggableEast || draggableNorth || draggableSouth)) {
                        selectedShape = null;
                    }
                } else {

                   try {
                        checkShapeColor(event, selectedShape);
                        oShape = (Shape) selectedShape.Clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    fillOnAction();
                }
            }else if(_select.isSelected()){
                if(!(draggableWest || draggableEast || draggableNorth || draggableSouth||movable)) {
                    tmpShape = shapeFactory.createShape(SHAPES.RECTANGLE);
                    //AVOID NULL POINTER EXCEPTION
                    checkShapeColor(event, tmpShape);
                    updateInfo(tmpShape);
                }else{
                    if(checkIfSelected(tmpShape,event)){
                        for(Shape s:AbstractShape.getSelectedShapes()) {
                            setShapeOffset(s,event);
                        }
                    }
                }

            }else if (_rectangle.isSelected()) {
                if (SHIFT_PRESSED||_uniform_shape.isSelected()) {
                    newshape = shapeFactory.createShape(SHAPES.SQUARE);
                }else{
                    newshape = shapeFactory.createShape(SHAPES.RECTANGLE);
                }
            } else if (_circle.isSelected()) {
                if (SHIFT_PRESSED||_uniform_shape.isSelected()) {
                    newshape = shapeFactory.createShape(SHAPES.CIRCLE);
                }else {
                    newshape = shapeFactory.createShape(SHAPES.ELLIPSE);
                }
            } else if (_line.isSelected()) {
                newshape = shapeFactory.createShape(SHAPES.LINE);
            } else if (_triangle.isSelected()) {
                newshape = shapeFactory.createShape(SHAPES.TRIANGLE);
            } else if (_plugin_shape.isSelected()) {
            	try {
					newshape = (Shape)plugin.getLoadedClass().newInstance();
				} catch (InstantiationException | IllegalAccessException e1) {
					e1.printStackTrace();
				}
            }
            if(SelectedTool.getSelectedToggle()!=_cursor &&SelectedTool.getSelectedToggle()!= _select) {
                checkShapeColor(event, newshape);
                addShape(newshape);

            }
        });



        _canvas.setOnMouseDragged(event-> {
            drawEndX=event.getX();
            drawEndY=event.getY();
                    if (_cursor.isSelected()) {
                        if (selectedShape != null) {
                            //selectedShape.setPosition(new Point((int) (event.getX() - select_xOffset), (int) (event.getY() - select_yOffset)));

                            if (movable)
                                selectedShape.setPosition(new Point((int) (event.getX() + selectedShape.getSelectOffset().getX())
                                                            , (int) (event.getY() + selectedShape.getSelectOffset().getY())));
                            DragShape(selectedShape, event);

                        }
                    }else if(_select.isSelected()){
                        if(!(draggableWest || draggableEast || draggableNorth || draggableSouth||movable)) {
                            updateInfo(tmpShape);
                        }else{
                            if (movable)
                                tmpShape.setPosition(new Point((int) (event.getX() + tmpShape.getSelectOffset().getX())
                                                    , (int) (event.getY() + tmpShape.getSelectOffset().getY())));
                            /**
                            THEY MAY BE DELETED OR MOVED ONLY !!

                             */
                         //   DragShape(tmpShape, event);//we dont need to resize the selection shape
                            for(Shape s:AbstractShape.getSelectedShapes()) {
                                if (movable)
                                    s.setPosition(new Point((int) (event.getX() + s.getSelectOffset().getX())
                                            , (int) (event.getY() +s.getSelectOffset().getY())));
                          //      DragShape(s, event);
                            }
                        }

                    }else{
                        updateInfo(newshape);
                    }
          refresh(g);
        });

        _canvas.setOnMouseReleased(event-> {
            if(_cursor.isSelected()){
                if(selectedShape!=null){
                    try {
                        nShape = (Shape) selectedShape.Clone();
                    }catch (CloneNotSupportedException e){
                        e.printStackTrace();
                    }
                    ChangeShapeProperties c=new ChangeShapeProperties(oShape,nShape);
                    addCommand(c);
                    removeShape(selectedShape);
                     addShape(selectedShape);//add selected shape to end of the list So it's above shapes drawn (solves Selecting issue)
                    //CHECK HOW IS SELECTING SHAPE IMPLEMENTED
                }
            }else if(_select.isSelected()){

                    for (Shape s : AbstractShape.getShapesList()) {
                        if (Utility.shapeInSelection(tmpShape, s)) {
                            AbstractShape.getSelectedShapes().add(s);
                        }
                    }

            } else{
                selectedShape=newshape;
                updateInfo(newshape);
                try {
                    CreateShape c = new CreateShape((Shape)newshape.Clone());
                    addCommand(c);
                }catch (CloneNotSupportedException e){
                    e.printStackTrace();
                }

            }
           refresh(g);

       });
        _canvas.setOnMouseMoved(e->{
          if(_cursor.isSelected()){

              if(selectedShape!=null) {
                  checkHoverPlace(selectedShape,e);
                  _canvas.setCursor(Cursor.DEFAULT);
                 // char location = checkHoverPlace(selectedShape, e);
                  if(draggableNorth||draggableSouth)
                    _canvas.setCursor(Cursor.S_RESIZE);
                  if(draggableEast||draggableWest)
                      _canvas.setCursor(Cursor.H_RESIZE);
                   if(draggableNorth&&draggableEast)
                	  _canvas.setCursor(Cursor.NE_RESIZE);
                  if(draggableSouth&&draggableEast)
                	  _canvas.setCursor(Cursor.SE_RESIZE);
                  if(draggableSouth&&draggableWest)
                	  _canvas.setCursor(Cursor.SW_RESIZE);
                  if(draggableNorth&&draggableWest)
                	  _canvas.setCursor(Cursor.NW_RESIZE);
                  if(movable)
                      _canvas.setCursor(Cursor.MOVE);

              }else{
                 _canvas.setCursor(Cursor.DEFAULT);
              }
          }else if(_select.isSelected()){

                if(tmpShape!=null) {
                    checkHoverPlace(tmpShape,e);
                    _canvas.setCursor(Cursor.DEFAULT);
                    // char location = checkHoverPlace(selectedShape, e);
                    if(draggableNorth||draggableSouth)
                        _canvas.setCursor(Cursor.S_RESIZE);
                    if(draggableEast||draggableWest)
                        _canvas.setCursor(Cursor.H_RESIZE);
                    if(draggableNorth&&draggableEast)
                        _canvas.setCursor(Cursor.NE_RESIZE);
                    if(draggableSouth&&draggableEast)
                        _canvas.setCursor(Cursor.SE_RESIZE);
                    if(draggableSouth&&draggableWest)
                        _canvas.setCursor(Cursor.SW_RESIZE);
                    if(draggableNorth&&draggableWest)
                        _canvas.setCursor(Cursor.NW_RESIZE);
                    if(movable)
                        _canvas.setCursor(Cursor.MOVE);

                }else{
                    _canvas.setCursor(Cursor.DEFAULT);
                }
            }else{
                _canvas.setCursor(Cursor.CROSSHAIR);
            }


        });
    }
    private int to255Int(double d) {
        return (int) (d * 255);
    }

    private String toRgbString(Color c){
        return "rgb("
                + to255Int(c.getRed())
                + "," + to255Int(c.getGreen())
                + "," + to255Int(c.getBlue())
                + ")";
    }

    /*FXML Supported Functions */
    //Save & SaveAs
    @FXML
    private void saveOnAction(){
        if(lastPath!=null)
        {
            save(lastPath);
        }else m_save_as.fire();
    }
    @FXML
    private void saveAsOnAction(){
        FileChooserView fileChooserView=new FileChooserView();
        ChooserController chooserController=new ChooserController();
        String path=chooserController.getSavePath(fileChooserView);
        lastPath=path;
        if(path!=null) save(path);
    }
    @FXML
    private void openOnAction()
    {
        _undo.setDisable(true);
        _redo.setDisable(true);
        careTaker=new CareTaker();
        originator=new Originator();
        FileChooserView fileChooserView=new FileChooserView();
        ChooserController chooserController=new ChooserController();
        String path=chooserController.getOpenPath(fileChooserView);
        if(path!=null){load(path);
                careTaker.clearPreviousSteps();
            }
    }
    //Menu Bar -> new on action

    @FXML
    private void menuNewOnAction(){
        //unbind so we can change the Canvas width&height
        _canvas.widthProperty().unbind();
        _canvas.heightProperty().unbind();
        //by setting canvas width and height to 0 it erase everything
        _canvas.setHeight(0);
        _canvas.setWidth(0);
         ArrayList<Shape> shapesList = new ArrayList<Shape>();
         AbstractShape.setShapesList(shapesList);
        //we init it by 0,0 so it resize with the container
        //bind so it could take same width & height as the container
        _canvas.widthProperty().bind(_canvas_container.widthProperty());
        _canvas.heightProperty().bind(_canvas_container.heightProperty());
        _primary_color_picker.setValue(Color.BLACK);
        _primary_color_picker.setStyle("-fx-background-color: rgb(0,0,0);");

    }
    @FXML
    private void menuResizeOnAction(){
        try{
            Stage stage = new Stage();
            ResizeController.setCurrentStage(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Resize Canvas");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/__View_FXML/ResizeWindowFXML.fxml"));
            Parent root = loader.load();
            ResizeController.setResizeController((ResizeController)loader.getController());
            stage.setScene(new Scene(root,285,110));
            stage.resizableProperty().set(false);
            stage.show();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    @FXML
    private void menuPluginOnAction() {
    	loadPlugins();
    	
    }
    //linewidth slider on dragging
    @FXML
    private void linewidthSliderOnDrag(){ 	
        if(selectedShape!=null){
            selectedShape.getProperties().put("Linewidth",_line_size.getValue());
          refresh(g);
        }
    }
    //fill checkbox on action
    @FXML
    private void fillOnAction(){
        if(selectedShape!=null){
            onColorValueChanged();
        }
    }

    //Color Picker on Changed Value

    @FXML
    private void onColorValueChanged(){
        Color newColor=_primary_color_picker.getValue();
        _primary_color_picker.setStyle("-fx-background-color: "+toRgbString(newColor)+";");
        newColor=_secondary_color_picker.getValue();
        _secondary_color_picker.setStyle("-fx-background-color: "+toRgbString(newColor)+";");

        g.setFill(_primary_color_picker.getValue());
        g.setStroke(_primary_color_picker.getValue());//Change color of the drawer

        if(selectedShape!=null){
            if(_fill_shape.isSelected()){
                Utility.PutColorPrimaryFilled(selectedShape);
            }else{
                Utility.PutColorPrimaryNotFilled(selectedShape);
            }

           refresh(g);
        }
    }

    @FXML
    private void deleteOnAction(){
        try {
            RemoveShape r = new RemoveShape((Shape)selectedShape.Clone());
            r.execute();
            addCommand(r);
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        if(_select.isSelected()){
            for(Shape s:AbstractShape.getSelectedShapes()){
                removeShape(s);
            }
            AbstractShape.setSelectedShapes(new ArrayList<>());
            tmpShape=null;
            refresh(g);
        }

    }
    @FXML
    private void undoOnAction()
    {
        undo();
    }
    @FXML
    private void redoOnAction()
    {
        redo();
    }

    @FXML
    private void copyOnAction() {
        try {
            CopyShape c = new CopyShape((Shape) selectedShape.Clone());
            c.execute();
            addCommand(c);
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
    }

    private void checkShapeColor(MouseEvent e,Shape s){
        if(_fill_shape.isSelected()) {
            if (e.isPrimaryButtonDown()) {
                Utility.PutColorPrimaryFilled(s);
            } else if (e.isSecondaryButtonDown()) {
                Utility.PutColorSecondaryFilled(s);
            }
        }else{
            if (e.isPrimaryButtonDown()) {
                Utility.PutColorPrimaryNotFilled(s);
            } else if (e.isSecondaryButtonDown()) {
                Utility.PutColorSecondaryNotFilled(s);
            }
        }
    }
    private void setShapeOffset(Shape s,MouseEvent e){
        double Xpos=e.getX();
        double Ypos=e.getY();
        Point shapePos= s.getPosition();
        select_xOffset =  shapePos.x -Xpos ;
        select_yOffset = shapePos.y-Ypos ;
        s.setSelectOffset(select_xOffset,select_yOffset);
    }
    private boolean checkIfSelected(Shape s,MouseEvent e){
        double Xpos=e.getX();
        double Ypos=e.getY();
        double shapeWidth=s.getProperties().get("Width");
        double shapeHeight=s.getProperties().get("Height");
        Point shapePos= s.getPosition();

        /**
         * x : Mouse Click
         * p : Shape Position
         *
         * coord. system is from left top to right bottom
         *  (0%,0%)
         *    <-  Width     ->
         *   p-----------------+
         *   |              x  | ^
         *   |                 | |
         *   |                 | Height
         *   |                 | |
         *   |                 | .
         *   +-----------------+
         *                      (100%,100%)
         */
        if(!(draggableNorth||draggableSouth||draggableEast||draggableWest)) {//if other shape is draggable don't deselect it
            if (Xpos <= shapePos.x + shapeWidth && Xpos >= shapePos.x
                    && Ypos <= shapePos.y + shapeHeight && Ypos >= shapePos.y) {
               setShapeOffset(s,e);
                selectedShape = s;
                return true;
            }
        }
        return false;
    }
    private boolean checkSelectedShape(ArrayList<Shape> shapeList,MouseEvent e){
        int length=shapeList.size();
        //Iterating backwards to detect last drawn shape first
        for(int i=length-1;i>=0;i-- ){
            if(checkIfSelected(shapeList.get(i),e))return true;
        }
        return false;
    }
    /**
     *RESIZE FUNCTIONS
     *
     */
    private boolean checkHoverPlace(Shape shape , MouseEvent e){
        double xPos = e.getX();
        double yPos = e.getY();
        draggableNorth=false;
        draggableSouth=false;
        draggableEast=false;
        draggableWest=false;
        movable=false;
        Point position = shape.getPosition();
        double shapeHeight = shape.getProperties().get("Height");
        double shapeWidth = shape.getProperties().get("Width");
        if(Utility.checkIfHoverNorth(xPos,yPos,shapeWidth ,position)) {
        draggableNorth=true;
        }
        if(Utility.checkIfHoverEast(xPos,yPos,shapeWidth,shapeHeight,position)) {
            draggableEast=true;
        }
        if(Utility.checkIfHoverWest(xPos,yPos,shapeHeight,position)){
            draggableWest=true;
        }
        if(Utility.checkIfHoverSouth(xPos,yPos,shapeWidth,shapeHeight,position)){
            draggableSouth=true;
        }
        if(Utility.checkIfHoverMove(xPos,yPos,shapeWidth,shapeHeight,position))
            movable=true;
        return draggableNorth||draggableSouth||draggableWest||draggableEast||movable;
    }
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

    private void DragShape(Shape shape,MouseEvent e){
        if(draggableNorth){
            Utility.resizeFromNorth(shape,e);
        }
        if(draggableSouth){
            Utility.resizeFromSouth(shape,e);
        }
        if(draggableEast){
            Utility.resizeFromEast(shape,e);
        }
        if(draggableWest){
            Utility.resizeFromWest(shape,e);
        }
    }
    /**
     *
     * DRAWING FUNCTIONS(UPDATES PROPERTIES FOR THE NEW SHAPE ON DRAG)
     */
    private void updateInfo(Shape s){
        s.updateInfo(drawStartX,drawStartY,drawEndX,drawEndY);
    }
    public void useShapeColors(Shape s){
        g.setStroke(Utility.awtToFxColor(s.getColor()));
        g.setFill(Utility.awtToFxColor(s.getFillColor()));
    }
    
    
    private void loadPlugins() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
		File selectedFile = fileChooser.getSelectedFile();
		String paths = selectedFile.getAbsolutePath();
		plugin.setPath(paths);
		plugin.getPlugin();
    	if(plugin.getLoadedClass()!=null) {
    	    _plugin_shape.setDisable(false);
    		_plugin_shape.setVisible(true);
    		_plugin_shape.setText("RoundRect");
    		System.out.println("Eshta la2eeto");
    		System.out.println(plugin.getLoadedClass());
    		
    	}else {
    		_plugin_shape.setVisible(false);
    		}
        }
    }
    /**
     *
     * INTERFACE FUNCTIONS
     *
     */
	@Override
	public void refresh(GraphicsContext canvas) {
		canvas.clearRect(0, 0, _canvas.getWidth(), _canvas.getHeight());
        g.setLineDashes(0);
		for(Shape shape:AbstractShape.getShapesList()) {
			canvas.setLineWidth(shape.getProperties().get("Linewidth"));
	        useShapeColors(shape);
	        shape.draw(canvas);
		}
        if(selectedShape!=null){
		    Utility.drawDashedRectAroundSelected(selectedShape,canvas);
        }
        else if(_select.isSelected()){
		    if(tmpShape!=null) {
                Utility.drawDashedRectAroundSelected(tmpShape, canvas);
            }
            for(Shape s:AbstractShape.getSelectedShapes()){
                Utility.drawDashedRectAroundSelected(s,canvas);
            }
        }
	}
	@Override
	public void addShape(Shape shape) {
		AbstractShape.getShapesList().add(shape);
	}
    public boolean checkIfShapesIdentical(Shape s1,Shape s2){
        if(s1.getPosition().x==s2.getPosition().x&&
          s1.getPosition().y==s2.getPosition().y&&
          s1.getColor()==s2.getColor()&&
          s1.getFillColor()==s2.getFillColor()&&
          s1.getProperties().get("Width")==s2.getProperties().get("Width")&&
          s1.getProperties().get("Height")==s2.getProperties().get("Height")&&
          s1.getProperties().get("Linewidth") == s2.getProperties().get("Linewidth")){
                   return true;
        }
        return false;
    }
	@Override
	public void removeShape(Shape shape) {
		for(Shape s : AbstractShape.getShapesList()){
		  {
		      if(checkIfShapesIdentical(s,shape)){
                  AbstractShape.getShapesList().remove(s);
                  return;//we must return because if the loop continue it will get NullPointerException
              }
            }
		}
	}
	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
        //THIS IMPLEMENTATION SUITS OUR NEEDS AS IT WILL ALWAYS INTERACT WITH LAST SHAPE IN THE ARRAY
        removeShape(oldShape);
        newshape=newShape;//exchange old ref. for new shape
        addShape(oldShape);

	}
	@Override
	public Shape[] getShapes() {
        int length =AbstractShape.getShapesList().size();
        Shape[] shapes=new Shape[length];
        for(int i=0;i<length;i++){
            shapes[i]=AbstractShape.getShapesList().get(i);
        }
		return shapes;
	}

    @SuppressWarnings("unchecked")
	public void addCommand(Command c) {
        originator.setState(c);
        careTaker.addMemento(originator.storeInMemento());
        _undo.setDisable(false);
    }

	@Override
    public void undo() {
        Memento memento=careTaker.undoMemento();
        if (memento!=null) {
            selectedShape=null;
            Command c=originator.restoreFromMemento(memento);
            c.undo();
            _redo.setDisable(false);
            refresh(g);
        }
         else{
            _undo.setDisable(true);
        }

    }

    @SuppressWarnings("unchecked")
	@Override
    public void redo() {
	    Memento memento=careTaker.redoByMemento();
        if (memento!=null){
            selectedShape=null;
            Command c=originator.restoreFromMemento(memento);
            c.redo();
            _undo.setDisable(false);
            refresh(g);
        } else{
            _redo.setDisable(true);
        }

    }
	@Override
	public void save(String path) {
      ArrayList<Shape> shapesToBeSaved= AbstractShape.getShapesList();
      IOFile ioFile=new IOFile();
      ioFile.setData(shapesToBeSaved);
        try {
            ioFile.Save(path);
        } catch (IOException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();//TODO error window
        }
    }
	@Override
	public void load(String path) {
        IOFile ioFile=new IOFile();
        try {
            ioFile.load(path);
        } catch (IOException | InvocationTargetException | ParseException | IllegalAccessException e) {
            e.printStackTrace();//TODO error window
        }
        ArrayList<Shape> shapesLoaded=ioFile.getData();
		AbstractShape.setShapesList(shapesLoaded);
		refresh(g);
	}
	@Override
    public java.util.List<Class<? extends Shape>> getSupportedShapes(){
		return supportedShapes;
	}
	@Override
	public void installPluginShape(String jarPath) {
    }

    /**
     * GETTERS
     */
    public AnchorPane get_canvas_container() {
        return _canvas_container;
    }
    public ToggleButton get_cursor() {
        return _cursor;
    }
    public ColorPicker get_primary_color_picker() {
        return _primary_color_picker;
    }
    public ColorPicker get_secondary_color_picker() {
        return _secondary_color_picker;
    }
    public Slider get_line_size() {
        return _line_size;
    }

    public Shape getTmpShape() {
        return tmpShape;
    }

    /**
     * SETTERS
     */
    public void setSelectedShape(Shape selectedShape) {
        this.selectedShape = selectedShape;
    }


    public void setMovable(boolean movable) {
        this.movable = movable;
    }
}