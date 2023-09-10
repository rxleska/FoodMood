package Backend.Responses.reviewResponses;

import Backend.Responses.responseInterface;

public record editReviewResp(boolean success,String status) implements responseInterface{};