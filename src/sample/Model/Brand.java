package sample.Model;

public class Brand {
    private int brand_id;
    private String brand;

    public Brand() {}

    public Brand(int brand_id, String brand) {
        this.brand_id = brand_id;
        this.brand = brand;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return getBrand();
    }
}
