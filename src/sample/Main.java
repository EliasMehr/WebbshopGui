package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("FXML/sign_in.fxml"));
        primaryStage.setTitle("Webshop shoeline 1.0");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        primaryStage.setOnCloseRequest(closeRequest -> {
            Platform.exit();
            System.exit(0);
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
