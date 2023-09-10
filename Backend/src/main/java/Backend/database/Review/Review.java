package Backend.database.Review;

import Backend.DateRec;
import Backend.database.Profile.*;
import Backend.database.Food.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String rvwMessage;
    private double rating;
    private ZonedDateTime date;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    @JsonIgnore
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "food_id")
    @JsonIgnore
    private Food food;


    public Review(String rvwMessage, double rating, Profile profile, Food food) {
        this.rvwMessage = rvwMessage;
        this.rating = rating;

        date = ZonedDateTime.now();

        this.profile = profile;
        this.food = food;
    }

    public Review(){}

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public  String getRvwMessage() {return rvwMessage;}

    public void setRvwMessage (String rvwMessage) {this.rvwMessage = rvwMessage;}

    public double getRating() {return rating;}

    public void setRating(double rating) {this.rating = rating;}

    public Profile getProfile() {return profile;}

    public Food getFood() {return food;}

    public ZonedDateTime getDate() {return date;}

    public void updateDate()
    {
        date = ZonedDateTime.now();
    }

}
