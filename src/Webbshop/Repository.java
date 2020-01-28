package Webbshop;

import Webbshop.Model.*;
import javafx.scene.control.Alert;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
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

    public static Map<Integer, Shoe> getAllProducts() {
        Map<Integer, Shoe> shoeMap = new HashMap<>();
        Map<Integer, Brand> brandMap = getAllBrands();
        Map<Integer, Size> sizeMap = getAllSizes();
        Map<Integer, Color> colorMap = getAllColors();
        Map<Integer, Category> categoryMap = getAllCategories();

        try (Connection conn = DriverManager.getConnection(host, root, keypass)) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM shoe");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int shoeID = resultSet.getInt(1);
                int categoryID = resultSet.getInt(2);
                int brandID = resultSet.getInt(3);
                String model = resultSet.getString(4);
                int colorID = resultSet.getInt(5);
                int sizeID = resultSet.getInt(6);
                int quantity_in_stock = resultSet.getInt(7);
                int unit_price = resultSet.getInt(8);

                shoeMap.put(shoeID, new Shoe(shoeID, categoryMap.get(categoryID),
                        brandMap.get(brandID), model,
                        colorMap.get(colorID),
                        sizeMap.get(sizeID),
                        quantity_in_stock, unit_price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shoeMap;
    }

    public static Map<Integer, Category> getAllCategories() {
        Map<Integer, Category> categoryMap = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(host, root, keypass)) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Category");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int categoryKey = resultSet.getInt(1);
                String CATEGORY = resultSet.getString(2);
                categoryMap.put(categoryKey, new Category(categoryKey, CATEGORY.toUpperCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryMap;
    }

    public static Map<Integer, Brand> getAllBrands() {
        Map<Integer, Brand> brandMap = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(host, root, keypass)) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Brand");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                final int BRAND_KEY = resultSet.getInt(1);
                final String BRAND_NAME = resultSet.getString(2);
                brandMap.put(BRAND_KEY, new Brand(BRAND_KEY, BRAND_NAME.toUpperCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brandMap;
    }

    public static Map<Integer, Color> getAllColors() {
        Map<Integer, Color> colorMap = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(host, root, keypass)) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM color");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                final int COLOR_KEY = resultSet.getInt(1);
                final String COLOR_NAME = resultSet.getString(2);
                colorMap.put(COLOR_KEY, new Color(COLOR_KEY, COLOR_NAME.toUpperCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return colorMap;
    }

    public static Map<Integer, Size> getAllSizes() {
        Map<Integer, Size> sizeMap = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(host, root, keypass)) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM size");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                final int SIZE_KEY = resultSet.getInt(1);
                final String SIZE_VALUE = resultSet.getString(2);
                sizeMap.put(SIZE_KEY, new Size(SIZE_KEY, SIZE_VALUE.toUpperCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sizeMap;
    }

    public static void addToCart(int orderID, int customerID, int shoeID) {
        try (Connection conn = DriverManager.getConnection(host, root, keypass)) {
            PreparedStatement statement = conn.prepareCall("CALL addToCart(?, ?, ?)");

            statement.setInt(1, orderID);
            statement.setInt(2, customerID);
            statement.setInt(3, shoeID);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            if (hasErrorColumn(resultSet, "ERROR_MESSAGE")) {
                Main.viewMessage(resultSet.getString("ERROR_MESSAGE"), "Kan inte behandla din order", Alert.AlertType.WARNING);
            } else if (hasErrorColumn(resultSet, "newOrderID")) {
                Main.currentOrderID = resultSet.getInt("newOrderID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean hasErrorColumn(ResultSet resultSet, String columnName) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            if (metaData.getColumnName(i).equals(columnName)) {
                return true;
            }
        }
        return false;
    }
}
