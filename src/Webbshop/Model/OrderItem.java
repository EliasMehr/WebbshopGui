package Webbshop.Model;

public class OrderItem {
    private Shoe shoe;
    private int quantity;

    public OrderItem(Shoe shoe, int quantity) {
        this.shoe = shoe;
        this.quantity = quantity;
    }

    public Shoe getShoe() {
        return shoe;
    }

    public void setShoe(Shoe shoe) {
        this.shoe = shoe;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return shoe.getBrand_id().getBrand() + " " + shoe.getModel_name() + " " + shoe.getSize_id().getSize() + " " + shoe.getColor_id().getColor() + " Antal: " + quantity;
    }
}
