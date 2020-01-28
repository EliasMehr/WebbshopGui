package Webbshop.Controllers;

import Webbshop.Repository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Webbshop.Main;
import Webbshop.Model.Customer;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    public TextField username_txt;
    public PasswordField password_txt;
    public ImageView sign_img;

    public void initialize() {
        Repository.loadServerSettings();

        username_txt.setText("adam.boyaci@gmail.com");
        password_txt.setText("Halloj12!");
    }

    public void authorize_login(MouseEvent actionEvent) throws IOException {
        if (username_txt.getLength() == 0 || password_txt.getLength() == 0) {
            Main.viewMessage("Var vänlig fyll in dina inloggningsuppgifter", "Varning", Alert.AlertType.WARNING);
        } else {
            boolean isVerified = Repository.logInToShop(username_txt.getText(), password_txt.getText());

            if (isVerified) {
                Parent portal_parent = FXMLLoader.load(getClass().getClassLoader().getResource("Webbshop/FXML/shoe_portal.fxml"));
                Scene portal_scene = new Scene(portal_parent);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(portal_scene);
                window.show();
            }
        }
    }


}
