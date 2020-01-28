package Webbshop.Model;

public class Review {
    private int review_id;
    private Customer customer_id;
    private Shoe shoe_id;
    private String comment;
    private Rating rating_id;

    public Review(int review_id, Customer customer_id, Shoe shoe_id, String comment, Rating rating_id) {
        this.review_id = review_id;
        this.customer_id = customer_id;
        this.shoe_id = shoe_id;
        this.comment = comment;
        this.rating_id = rating_id;
    }

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }

    public Customer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Customer customer_id) {
        this.customer_id = customer_id;
    }

    public Shoe getShoe_id() {
        return shoe_id;
    }

    public void setShoe_id(Shoe shoe_id) {
        this.shoe_id = shoe_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Rating getRating_id() {
        return rating_id;
    }

    public void setRating_id(Rating rating_id) {
        this.rating_id = rating_id;
    }
}
