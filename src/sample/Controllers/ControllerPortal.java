package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
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
    public ListView<Shoe> cartView;
    public Label shoe_availability;
    public Button addItemToCart;
    public Label identity_label;
    public Button delete_item_btn;
    public Button process_order_btn;

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
    private Map<Integer, OrderItem> itemCart_list = new HashMap<>();

    public void initialize() {
        loadServerSettings();
        getCategories();
        getBrands();
        getColor();
        getSize();
        getAllProductItems();


        product_list.entrySet().stream().filter(shoe -> shoe.getValue().getQuantity_in_stock() > 0).forEach(index -> product_tree.getItems().add(index.getValue()));

        shoe_availability.setText("Tillgängliga skor: " + product_tree.getItems().size());
        identity_label.setText("Inloggad som: " + Controller.customer.getFirst_name() + "." + Controller.customer.getLast_name());

        addItemToCart.setGraphic(new ImageView("img/icons/add_btn.png"));
        delete_item_btn.setGraphic(new ImageView("img/icons/delete_btn.png"));
        process_order_btn.setGraphic(new ImageView("img/icons/purchase_btn.png"));
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
//        itemCart_list.put(
//                product_tree.getSelectionModel().getSelectedItem().getShoe_id(),
//               // product_tree.getSelectionModel().getSelectedItem()
//        );

        cartView.getItems().addAll(product_tree.getSelectionModel().getSelectedItems());
        process_order_btn.setText("Slutför beställning (" + itemCart_list.size() +")");
        itemCart_list.forEach((key, value) -> System.out.println(value));
    }

    public void signOut(ActionEvent actionEvent) {
        // TODO
    }

    public void viewProfile(ActionEvent actionEvent) throws IOException {
        Parent profile_parent = FXMLLoader.load(getClass().getClassLoader().getResource("sample/FXML/customer_info.fxml"));
        Scene profile_scene = new Scene(profile_parent);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(profile_scene);
        window.show();
    }

    public void deleteItem(ActionEvent actionEvent) {
        cartView.getItems().remove(cartView.getSelectionModel().getSelectedItem());
        int index = cartView.getSelectionModel().getSelectedItem().getShoe_id();
        itemCart_list.remove(index);
        process_order_btn.setText("Slutför beställning (" + itemCart_list.size() +")");
        if (itemCart_list.size() == 0) {
            System.out.println("Varukorg tom");
        } else {
            itemCart_list.forEach((key, value) -> System.out.println(value));
        }
    }
}

// -- TODO
// Ändra så att ifall man lägger till samma produkt i varukorgen så ökas kvantiteten istället.
// Vill inte lägga till i min varukorg ifall min Stored procedure misslyckas -> Throw Exception för SQL
// Ta fram LAST INSERT ID i java
//