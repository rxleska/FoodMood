package HB7.helloWorld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication
public class HelloWorldApplication {

	@RequestMapping("/")
	String home() {
		return "hello World!";
	}
	public static void main(String[] args) {
		SpringApplication.run(HelloWorldApplication.class, args);
	}

}
