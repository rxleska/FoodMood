package Backend.Responses.allDataResponses;

import java.util.List;
import Backend.database.Restaurant.Restaurant;
import Backend.database.Food.Food;

import Backend.Responses.responseInterface;

public record allDataResp(List<Restaurant> Restarants,List<Food> Foods) implements responseInterface{};