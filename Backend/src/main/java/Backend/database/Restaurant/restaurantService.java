package Backend.database.Restaurant;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Backend.database.webCaller;
import Backend.database.webFoodSplitters.allRestaurantSplitter;
import Backend.database.webHoursSplitters.hoursOverall;
import Backend.database.webHoursSplitters.hoursSplitter;
import Backend.database.webHoursSplitters.mealSplitter;

@Service
public class restaurantService 
{
    @Autowired
    private webCaller client;

    @Autowired
    private RestaurantRepository restaurants;

    public restaurantService()
    {
        //updateAllRestaurants();
    }

    /**
     * This method will update the database with all the Rstaurants from the API
     * @param
     * @return None
     */
    public void updateAllRestaurants()
    {
        makeClosed();
        List<allRestaurantSplitter> allRestaurants = client.getAllRestaurants();
        for(allRestaurantSplitter individualSplitter: allRestaurants)
        {
            LinkedList<hours> hours = hoursFormater(client.getOneSchedule(individualSplitter.id));
            if(!restaurants.existsById(individualSplitter.id))
                restaurants.save(new Restaurant(individualSplitter.id, individualSplitter.lat, individualSplitter.lng, individualSplitter.title,null, individualSplitter.locationType[0], individualSplitter.address, individualSplitter.slug,true,hours));
            else if(!restaurants.findById(individualSplitter.id).getIsOpen())
            {
                Restaurant temp = restaurants.findById(individualSplitter.id);
                temp.setIsOpen(true);
                temp.setHours(hours);
            }
        }
    }

    public List<Restaurant> getAllRestaurants()
    {
        return restaurants.findAll();
    }

    /**
     * This method will return a list of all the slugs of the restaurants
     * slugs are the unique url parameters for each restaurant
     * slugs are guarenteed to be unique so they can be used for identification even if not tagged as an id
     * @param
     * @return List of all slugs
     */
    public List<String> getAllslugs()
    {
        List<String> slugs = new LinkedList<String>();
        for (Restaurant restaurant : getAllRestaurants()) {
            slugs.add(restaurant.getSlug());
        }
        return slugs;
    }

    public Restaurant getRestaurantBySlug(String slug)
    {
        return restaurants.findBySlug(slug);
    }

    public List<Restaurant> getRestaurantsByNew(Date date)
    {
        List<Restaurant> temp = new ArrayList<Restaurant>();
        for(Restaurant restaurant: restaurants.findAll())
        {
            if(restaurant.getLastUpdated().after(date) || restaurant.getLastUpdated().equals(date))
                temp.add(restaurant);
        }
        return temp;
    }

    private LinkedList<hours> hoursFormater(hoursSplitter splitter)
    {
        LinkedList<hours> hours = new LinkedList<hours>();
        if(splitter.hours != null)
        {
            for(String keyString: splitter.hours.keySet())
            {
                mealSplitter tempValue = splitter.hours.get(keyString);
                hours.add(new hours(keyString,tempValue.start_time,tempValue.end_time));
            }
        }else{hours.add(new hours());}
        return hours;
    }

    private void makeClosed()
    {
        if(!restaurants.findAll().isEmpty())
        {
            for(Restaurant restaurant: restaurants.findAll())
            {
                restaurant.setIsOpen(false);
            }
        }
    }

    public List<Restaurant> getOpen()
    {
        List<Restaurant> restaurantsOpen = new LinkedList<>();
        List<Restaurant> allRestaurants = restaurants.findAll();
        for(Restaurant restaurant : allRestaurants)
        {
            if(restaurant.getIsOpen() == true)
            {
                restaurantsOpen.add(restaurant);
            }
        }
        return restaurantsOpen;
    }

}
