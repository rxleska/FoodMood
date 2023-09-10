package com.example.jsonvolleytest;

public class CourseModal {
    private String food;
    private String foodImg;
    private String rating;
    private String user;
    private String review;

    public CourseModal(String food, String foodImg, String rating, String review) {
        this.food = food;
        this.foodImg = foodImg;
        this.rating = rating;
        this.review = review;
    }
    public String getFood(){
        return food;
    }
    public void setFood(String food){
        this.food = food;
    }
    public String getFoodImg(){
        return foodImg;
    }
    public void setFoodImg(String foodImg){
        this.foodImg = foodImg;
    }
    public String getRating(){
        return rating;
    }
    public void setRating(String rating){
        this.rating = rating;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}

