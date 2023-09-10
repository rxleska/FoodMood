package Backend.database;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import Backend.database.Food.FoodService;
import Backend.database.Restaurant.restaurantService;
import jakarta.websocket.EncodeException;

@Service
public class scheduleUpdater {
    
    @Autowired
    private restaurantService restaurantService;

    @Autowired
    private FoodService foodService;

    @Scheduled(fixedRate = 60*15*1000)
    public void updateAll() throws IOException, EncodeException
    {
        //System.out.println("Updating all restaurants and foods");
        //System.out.println("------------------------------------------------------------------------------------------");
        restaurantService.updateAllRestaurants();
        foodService.updateAllFoods();
    }
}
