package com.example.mainapplication.datainterface.DataClasses;


import org.json.JSONObject;

/**
 * lDate class is used to store the information of a date and time
 * This was needed because of the change from Date to LocalDateTime in Java (8 to 11)
 * that is reflected in the changing of android versions (24 to 26)
 *
 * @author Ryan Leska
 */
public class lDate {

    /**
     * The year of the date
     */
    private int year;
    /**
     * The month of the date
     */
    private int month;
    /**
     * The day of the date
     */
    private int day;
    /**
     * The hour of the date
     */
    private int hour;
    /**
     * The minute of the date
     */
    private int minute;

    // Constructor
    /**
     * Constructor for the lDate class
     * @param year the year of the date
     * @param month the month of the date
     * @param day the day of the date
     * @param hour the hour of the date
     * @param minute the minute of the date
     * */
    public lDate(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }
    /**
     * Constructor for the lDate class
     * @param year the year of the date
     * @param month the month of the date
     * @param day the day of the date
     *
     * This constructor is used when the time is not needed
     *            the time is set to 0
     * */
    public lDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = 0;
        this.minute = 0;
    }

    /**
     * Constructor for the lDate class
     * @param jsonObject the json object to create the lDate object from
     * */
    public lDate(JSONObject jsonObject){
        // This constructor is used to create a lDate object from a json string
        // try each of the fields and if it fails set it to 0
        try{
            this.year = jsonObject.getInt("year");
        }catch(Exception e){
            this.year = 0;
        }

        try{
            this.month = jsonObject.getInt("month");
        }catch(Exception e){
            this.month = 0;
        }

        try{
            this.day = jsonObject.getInt("day");
        }catch(Exception e){
            this.day = 0;
        }

        try{
            this.hour = jsonObject.getInt("hour");
        }catch(Exception e){
            this.hour = 0;
        }

        try{
            this.minute = jsonObject.getInt("minute");
        }catch(Exception e){
            this.minute = 0;
        }
    }

    // Getters and setters
    // Year
    /**
     * Gets the year of the date
     * @return the year of the date (int)
     * */
    public int getYear() {
        return year;
    }
    /**
     * Sets the year of the date
     * @param year the year of the date
     * */
    public void setYear(int year) {
        this.year = year;
    }
    // Month
    /**
     * Gets the month of the date
     * @return the month of the date (int)
     * */
    public int getMonth() {
        return month;
    }
    /**
     * Sets the month of the date
     * @param month the month of the date
     * */
    public void setMonth(int month) {
        this.month = month;
    }
    // Day
    /**
     * Gets the day of the date
     * @return the day of the date (int)
     * */
    public int getDay() {
        return day;
    }
    /**
     * Sets the day of the date
     * @param day the day of the date
     * */
    public void setDay(int day) {
        this.day = day;
    }
    // Hour
    /**
     * Gets the hour of the date
     * @return the hour of the date (int)
     * */
    public int getHour() {
        return hour;
    }
    /**
     * Sets the hour of the date
     * @param hour the hour of the date
     * */
    public void setHour(int hour) {
        this.hour = hour;
    }
    // Minute
    /**
     * Gets the minute of the date
     * @return the minute of the date (int)
     * */
    public int getMinute() {
        return minute;
    }
    /**
     * Sets the minute of the date
     * @param minute the minute of the date
     * */
    public void setMinute(int minute) {
        this.minute = minute;
    }

    // toString
    /**
     * Converts the lDate object to a string
     * @return the lDate object as a string (String)
     * */
    public String toString() {
        return day + "-" + month + "-" + year + " " + hour + ":" + minute;
    }
    //toString without time
    /**
     * Converts the lDate object to a string without the time
     * @return the lDate object as a string without the time (String)
     * */
    public String toStringWithoutTime() {
        return day + "-" + month + "-" + year;
    }

    //Get DateDMY
    /**
     * Gets the date in the format day-month-year
     * @return the date in the format day-month-year (String)
     * */
    public String getDateDMYtoString() {
        return day + "-" + month + "-" + year;
    }
    /**
     * Gets the date in the format day-month-year as an array
     * @return the date in the format day-month-year (int[])
     * */
    public int[] getDateDMY() {
        return new int[]{day, month, year};
    }

    //Get DateMDY
    /**
     * Gets the date in the format month-day-year
     * @return the date in the format month-day-year (String)
     * */
    public String getDateMDYtoString() {
        return month + "-" + day + "-" + year;
    }
    /**
     * Gets the date in the format month-day-year as an array
     * @return the date in the format month-day-year (int[])
     * */
    public int[] getDateMDY() {
        return new int[]{month, day, year};
    }

    //Get time HM
    /**
     * Gets the time in the format hour:minute
     * @return the time in the format hour:minute (String)
     * */
    public String getTimeHMtoString() {
        return hour + ":" + minute;
    }
    /**
     * Gets the time in the format hour:minute as an array
     * @return the time in the format hour:minute (int[])
     * */
    public int[] getTimeHM() {
        return new int[]{hour, minute};
    }

    /**
     * Converts the lDate object to a json string
     * @return the lDate object as a json string (String)
     * */
    public String toJson() {
        return "{\"year\":" + year + ",\"month\":" + month + ",\"day\":" + day + ",\"hour\":" + hour + ",\"minute\":" + minute + "}";
    }

    /**
     * Converts the lDate object to a json object
     * @return the lDate object as a json object (JSONObject)
     * @see JSONObject
     * */
    public JSONObject toJsonObject(){
        //converts the lDate object to a json object
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("year", year);
            jsonObject.put("month", month);
            jsonObject.put("day", day);
            jsonObject.put("hour", hour);
            jsonObject.put("minute", minute);
        }catch(Exception e){

        }
        return jsonObject;
    }
    /**
     * Converts the lDate object to a json object only using the month, day and year
     * @return the lDate object as a json object (JSONObject)
     * */
    public JSONObject toJSONMDY(){
        //converts the lDate object to a json object
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("month", month);
            jsonObject.put("day", day);
            jsonObject.put("year", year);
        }catch(Exception e){

        }
        return jsonObject;
    }

    /**
     * Compares the date to another date
     * @param date the date to compare to
     * @return true if the date is after the date passed in
     */
    public boolean compareTo(lDate date){
        if(date == null){
            return false;
        }

        if(date.getYear() != this.year){
            return date.getYear() > this.year;
        }
        if(date.getMonth() != this.month){
            return date.getMonth() > this.month;
        }
        if(date.getDay() != this.day){
            return date.getDay() > this.day;
        }
        if(date.getHour() != this.hour){
            return date.getHour() > this.hour;
        }
        if(date.getMinute() != this.minute){
            return date.getMinute() > this.minute;
        }
        return false;
    }

    /**
     * Converts the lDate object to a url attribute used in the url of a request
     * @return the lDate object as a url attribute (String)
     * */
    public String toUrlAttribute(){
        //converts the lDate object to a url attribute
        return "year=" + year + "&month=" + month + "&day=" + day;
    }


}
