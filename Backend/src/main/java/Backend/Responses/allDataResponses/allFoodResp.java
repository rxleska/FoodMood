package Backend.Responses.allDataResponses;

import java.util.List;
import Backend.database.Food.Food;
import Backend.Responses.responseInterface;

public record allFoodResp(List<Food> Foods) implements responseInterface{}
