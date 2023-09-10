package Backend;

import Backend.Responses.openDataResponses.currentlyOpenResp;
import Backend.Responses.responseInterface;
import Backend.Responses.UserResponses.*;
import Backend.Responses.adminResponses.userBlacklistResp;
import Backend.database.scheduleUpdater;
import Backend.database.Food.*;
import Backend.database.Review.*;
import Backend.Responses.reviewResponses.*;
import Backend.Responses.allDataResponses.*;
import Backend.jsonClasses.adminJson.userBanJson;
import Backend.jsonClasses.reviewJson.*;
import Backend.jsonClasses.userJson.*;
import Backend.database.Profile.*;
import Backend.database.Restaurant.restaurantService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableScheduling
@RestController
@SpringBootApplication
@ComponentScan
public class BackendApplication {

	@Autowired
	private loginManager log;

	@Autowired
	private FoodService foodService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private profileService profileService;

	@Autowired
	private restaurantService restaurantService;

	@Autowired
	private scheduleUpdater updater;

	@Autowired
	private foodWebSocket foodSocket;

	@Autowired
	private reviewWebSocket reviewSocket;

	@Operation(summary = "logs in a user passed on username and password and returns a token")
	@ApiResponse(description = "returns a token if login is successful, otherwise returns an error message -1 = wrong password, -2 = username doesn't match any in database, -3 = already logged in")
	@PutMapping("/login")//accepts http request containing username and password and attempts to log in
	public loginResp login(@RequestBody loginJson json)
	{
		int tempToken=log.login(json.username,json.password);
		switch(tempToken)
		{
			case -1: return new loginResp(false,"wrong password", -1);
			case -2: return new loginResp(false, "username doesn't match any in database", -2);
			case -3: return new loginResp(true, "already logged in", -3);
			default: return new loginResp(true, "none", tempToken);
		}
	}

	@Operation(summary = "registers a new user with username and password and returns a token")
	@ApiResponse(description = "returns a token if registration is successful, otherwise returns an error message -1 = username already taken")
	@PostMapping("/registration")//attempts a new registration with username and password
	public loginResp registration(@RequestBody registerJson json)
	{
		int tempToken=log.register(json.username,json.password,json.isAdmin);//attempts a login + add token 1680014730
		switch(tempToken)
		{
			case -1: return new loginResp(false,"invalid username", -1);
			default: return new loginResp(true, "none", tempToken);
		}
	}

	@Operation(summary = "logs out a user given a token")
	@ApiResponse(description = "returns a success status if logout is successful, otherwise returns an error message -1 = not logged in to begin with")
	@PutMapping("/logout")
	public logoutResp logout(@RequestBody singleTokenJson json)
	{
		if(log.checkToken(json.token)==null)//checks token
		{
			return new logoutResp(false,"not logged in to begin with");
		}
		log.clearToken(json.token);//clears token from currently logged in list
		return new logoutResp(false,"logged out");
	}

	@Operation(summary = "deletes a user with a matching username, password, and logged in token")
	@ApiResponse(description = "returns a success status if deletion is successful, otherwise returns an error message -1 = not logged in, -2 = username doesn't match token, -3 = wrong password")
	@DeleteMapping("/deleteUser")//deletes a user from the database
	public deleteUserResp deleteUser(@RequestParam String username, @RequestParam String password, @RequestParam int token)
	{
		int temp = log.deleteUser(username,password,token);
		switch(temp)
		{
			case 0: return new deleteUserResp(true,"deleted");
			case -1: return new deleteUserResp(false,"Not logged in");
			case -2: return new deleteUserResp(false,"username doesn't match token");
			case -3: return new deleteUserResp(false,"wrong password");
		}
		return new deleteUserResp(false,"Invalid request/Parsing error");
	}

	@Operation(summary = "accepts in a token and checks if it is logged in or not")
	@ApiResponse(description = "returns if a token is logged in or not")
	@GetMapping("/status")//returns if a token is logged in or not
	public checkTokenResp status(@RequestParam int token)
	{
		if(log.checkToken(token)==null)
		{
			return new checkTokenResp(false,"not logged in");
		}
		return new checkTokenResp(true,"logged in");
	}

