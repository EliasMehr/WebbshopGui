package sample.Model;

import com.mysql.cj.x.protobuf.MysqlxCrud;

public class OrderItem {
    private Shoe shoe_id;
    private int orderItem_id;
    private int quantity;

    public OrderItem(int orderItem_id, Shoe shoe_id, int quantity) {
        this.orderItem_id = orderItem_id;
        this.shoe_id = shoe_id;
        this.quantity = quantity;
    }

    public int getOrderItem_id() {
        return orderItem_id;
    }

    public void setOrderItem_id(int orderItem_id) {
        this.orderItem_id = orderItem_id;
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
