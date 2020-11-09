//AUTHOR: Luke Kellett 2020
package dsstorebegone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public Main() throws IOException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = FXMLLoader.load(getClass().getResource("mainview.fxml"));
        primaryStage.setTitle("DS_Store Begone");
        primaryStage.setScene(new Scene(root, 550, 400));
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }
}
