package com.example.mainapplication.datainterface.mockClasses;

import com.example.mainapplication.datainterface.DataClasses.Review;
import com.example.mainapplication.datainterface.notinuse.DataSender;

/**
 * @deprecated
 * This class is a mock class for the DataSender interface. It is used for testing purposes.
 * It is not used in the final application.
 *
 * This classes purpose was replaced with the Middle Man.
 * @see com.example.mainapplication.datainterface.MiddleMan
 *
 * @author Ryan Leska
 *
 */

@Deprecated
public class MockDataSender implements DataSender {
    public MockDataSender(){
        // Constructor
    }

    // REVIEW METHODS
    @Override
    public int addReview(Review review, int token) {
        return 0;
    }

    @Override
    public int updateReview(Review review, int token) {
        return 0;
    }

    @Override
    public int deleteReview(int id, int token) {
        return 0;
    }

    @Override
    public int updateReviewRating(int id, double rating, int token) {
        return 0;
    }

    @Override
    public int updateReviewBody(int id, String body, int token) {
        return 0;
    }


    //USER METHODS
    @Override
    public int registerUser(String user, String pass) {
        return 0;
    }

    @Override
    public int loginUser(String user, String pass) {
        return 0;
    }

    @Override
    public int logoutUser(int token) {
        return 0;
    }
}
