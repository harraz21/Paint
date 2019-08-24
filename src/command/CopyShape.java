package command;

import _Controllers.Controller;
import __Model.Shape;
import invoker.ControllerInvoker;

public class CopyShape implements Command {
    ControllerInvoker invoker=new ControllerInvoker();
    Shape copiedShape;
    public CopyShape(Shape s){
        copiedShape=s;
    }

    public Shape getCreatedShape() {
        return copiedShape;
    }

@Override
    public void execute(){
    	invoker.Copy(copiedShape);
	}
    @Override
    public void undo(){
        Controller.getInstance().removeShape(copiedShape);
    }
    @Override
    public void redo(){
        execute();
    }
}
