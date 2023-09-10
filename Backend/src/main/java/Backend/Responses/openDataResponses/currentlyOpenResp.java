package Backend.Responses.openDataResponses;

import Backend.Responses.responseInterface;
import Backend.database.Restaurant.Restaurant;

import java.util.List;

public record currentlyOpenResp(List<Restaurant> restaurants) implements responseInterface{};