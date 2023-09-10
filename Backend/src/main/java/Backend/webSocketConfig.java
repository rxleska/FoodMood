package Backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import Backend.database.Food.FoodRepository;

@Configuration
@EnableWebSocket
public class webSocketConfig implements WebSocketConfigurer
{

    @Autowired
    private foodWebSocket foodWebSocket;
    //allows for autowiring inside of the foodwebsocket class

    @Autowired
    private reviewWebSocket reviewWebSocket;
    //allows for autowiring inside of the reviewWebsocket class

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        registry.addHandler(foodWebSocket, "/socket/food")
        .addInterceptors(new HttpSessionHandshakeInterceptor());
        //adds the foodwebsocket to the list of valid endpoints

        registry.addHandler(reviewWebSocket, "/socket/review")
        .addInterceptors(new HttpSessionHandshakeInterceptor());
        //adds the reviewwebsocket to the list of valid endpoints
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
        //sets the max buffer size for the websocket
    }

    @Bean
    public WebSocketHandler foodWebSocketHandler() {
        return foodWebSocket;
    }//sets up a valid bean for the foodwebsocket

    @Bean
    public WebSocketHandler reviewWebSocketHandler() {
        return reviewWebSocket;
    }//sets up a valid bean for the reviewwebsocket
}
