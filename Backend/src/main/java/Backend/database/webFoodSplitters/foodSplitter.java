package Backend.database.webFoodSplitters;

public class foodSplitter
{
    public String name;
    /**
     * IDFK WHY THIS IS A STRING IN THE API IT SHOULDN'T BE BUT IT IS. MAKE SURE TO PARSE IT TO AN INT BEFORE USE
     */
    public String totalCal;
    /**
     * IDFK WHY THIS IS A STRING IN THE API IT SHOULDN'T BE BUT IT IS. MAKE SURE TO PARSE IT TO AN INT BEFORE USE
     */
    public String isHalal;
    /**
     * IDFK WHY THIS IS A STRING IN THE API IT SHOULDN'T BE BUT IT IS. MAKE SURE TO PARSE IT TO AN INT BEFORE USE
     */
    public String isVegetarian;
    /**
     * IDFK WHY THIS IS A STRING IN THE API IT SHOULDN'T BE BUT IT IS. MAKE SURE TO PARSE IT TO AN INT BEFORE USE
     */
    public String isVegan;
    /**
     * WARNING - THIS IS NOT A FIELD PASSED FROM THE ISU API - THIS IS A FIELD ADDED BY CALLING GETALLFOODS() ON A CATEGORY
     */
    public String category;
    /**
     * WARNING - THIS IS NOT A FIELD PASSED FROM THE ISU API - THIS IS A FIELD ADDED BY CALLING GETALLFOODS() ON A SUBRESTAURANT
     */
    public String subRestaurant;

    public String toString()
    {
        return "Name: " + name + " Calories: " + totalCal + " Halal: " + isHalal + " Vegetarian: " + isVegetarian + " Vegan: " + isVegan + "\n";
    }
}