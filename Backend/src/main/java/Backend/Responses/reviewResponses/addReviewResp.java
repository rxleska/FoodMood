package Backend.Responses.reviewResponses;

import Backend.Responses.responseInterface;

public record addReviewResp(boolean success,String status) implements responseInterface{};