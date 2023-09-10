package Backend.database.Restaurant;

import java.util.List;

import jakarta.persistence.Embeddable;

@Embeddable
public class hours {
    private String section;
    private String from;
    private String to;

    public hours() {
    }
    //constructor for the correct fields
    public hours(String section, String from, String to) {
        this.section = section;
        this.from = from;
        this.to = to;
    }

    //getters and setters
    public String getSection() {
        return section;
    }
    public void setSection(String section) {
        this.section = section;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
}
