package HB7.basicRestful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@SpringBootApplication
public class BasicRestfulApplication {

	private static final String template = "Hello, %s!";
	private int counter = 0;
	private ArrayList<String> names = new ArrayList();
	private HashMap<Integer,String> data = new HashMap();
	
	@GetMapping("/users")
	public Greeting greeting(@RequestParam(value = "name") String name) {
		addName(name);

		return new Greeting(counter, String.format(template, name), names);
	}

	@GetMapping("/use")
	public Output output(@RequestParam(value = "name") String name) {
		addName(name);

		return new Output(counter,data);
	}

	@PutMapping("/use")
	public Input input(@RequestParam(value = "name") String name,@RequestBody String body){
		boolean valid = addName(name);
		try
		{
			if(valid)
			{
				jsonParser json = new ObjectMapper().readValue(body,jsonParser.class);
				//System.out.println(json);
				data.putIfAbsent(json.id, json.data);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new Input(counter,valid);
	}

	@DeleteMapping("/use")
	public Delete delete(@RequestParam(value = "name") String name,@RequestBody String body)
	{
		boolean valid = addName(name);
		try
		{
			
			if(valid)
			{
				//jsonParser json = new ObjectMapper().readerFor(jsonParser.class).readValue(body);
				valid = data.containsKey(Integer.parseInt(body));
				if(valid)
				{
					data.remove(Integer.parseInt(body)); 
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new Delete(counter, valid);
	}

	private boolean addName(String name)
	{
		boolean temp = !name.equalsIgnoreCase("");
		if(temp)
		{
		names.add(name);
		counter++;
		}
		return temp;
	}

	public static void main(String[] args) {
		SpringApplication.run(BasicRestfulApplication.class, args);
	}

	
}

