package Backend.database.webFoodSplitters;

import Backend.jsonParsingInterface;

public class allRestaurantSplitter implements jsonParsingInterface
{
    public long id;
    public double lat;
    public double lng;
    public String title;
    public String[] locationType;
    public String address;
    public String slug;

    public String toString()//returns a string of all the fields in the class with their names
    {
        return "id: "+id+"\nlat: "+lat+"\nlng: "+lng+"\ntitle: "+title+"\nlocationType: "+locationType[0]+"\naddress: "+address+"\nslug: "+slug+"\n";
    }
}
