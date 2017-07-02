package views;

import database.Database;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.UserManager;
import model.authentication.UsedUsernameException;
import model.services.DateCheckerService;

import java.util.Timer;

public class Main extends Application {
    private ViewWrapper viewWrapper = ViewWrapper.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Platform.setImplicitExit(false);
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Carendar");
        primaryStage.setScene(new Scene(root,1280,800));
        primaryStage.show();

        DateCheckerService checkerService = new DateCheckerService();
        checkerService.startService(10,1,"hour");

    }


    public static void main(String[] args) {
        launch(args);
    }


}
