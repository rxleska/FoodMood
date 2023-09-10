package Backend.database.Profile;

import Backend.database.Review.Review;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Date;
import java.util.LinkedList;

@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    @JsonIgnore
    private String password;
    private Date lastLogin;
    private boolean banStatus;
    private boolean isAdmin;//removed email and added isAdmin

    @OneToMany(mappedBy = "profile")
    private List<Review> reviews;

    public Profile(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        reviews = new ArrayList<Review>();
        this.banStatus = false;
        this.lastLogin = new Date(System.currentTimeMillis());
        this.isAdmin = isAdmin;
    }

    public Profile() {
        reviews = new ArrayList<Review>();
    }

    public long getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setName(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password= password;
    }

    public Date getLastLogin(){
        return lastLogin;
    }
    public void setLastLogin(){
        this.lastLogin = new Date(System.currentTimeMillis());
    }
    public boolean getBanStatus(){
        return this.banStatus;
    }
    public void setBanStatus(boolean banStatus){
        this.banStatus = banStatus;
    }
    public boolean getIsAdmin(){
        return this.isAdmin;
    }
    public void setIsAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }

    public List<Review> getReviews() { return reviews;}

    public List<Integer> getReviewList(){
        List<Integer> temp = new LinkedList<Integer>();
         for(Review review:reviews){
             temp.add(review.getId());
         }
         return temp;
    }


}
