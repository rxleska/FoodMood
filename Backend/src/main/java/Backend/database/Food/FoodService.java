package Backend.database.Food;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.io.IOException;
import java.util.Date;

import Backend.foodWebSocket;
import Backend.database.webCaller;
import Backend.database.Restaurant.Restaurant;
import Backend.database.Restaurant.restaurantService;
import Backend.database.webFoodSplitters.*;
import jakarta.websocket.EncodeException;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private restaurantService restaurantService;

    @Autowired
    private webCaller client;

    @Autowired
    private foodWebSocket socket;

    public List<Food> getAll()
    {
        return foodRepository.findAll();
    }

    public Food getFood(int id) {return foodRepository.findById(id);}

    public int length()
    {
        return (int)foodRepository.count();
    }

    public void add(String foodName, int Calories, List<foodType> type, String category, String subrestaurant, Restaurant restaurant)
    {
        foodRepository.save(new Food(foodName, Calories, category, subrestaurant, type, restaurant,true));
    }

    public void update(Food food){foodRepository.save(food);}

    public long addGetID(String foodName, int Calories, List<foodType> type, String category, String subrestaurant, Restaurant restaurant)
    {
        Food food = new Food(foodName, Calories, category, subrestaurant, type, restaurant,true);
        foodRepository.save(food);
        return food.getId();
    }

    /**
     * This method will update the database with all the foods from the API
     * @throws EncodeException
     * @throws IOException
     * @Param None
     * @Return None
     */
    public void updateAllFoods() throws IOException, EncodeException
    {
        makeClosed();
        for(String slug: restaurantService.getAllslugs())
        {
            allMenusSplitter splitter = client.getOneMenu(slug);
            for(foodSplitter food: splitter.getAllFoods())
            {
                if(!(foodRepository.existsByName(food.name) && foodRepository.existsBySubRestaurant(food.subRestaurant)))
                {
                    List<foodType> tempType = new LinkedList<foodType>();
                            
                    if(Integer.parseInt(food.isHalal)==1)tempType.add(foodType.HALAL);
                    if(Integer.parseInt(food.isVegetarian)==1)tempType.add(foodType.VEGETARIAN);
                    if(Integer.parseInt(food.isVegan)==1)tempType.add(foodType.VEGAN);

                    Food tempFood = new Food(food.name, Integer.parseInt(food.totalCal), food.category, food.subRestaurant,tempType, restaurantService.getRestaurantBySlug(slug),true);
                    foodRepository.save(tempFood);
                    socket.sendFood(tempFood);//sends the new food to the frontend
                }else if(foodRepository.existsByNameAndSubRestaurant(food.name, food.subRestaurant))
                {
                    Food tempFood = foodRepository.findByNameAndSubRestaurant(food.name, food.subRestaurant);
                    tempFood.setIsServed(true);
                    if(!tempFood.equals(food))//if the food is different from the one in the database update it
                    {
                        tempFood.setCalories(Integer.parseInt(food.totalCal));
                        tempFood.setCategory(food.category);
                        tempFood.setLastUpdated();
                        tempFood.setType(new LinkedList<foodType>());
                        if(Integer.parseInt(food.isHalal)==1)tempFood.getType().add(foodType.HALAL);
                        if(Integer.parseInt(food.isVegetarian)==1)tempFood.getType().add(foodType.VEGETARIAN);
                        if(Integer.parseInt(food.isVegan)==1)tempFood.getType().add(foodType.VEGAN);
                        socket.sendFood(tempFood); //send the updated food to the frontend
                    }
                    foodRepository.save(tempFood);
                }//notes: if the only different thing is the isServed then it will not update the food
            }
        }
    }

    public List<Food> getFoodsByNew(Date date)
    {
        List<Food> temp= new LinkedList<Food>();
        for(Food food: foodRepository.findAll())
        {
            if(food.getLastUpdated().after(date) || food.getLastUpdated().equals(date))
                temp.add(food);
        }
        return temp;
    }

    public List<Integer> getCurrentFoods()
    {
        List<Integer> temp = new LinkedList<Integer>();
        for(Food food: foodRepository.findAllByIsServed(true))
        {
            temp.add(Integer.valueOf((int)food.getId()));
        }
        return temp;
    }

    public List<Integer> getRecommendedFoods()
    {
        List<Integer> temp = new LinkedList<>();
        List<Integer> compare = getCurrentFoods();

        for(Integer i : compare)
        {
            if(foodRepository.findById(i.intValue()).getAvgStars() > 3.5)
            {
                temp.add(Integer.valueOf(i.intValue()));
            }
        }

        //sorts recommended food in descending order for rating
        for(int i = 0; i < temp.size(); i++)
        {
            int maxIndex = i;
            for(int j = i+1; j < temp.size(); j++)
            {
                if(temp.get(j) > temp.get(maxIndex))
                {
                    maxIndex = j;
                }
            }//there might have been an easier way to do this with jdbc but should work fine for the time being

            Integer swap = temp.get(maxIndex);
            temp.set(maxIndex, temp.get(i));
            temp.set(i, swap);
        }

        List<Integer> sorted = new LinkedList<>();
        for(int i = 0; i < 10; i++)
        {
            if(i < temp.size())
                sorted.add(temp.get(i));
        }

        return sorted;
    }

    /**
     * This method will make all of the foods in the database closed, used in before updating the database
     * @param
     * @return None
     *
     */
    private void makeClosed()
    {
        if(!foodRepository.findAll().isEmpty())
        {
            for(Food food: foodRepository.findAll())
            {
                food.setIsServed(false);
                foodRepository.save(food);
            }
        }
    }
}
