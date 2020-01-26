package sample.Controllers;

import sample.Model.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ControllerPortal {
    protected int SHOE_IDENTITY_KEY;
    protected int CATEGORY_IDENTITY_KEY;
    protected int BRAND_IDENTITY_KEY;
    protected int COLOR_IDENTITY_KEY;
    protected int SIZE_IDENTITY_KEY;

    protected String host;
    protected String root;
    protected String keypass;

    private Map<Integer, Category> category_list = new HashMap<>();
    private Map<Integer, Brand> brand_list = new HashMap<>();
    private Map<Integer, Color> color_list = new HashMap<>();
    private Map<Integer, Size> size_list = new HashMap<>();
    private Map<Integer, Shoe> product_list = new HashMap<>();
    private Map<Integer, Shoe> itemCart_list = new HashMap<>();

    public void initialize() {
        loadServerSettings();
        getCategories();

        category_list.forEach((key, value) -> {
            System.out.println(key + ") " + value);
        });
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

    public void getProductItemsFromserver() {
        try (Connection conn = DriverManager.getConnection(host, root, keypass)) {
            PreparedStatement statement = conn.prepareStatement("SELECT view_shoe_table");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getCategories() {
        try (Connection conn = DriverManager.getConnection(host, root, keypass)) {
            PreparedStatement statement = conn.prepareStatement("SELECT category_id, type FROM Category");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                CATEGORY_IDENTITY_KEY = resultSet.getInt("category_id");
                String category = resultSet.getString("type");
                category_list.put(CATEGORY_IDENTITY_KEY, new Category(CATEGORY_IDENTITY_KEY, category));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Category getCategoryFromServer(ResultSet result) {

        return null;
    }
}
