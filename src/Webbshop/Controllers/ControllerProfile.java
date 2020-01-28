package Webbshop.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Webbshop.Main;

import java.io.IOException;

public class ControllerProfile {

    public TextField cust_name;
    public TextField cust_last;
    public TextField cust_street;
    public TextField cust_area;
    public TextField cust_zip;
    public TextField cust_phone;
    public TextField cust_mail;
    public Button cust_delete_btn;
    public Button return_to_portal_btn;


    public void initialize(){
        cust_name.setText(Main.customerObject.getFirst_name());
        cust_last.setText(Main.customerObject.getLast_name());
        cust_street.setText(Main.customerObject.getAddress());
        cust_area.setText(Main.customerObject.getCity());
        cust_zip.setText(Main.customerObject.getPostal_code());
        cust_phone.setText(Main.customerObject.getPhone());
        cust_mail.setText(Main.customerObject.getEmail());
    }


    public void returnToPortal(ActionEvent actionEvent) throws IOException {
        Parent portal_parent = FXMLLoader.load(getClass().getClassLoader().getResource("Webbshop/FXML/shoe_portal.fxml"));
        Scene portal_scene = new Scene(portal_parent);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(portal_scene);
        window.show();
    }

    public void deleteAccount(ActionEvent actionEvent) {
    }
}
