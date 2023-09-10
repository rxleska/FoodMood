package com.example.mainapplication.datainterface.DataClasses;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Restaurant class is used to store the information of a restaurant
 *
 * @author Ryan Leska
 * */
public class Restaurant {
    // Stores the information of a restaurant

    /**
     * id - The id of the restaurant
     * */
    private int id;
    /**
     * name - The name of the restaurant
     * */
    private String name;
    /**
     * address - The address of the restaurant
     * */
    private String address;
    /**
     * description - The description of the restaurant
     * */
    private String description;
    /**
     * type - The type of the restaurant
     * */
    private String type;
    /**
     * subRestaurantNames - The names of the sub restaurants
     * */
    private String[] subRestaurantNames;
    /**
     * associatedFoodIds - The ids of the food items associated with the restaurant
     * */
    private int[] associatedFoodIds;
    /**
     * latitude - The latitude of the restaurant
     * */
    private double latitude;
    /**
     * longitude - The longitude of the restaurant
     * */
    private double longitude;
    /**
     * hours - The hours of the restaurant
     * @implNote this is called in a different call from get all restaurants
     * @see com.example.mainapplication.datainterface.MiddleMan
     * */
    private String hours;
    /**
     * lastUpdated - lDate of the last date the restaurant information was updated
     * */
    private lDate lastUpdated;

