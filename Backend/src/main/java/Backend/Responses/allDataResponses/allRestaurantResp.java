package Backend.Responses.allDataResponses;

import Backend.Responses.responseInterface;
import Backend.database.Restaurant.Restaurant;
import java.util.List;

public record allRestaurantResp(List<Restaurant> Restaurants) implements responseInterface{}
