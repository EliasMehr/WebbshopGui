package sample;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Controller {
    public static Customer customer;
    private static int IDENTIFICATION_KEY;
    public Map<Integer, Customer> customerMap = new HashMap<>();
    public TextField username_txt;
    public PasswordField password_txt;
    public ImageView sign_img;

    protected String host;
    protected String root;
    protected String keypass;


    public void initialize() {
        loadServerSettings();
    }

    public void authorize_login(MouseEvent actionEvent) {
        if (username_txt.getLength() == 0 || password_txt.getLength() == 0) {
            viewMessage("Var vänlig fyll in dina inloggningsuppgifter");
        } else {
            connectToDatabase(host, root, keypass);
            customerMap.put(IDENTIFICATION_KEY, customer);
        }
    }

    public void connectToDatabase(String host, String user, String password) {
        try (Connection conn = DriverManager.getConnection(host, user, password)) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM customer WHERE (email = ? OR person_id = ?) AND password = ?");

            statement.setString(1, username_txt.getText());
            statement.setString(2, username_txt.getText());
            statement.setString(3, password_txt.getText());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                viewMessage("Välkommen till shoeline");
                customer = fetchCustomerData(resultSet);

            } else {
                viewMessage("Felaktig data inmatat");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void viewMessage(String message){
        JOptionPane.showMessageDialog(null, message);
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
