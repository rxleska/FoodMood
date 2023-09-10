package com.example.mainapplication.datainterface.DataClasses;

//import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Food class is used to store the information of a food item.
 *
 * It is used to store the information of a food item in the database.
 *
 * The number of reviews is ignored as at the moment we are not using it.
 *
 * The reviews are ignored as at the moment we are not storing them in the local storage.
 *
 * @author Ryan Leska
 *
 * */
public class Food {
    /**
     * The id of the food item.
     * */
    private int id;
    /**
     * The average number of stars of the food item.
     * */
    private double averageNumberOfStars;
  //private int numberOfReviews;
    /**
     * The name of the food item.
     * */
    private String name;
    /**
     * The category of the food item.
     * */
    private String category;
    /**
     * The types of the food item.
     * */
    private String[] type;
    /**
     * The date the food item was added to the database.
     * */
    private lDate date;
  //private LinkedList<int> reviews;

    // Constructor
    /**
     * Food constructor.
     * @param id The id of the food item.
     * @param averageNumberOfStars The average number of stars of the food item.
     * @param name The name of the food item.
     * @param category The category of the food item.
     * @param date The date the food item was added to the database.
     *             If the date is null, the date will be set to 0/0/0 0:0.
     * */
    public Food(int id, double averageNumberOfStars, String name, String category, lDate date) {
        this.id = id;
        this.averageNumberOfStars = averageNumberOfStars;
        this.name = name;
        this.category = category;
        this.type = null;
        if(date == null)
            this.date = new lDate(0, 0, 0, 0, 0);
        else
            this.date = date;
    }

