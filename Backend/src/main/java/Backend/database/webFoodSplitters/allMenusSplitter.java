package Backend.database.webFoodSplitters;

import Backend.jsonParsingInterface;

import java.util.LinkedList;
import java.util.List;

public class allMenusSplitter implements jsonParsingInterface
{
    public List<section> menus;

    public allMenusSplitter(){}


    /**
     * This method is used to get all the food from all the menus
     * also fills in the subRestaurant and category fields
     * cascades down from allMenusSplitter -> section -> subRestaurants -> category
     * @return a list of all the food in all the menus
     * @param none
     */
    public List<foodSplitter> getAllFoods()
    {
        List<foodSplitter> allFood = new LinkedList<foodSplitter>();
        for(section section: menus)
        {
            allFood.addAll(section.getAllFoods());
        }
        return allFood;
    }
    
    public String toString()
    {
        return menus.toString();
    }  
}