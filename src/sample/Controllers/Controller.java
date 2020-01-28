package sample.Controllers;

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
import sample.Model.Customer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Controller {

    public static Customer customer;
    public Map<Integer, Customer> customerMap = new HashMap<>();
    public TextField username_txt;
    public PasswordField password_txt;
    public ImageView sign_img;
    public static int IDENTIFICATION_KEY;

    private boolean isVerified = false;

    protected String host;
    protected String root;
    protected String keypass;

    public void initialize() {
        loadServerSettings();
        username_txt.setText("adam.boyaci@gmail.com");
        password_txt.setText("Halloj12!");
    }

    public void authorize_login(MouseEvent actionEvent) throws IOException {
        if (username_txt.getLength() == 0 || password_txt.getLength() == 0) {
            viewMessage("Var vänlig fyll in dina inloggningsuppgifter", "Varning", Alert.AlertType.WARNING);
        } else {
            connectToDatabase(host, root, keypass);
            customerMap.put(IDENTIFICATION_KEY, customer);

            if (isVerified == true) {
                Parent portal_parent = FXMLLoader.load(getClass().getClassLoader().getResource("sample/FXML/shoe_portal.fxml"));
                Scene portal_scene = new Scene(portal_parent);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(portal_scene);
                window.show();
            }
        }
    }

    public boolean connectToDatabase(String host, String user, String password) {
        try (Connection conn = DriverManager.getConnection(host, user, password)) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM customer WHERE (email = ? OR person_id = ?) AND password = ?");

            statement.setString(1, username_txt.getText());
            statement.setString(2, username_txt.getText());
            statement.setString(3, password_txt.getText());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                viewMessage("Välkommen till shoeline", "Information", Alert.AlertType.CONFIRMATION);
                customer = fetchCustomerData(resultSet);
                isVerified = true;
            } else {
                viewMessage("Felaktig data inmatat", "Varning" , Alert.AlertType.WARNING);
                isVerified = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isVerified;

    }

    public void loadServerSettings() {
        try (InputStream inputStream = new FileInputStream("src/sample/db_server.properties")) {
            Properties load_server_settings = new Properties();
            load_server_settings.load(inputStream);
            host = load_server_settings.getProperty("host");
            root = load_server_settings.getProperty("root");
            keypass = load_server_settings.getProperty("keypass");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewMessage(String message, String title , Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setTitle(title);

        alert.showAndWait();
    }

    public Customer fetchCustomerData(ResultSet resultSet) {
        Customer customer = new Customer();
        try {
            customer.setCustomer_id(resultSet.getInt("customer_id"));
            customer.setFirst_name(resultSet.getString("first_name"));
            customer.setLast_name(resultSet.getString("last_name"));
            customer.setAddress(resultSet.getString("address"));
            customer.setCity(resultSet.getString("city"));
            customer.setPostal_code(resultSet.getString("postal_code"));
            customer.setPhone(resultSet.getString("phone"));
            customer.setEmail(resultSet.getString("email"));
            customer.setPerson_id(resultSet.getString("person_id"));

            IDENTIFICATION_KEY = resultSet.getInt("customer_id");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

}
