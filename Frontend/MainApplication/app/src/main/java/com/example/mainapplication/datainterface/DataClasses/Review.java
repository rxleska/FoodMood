package com.example.mainapplication.datainterface.DataClasses;

import org.json.JSONObject;

/**
 * The Review class stores the information of a review
 *
 * @author Ryan Leska
 */
public class Review {
    // Stores the information of a review
    /**
     * id: the id of the review
     * */
    private int id;
    /**
     * associatedProfileId: the id of the profile that wrote the review
     * */
    private int associatedProfileId;

    /**
     * userName: the name of the user that wrote the review
     * */
    private String userName;
    /**
     * rating: the rating of the review
     * */
    private double rating;//number of stars
    /**
     * reviewBody: the body of the review
     * */
    private String reviewBody;
    /**
     * date: the date of the review
     * */
    private lDate date;
    /**
     * associatedFoodId: the id of the food that the review is associated with
     * */
    private int associatedFoodId;

    // Constructor
    /**
     * Constructor for the Review class
     * @param id: the id of the review
     * @param associatedProfileId: the id of the profile that wrote the review
     * @param rating: the rating of the review
     * @param userName: the name of the user that wrote the review
     * @param reviewBody: the body of the review
     * @param date: the date of the review
     * @param associatedFoodId: the id of the food that the review is associated with
     * */
    public Review(int id, int associatedProfileId, double rating,String userName, String reviewBody, lDate date, int associatedFoodId) {
        this.id = id;
        this.associatedProfileId = associatedProfileId;
        this.rating = rating;
        this.reviewBody = reviewBody;
        this.date = date;
        this.associatedFoodId = associatedFoodId;
        this.userName = userName;
    }
    /**
     * Constructor for the Review class
     * @param id: the id of the review
     * @param associatedProfileId: the id of the profile that wrote the review
     * @param rating: the rating of the review
     * @param reviewBody: the body of the review
     * @param date: the date of the review
     * @param associatedFoodId: the id of the food that the review is associated with
     * */
    public Review(int id, int associatedProfileId, double rating, String reviewBody, lDate date, int associatedFoodId) {
        this.id = id;
        this.associatedProfileId = associatedProfileId;
        this.rating = rating;
        this.reviewBody = reviewBody;
        this.date = date;
        this.associatedFoodId = associatedFoodId;
        this.userName = "fakeName";
    }

    /**
     * Constructor for the Review class
     * @param review: the JSONObject that contains the information of the review
     *
     * @implNote Any fields that are not in the JSONObject will be set to a default value
     * */
    public Review(JSONObject review) {
        // Constructor from JSONObject
//        try {
//            this.id = review.getInt("id");
//            this.associatedProfileId = review.getInt("associatedProfileId");
//            this.rating = review.getDouble("rating");
//            this.reviewBody = review.getString("reviewBody");
//            this.date = new lDate(review.getJSONObject("date"));
//            this.associatedFoodId = review.getInt("associatedFoodId");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        // seperate try catch blocks for each field on failure to parse the json object set the field to a default value
        try {
            this.id = review.getInt("reviewId");
        } catch (Exception e) {
            this.id = -1;
        }

        try {
            this.associatedProfileId = review.getInt("userId");
        } catch (Exception e) {
            this.associatedProfileId = -1;
        }

        try {
            this.rating = review.getDouble("rating");
        } catch (Exception e) {
            this.rating = 0;
        }

        try {
            this.reviewBody = review.getString("reviewBody");
        } catch (Exception e) {
            try{
                this.reviewBody = review.getString("body");
            } catch (Exception e2) {
                this.reviewBody = "";
            }
        }

        try {
            this.date = new lDate(review.getJSONObject("date"));
        } catch (Exception e) {
            this.date = new lDate(1969, 12, 31);
        }

        try {
            this.associatedFoodId = review.getInt("food");
        } catch (Exception e) {
            this.associatedFoodId = -1;
        }

        try{
            this.userName = review.getString("userName");
        } catch (Exception e) {
            this.userName = "fakeName";
        }
    }

    // Getters and setters
    // ID
    /**
     * Getter for the id of the review
     * @return the id of the review
     * */
    public int getId() {
        return id;
    }
    /**
     * Setter for the id of the review
     * @param id: the id of the review
     * */
    public void setId(int id) {
        this.id = id;
    }
    // Associated profile ID
    /**
     * Getter for the id of the profile that wrote the review
     * @return the id of the profile that wrote the review
     * */
    public int getAssociatedProfileId() {
        return associatedProfileId;
    }
    /**
     * Setter for the id of the profile that wrote the review
     * @param associatedProfileId: the id of the profile that wrote the review
     * */
    public void setAssociatedProfileId(int associatedProfileId) {
        this.associatedProfileId = associatedProfileId;
    }
    // Rating
    /**
     * Getter for the rating of the review
     * @return the rating of the review
     * */
    public double getRating() {
        return rating;
    }
    /**
     * Setter for the rating of the review
     * @param rating: the rating of the review
     * */
    public void setRating(double rating) {
        this.rating = rating;
    }
    // Review body
    /**
     * Getter for the body of the review
     * @return the body of the review
     * */
    public String getReviewBody() {
        return reviewBody;
    }
    /**
     * Setter for the body of the review
     * @param reviewBody: the body of the review
     * */
    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }
    // Date
    /**
     * Getter for the date of the review
     * @return the date of the review
     * */
    public lDate getDate() {
        return date;
    }
    /**
     * Setter for the date of the review
     * @param date: the date of the review
     * */
    public void setDate(lDate date) {
        this.date = date;
    }
    // Associated food ID
    /**
     * Getter for the id of the food that the review is associated with
     * @return the id of the food that the review is associated with
     * */
    public int getAssociatedFoodId() {
        return associatedFoodId;
    }
    /**
     * Setter for the id of the food that the review is associated with
     * @param associatedFoodId: the id of the food that the review is associated with
     * */
    public void setAssociatedFoodId(int associatedFoodId) {
        this.associatedFoodId = associatedFoodId;
    }
    // User name
    /**
     * Getter for the name of the user that wrote the review
     * @return the name of the user that wrote the review
     * */
    public String getUserName() {
        return userName;
    }
    /**
     * Setter for the name of the user that wrote the review
     * @param userName: the name of the user that wrote the review
     * */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // toJson probably not needed
    /**
     * The toJson Converts the review object to a json string
     * @return the json string representation of the review object
     * */
    public String toJson(){
        return "{\"id\":" + id + ",\"associatedProfileId\":" + associatedProfileId + ",\"rating\":" + rating + ",\"reviewBody\":\"" + reviewBody + "\",\"date\":" + date.toJson() + ",\"associatedFoodId\":" + associatedFoodId + "}";
    }

    /**
     * The toJsonObject Converts the review object to a json object
     * @return the json object representation of the review object
     * */
    public JSONObject toJsonObject(){ // also probably not needed
        // This constructor is used to create a food object from a json string
        try{
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("reviewId", id);
            jsonObj.put("userId", associatedProfileId);
            jsonObj.put("userName", userName);
            jsonObj.put("rating", rating);
            jsonObj.put("body", reviewBody);
            jsonObj.put("date", date.toJsonObject());
            jsonObj.put("associatedFoodId", associatedFoodId);
            return jsonObj;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
