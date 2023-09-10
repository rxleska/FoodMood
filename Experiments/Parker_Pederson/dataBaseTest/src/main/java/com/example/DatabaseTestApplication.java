package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@RestController
@SpringBootApplication
public class DatabaseTestApplication {

	private int counter = 0;

	@Autowired
	private profileRepository ProfileRepository;
	
	@GetMapping("/use")
	public Greeting greeting(@RequestParam(value = "name") String name) {
		ProfileRepository.save(new profile(name,"woot"));
		return new Greeting(counter, ProfileRepository.findAll().getData().toString());
	}

	@PutMapping("/use")
	public Input input(@RequestParam(value = "name") String name,@RequestBody String body){
		
		return new Input(counter);
	}

	@DeleteMapping("/use")
	public Delete delete(@RequestParam(value = "name") String name,@RequestBody String body)
	{
		return new Delete(counter);
	}

	public static void main(String[] args) {
		

		//insert into user values("parker","woot");
		//system.out.println(userRepository.findAll());
		SpringApplication.run(DatabaseTestApplication.class, args);
	}

}