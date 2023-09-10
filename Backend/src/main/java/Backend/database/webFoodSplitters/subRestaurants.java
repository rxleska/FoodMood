package Backend.database.webFoodSplitters;

import java.util.List;
import java.util.LinkedList;

public class subRestaurants
{
    public String name;
    /**
     * IDK why the ISU api has this as a float but it does
     */
    public float id;
    public List<category> categories;

    public List<foodSplitter> getAllFoods()
    {
        List<foodSplitter> allFood = new LinkedList<foodSplitter>();
        for(category category: categories)
        {
            for(foodSplitter food: category.menuItems)
            {
                food.subRestaurant = name;
            }
            allFood.addAll(category.menuItems);
        }
        return allFood;
    }

    public String toString()
    {
        return "Name: " + name + " ID: " + id + "\n" + categories;
    }
   
}