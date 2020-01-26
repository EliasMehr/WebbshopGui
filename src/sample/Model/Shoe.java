package sample.Model;

public class Shoe {
    private int shoe_id;
    private Category category_id;
    private Brand brand_id;
    private String model_name;
    private Color color_id;
    private Size size_id;
    private int quantity_in_stock;
    private int unit_price;

    public Shoe() {}

    public Shoe(int shoe_id, Category category_id, Brand brand_id, String model_name, Color color_id, Size size_id, int quantity_in_stock, int unit_price) {
        this.shoe_id = shoe_id;
        this.category_id = category_id;
        this.brand_id = brand_id;
        this.model_name = model_name;
        this.color_id = color_id;
        this.size_id = size_id;
        this.quantity_in_stock = quantity_in_stock;
        this.unit_price = unit_price;
    }

    public int getShoe_id() {
        return shoe_id;
    }

    public void setShoe_id(int shoe_id) {
        this.shoe_id = shoe_id;
    }

    public Category getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Category category_id) {
        this.category_id = category_id;
    }

    public Brand getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Brand brand_id) {
        this.brand_id = brand_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public Color getColor_id() {
        return color_id;
    }

    public void setColor_id(Color color_id) {
        this.color_id = color_id;
    }

    public Size getSize_id() {
        return size_id;
    }

    public void setSize_id(Size size_id) {
        this.size_id = size_id;
    }

    public int getQuantity_in_stock() {
        return quantity_in_stock;
    }

    public void setQuantity_in_stock(int quantity_in_stock) {
        this.quantity_in_stock = quantity_in_stock;
    }

    public int getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }
}
