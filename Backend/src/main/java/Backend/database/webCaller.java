package Backend.database;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import Backend.jsonParsingInterface;
import Backend.database.webFoodSplitters.allMenusSplitter;
import Backend.database.webFoodSplitters.allRestaurantSplitter;
import Backend.database.webHoursSplitters.hoursSplitter;
import Backend.database.webHoursSplitters.hoursOverall;

//uses spring webclient to call the iowa state dining api
@Service
public class webCaller 
{

    private final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024*1024)).build();
    private final String url = "https://www.dining.iastate.edu/wp-json/dining/menu-hours/";
    private WebClient client = WebClient.builder().exchangeStrategies(exchangeStrategies).baseUrl(url).build();
    private String uri;
    private String uri2;

    public webCaller()
    {
    }

    public List<allRestaurantSplitter> getAllRestaurants()
    {
        uri = "get-locations";
        return client.get().uri(uri).retrieve().bodyToFlux(allRestaurantSplitter.class).collectList().block();
    }

    public allMenusSplitter getOneMenu(String slug)
    {
        uri = "get-single-location/?slug=";
        return client.get().uri(uri+slug+"&time=").retrieve().bodyToFlux(allMenusSplitter.class).collectList().block().get(0);
    }

    public hoursSplitter getOneSchedule(long id)
    {
        uri = "get-weekly-hours/?ids[]=";
        uri2 = "&timestamp=";
        System.out.println(uri+id+uri2+"-----------------------------------------------"+id);
        try{

        return client.get().uri(uri+id+uri2).retrieve().bodyToFlux(hoursOverall.class).collectList().block().get(0).data.get(5);

        }
        catch(Exception e)
        {
            System.out.println("-------------------tripping flag------------------------");
            return new hoursSplitter();
        }
    }

    public jsonParsingInterface webTest(String extension,Class<? extends jsonParsingInterface> c)
    {
        return client.get().uri(extension).retrieve().bodyToMono(c).block();
    }
}
