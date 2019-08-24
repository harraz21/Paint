package _main;

import _Controllers.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/__View_FXML/SceneFXML.fxml"));
        Parent root = loader.load();
        Controller.setSceneController((Controller)loader.getController());
        primaryStage.setTitle("Paint");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }
//zabat el fill color lama el shape yetrasam

    public static void main(String[] args) {
        launch(args);
    }
}
