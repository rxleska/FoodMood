package Backend.Responses.reviewResponses;

import Backend.Responses.responseInterface;
import Backend.DateRec;

public record reviewRec(int reviewId, int userId, int FoodId, String userName, double rating, String body, DateRec date) implements responseInterface{};
