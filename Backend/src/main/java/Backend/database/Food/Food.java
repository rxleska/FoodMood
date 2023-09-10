package Backend.database.Food;

import Backend.database.Restaurant.Restaurant;
import Backend.database.Review.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;


@Entity
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double avgStars;
    private int numReviews;
    private String name;
    private int calories;
    private String category;
    private String subRestaurant;
    private List<foodType> type;
    private boolean isServed;
    private Date lastUpdated;

    @OneToMany(mappedBy = "food")
    private List<Review> reviews;

    @ManyToOne
    @JoinColumn(name = "restaurant_id",nullable = true)
    private Restaurant restaurant;

    
    public Food(String name, int calories, String category, String subRestaurant, List<foodType> type, List<Review> reviews, Restaurant restaurant,boolean isServed) {//idk why this would be used but it is here
        this.avgStars = 0;
        this.numReviews = reviews.size();
        this.name = name;
        this.calories = calories;
        this.category = category;
        this.subRestaurant = subRestaurant;
        this.type = type;
        this.reviews = reviews;
        this.restaurant = restaurant;
        this.isServed = isServed;
        this.lastUpdated = new Date(System.currentTimeMillis());
    }

    public Food(String name, int calories, String category, String subRestaurant, List<foodType> type, Restaurant restaurant,boolean isServed) {//full constructor wihtout id or reviews
        this.avgStars = 0;
        this.numReviews = 0;
        this.name = name;
        this.calories = calories;
        this.category = category;
        this.subRestaurant = subRestaurant;
        this.type = type;
        this.reviews = new ArrayList<Review>();
        this.restaurant = restaurant;
        this.isServed = isServed;
        this.lastUpdated = new Date(System.currentTimeMillis());
    }

    public Food(String name,boolean isServed,Date date) {//THIS IS A TEST CONSTRUCTOR TO TEST DATE FUNCTIONS
        this.avgStars = 0;
        this.numReviews = 0;
        this.name = name;
        this.calories = 0;
        this.category = "entree";
        this.subRestaurant = "none";
        this.type = new ArrayList<foodType>();
        this.reviews = new ArrayList<Review>();
        this.restaurant = null;
        this.isServed = isServed;
        this.lastUpdated = date;
    }

    public Food()//empty constructor makes empty Reviews
    {
        this.reviews = new ArrayList<Review>();
    }

    public void calculateAvg() {
        this.numReviews = reviews.size();
        double sum = 0;
        for(Review temp : reviews)
        {
            sum+=temp.getRating();
        }
        this.avgStars = sum/numReviews;
    }

    public boolean equals(Food food)
    {
        return (this.name.equals(food.getName()) && this.calories == food.getCalories() && this.category.equals(food.getCategory()) && this.subRestaurant.equals(food.getSubrestaurant()) && this.type.equals(food.getType()) && this.getRestaurant()==food.getRestaurant() && this.isServed == food.getIsServed());
    }

    public String toJson()
    {
        String json = "{\"id\":"+this.id+",\"avgStars\":"+this.avgStars+",\"numReviews\":"+this.numReviews+",\"name\":\""+this.name+"\",\"calories\":"+this.calories+",\"category\":\""+this.category+"\",\"subRestaurant\":\""+this.subRestaurant+"\",\"type\":[";
        for(int i = 0; i < this.type.size(); i++)
        {
            json+="\""+this.type.get(i)+"\"";
            if(i!=this.type.size()-1)
            {
                json+=",";
            }
        }
        json+="],\"isServed\":"+this.isServed+",\"lastUpdated\":\""+this.lastUpdated.toString()+"\"}";
        return json;
    }

    //getters and setters for all the fields no setter for the id
    public long getId() {
        return id;
    }
    public double getAvgStars() {
        return avgStars;
    }
    public void setAvgStars(double avgStars) {
        this.avgStars = avgStars;
    }
    public int getNumReviews() {
        return numReviews;
    }
    public void setNumReviews(int numReviews) {
        this.numReviews = numReviews;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCalories() {
        return calories;
    }
    public void setCalories(int calories) {
        this.calories = calories;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getSubrestaurant() {
        return subRestaurant;
    }
    public void setSubrestaurant(String subRestaurant) {
        this.subRestaurant = subRestaurant;
    }
    public List<foodType> getType() {
        return type;
    }
    public void setType(List<foodType> type) {
        this.type = type;
    }
    public List<Review> getReviews() {
        return reviews;
    }
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    public long getRestaurant() {
        return restaurant.getId();
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    public boolean getIsServed() {
        return isServed;
    }
    public void setIsServed(boolean isServed) {
        this.isServed = isServed;
    }
    public Date getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated() {
        this.lastUpdated = new Date(System.currentTimeMillis());
    }
}