    // Constructor
    /**
     * Constructor for the Restaurant class
     * @param id - The id of the restaurant
     * @param name - The name of the restaurant
     * @param address - The address of the restaurant
     * @param description - The description of the restaurant
     * @param type - The type of the restaurant
     * @param subRestaurantNames - The names of the sub restaurants
     * @param associatedFoodIds - The ids of the food items associated with the restaurant
     * @param latitude - The latitude of the restaurant
     * @param longitude - The longitude of the restaurant
     * @param hours - The hours of the restaurant
     * */
    public Restaurant(int id, String name, String address, String description, String type, String[] subRestaurantNames, int[] associatedFoodIds, double latitude, double longitude, String hours) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.type = type;
        this.subRestaurantNames = subRestaurantNames;
        this.associatedFoodIds = associatedFoodIds;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hours = hours;
    }
    /**
     * Constructor for the Restaurant class
     * @param id - The id of the restaurant
     * @param name - The name of the restaurant
     * @param address - The address of the restaurant
     * @param description - The description of the restaurant
     * @param type - The type of the restaurant
     * @param subRestaurantNames - The names of the sub restaurants
     * @param associatedFoodIds - The ids of the food items associated with the restaurant
     * @param latitude - The latitude of the restaurant
     * @param longitude - The longitude of the restaurant
     * @param hours - The hours of the restaurant
     * @param lastUpdated - lDate of the last date the restaurant information was updated
     * */
    public Restaurant(int id, String name, String address, String description, String type, String[] subRestaurantNames, int[] associatedFoodIds, double latitude, double longitude, String hours, lDate lastUpdated) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.type = type;
        this.subRestaurantNames = subRestaurantNames;
        this.associatedFoodIds = associatedFoodIds;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hours = hours;
        this.lastUpdated = lastUpdated;
    }

    /**
     * Constructor for the Restaurant class
     * @param jsonObject - The json object to create the restaurant from
     * */
    public Restaurant(JSONObject jsonObject){
        String response = jsonObject.toString();
        response = response.replaceAll("'", "\\'");
        try {
            jsonObject = new JSONObject(response);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        // This constructor is used to create a Restaurant object from a json string
        // try each of the fields and if it fails set it to null
        try{
            this.id = jsonObject.getInt("id");
        }catch(Exception e){
            this.id = -1;
        }

        try{
            this.name = jsonObject.getString("name");
        }catch(Exception e){
            this.name = "";
        }

        try{
            this.address = jsonObject.getString("address");
        }catch(Exception e){
            this.address = "";
        }

        try{
            this.description = jsonObject.getString("description");
        }catch(Exception e){
            this.description = "";
        }

        try{
            this.type = jsonObject.getString("type");
        }catch(Exception e){
            this.type = "";
        }

        try{
            this.subRestaurantNames = new String[jsonObject.getJSONArray("subRestaurantNames").length()];
            for(int i = 0; i < jsonObject.getJSONArray("subRestaurantNames").length(); i++){
                this.subRestaurantNames[i] = jsonObject.getJSONArray("subRestaurantNames").getString(i);
            }
        }catch(Exception e){
            this.subRestaurantNames = new String[0];
        }

        try{
            this.associatedFoodIds = new int[jsonObject.getJSONArray("menu").length()];
            for(int i = 0; i < jsonObject.getJSONArray("menu").length(); i++){
                this.associatedFoodIds[i] = jsonObject.getJSONArray("menu").getInt(i);
            }
        }catch(Exception e){
            this.associatedFoodIds = new int[0];
        }

        try{
            this.latitude = jsonObject.getDouble("lat");
        }catch(Exception e){
            this.latitude = 0;
        }

        try{
            this.longitude = jsonObject.getDouble("lng");
        }catch(Exception e){
            this.longitude = 0;
        }

        try{
            this.hours = jsonObject.getString("hours");
        }catch(Exception e){
            this.hours = "";
        }

        try{
            String[] date = jsonObject.getString("lastUpdated").split("-");
            if(date.length == 3){
                this.lastUpdated = new lDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
            }
            else{
                this.lastUpdated = new lDate(jsonObject.getJSONObject("lastUpdated"));
            }
        } catch (JSONException e) {
            this.lastUpdated = new lDate(0,0,0);
        }
    }

    // Getters and setters
    // ID
    /**
     * The getId method returns the id of the restaurant
     * @return id - The id of the restaurant
     * */
    public int getId() {
        return id;
    }
    /**
     * The setId method sets the id of the restaurant
     * @param id - The id of the restaurant
     * */
    public void setId(int id) {
        this.id = id;
    }
    // Name
    /**
     * The getName method returns the name of the restaurant
     * @return name - The name of the restaurant
     * */
    public String getName() {
        return name;
    }
    /**
     * The setName method sets the name of the restaurant
     * @param name - The name of the restaurant
     * */
    public void setName(String name) {
        this.name = name;
    }
    // Address
    /**
     * The getAddress method returns the address of the restaurant
     * @return address - The address of the restaurant
     * */
    public String getAddress() {
        return address;
    }
    /**
     * The setAddress method sets the address of the restaurant
     * @param address - The address of the restaurant
     * */
    public void setAddress(String address) {
        this.address = address;
    }
    // Description
    /**
     * The getDescription method returns the description of the restaurant
     * @return description - The description of the restaurant
     * */
    public String getDescription() {
        return description;
    }
    /**
     * The setDescription method sets the description of the restaurant
     * @param description - The description of the restaurant
     * */
    public void setDescription(String description) {
        this.description = description;
    }
    // Type
    /**
     * The getType method returns the type of the restaurant
     * @return type - The type of the restaurant
     * */
    public String getType() {
        return type;
    }
    /**
     * The setType method sets the type of the restaurant
     * @param type - The type of the restaurant
     * */
    public void setType(String type) {
        this.type = type;
    }
    // Sub restaurant names
    /**
     * The getSubRestaurantNames method returns the sub restaurant names of the restaurant
     * @return subRestaurantNames - The sub restaurant names of the restaurant
     * */
    public String[] getSubRestaurantNames() {
        return subRestaurantNames;
    }
    /**
     * The getSubRestaurantName method returns the sub restaurant name of the restaurant at the given index
     * @param index - The index of the sub restaurant name to return
     *
     * @return subRestaurantNames[index] - The sub restaurant name of the restaurant at the given index
     * */
    public String getSubRestaurantName(int index) {
        return subRestaurantNames[index];
    }
    /**
     * The setSubRestaurantNames method sets the sub restaurant names of the restaurant
     * @param subRestaurantNames - The sub restaurant names of the restaurant
     * */
    public void setSubRestaurantNames(String[] subRestaurantNames) {
        this.subRestaurantNames = subRestaurantNames;
    }
    // Associated food IDs
    /**
     * The getAssociatedFoodIds method returns the associated food IDs of the restaurant
     * @return associatedFoodIds - The associated food IDs of the restaurant
     * */
    public int[] getAssociatedFoodIds() {
        return associatedFoodIds;
    }
    /**
     * The getAssociatedFoodId method returns the associated food ID of the restaurant at the given index
     * @param index - The index of the associated food ID to return
     *
     * @return associatedFoodIds[index] - The associated food ID of the restaurant at the given index
     * */
    public int getAssociatedFoodId(int index) {
        return associatedFoodIds[index];
    }
    /**
     * The setAssociatedFoodIds method sets the associated food IDs of the restaurant
     * @param associatedFoodIds - The associated food IDs of the restaurant
     * */
    public void setAssociatedFoodIds(int[] associatedFoodIds) {
        this.associatedFoodIds = associatedFoodIds;
    }
    // Latitude
    /**
     * The getLatitude method returns the latitude of the restaurant
     * @return latitude - The latitude of the restaurant
     * */
    public double getLatitude() {
        return latitude;
    }
    /**
     * The setLatitude method sets the latitude of the restaurant
     * @param latitude - The latitude of the restaurant
     * */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    // Longitude
    /**
     * The getLongitude method returns the longitude of the restaurant
     * @return longitude - The longitude of the restaurant
     * */
    public double getLongitude() {
        return longitude;
    }
    /**
     * The setLongitude method sets the longitude of the restaurant
     * @param longitude - The longitude of the restaurant
     * */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    // Hours
    /**
     * The getHours method returns the hours of the restaurant
     * @return hours - The hours of the restaurant
     * */
    public String getHours() {
        return hours;
    }
    /**
     * The setHours method sets the hours of the restaurant
     * @param hours - The hours of the restaurant
     * */
    public void setHours(String hours) {
        this.hours = hours;
    }
    // Last updated
    /**
     * The getLastUpdated method returns the last updated date of the restaurant
     * @return lastUpdated - The last updated date of the restaurant
     * */
    public lDate getLastUpdated() {
        return lastUpdated;
    }
    /**
     * The setLastUpdated method sets the last updated date of the restaurant
     * @param lastUpdated - The last updated date of the restaurant
     * */
    public void setLastUpdated(lDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * The toJson method returns the restaurant object as a json string
     * @return json - The restaurant object as a json string
     * */
    public String toJson(){
        return "{\"id\":" + id + ",\"name\":\"" + name + "\",\"address\":\"" + address + "\",\"description\":\"" + description + "\",\"type\":\"" + type + "\",\"subRestaurantNames\":" + subRestaurantNames + ",\"associatedFoodIds\":" + associatedFoodIds + ",\"latitude\":" + latitude + ",\"longitude\":" + longitude + ",\"hours\":\"" + hours + "\"}";
    }

    /**
     * The toJsonObject method returns the restaurant object as a json object
     * @return jsonObject - The restaurant object as a json object
     * */
    public JSONObject toJsonObject(){
        // This method is used to convert the Restaurant object to a json object
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("id", id);
            jsonObject.put("name", name);
            jsonObject.put("address", address);
            jsonObject.put("description", description);
            jsonObject.put("type", type);
            jsonObject.put("subRestaurantNames", subRestaurantNames);
            JSONArray menuArray = new JSONArray();
            for(int i = 0; i < associatedFoodIds.length; i++){
                menuArray.put(associatedFoodIds[i]);
            }
            jsonObject.put("menu", menuArray);
            jsonObject.put("lat", latitude);
            jsonObject.put("lng", longitude);
            jsonObject.put("hours", hours);
            jsonObject.put("lastUpdated", lastUpdated.toJsonObject());
        }catch(Exception e){
            Log.e("Restaurant", "Error converting Restaurant object to json object");
        }
        return jsonObject;
    }

}
