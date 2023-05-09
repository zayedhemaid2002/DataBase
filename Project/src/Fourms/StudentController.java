/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fourms;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import java.sql.PreparedStatement;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ahmed mahdi
 */
public class StudentController implements Initializable {

    @FXML
    private TextField iD;
    @FXML
    private Label labelError;
    @FXML
    private TextField name;
    @FXML
    private TextField major;
    @FXML
    private TextField grade;
    @FXML
    private ListView<String> listView;

    Stage stage = new Stage();
    Stage backStage;
    Connection conn;
    Statement stat;

    public StudentController(Stage backStage, Connection conn) throws SQLException {
        this.stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        this.backStage = backStage;
        this.conn = conn;
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
        try{
            int studentId = Integer.parseInt(this.iD.getText());
            String named = this.name.getText();
            String major = this.major.getText();
            int grade = Integer.parseInt(this.grade.getText() );
            System.out.println("(" + studentId + "', '" + major + "', " + grade + ")");
            String addS = "INSERT INTO student (id, name, major,grade)"
                        + "VALUES(?,?,?,?)";
            PreparedStatement preparedStatment = conn.prepareStatement(addS);

            preparedStatment.setInt(1,studentId);                
            preparedStatment.setString(2,named);
            preparedStatment.setString(3, major);
            preparedStatment.setInt(4,grade);

            int rows = preparedStatment.executeUpdate();
            if(rows>0)
                System.err.println("row inserted!!");
            
            show();
        }catch(Exception ex){
            System.out.println("Please Enter Data!!");
        }
            
                
    }

    @FXML
    private void show() throws SQLException {
        listView.getItems().clear();
        ResultSet rs = stat.executeQuery("SELECT * FROM student");
        while (rs.next()) {
            String s = "";
            s += rs.getInt("id") + "                  ";
            s += rs.getString("name") + "                  ";
            s += rs.getString("major") + "                  ";
            s += rs.getInt("grade") + "                  ";
            listView.getItems().add(s);
        }
        this.iD.setText("");
        this.name.setText("");
        this.major.setText("");
        this.grade.setText("");
        
    }

    @FXML
    private void delete() throws SQLException, IOException {
        try{int id = Integer.parseInt(iD.getText());
        int query = stat.executeUpdate("delete FROM student where id = " + id);
        if(query>0)
            System.err.println("Deleted!!");

        show();
    }catch(Exception ex){
            System.out.println("Please Choose data to delete!!");
        }
        
        
    }

    @FXML
    private void update() throws SQLException {
        try{int studentId = Integer.parseInt(this.iD.getText());
        String named = this.name.getText();
        String major = this.major.getText();
        int grade = Integer.parseInt(this.grade.getText() );
        String query = "update Student set NAME = ?,major = ?,grade = ? where id = "+studentId;
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, named);
        preparedStmt.setString(2, major);
        preparedStmt.setInt(3, grade);
        preparedStmt.executeUpdate();
        
        int rows = preparedStmt.executeUpdate();
        if(rows>0)
            System.err.println("Updated!");
        
        show();
        
    }catch(Exception ex){
            System.out.println("Please Choose data to update!!");
        }
    }

    @FXML
    private void changeToCours(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loder = new FXMLLoader(getClass().getResource("Course.fxml"));
        loder.setController(new CourseController(stage, conn));
        Scene scene = new Scene(loder.load());
        stage.setScene(scene);
        stage.show();
        backStage.close();
    }

    private void showSelectionModel() throws SQLException {
        if (!listView.getSelectionModel().getSelectedItems().isEmpty()) {
            int id = Integer.parseInt(listView.getSelectionModel().getSelectedItems().toString().substring(1,
                    listView.getSelectionModel().getSelectedItems().toString().indexOf(" ")));
            ResultSet rs = stat.executeQuery("SELECT * FROM student where id=" + id);
            rs.next();
            iD.setText(rs.getInt("id") + "");
            name.setText(rs.getString("name"));
            major.setText(rs.getString("major"));
            grade.setText(rs.getInt("grade") + "");
        }
    }

}