	/**
	 * Get function for a single review. Takes a review id and gives a review.
	 * @param reviewId
	 * @return returns a formatted review.
	 */
	@Operation(summary = "Gets a single review given a review Id.")
	@ApiResponse(description = "Returns a reviewsResp containing a single formatted Review that matches front end specs.")
	@GetMapping("/review")//returns a review given the id
	public responseInterface reviews(@RequestParam int reviewId)
	{
		try {
			Review r = reviewService.getReview(reviewId);
			List<Review> l = new ArrayList<>();
			l.add(r);
			return new reviewsResp(new reviewsFormatted(l).getReviews(0,1));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}


	@Operation(summary = "Stores a review in the database from a specific user and for a specific food")
	@ApiResponse(description = "Takes request body and stores review data, then updates the food's average rating. Returns success status.")
	@PostMapping ("/review")
	public addReviewResp createReview(@RequestBody postReviewJson json) {

		try{//probably was a good idea to move the catch to the top to get all of the errors
			String user = log.checkToken(json.token);
			Profile p = profileService.getProfile(user);
			if(p.getBanStatus())return new addReviewResp(false, "User is banned from making reviews"); //adds ban check to prevent banned users from making reviews, not needed in modify because banned user has no reviews
			Food f = foodService.getFood(json.food);
			Review r = reviewService.add(json.body, json.score, p, f);

			f = r.getFood();
			f.calculateAvg();
			foodService.update(f);

			foodSocket.sendFood(f);
			reviewSocket.sendReview(r);

			return new addReviewResp(true, "review added");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new addReviewResp(false, "review failed to be resent");
	}

	@Operation(summary = "Gets all of the restaurants and food in the database")
	@ApiResponse(description = "Returns all restaurant and food objects in a json list")
	@GetMapping("/data")//returns the data in database for all restaurants afteer a certain date or all if body is left blank
	public allDataResp allData()
	{
		return new allDataResp(restaurantService.getAllRestaurants(), foodService.getAll());
	}

	@Operation(summary = "Gets all of the restaurants and food in the database after a certain date")
	@ApiResponse(description = "Returns all restaurant and food objects in a json list after a certain date")
	@GetMapping("/data/new")//returns the data in database for all restaurants and foods after a certain date or all if body is left blank
	public allDataResp allNewData(@RequestParam int year, @RequestParam int month, @RequestParam int day)
	{
		Date temp = new Date(year-1900,month-1,day);
		return new allDataResp(restaurantService.getRestaurantsByNew(temp), foodService.getFoodsByNew(temp));
	}

	@Operation(summary = "Gets all of the foods in the database")
	@ApiResponse(description = "Returns all food objects in a json list")
	@GetMapping("/foods")//returns the data in database for all foods
	public allFoodResp allFoodData()
	{
		return new allFoodResp(foodService.getAll());
	}

	@Operation(summary = "Gets all of the foods in the database after a certain date")
	@ApiResponse(description = "Returns all food objects in a json list after a certain date")
	@GetMapping("/foods/new")//returns the data in database for all foods after a certain date
	public allFoodResp allNewFoods(@RequestParam int year, @RequestParam int month, @RequestParam int day)
	{
		Date temp = new Date(year-1900,month-1,day);
		return new allFoodResp(foodService.getFoodsByNew(temp));
	}

	@Operation(summary = "Gets all of the restaurants in the database")
	@ApiResponse(description = "Returns all restaurant objects in a json list")
	@GetMapping("/restaurants")//returns all foods
	public allRestaurantResp allRestaurantData()
	{
		return new allRestaurantResp(restaurantService.getAllRestaurants());
	}

	@Operation(summary = "Gets all of the restaurants in the database after a certain date")
	@ApiResponse(description = "Returns all restaurant objects in a json list after a certain date")
	@GetMapping("/restaurants/new")//returns the data in database for all restaurants after a certain date
	public allRestaurantResp allNewRestaurants(@RequestParam int year, @RequestParam int month, @RequestParam int day)
	{
		Date temp = new Date(year-1900,month-1,day);
		return new allRestaurantResp(restaurantService.getRestaurantsByNew(temp));
	}

	@Operation(summary = "Gets a list of foods that are available in current restaraunts")
	@ApiResponse(description = "Finds foods that are available by calling the isu dining api")
	@GetMapping("/cfoods")//returns a current list of served food
	public openFoodResp currentFoods()
	{
		return new openFoodResp(foodService.getCurrentFoods());
	}

	@Operation(summary = "Gets the profile of a user that is asscoiated with a token")
	@ApiResponse(description = "Takes a token and returns a profile, returns logged in false if the token is not valid")
	@GetMapping("/status/info")//returns the current status of the user who is logged in
	public responseInterface statusInfo(@RequestParam int token)
	{
		String user = log.checkToken(token);
		if(user==null)
		{
			return new checkTokenResp(false,"not logged in");
		}
		Profile temp=profileService.getProfile(user);
		return new userResp(temp.getId(),temp.getUsername(),temp.getLastLogin(),temp.getBanStatus(),temp.getReviewList(),temp.getIsAdmin());


	}

	@Operation(summary = "Returns a list of reviews for a given food")
	@ApiResponse(description = "Takes a food id and returns a formatted list of reviews that matches the frontend specs. The list can be partitioned into segments as needed.")
	@GetMapping("/review/food")//returns all reviews for a specific food
	public responseInterface reviewFoodId(@RequestParam int food, @RequestParam int index, @RequestParam int size)//allow for a partition of the search results that is optional in the json
	{
		try {
			Food f = foodService.getFood(food);
			return new reviewsResp(new reviewsFormatted(f.getReviews()).getReviews(index,size));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Operation(summary = "Returns a list of reviews for a given user")
	@ApiResponse(description = "Takes a username and returns a formatted list of reviews that matches the frontend specs. The list can be partitioned into segments as needed.")
	@GetMapping("/review/user")//returns all reviews from a specific user
	public responseInterface reviewUserId(@RequestParam int userId, @RequestParam int index, @RequestParam int size)//allow for a partition of the search results that is optional in the json
	{
		try {
			Profile p = profileService.getProfileId(userId);
			return new reviewsResp(new reviewsFormatted(p.getReviews()).getReviews(index, size));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * modifies a review that already exists in the database
	 * @param body json array (see the postReviewJson class)
	 * @return returns a response interface record with update status
	 */
	@Operation(summary = "Modifies a review that already exists in the database.")
	@ApiResponse(description = "Returns a response interface record with update status.")
	@PutMapping("/review")
	public responseInterface editReview(@RequestBody String body)
	{
		try {
			postReviewJson json = new ObjectMapper().readValue(body, postReviewJson.class);
			String user = log.checkToken(json.token);
			Profile p = profileService.getProfile(user);
			Food f = foodService.getFood(json.food);
			Review r = reviewService.getReviewByRelations(f, p);
			r.setRvwMessage(json.body);
			r.setRating(json.score);
			r.updateDate();
			r = reviewService.update(r);
			f = r.getFood();
			f.calculateAvg();
			foodService.update(f);

			foodSocket.sendFood(f);
			reviewSocket.sendReview(r);//sends of updates to all sockets that need it

			return new editReviewResp(true, "review modified");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new editReviewResp(false, "failed to modify review");

	}

	@Operation(summary = "Removes a review from the database")
	@ApiResponse(description = "Takes a review id, deletes the review, then returns the status of the deletion.")
	@DeleteMapping("/review")//deletes a review from the database
	public responseInterface deleteReview(@RequestParam int reviewId)
	{
		if(reviewService.deleteReview(reviewId))
			return new deleteReviewResp(true, "review deleted");
		return new deleteReviewResp(false, "review failed to delete");
	}

	@Operation(summary = "Returns a list of recommended food")
	@ApiResponse(description = "Finds foods that are high rated and currently available.")
	@GetMapping("/recon")//returns the top rated foods at a restaurant that is currently open
	public responseInterface recon()
	{
		return new allRecommendResp(foodService.getRecommendedFoods());
	}

	@Operation(summary = "Gets a list of the currently open restaurants")
	@ApiResponse(description = "Returns a list of Restaurants.")
	@GetMapping("/restaurants/open")//returns the currently open restaurants
	public responseInterface getOpenRestaurants()
	{
		return new currentlyOpenResp(restaurantService.getOpen());
	}



	@Operation(summary = "Deletes a review from the database provided a user is an admin")
	@ApiResponse(description = "Returns if the review was deleted or not and a status string")
	@DeleteMapping("/review/admin")
	public deleteReviewResp deleteReviewAdmin(@RequestParam int reviewId, @RequestParam int token)
	{
		switch(log.checkAdmin(token))//checks if the user is an admin
		{
			case 1: if(reviewService.deleteReview(reviewId))
						return new deleteReviewResp(true, "review deleted");
					return new deleteReviewResp(false, "review failed to delete");
			case -2: return new deleteReviewResp(false, "Not logged in");
			case -3: return new deleteReviewResp(false, "User is not an admin");
		}
		return new deleteReviewResp(false, "Unknown error");
	}

	@Operation(summary = "Bans a user from the app provided a user is an admin")
	@ApiResponse(description = "Returns if the user was banned or not and a status string")
	@PutMapping("/ban")
	public userBlacklistResp banUser(@RequestBody userBanJson json)
	{
		switch(log.checkAdmin(json.token))//makes sure logged in user is an admin before letting them ban users
		{
			case 1: switch(profileService.banUser(json.userId))
					{
						case 1: return new userBlacklistResp(true, "user banned");
						case -1: return new userBlacklistResp(false, "user is not in the database");
						case -2: return new userBlacklistResp(false, "user is an admin and can't be banned");
					}
			case -2: return new userBlacklistResp(false, "Not logged in");
			case -3: return new userBlacklistResp(false, "User is not an admin");
		}

		return new userBlacklistResp(false, null);
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
