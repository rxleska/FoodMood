package com.example.mainapplication.datainterface.notinuse;

import com.example.mainapplication.datainterface.DataClasses.Review;

/**
 * @deprecated
 * The DataSender interface was used to send data to the database.
 *
 * It is not used in the final application.
 *
 * This classes purpose was replaced with the Middle Man.
 * @see com.example.mainapplication.datainterface.MiddleMan
 *
 * @author Ryan Leska
 * */
@Deprecated
public interface DataSender {
    // This interface is for sending data to the database
    // and to local storage (if necessary)

    // Review Methods //returns integer error codes
//    boolean addReview(Review review, int token); // token is the user's token
//    boolean updateReview(Review review, int token); // token is the user's token
//    boolean deleteReview(int id, int token); // token is the user's token, id is the review id
//    boolean updateReviewRating(int id, double rating, int token); // token is the user's token, id is the review id
//    boolean updateReviewBody(int id, String body, int token); // token is the user's token, id is the review id
    int addReview(Review review, int token); // token is the user's token
    int updateReview(Review review, int token); // token is the user's token
    int deleteReview(int id, int token); // token is the user's token, id is the review id
    int updateReviewRating(int id, double rating, int token); // token is the user's token, id is the review id
    int updateReviewBody(int id, String body, int token); // token is the user's token, id is the review id


    //User Methods //returns integer positive is a token and negative is an error code
//    boolean registerUser(String user, String pass);
//    boolean loginUser(String user, String pass);
//    boolean logoutUser(int token);
    int registerUser(String user, String pass);
    int loginUser(String user, String pass);
    int logoutUser(int token);
    // TODO: Add more methods as needed like update user, delete user, etc.

}
