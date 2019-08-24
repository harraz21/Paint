package Utility;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 * To make Any Container Resizable by the User
 *
 * @author Omar_Lort(emaranasr@gmail.com)
 */
public class ContainerResizer {

    //The Space around the container that the user can drag and resize
    private static final int RESIZE_SPACE= 3;
    //The Container
    private final Region region;

    private double y;

    private double x;
    private boolean draggableZoneX, draggableZoneY;

    private boolean currently_dragging;
    private ContainerResizer(Region Container) {
        region = Container;
    }
    public static void makeResizable(Region region) {
        final ContainerResizer cResizer = new ContainerResizer(region);

        //Be Noted That we Specify Some Events (Pressed Dragged Released Moving) so i think we will overwrite container
        //Events if there are any
        region.setOnMousePressed(event -> {
                cResizer.mousePressed(event);
        });
        region.setOnMouseDragged(event -> {
                cResizer.mouseDragged(event);

        });
        region.setOnMouseMoved(event ->{
                cResizer.mouseMovement(event);

        });
        region.setOnMouseReleased(event -> {
                cResizer.mouseReleased(event);

        });
    }
    private void mousePressed(MouseEvent event) {

        // ignore clicks outside of the draggable margin
        if (!isInDraggableZone(event)) {
            return;
        }
        currently_dragging= true;

        region.setMinHeight(region.getHeight());

        y = event.getY();
        region.setMinWidth(region.getWidth());

        x = event.getX();
    }
    private void mouseDragged(MouseEvent event) {
        if (!currently_dragging) {
            return;
        }
        if (draggableZoneY) {
            double mouseY = event.getY();
            double newHeight = region.getMinHeight() + (mouseY - y);
            region.setMinHeight(newHeight);
            y = mouseY;
        }
        if (draggableZoneX) {
            double mouseX = event.getX();

            double newWidth = region.getMinWidth() + (mouseX - x);

            region.setMinWidth(newWidth);

            x = mouseX;
        }
    }
    private void mouseMovement(MouseEvent event) {
        if (isInDraggableZone(event) || currently_dragging) {
            if (draggableZoneY) {
                region.setCursor(Cursor.S_RESIZE);
            }

            if (draggableZoneX) {
                region.setCursor(Cursor.E_RESIZE);
            }
            if (draggableZoneY&&draggableZoneX) {
                region.setCursor(Cursor.SE_RESIZE);
            }
        } else {
          //  region.setCursor(Cursor.CROSSHAIR);
        }
    }
    private void mouseReleased(MouseEvent event) {
        currently_dragging = false;
        region.setPrefHeight(region.getHeight());
        region.setPrefWidth(region.getWidth());
    }
    private boolean isInDraggableZone(MouseEvent event) {
        draggableZoneY = (event.getY() > (region.getHeight() - RESIZE_SPACE));
        draggableZoneX = (event.getX() > (region.getWidth() - RESIZE_SPACE));
        return draggableZoneX||draggableZoneY;
    }


}
