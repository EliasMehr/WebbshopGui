package Webbshop.Controllers;

import Webbshop.Repository;
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
import Webbshop.Main;
import Webbshop.Model.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerPortal {

    public ListView<Shoe> product_tree;
    public ListView<OrderItem> cartView;
    public Label shoe_availability;
    public Label total_cart_amount;
    public Label customerLabel;
    public Button addItemToCart;
    public Button deleteItemBtn;
    public Button processOrderBtn;

    private Map<Integer, Shoe> productMap = new HashMap<>();

    public void initialize() {

        productMap = Repository.getAllProducts();
        refreshProductItems();

        customerLabel.setText("Inloggad som: " + Main.customerObject.getFirst_name() + "." + Main.customerObject.getLast_name());

        addItemToCart.setGraphic(new ImageView("img/icons/add_btn.png"));
        deleteItemBtn.setGraphic(new ImageView("img/icons/delete_btn.png"));
        processOrderBtn.setGraphic(new ImageView("img/icons/purchase_btn.png"));
    }

    private void refreshProductItems() {
        productMap.entrySet().stream().filter(shoe -> shoe.getValue().getQuantity_in_stock() > 0).forEach(index -> product_tree.getItems().add(index.getValue()));
        shoe_availability.setText("Tillgängliga skor: " + product_tree.getItems().size());
        product_tree.refresh();
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
        processOrderBtn.setText("Slutför beställning (" + quantity + ")");
    }

    public void signOut(ActionEvent actionEvent) throws IOException {
        Main.customerObject = null;
        Main.currentOrderID = 0;

        Parent login_parent = FXMLLoader.load(getClass().getClassLoader().getResource("Webbshop/FXML/sign_in.fxml"));
        Scene login_scene = new Scene(login_parent);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(login_scene);
        window.show();
    }

    public void viewProfile(ActionEvent actionEvent) throws IOException {
        Parent profile_parent = FXMLLoader.load(getClass().getClassLoader().getResource("Webbshop/FXML/customer_info.fxml"));
        Scene profile_scene = new Scene(profile_parent);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(profile_scene);
        window.show();
    }

    public void processOrder(ActionEvent actionEvent) {
        for (OrderItem item : cartView.getItems()) {
            for (int i = 0; i < item.getQuantity(); i++) {
                Repository.addToCart(Main.currentOrderID, Main.CUSTOMER_KEY, item.getShoe().getShoe_id());
            }
        }

        Main.viewMessage("Din order har nu lagts", "ORDER BEKRÄFTELSE", Alert.AlertType.INFORMATION);
        resetWebShop();
    }

    private void resetWebShop() {
        cartView.getItems().clear();
        product_tree.getItems().clear();
        productMap = Repository.getAllProducts();
        Main.currentOrderID = 0;
        updateCartInformation();
        refreshProductItems();
    }


}