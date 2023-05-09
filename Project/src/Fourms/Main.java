package Fourms;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException, ClassNotFoundException {

        try{
        String url = ("jdbc:oracle:thin:@localhost:1521:xe");
        String username = "proj";
        String password = "1234";
        Connection conn = DriverManager.getConnection(url, username, password);

        FXMLLoader loder = new FXMLLoader(getClass().getResource("Student.fxml"));
        loder.setController(new StudentController(primaryStage,conn));
        Parent root = loder.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Datbase Fourms");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        } catch (SQLException ex) {
                System.err.println("Class Not Found");
                ex.printStackTrace();
            }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
