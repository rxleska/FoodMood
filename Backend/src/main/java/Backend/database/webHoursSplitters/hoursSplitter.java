package Backend.database.webHoursSplitters;

import java.sql.Date;
import java.util.Map;

public class hoursSplitter {
    public int index;
    public String label;
    public Date date;
    public Map<String,mealSplitter> hours;

    public String toString()
    {
        return "index: " + index + " label: " + label + " date: " + date + " hours: " + hours;
    }


    public hoursSplitter() {
    }
}
