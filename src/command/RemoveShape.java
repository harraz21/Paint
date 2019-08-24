package command;

import _Controllers.Controller;
import __Model.Shape;
import invoker.ControllerInvoker;

public class RemoveShape implements Command {
    ControllerInvoker invoker=new ControllerInvoker();
    Shape removedShape;
    public RemoveShape(Shape s){
        removedShape=s;
    }

    public Shape getCreatedShape() {
        return removedShape;
    }

    @Override
    public void execute(){
       invoker.Remove(removedShape);

    }
    @Override
    public void undo(){
        Controller.getInstance().addShape(removedShape);
    }
    @Override
    public void redo(){
        this.execute();
    }
}
