package Webbshop;

import Webbshop.Model.Customer;
import javafx.scene.control.Alert;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class Repository {
    private static String host;
    private static String root;
    private static String keypass;

    public static void loadServerSettings() {

        try (InputStream inputStream = new FileInputStream("src/Webbshop/db_server.properties")) {
            Properties prop = new Properties();
            prop.load(inputStream);
            host = prop.getProperty("host");
            root = prop.getProperty("root");
            keypass = prop.getProperty("keypass");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean logInToShop(String username, String password) {

        try (Connection conn = DriverManager.getConnection(host, root, keypass)) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM customer WHERE (email = ? OR person_id = ?) AND password = ?");

            statement.setString(1, username);
            statement.setString(2, username);
            statement.setString(3, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Main.viewMessage("Välkommen till shoeline", "Information", Alert.AlertType.CONFIRMATION);
                Main.customerObject = fetchCustomerData(resultSet);
                return true;
            } else {
                Main.viewMessage("Felaktig data inmatat", "Varning", Alert.AlertType.WARNING);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Customer fetchCustomerData(ResultSet resultSet) {

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

            Main.CUSTOMER_KEY = resultSet.getInt("customer_id");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
}
