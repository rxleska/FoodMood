package HB7.basicRestful;
import java.util.ArrayList;

public record Greeting (long id,String content,ArrayList<String> users){};
