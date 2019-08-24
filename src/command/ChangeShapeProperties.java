package command;

import _Controllers.Controller;
import __Model.Shape;
import invoker.ControllerInvoker;

public class ChangeShapeProperties implements Command {
    ControllerInvoker invoker=new ControllerInvoker();
    Shape oldShape,newShape;

    public ChangeShapeProperties(Shape oldShape,Shape newShape){
        this.oldShape=oldShape;
        this.newShape=newShape;
    }

    public Shape getOldShape() {
        return oldShape;
    }

    public Shape getNewShape() {
        return newShape;
    }

    @Override
    public void execute() {
       // if(oldShape!=null&&newShape!=null)
        invoker.ChangeShapeProperties(oldShape,newShape);

    }
    @Override
    public void undo(){
        Controller.getInstance().removeShape(newShape);
        Controller.getInstance().addShape(oldShape);
    }
    @Override
    public void redo(){
        Controller.getInstance().removeShape(oldShape);
        Controller.getInstance().addShape(newShape);
    }
}
