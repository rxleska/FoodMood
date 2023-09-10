package Backend.database.Restaurant;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import Backend.database.Food.Food;
import Backend.database.webHoursSplitters.hoursSplitter;
import jakarta.persistence.*;


@Entity
public class Restaurant{

    @Id
    @Column(unique=true)
    private long id;
    private double lat;
    private double lng;
    private String name;
    private String description;
    private String type;
    private String address;
    private String slug;
    private Date lastUpdated;
    private boolean isOpen;
    @Embedded
    @JsonIgnore
    private LinkedList<hours> hours;

    @OneToMany(mappedBy = "restaurant")
    private List<Food> menu;

    //default constructor
    public Restaurant() {
    }

    //constructor for the correct fields
    public Restaurant(long id, double lat, double lng, String name, String description, String type, String address, String slug, boolean isOpen) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.description = description;
        this.type = type;
        this.address = address;
        this.slug = slug;
        this.lastUpdated = new Date(System.currentTimeMillis());
        this.isOpen = isOpen;
        menu = new LinkedList<Food>();
    }

    public Restaurant(long id, double lat, double lng, String name, String description, String type, String address, String slug, boolean isOpen, LinkedList<hours> currentHours) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.description = description;
        this.type = type;
        this.address = address;
        this.slug = slug;
        this.lastUpdated = new Date(System.currentTimeMillis());
        this.isOpen = isOpen;
        menu = new LinkedList<Food>();
        this.hours = currentHours;
    }


    //getters and setters for all the fields
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public double getlat() {
        return lat;
    }
    public void setlat(double lat) {
        this.lat = lat;
    }
    public double getlng() {
        return lng;
    }
    public void setlng(double lng) {
        this.lng = lng;
    }
    public String getName() {
        return name;
    }
    public void setTitle(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getSlug(){
        return slug;
    }
    public void setSlug(String slug){
        this.slug = slug;
    }
    public Date getLastUpdated(){
        return lastUpdated;
    }
    public void setLastUpdated(){
        this.lastUpdated = new Date(System.currentTimeMillis());
    }
    public List<Long> getMenu(){
        List<Long> tempList = new LinkedList<Long>();
        for(Food food:menu)tempList.add(food.getId());
        return tempList;
    }
    public void setMenu(List<Food> menu){
        this.menu = menu;
    }
    public boolean getIsOpen(){
        return isOpen;
    }
    public void setIsOpen(boolean isOpen){
        this.isOpen = isOpen;
    }
    public List<hours> getHours(){
        return hours;
    }
    public void setHours(LinkedList<hours> currentHours){
        this.hours = currentHours;
    }
}
