package Backend.Responses.allDataResponses;

import Backend.Responses.responseInterface;

import java.util.List;

public record allRecommendResp(List<Integer> foods) implements responseInterface{};