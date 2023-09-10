package com.example.mainapplication.datainterface.DataClasses;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Stores the information of a user profile that is public
 * (i.e. not including email or password)
 *
 * @author Ryan Leska
 * */
public class Profile {
    /**
     * id: The id of the user
     * */
    private int id;
    /**
     * name: The name of the user
     * */
    private String name;
//    private String email;
//    private String password;
    /**
     * favouriteRestaurantIds: The ids of the restaurants that the user has favourited
     * */
    private int[] favouriteRestaurantIds;
    /**
     * favouriteFoodIds: The ids of the foods that the user has favourited
     * */
    private int[] favouriteFoodIds;
    /**
     * lastLoginDate: The date of the last time the user logged in
     * @see lDate
     * */
    private lDate lastLoginDate;
    /**
     * banStatus: The ban status of the user (Banned or Good Standing or something else)
     * */
    private String banStatus;
    /**
     * Tracks whether a user is an admin or not
     */
    private boolean isAdmin;

    // Constructor
    /**
     * Creates a new Profile object
     * @param id: The id of the user
     * @param name: The name of the user
     * @param favouriteRestaurantIds: The ids of the restaurants that the user has favourited
     * @param favouriteFoodIds: The ids of the foods that the user has favourited
     * @param lastLoginDate: The date of the last time the user logged in
     * @param banStatus: The ban status of the user (Banned or Good Standing or something else)
     * */
    public Profile(int id, String name, int[] favouriteRestaurantIds, int[] favouriteFoodIds, lDate lastLoginDate, String banStatus, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.favouriteRestaurantIds = favouriteRestaurantIds;
        this.favouriteFoodIds = favouriteFoodIds;
        this.lastLoginDate = lastLoginDate;
        this.banStatus = banStatus;
        this.isAdmin = isAdmin;
    }

    /**
     * Creates a new Profile object from a json string
     * @param jsonObject: The json string to create the object from
     *                  (must be in the format of the json string returned by the server)
     *                  (i.e. {"id":1,"username":"Ryan","restaurants":[1,2,3],"foods":[1,2,3],"lastLogin":{"year":2020,"month":1,"day":1,"hour":1,"minute":1},"banStatus":false})
     * */
    public Profile(JSONObject jsonObject){


        // This constructor is used to create a Profile object from a json string
        // try each of the fields and if it fails set it to null
        try{
            this.id = jsonObject.getInt("id");
        }catch(Exception e){
            this.id = -1;
        }

        try{
            this.name = jsonObject.getString("username");
        }catch(Exception e){
            this.name = "";
        }

        try{
            this.favouriteRestaurantIds = new int[jsonObject.getJSONArray("restaurants").length()];
            for(int i = 0; i < jsonObject.getJSONArray("restaurants").length(); i++){
                this.favouriteRestaurantIds[i] = jsonObject.getJSONArray("restaurants").getInt(i);
            }
        }catch(Exception e){
            this.favouriteRestaurantIds = new int[0];
        }

        try{
            this.favouriteFoodIds = new int[jsonObject.getJSONArray("foods").length()];
            for(int i = 0; i < jsonObject.getJSONArray("foods").length(); i++){
                this.favouriteFoodIds[i] = jsonObject.getJSONArray("foods").getInt(i);
            }
        }catch(Exception e){
            this.favouriteFoodIds = new int[0];
        }

        try{
            this.lastLoginDate = new lDate(jsonObject.getJSONObject("lastLogin"));
        }catch(Exception e){
            this.lastLoginDate = new lDate(0,0,0,0,0);
        }

        try{
            this.banStatus = jsonObject.getBoolean("banStatus") ? "Banned" : "Good Standing";
        }catch(Exception e){
            this.banStatus = "";
        }
        try{
            this.isAdmin = jsonObject.getBoolean("isAdmin");
        } catch (Exception e) {
            this.isAdmin = false;
        }
    }

    // Getters and setters
    // ID
    /**
     * Gets the id of the user
     * @return The id of the user
     * */
    public int getId() {
        return id;
    }
    /**
     * Sets the id of the user
     * @param id: The id of the user
     * */
    public void setId(int id) {
        this.id = id;
    }
    // Name
    /**
     * Gets the name of the user
     * @return The name of the user
     * */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the user
     * @param name: The name of the user
     * */
    public void setName(String name) {
        this.name = name;
    }
    // Favourite restaurant IDs
    /**
     * Sets if user is an admin user
     * @param isAdmin: bool, true if admin, false if not
     */
    public void setAdmin(boolean isAdmin) {this.isAdmin = isAdmin;}

