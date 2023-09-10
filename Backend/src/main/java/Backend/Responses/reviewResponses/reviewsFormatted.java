package Backend.Responses.reviewResponses;

import Backend.DateRec;
import Backend.database.Review.Review;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class reviewsFormatted {

    private List<reviewRec> reviews;

    public reviewsFormatted(List<Review> reviewList)
    {
        this.reviews = new ArrayList<>();
        for(int i = 0; i < reviewList.size(); i++)
        {
            int reviewId = reviewList.get(i).getId();
            int userId = (int) reviewList.get(i).getProfile().getId();
            int foodId = (int) reviewList.get(i).getFood().getId();
            String userName = reviewList.get(i).getProfile().getUsername();
            double rating = reviewList.get(i).getRating();
            String body = reviewList.get(i).getRvwMessage();
            ZonedDateTime zdate = reviewList.get(i).getDate();
            DateRec date = new DateRec(zdate.getMonthValue(), zdate.getDayOfMonth(), zdate.getYear());

            reviews.add(new reviewRec(reviewId, userId, foodId, userName, rating, body, date));
        }
    }

    public List<reviewRec> getReviews(int index, int size)
    {
        if(index == 0 && size == 0)
            return reviews;

        List<reviewRec> partionedList = new ArrayList<>() ;

        for(int i = index; i < index + size; i++)
        {
            partionedList.add(reviews.get(i));
        }

        return partionedList;//partitioning should be safe for 0 & 0
    }

}
