package sample.Model;

import java.time.LocalDate;
import java.util.Date;

public class Order {
    private int order_id;
    private Customer customer_id;
    private LocalDate currentDate;

    public Order() {}

    public Order(int order_id, Customer customer_id) {
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.currentDate =  LocalDate.now();
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public Customer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Customer customer_id) {
        this.customer_id = customer_id;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }
}
