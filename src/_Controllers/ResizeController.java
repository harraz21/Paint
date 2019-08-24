package _Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author  Omar_Lort (emaranasr@gmail.com)
 */
public class ResizeController  implements Initializable  {
    private static Stage currentStage;
    private static ResizeController resizeController;
    @FXML
    private TextField _width;
    @FXML
    private TextField _height;
    @FXML
    private Label _console;

    private Controller mainSceneController;

    /**
     * Buttons on Action
     */
    @FXML
    private void resizeOnAction(){
        try{
            double w=Double.parseDouble(_width.getText());
            double h=Double.parseDouble(_height.getText());

            mainSceneController.get_canvas_container().setMinWidth(w);
            mainSceneController.get_canvas_container().setPrefWidth(w);
            mainSceneController.get_canvas_container().setMinHeight(h);
            mainSceneController.get_canvas_container().setPrefHeight(h);

            currentStage.close();//Terminate on Success
        }catch (Exception e){
            _console.setText("Invalid Input !!");
        }
    }
    @FXML
    private void cancelOnAction(){
        currentStage.close();//Terminate on Cancellation
    }

    /**
     * Getters & Setters
     *
     */
    public static ResizeController getInstance(){
        return resizeController;
    }
    public static void setResizeController(ResizeController rc){
        resizeController=rc;
    }
    public static void setCurrentStage(Stage s){
        currentStage=s;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainSceneController= Controller.getInstance();
    }
}
