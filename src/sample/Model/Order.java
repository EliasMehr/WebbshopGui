package sample.Model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private int order_id;
    private LocalDate currentDate;
    private Map<Integer, OrderItem> orderItems = new HashMap<>();

    public Order(int order_id) {
        this.order_id = order_id;
        this.currentDate = LocalDate.now();
    }

    public Map<Integer, OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Map<Integer, OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }
}
