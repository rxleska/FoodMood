package com.example.mainapplication.datainterface.DataClasses;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The RestaurantHours class is a data class that holds the hours of a restaurant.
 * It is used to store the hours of a restaurant for the day
 *
 * It contains a list of RestaurantHour objects
 * - RestaurantHour is a data class that holds the hours of a restaurant for a specific section
 *
 * @author Ryan Leska
 * */
public class RestaurantHours {

    /**
     * The RestaurantHour class is a data class that holds the hours of a restaurant for a specific day
     * It is a sub class of RestaurantHours and is used to store 1 section of hours for a restaurant as well as the section name
     *
     * @author Ryan Leska
     */
    private class RestaurantHour {
        /**
         * The section is the name of the section of hours
         * */
        private String section;
        /**
         * The start is the start time of the section of hours
         * */
        private String start;
        /**
         * The end is the end time of the section of hours
         * */
        private String end;

        /**
         * The RestaurantHour constructor takes in a section, start, and end time
         * @param section is the name of the section of hours
         * @param start is the start time of the section of hours
         * @param end is the end time of the section of hours
         * */
        public RestaurantHour(String section, String start, String end) {
            this.section = section;
            this.start = start;
            this.end = end;
        }
        /**
         * The RestaurantHour constructor without parameters
         * All fields are blank
         * */
        public RestaurantHour() {
            this.section = "";
            this.start = "";
            this.end = "";
        }
        /**
         * The RestaurantHour constructor takes in a JSONObject
         * It will attempt to get the section, start, and end time from the JSONObject
         * If it fails to get a field, it will be set to blank
         * @param jsonObject is the JSONObject that contains the section, start, and end time
         * */
        public RestaurantHour(JSONObject jsonObject) {
            try {
                this.section = jsonObject.getString("section");
            } catch (Exception e) {
                this.section = "";
            }
            try {
                this.start = jsonObject.getString("from");
            } catch (Exception e) {
                this.start = "";
            }
            try {
                this.end = jsonObject.getString("to");
            } catch (Exception e) {
                this.end = "";
            }
        }

        // Getters and Setters
        /**
         * The getSection method returns the section of hours
         * @return the section of hours
         * */
        public String getSection() {
            return section;
        }
        /**
         * The setSection method sets the section of hours
         * @param section is the section of hours
         * */
        public void setSection(String section) {
            this.section = section;
        }
        /**
         * The getStart method returns the start time of the section of hours
         * @return the start time of the section of hours
         * */
        public String getStart() {
            return start;
        }
        /**
         * The setStart method sets the start time of the section of hours
         * @param start is the start time of the section of hours
         * */
        public void setStart(String start) {
            this.start = start;
        }
        /**
         * The getEnd method returns the end time of the section of hours
         * @return the end time of the section of hours
         * */
        public String getEnd() {
            return end;
        }
        /**
         * The setEnd method sets the end time of the section of hours
         * @param end is the end time of the section of hours
         * */
        public void setEnd(String end) {
            this.end = end;
        }

        /**
         * The getJSONObject method returns a JSONObject of the RestaurantHour
         * @return a JSONObject of the RestaurantHour
         * */
        public JSONObject getJSONObject() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("section", this.section);
                jsonObject.put("from", this.start);
                jsonObject.put("to", this.end);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        /**
         * The toString method returns a string representation of the RestaurantHour
         * @return a string representation of the RestaurantHour
         * @implNote The format is: section: start - end meant to be easy to read
         * */
        public String toString() {
            return this.section + ": " + this.start + " - " + this.end;
        }
    }

    /**
     * The hours is a list of RestaurantHour objects
     * */
    List<RestaurantHour> hours;

    /**
     * The RestaurantHours constructor takes in a list of RestaurantHour objects
     * @param hours is a list of RestaurantHour objects
     * */
    public RestaurantHours(List<RestaurantHour> hours) {
        this.hours = hours;
    }
    /**
     * The RestaurantHours constructor without parameters
     * The hours list is initialized to an empty list
     * */
    public RestaurantHours() {
        this.hours = new ArrayList<RestaurantHour>();
    }
    /**
     * The RestaurantHours constructor takes in a JSONObject
     * It will attempt to get the hours from the JSONObject
     * If it fails to get the hours, it will be set to an empty list
     * @param jsonObject is the JSONObject that contains the hours
     * */
    public RestaurantHours(JSONObject jsonObject) {
        this.hours = new ArrayList<RestaurantHour>();
        try {
            JSONArray hours = jsonObject.getJSONArray("hours");
            for (int i = 0; i < hours.length(); i++) {
                this.hours.add(new RestaurantHour(hours.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getters and Setters
    /**
     * The getHours method returns the list of RestaurantHour objects
     * @return the list of RestaurantHour objects
     * */
    public List<RestaurantHour> getHours() {
        return hours;
    }
    /**
     * The setHours method sets the list of RestaurantHour objects
     * @param hours is the list of RestaurantHour objects
     * */
    public void setHours(List<RestaurantHour> hours) {
        this.hours = hours;
    }

    // Other Methods
    // toString
    /**
     * The toString method returns a string representation of the RestaurantHours
     * @return a string representation of the RestaurantHours
     * @implNote The format is: section: start - end (each section is on a new line)
     * */
    public String toString() {
        String result = "";
        for (RestaurantHour hour : hours){
            Log.e("cchour", hour.toString());
            if(!hour.toString().equals("null: null - null")){
                result += hour.toString() + "\n";
            }
        }
        if(result.length() > 2){
            return result.substring(0, result.length() - 2);
        }
        else{
            return result;
        }
    }

    /**
     * The getJSONObject method returns a JSONObject of the RestaurantHours
     * @return a JSONObject of the RestaurantHours (hours is a JSONArray of RestaurantHour JSONObjects)
     * */
    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray hours = new JSONArray();
            for (RestaurantHour hour : this.hours) {
                hours.put(hour.getJSONObject());
            }
            jsonObject.put("hours", hours);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}