    /**
     * Returns the value of isAdmin which marks whether or not a
     * user is an admin or not
     *
     * @return boolean value of whether or not user is an admin
     */
    public boolean getAdmin() { return isAdmin;}
    /**
     * Gets the ids of the restaurants that the user has favourited
     *
     * @return The ids of the restaurants that the user has favourited
     * */
    public int[] getFavouriteRestaurantIds() {
        return favouriteRestaurantIds;
    }
    /**
     * Gets the id of the restaurant at the specified index that the user has favourited
     * @param index: The index of the restaurant to get the id of
     * @return The id of the restaurant at the specified index that the user has favourited
     * */
    public int getFavouriteRestaurantId(int index) {
        return favouriteRestaurantIds[index];
    }
    /**
     * Sets the ids of the restaurants that the user has favourited
     * @param favouriteRestaurantIds: The ids of the restaurants that the user has favourited
     * */
    public void setFavouriteRestaurantIds(int[] favouriteRestaurantIds) {
        this.favouriteRestaurantIds = favouriteRestaurantIds;
    }
    // Favourite food IDs
    /**
     * Gets the ids of the foods that the user has favourited
     * @return The ids of the foods that the user has favourited
     * */
    public int[] getFavouriteFoodIds() {
        return favouriteFoodIds;
    }
    /**
     * Gets the id of the food at the specified index that the user has favourited
     * @param index: The index of the food to get the id of
     * @return The id of the food at the specified index that the user has favourited
     * */
    public int getFavouriteFoodId(int index) {
        return favouriteFoodIds[index];
    }
    /**
     * Sets the ids of the foods that the user has favourited
     * @param favouriteFoodIds: The ids of the foods that the user has favourited
     * */
    public void setFavouriteFoodIds(int[] favouriteFoodIds) {
        this.favouriteFoodIds = favouriteFoodIds;
    }
    // Last login date
    /**
     * Gets the date of the last time the user logged in
     * @return The date of the last time the user logged in
     * */
    public lDate getLastLoginDate() {
        return lastLoginDate;
    }
    /**
     * Sets the date of the last time the user logged in
     * @param lastLoginDate: The date of the last time the user logged in
     * */
    public void setLastLoginDate(lDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
    // Ban status
    /**
     * Gets the ban status of the user
     * @return The ban status of the user
     * */
    public String getBanStatus() {
        return banStatus;
    }
    /**
     * Sets the ban status of the user
     * @param banStatus: The ban status of the user
     * */
    public void setBanStatus(String banStatus) {
        this.banStatus = banStatus;
    }

    /**
     * Converts the Profile object to a JSON string
     * @return The JSON string
     * */
    public String toJson() {
        return "{\"id\":" + id + ",\"name\":\"" + name + "\",\"favouriteRestaurantIds\":" + favouriteRestaurantIds + ",\"favouriteFoodIds\":" + favouriteFoodIds + ",\"lastLoginDate\":" + lastLoginDate.toJson() + ",\"banStatus\":\"" + banStatus + "\"}";
    }

    /**
     * Converts the Profile object to a string readable by humans each value on a new line
     * @return The string
     * */
    public String toString(){
        return "ID: " + id + "\nName: " + name + "\nFavourite Restaurant IDs: " + favouriteRestaurantIds + "\nFavourite Food IDs: " + favouriteFoodIds + "\nLast Login Date: " + lastLoginDate.toString() + "\nBan Status: " + banStatus;
    }

    /**
     * Converts the Profile object to a JSONObject
     * @return The JSONObject
     * */
    public JSONObject toJsonObject() {
        // This method is used to convert the Profile object to a JSONObject
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("id", id);
            jsonObject.put("name", name);
            jsonObject.put("favouriteRestaurantIds", favouriteRestaurantIds);
            jsonObject.put("favouriteFoodIds", favouriteFoodIds);
            jsonObject.put("lastLogin", lastLoginDate.toJsonObject());
            jsonObject.put("banStatus", banStatus);
        }catch(Exception e){

        }
        return jsonObject;
    }

}