    /**
     * Food constructor.
     * @param jsonObj The json object to create the food object from.
     *
     *               The json object must have the following fields:
     *                id: The id of the food item.
     *                avgStars: The average number of stars of the food item.
     *                name: The name of the food item.
     *                category: The category of the food item.
     *                dateAdded: The date the food item was added to the database.
     *                The date must be in the json date format described in the lDate class.
     * @see com.example.mainapplication.datainterface.DataClasses.lDate
     * */
    public Food(JSONObject jsonObj){
        // This constructor is used to create a food object from a json string
        try{
            this.id = jsonObj.getInt("id");
        }catch(Exception e){
            this.id = -1;
        }

        String[] dateArr = null;
        try {
            dateArr = jsonObj.getString("lastUpdated").split("-");
        } catch (JSONException e) {
            try{
                this.date = new lDate(jsonObj.getJSONObject("lastUpdated"));
            }
            catch(JSONException e2){
                this.date = new lDate(0, 0, 0, 0, 0);
            }
        }
        if(dateArr != null && dateArr.length == 3){
            try{
                this.date = new lDate(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]));
            }
            catch (Exception e){
                this.date = new lDate(0, 0, 0, 0, 0);
            }
        }
        else{
            this.date = new lDate(0, 0, 0, 0, 0);
        }

        try{
            this.averageNumberOfStars = jsonObj.getDouble("avgStars");
        }
        catch(JSONException e){
            this.averageNumberOfStars = 0;
        }

        try{
            JSONArray typeArr = jsonObj.getJSONArray("type");
            this.type = new String[typeArr.length()];
            for(int i = 0; i < typeArr.length(); i++){
                this.type[i] = typeArr.getString(i);
            }
        }
        catch(JSONException e){
            this.type = null;
        }

        try{
            this.name = jsonObj.getString("name");
        }
        catch(JSONException e){
            this.name = "";
        }
        try{
            this.category = jsonObj.getString("category");
        }
        catch(JSONException e){
            this.category = "";
        }


    }

    // Getters and setters
    // ID

    /**
     * The getId method returns the id of the food item.
     *
     * @return The id of the food item.
     * */
    public int getId() {
        return id;
    }
    /**
     * The setId method sets the id of the food item.
     *
     * @param id The id of the food item.
     * */
    public void setId(int id) {
        this.id = id;
    }
    // Average number of stars
    /**
     * The getAverageNumberOfStars method returns the average number of stars of the food item.
     *
     * @return The average number of stars of the food item. (double)
     * */
    public double getAverageNumberOfStars() {
        return averageNumberOfStars;
    }
    /**
     * The setAverageNumberOfStars method sets the average number of stars of the food item.
     *
     * @param averageNumberOfStars The average number of stars of the food item.
     * */
    public void setAverageNumberOfStars(double averageNumberOfStars) {
        this.averageNumberOfStars = averageNumberOfStars;
    }
    // Number of reviews
    //public int getNumberOfReviews() {
    //    return numberOfReviews;
    //}
    //public void setNumberOfReviews(int numberOfReviews) {
    //    this.numberOfReviews = numberOfReviews;
    //}
    // Name
    /**
     * The getName method returns the name of the food item.
     *
     * @return The name of the food item. (String)
     * */
    public String getName() {
        return name;
    }
    /**
     * The setName method sets the name of the food item.
     *
     * @param name The name of the food item.
     * */
    public void setName(String name) {
        this.name = name;
    }
    // Category
    /**
     * The getCategory method returns the category of the food item.
     *
     * @return The category of the food item. (String)
     * */
    public String getCategory() {
        return category;
    }
    /**
     * The setCategory method sets the category of the food item.
     *
     * @param category The category of the food item.
     * */
    public void setCategory(String category) {
        this.category = category;
    }
    // Date
    /**
     * The getDate method returns the date the food item was added to the database.
     *
     * @return The date the food item was added to the database. (lDate)
     * @see com.example.mainapplication.datainterface.DataClasses.lDate
     * */
    public lDate getDate() {
        return date;
    }
    /**
     * The setDate method sets the date the food item was added to the database.
     *
     * @param date The date the food item was added to the database.
     * @see com.example.mainapplication.datainterface.DataClasses.lDate
     * */
    public void setDate(lDate date) {
        this.date = date;
    }
    // All date information
    /**
     * The getYear method returns the year the food item was added to the database.
     *
     * @return The year the food item was added to the database. (int)
     * */
    public int getYear() {
        return date.getYear();
    }
    /**
     * The setYear method sets the year the food item was added to the database.
     *
     * @param year The year the food item was added to the database.
     * */
    public void setYear(int year) {
        this.date.setYear(year);
    }
    /**
     * The getMonth method returns the month the food item was added to the database.
     *
     * @return The month the food item was added to the database. (int)
     * */
    public int getMonth() {
        return date.getMonth();
    }
    /**
     * The setMonth method sets the month the food item was added to the database.
     *
     * @param month The month the food item was added to the database.
     * */
    public void setMonth(int month) {
        this.date.setMonth(month);
    }
    /**
     * The getDay method returns the day the food item was added to the database.
     *
     * @return The day the food item was added to the database. (int)
     * */
    public int getDay() {
        return date.getDay();
    }
    /**
     * The setDay method sets the day the food item was added to the database.
     *
     * @param day The day the food item was added to the database.
     * */
    public void setDay(int day) {
        this.date.setDay(day);
    }
    /**
     * The getHour method returns the hour the food item was added to the database.
     *
     * @return The hour the food item was added to the database. (int)
     *
     * @implNote The hour is not used in the application.
     * */
    public int getHour() {
        return date.getHour();
    }
    /**
     * The setHour method sets the hour the food item was added to the database.
     *
     * @param hour The hour the food item was added to the database.
     *
     * @implNote The hour is not used in the application.
     * */
    public void setHour(int hour) {
        this.date.setHour(hour);
    }
    /**
     * The getMinute method returns the minute the food item was added to the database.
     *
     * @return The minute the food item was added to the database. (int)
     *
     * @implNote The minute is not used in the application.
     * */
    public int getMinute() {
        return date.getMinute();
    }
    /**
     * The setMinute method sets the minute the food item was added to the database.
     *
     * @param minute The minute the food item was added to the database.
     *
     * @implNote The minute is not used in the application.
     * */
    public void setMinute(int minute) {
        this.date.setMinute(minute);
    }

    /**
     * The getType method returns the type of the food item.
     *
     * @return The type of the food item. (String[])
     * */
    public String[] getType() {
        return type;
    }
    /**
     * The setType method sets the type of the food item.
     *
     * @param type The type of the food item.
     * */
    public void setType(String[] type) {
        this.type = type;
    }



    /**
     * The toJson method returns the food object as a json string.
     *
     * @return The food object as a json string. (String)
     * */
    public String toJson(){
        // This method is used to convert the food object to a json string
        return "{\"id\":" + id + ",\"averageNumberOfStars\":" + averageNumberOfStars + ",\"name\":\"" + name + "\",\"category\":\"" + category + "\",\"date\":" + date.toJson() + "}";
    }

    /**
     * The toJsonObject method returns the food object as a json object.
     *
     * @return The food object as a json object. (JSONObject)
     * @see org.json.JSONObject
     * */
    public JSONObject toJsonObject(){
        // This method is used to convert the food object to a json object
        JSONObject jsonObj = new JSONObject();
        try{
            jsonObj.put("id", id);
            jsonObj.put("avgStars", averageNumberOfStars);
            jsonObj.put("name", name);
            jsonObj.put("category", category);
            JSONArray typeArr = new JSONArray();
            if(type != null){
                for(int i = 0; i < type.length; i++){
                    typeArr.put(type[i]);
                }
            }
            jsonObj.put("type", typeArr);
            jsonObj.put("dateAdded", date.toJsonObject());
        }catch(Exception e){

        }



        return jsonObj;
    }

    // Reviews
    //public LinkedList<int> getReviews() {
    //    return reviews;
    //}
    //public void setReviews(LinkedList<int> reviews) {
    //    this.reviews = reviews;
    //}
    //public void addReview(int review) {
    //    this.reviews.add(review);
    // }
    //public void removeReview(int review) {
    //    this.reviews.remove(review);
    // }





}
