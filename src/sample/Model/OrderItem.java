package sample.Model;

import com.mysql.cj.x.protobuf.MysqlxCrud;

public class OrderItem {
    private int orderItem_id;
    private Order order_id;
    private Shoe shoe_id;
    private int quantity;

    public OrderItem() {}

    public OrderItem(int orderItem_id, Order order_id, Shoe shoe_id, int quantity) {
        this.orderItem_id = orderItem_id;
        this.order_id = order_id;
        this.shoe_id = shoe_id;
        this.quantity = quantity;
    }

    public int getOrderItem_id() {
        return orderItem_id;
    }

    public void setOrderItem_id(int orderItem_id) {
        this.orderItem_id = orderItem_id;
    }

    public Order getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Order order_id) {
        this.order_id = order_id;
    }

    public Shoe getShoe_id() {
        return shoe_id;
    }

    public void setShoe_id(Shoe shoe_id) {
        this.shoe_id = shoe_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
