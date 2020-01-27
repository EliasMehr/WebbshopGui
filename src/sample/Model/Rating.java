package sample.Model;

public class Rating {
    private int rating_id;
    private String rating_txt;
    private double rating_numeric;


    public Rating(int rating_id, String rating_txt, double rating_numeric) {
        this.rating_id = rating_id;
        this.rating_txt = rating_txt;
        this.rating_numeric = rating_numeric;
    }

    public int getRating_id() {
        return rating_id;
    }

    public void setRating_id(int rating_id) {
        this.rating_id = rating_id;
    }

    public String getRating_txt() {
        return rating_txt;
    }

    public void setRating_txt(String rating_txt) {
        this.rating_txt = rating_txt;
    }

    public double getRating_numeric() {
        return rating_numeric;
    }

    public void setRating_numeric(double rating_numeric) {
        this.rating_numeric = rating_numeric;
    }
}
