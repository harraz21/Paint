package invoker;

import __Model.Shape;

public interface Invoker {
    public void Copy(Shape s);

    public void ChangeShapeProperties(Shape oldShape, Shape newShape) ;
    public void CreateShape(Shape s);

    public void Remove(Shape s);

}
