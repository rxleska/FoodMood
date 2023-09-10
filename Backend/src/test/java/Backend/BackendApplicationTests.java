package Backend;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import Backend.database.Food.FoodRepository;
import Backend.database.webCaller;
import Backend.database.Food.Food;
import Backend.database.Restaurant.restaurantService;

@WebAppConfiguration
@SpringBootTest
class BackendApplicationTests {

	// @Autowired
	// restaurantService server;

	// @Autowired
	// webCaller client;

	@Test
	void contextLoads() 
	{
		
	}

	// @Test
	// void testHoursSet()
	// {
	// 	System.out.println("----------------------------WEBCALLERTEST------------------------------");
	// 	System.out.println(client.getOneSchedule(319));
	// 	System.out.println("-----------------------------------------------------------------------");
	// }

	// @Test
	// void testHours()
	// {
	// 	System.out.println("-----------------------UPDATERTEST-------------------------------------");
	// 	server.updateAllRestaurants();
	// 	System.out.println("-----------------------------------------------------------------------");
	// }


}
