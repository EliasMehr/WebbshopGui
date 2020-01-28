package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    public ListView<OrderItem> cartView;
    public Label shoe_availability;
    public Label total_cart_amount;
    public Label identity_label;
    public Button addItemToCart;
    public Button delete_item_btn;
    public Button process_order_btn;
    public int currentOrderID = 0;

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

    private Map<Integer, Category> categoryMap = new HashMap<>();
    private Map<Integer, Brand> brandMap = new HashMap<>();
    private Map<Integer, Color> colorMap = new HashMap<>();
    private Map<Integer, Size> sizeMap = new HashMap<>();
    private Map<Integer, Shoe> productMap = new HashMap<>();

    public void initialize() {
        loadServerSettings();
        getCategories();
        getBrands();
        getColor();
        getSize();
        getAllProductItems();
        refreshProductItems();


        identity_label.setText("Inloggad som: " + Controller.customer.getFirst_name() + "." + Controller.customer.getLast_name());

        addItemToCart.setGraphic(new ImageView("img/icons/add_btn.png"));
        delete_item_btn.setGraphic(new ImageView("img/icons/delete_btn.png"));
        process_order_btn.setGraphic(new ImageView("img/icons/purchase_btn.png"));
    }

    private void refreshProductItems() {
        productMap.entrySet().stream().filter(shoe -> shoe.getValue().getQuantity_in_stock() > 0).forEach(index -> product_tree.getItems().add(index.getValue()));
        shoe_availability.setText("Tillgängliga skor: " + product_tree.getItems().size());
        product_tree.refresh();
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

                productMap.put(shoeID, new Shoe(shoeID, categoryMap.get(categoryID),
                        brandMap.get(brandID), model,
                        colorMap.get(colorID),
                        sizeMap.get(sizeID),
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
                categoryMap.put(CATEGORY_IDENTITY_KEY, new Category(CATEGORY_IDENTITY_KEY, CATEGORY.toUpperCase()));
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
                brandMap.put(BRAND_IDENTITY_KEY, new Brand(BRAND_IDENTITY_KEY, BRAND.toUpperCase()));
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
                colorMap.put(COLOR_IDENTITY_KEY, new Color(COLOR_IDENTITY_KEY, COLOR.toUpperCase()));
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
                sizeMap.put(SIZE_IDENTITY_KEY, new Size(SIZE_IDENTITY_KEY, SIZE.toUpperCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSelectedItem(ActionEvent actionEvent) {
        boolean isAlreadyInCart = false;

        Shoe shoe = product_tree.getSelectionModel().getSelectedItem();

        for (OrderItem item : cartView.getItems()) {
            if (shoe.equals(item.getShoe())) {
                item.setQuantity(item.getQuantity() + 1);
                isAlreadyInCart = true;
                cartView.refresh();
            }
        }

        if (!isAlreadyInCart) {
            cartView.getItems().add(new OrderItem(shoe, 1));
        }

        updateCartInformation();

    }

    public void deleteSelectedItem(ActionEvent actionEvent) {
        OrderItem selectedItem = cartView.getSelectionModel().getSelectedItem();

        for (OrderItem item : cartView.getItems()) {
            if (item.equals(selectedItem)) {
                selectedItem.setQuantity(selectedItem.getQuantity() - 1);
            }
        }
        if (selectedItem.getQuantity() == 0) {
            cartView.getItems().remove(selectedItem);
        }

        cartView.refresh();
        updateCartInformation();
    }

    private void updateCartInformation() {
        int sum;
        int quantity;
        sum = cartView.getItems().stream().mapToInt(orderItem -> orderItem.getQuantity() * orderItem.getShoe().getUnit_price()).sum();
        total_cart_amount.setText("Total summa: " + sum + "SEK");

        quantity = cartView.getItems().stream().mapToInt(OrderItem::getQuantity).sum();
        process_order_btn.setText("Slutför beställning (" + quantity + ")");
    }

    public void signOut(ActionEvent actionEvent) throws IOException {
        Controller.customer = null;
        currentOrderID = 0;

        Parent login_parent = FXMLLoader.load(getClass().getClassLoader().getResource("sample/FXML/sign_in.fxml"));
        Scene login_scene = new Scene(login_parent);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(login_scene);
        window.show();
    }

    public void viewProfile(ActionEvent actionEvent) throws IOException {
        Parent profile_parent = FXMLLoader.load(getClass().getClassLoader().getResource("sample/FXML/customer_info.fxml"));
        Scene profile_scene = new Scene(profile_parent);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(profile_scene);
        window.show();
    }

    public void processOrder(ActionEvent actionEvent) {
        for (OrderItem item : cartView.getItems()) {
            for (int i = 0; i < item.getQuantity(); i++) {
                addToCart(currentOrderID, Controller.IDENTIFICATION_KEY, item.getShoe().getShoe_id());
            }
        }

        Controller.viewMessage("Din order har nu lagts", "ORDER BEKRÄFTELSE", Alert.AlertType.INFORMATION);
        cartView.getItems().clear();
        product_tree.getItems().clear();
        currentOrderID = 0;
        updateCartInformation();
        getAllProductItems();
        refreshProductItems();

    }

    private void addToCart(int orderID, int customerID, int shoeID) {
        try (Connection conn = DriverManager.getConnection(host, root, keypass)) {
            PreparedStatement statement = conn.prepareCall("CALL addToCart(?, ?, ?)");

            statement.setInt(1, orderID);
            statement.setInt(2, customerID);
            statement.setInt(3, shoeID);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            if (hasErrorColumn(resultSet, "ERROR_MESSAGE")) {
                Controller.viewMessage(resultSet.getString("ERROR_MESSAGE"), "Kan inte behandla din order", Alert.AlertType.WARNING);
            } else if (hasErrorColumn(resultSet, "newOrderID")) {
                currentOrderID = resultSet.getInt("newOrderID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasErrorColumn(ResultSet resultSet, String columnName) throws SQLException {
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