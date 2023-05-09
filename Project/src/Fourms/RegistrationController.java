/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ahmed mahdi
 */
public class RegistrationController implements Initializable {

    @FXML
    private TextField courseID;

    @FXML
    private TextField semester;

    @FXML
    private TextField studentID;

    @FXML
    private ListView<String> listView;

    Stage stage = new Stage();
    Stage backStage;
    Connection connetion;
    Statement statment;

    public RegistrationController(Stage backStage, Connection conn) throws SQLException {
        this.statment = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        this.backStage = backStage;
        this.connetion = conn;
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

    @FXML
    private void add(ActionEvent event) throws SQLException {
        try {
            if (!studentID.getText().isEmpty() && !courseID.getText().isEmpty() && !semester.getText().isEmpty()) {
                int courseId = Integer.parseInt(courseID.getText());
                int studentId = Integer.parseInt(studentID.getText());
                String semes = semester.getText();
                PreparedStatement query = connetion.prepareStatement("INSERT INTO registration (courseId, studentId, semester)"
                        + "VALUES(?,?,?)");
                query.setInt(1, courseId);
                query.setInt(2, studentId);
                query.setString(3, semes);

                int rows = query.executeUpdate();
                if (rows > 0) {
                    System.err.println("row inserted!!");
                }
                show();
            }

        } catch (NumberFormatException | SQLException ex) {
            System.out.println("Please Enter valid Data!!");
        }
    }

    @FXML
    void show() throws SQLException {
        listView.getItems().clear();
        ResultSet query = statment.executeQuery("SELECT * FROM registration");
        while (query.next()) {
            String s = "";
            s += query.getString("studentId") + "                ";
            s += query.getString("courseId") + "                ";
            s += query.getString("semester") + "            ";
            listView.getItems().add(s);
            this.courseID.setText("");
            this.studentID.setText("");
            this.semester.setText("");
        }
    }

    @FXML
    void delete(ActionEvent event) throws SQLException {
        try {
            int courseID = Integer.parseInt(this.courseID.getText());
            int studentID = Integer.parseInt(this.studentID.getText());
            int query = statment.executeUpdate("delete FROM registration where studentID = " + studentID + " and courseID=" + courseID);
            if(query>0)
                System.err.println("Deleted!!");
            show();
        } catch (NumberFormatException | SQLException ex) {
            System.out.println("Please Choose data to delete!!");
        }
    }

    @FXML
    private void backToStudent(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loder = new FXMLLoader(getClass().getResource("Student.fxml"));
        loder.setController(new StudentController(stage, connetion));
        Scene scene = new Scene(loder.load());
        stage.setScene(scene);
        stage.show();
        backStage.close();
    }

    private void showSelectionModel() throws SQLException {
        if (!listView.getSelectionModel().getSelectedItems().isEmpty()) {
            int id = Integer.parseInt(listView.getSelectionModel().getSelectedItems().toString().substring(1,
                    listView.getSelectionModel().getSelectedItems().toString().indexOf(" ")));
            ResultSet query = statment.executeQuery("SELECT * FROM registration where studentID=" + id);
            query.next();
            courseID.setText(query.getString("courseID"));
            studentID.setText(query.getString("studentID"));
            semester.setText(query.getString("semester"));
        }
    }

}
