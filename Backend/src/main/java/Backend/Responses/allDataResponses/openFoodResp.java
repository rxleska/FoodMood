package Backend.Responses.allDataResponses;

import Backend.Responses.responseInterface;
import java.util.List;

public record openFoodResp(List<Integer> Foods) implements responseInterface{
    
}
