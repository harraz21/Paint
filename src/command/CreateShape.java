package command;

import _Controllers.Controller;
import __Model.Shape;
import invoker.ControllerInvoker;

public class CreateShape implements Command {
    ControllerInvoker invoker=new ControllerInvoker();
    Shape createdShape;
    public CreateShape(Shape s){
        createdShape=s;
    }

    public Shape getCreatedShape() {
        return createdShape;
    }

    @Override
    public void execute(){
        invoker.CreateShape(createdShape);

    }
    @Override
    public void undo(){
        Controller.getInstance().removeShape(createdShape);
    }
    @Override
    public void redo(){
        this.execute();
    }
}
