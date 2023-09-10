package Backend.database.webFoodSplitters;

import java.util.List;
import java.util.LinkedList;

public  class section
{
    public String section;
    public List<subRestaurants> menuDisplays;

    public List<foodSplitter> getAllFoods()
    {
        List<foodSplitter> allFood = new LinkedList<foodSplitter>();
        for(subRestaurants subRestaurant: menuDisplays)
        {
            allFood.addAll(subRestaurant.getAllFoods());
        }
        return allFood;
    }

    public String toString()
    {
        return "Section: " + section + "\n" + menuDisplays;
    } 
}
