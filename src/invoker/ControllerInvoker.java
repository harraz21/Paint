package invoker;

import _Controllers.Controller;
import __Model.Shape;

public class ControllerInvoker implements Invoker {
    Controller controller;
    public ControllerInvoker(){
        controller=Controller.getInstance();
    }

    @Override
    public void Copy(Shape s){
            if (s != null) {
                controller.get_cursor().setSelected(true);
                controller.addShape(s);
                controller.setSelectedShape(null);
               controller.setMovable(true);
            }

    }

    @Override
    public void ChangeShapeProperties(Shape oldShape,Shape newShape){


    }

    @Override
    public void CreateShape(Shape s){
        if (s != null) {
            controller.addShape(s);
        }

    }


    @Override
    public void Remove(Shape s){
        if(s!=null) {
            controller.removeShape(s);
            controller.setSelectedShape(null);
        }
    }
}
