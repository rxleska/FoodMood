package Backend.Responses.reviewResponses;

import Backend.Responses.responseInterface;

public record deleteReviewResp(boolean success, String status) implements responseInterface{};