package __Model;

public class ShapeFactory{

    public Shape createShape(SHAPES s) {
        switch (s){
            case CIRCLE:
                return new Circle();
            case ELLIPSE:
                return new Ellipse();
            case LINE:
                return new Line();
            case RECTANGLE:
                return new Rectangle();
            case SQUARE:
                return new Square();
            case TRIANGLE:
                return new Triangle();
		default:
			break;
        }
        return null;
    }
}
