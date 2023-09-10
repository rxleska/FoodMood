package Backend.database.webFoodSplitters;

import java.util.List;

public class category
{
    public String category;
    public List<foodSplitter> menuItems;

    public String toString()
    {
        return "Category: " + category + "\n" + menuItems;
    }  
    
    public List<foodSplitter> getAllFoods()
    {
        for(foodSplitter food: menuItems)
        {
            food.category = category;
        }
        return menuItems;
    }
}
