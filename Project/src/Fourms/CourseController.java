package Fourms;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CourseController implements Initializable {

    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField room;
    @FXML
    private ListView<String> listView;

    Stage stage = new Stage();
    Stage backStage;
    Connection connection;
    Statement statatment;

    public CourseController(Stage backStage, Connection conn) throws SQLException {
        this.statatment = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        this.backStage = backStage;
        this.connection = conn;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listView.getSelectionModel().selectedItemProperty().addListener((observable) -> {
            try {
                showSelectionModel();
            } catch (SQLException ex) {
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    //Add query method
    @FXML
    private void add(ActionEvent event) throws SQLException {
        try {
            int courseId = Integer.parseInt(this.id.getText());
            String name = this.name.getText();
            String room = this.room.getText();
            String query = "INSERT INTO course (id, name, room)" + "VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, courseId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, room);
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                System.err.println("row inserted!!");
            }
            show();
        } catch (NumberFormatException | SQLException ex) {
            System.out.println("Please enter valid data!!");
        }

    }

    @FXML
    private void show() throws SQLException {
        listView.getItems().clear();
        ResultSet query = statatment.executeQuery("SELECT * FROM course");
        while (query.next()) {
            String s = "";
            s += query.getInt("id") + "                  ";
            s += query.getString("name") + "                  ";
            s += query.getString("room") + "                  ";
            listView.getItems().add(s);
        }
        this.id.setText("");
        this.name.setText("");
        this.room.setText("");

    }

    @FXML
    private void delete(ActionEvent event) throws SQLException {
        try {
            int id = Integer.parseInt(this.id.getText());
            int query = statatment.executeUpdate("delete FROM Course where id = " + id);
            if (query > 0) {
                System.err.println("Deleted!!");
            }
            show();
        } catch (NumberFormatException | SQLException ex) {
            System.out.println("Please Choose data to delete!!");
        }
    }

    @FXML
    private void changeToRegistration(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loder = new FXMLLoader(getClass().getResource("Registration.fxml"));
        loder.setController(new RegistrationController(stage, connection));
        Scene scene = new Scene(loder.load());
        stage.setScene(scene);
        stage.show();
        backStage.close();
    }

    @FXML
    private void update(ActionEvent event) throws SQLException {
        try {
            int id = Integer.parseInt(this.id.getText());
            String name = this.name.getText();
            String room = this.room.getText();
            String query = "update course set NAME = ?,ROOM = ? where id = " + id;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, room);
            preparedStatement.executeUpdate();

            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                System.err.println("Updated!");
            }
            show();
        } catch (NumberFormatException | SQLException ex) {
            System.out.println("Please Choose data to update!!");
        }

    }

    private void showSelectionModel() throws SQLException {
        if (!listView.getSelectionModel().getSelectedItems().isEmpty()) {
            int id = Integer.parseInt(listView.getSelectionModel().getSelectedItems().toString().substring(1,
                    listView.getSelectionModel().getSelectedItems().toString().indexOf(" ")));
            ResultSet query = statatment.executeQuery("SELECT * FROM course where id=" + id);
            query.next();
            this.id.setText(query.getString("id"));
            this.name.setText(query.getString("name"));
            this.room.setText(query.getString("room"));
        }
    }

}
