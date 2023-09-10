package Backend.database.Review;

import java.io.IOException;
import java.util.List;

import Backend.foodWebSocket;
import Backend.database.Food.Food;
import Backend.database.Food.foodType;
import Backend.database.Profile.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Backend.database.Review.*;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private foodWebSocket foodSocket;

    public List getAll()
    {
        return reviewRepository.findAll();
    }

    public Review getReview(int id) {return reviewRepository.findById(id);}

    public Review getReviewByRelations(Food f, Profile p)
    {
        List<Review> l = p.getReviews();

        for(int i = 0; i < l.size(); i++)
        {
            if(l.get(i).getFood().getId() == f.getId())
            {
                return l.get(i);
            }
        }
        return null;
    }

    public int length()
    {
        return (int)reviewRepository.count();
    }

    public Review add(String rvwMessage, double rating, Profile profile, Food food)
    {
        Review r = new Review(rvwMessage, rating, profile, food);
        reviewRepository.save(r);
        return r;
    }

    public Review update(Review review)
    {
        reviewRepository.save(review);
        return review;
    }


    public int addGetID(String rvwMessage, double rating, Profile profile, Food food)
    {
        Review review= new Review(rvwMessage, rating, profile, food);
        reviewRepository.save(new Review(rvwMessage, rating, profile, food));
        return review.getId();
    }

    public boolean deleteReview(int id)
    {
        if(reviewRepository.existsById(id))
        {
            reviewRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
