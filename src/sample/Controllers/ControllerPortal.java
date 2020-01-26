package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import sample.Model.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ControllerPortal {

    public ListView<Shoe> product_tree;
    public Label shoe_availability;
    public Button addItemToCart;
    public ListView<Shoe> cartView;

    // SQL Table_ID Tags
    protected int CATEGORY_IDENTITY_KEY;
    protected int BRAND_IDENTITY_KEY;
    protected int COLOR_IDENTITY_KEY;
    protected int SIZE_IDENTITY_KEY;

    // SQL Server user configuration
    protected String host;
    protected String root;
    protected String keypass;

    // HashMaps representing each table in SQL-Server
    private Map<Integer, Category> category_list = new HashMap<>();
    private Map<Integer, Brand> brand_list = new HashMap<>();
    private Map<Integer, Color> color_list = new HashMap<>();
    private Map<Integer, Size> size_list = new HashMap<>();
    private Map<Integer, Shoe> product_list = new HashMap<>();
    private Map<Integer, Shoe> itemCart_list = new HashMap<>();

    public void initialize() {
        loadServerSettings();
        getCategories();
        getBrands();
        getColor();
        getSize();
        getAllProductItems();

        product_list.entrySet().stream().filter(shoe -> shoe.getValue().getQuantity_in_stock() > 0).forEach(index -> product_tree.getItems().add(index.getValue()));
        shoe_availability.setText("Tillgängliga skor: " + product_tree.getItems().size());


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


    public void getAllProductItems() {
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

                product_list.put(shoeID, new Shoe(shoeID, category_list.get(categoryID),
                        brand_list.get(brandID), model,
                        color_list.get(colorID),
                        size_list.get(sizeID),
                        quantity_in_stock, unit_price));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getCategories() {
        try (Connection conn = DriverManager.getConnection(host, root, keypass)) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Category");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                CATEGORY_IDENTITY_KEY = resultSet.getInt(1);
                String CATEGORY = resultSet.getString(2);
                category_list.put(CATEGORY_IDENTITY_KEY, new Category(CATEGORY_IDENTITY_KEY, CATEGORY.toUpperCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getBrands() {
        try (Connection conn = DriverManager.getConnection(host, root, keypass)) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Brand");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                BRAND_IDENTITY_KEY = resultSet.getInt(1);
                String BRAND = resultSet.getString(2);
                brand_list.put(BRAND_IDENTITY_KEY, new Brand(BRAND_IDENTITY_KEY, BRAND.toUpperCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getColor() {
        try (Connection conn = DriverManager.getConnection(host, root, keypass)) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM color");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                COLOR_IDENTITY_KEY = resultSet.getInt(1);
                String COLOR = resultSet.getString(2);
                color_list.put(COLOR_IDENTITY_KEY, new Color(COLOR_IDENTITY_KEY, COLOR.toUpperCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getSize() {
        try (Connection conn = DriverManager.getConnection(host, root, keypass)) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM size");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                SIZE_IDENTITY_KEY = resultSet.getInt(1);
                String SIZE = resultSet.getString(2);
                size_list.put(SIZE_IDENTITY_KEY, new Size(SIZE_IDENTITY_KEY, SIZE.toUpperCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addItem(ActionEvent actionEvent) {
       itemCart_list.put(
        product_tree.getSelectionModel().getSelectedItem().getShoe_id(),
        product_tree.getSelectionModel().getSelectedItem()
       );


    }
}
