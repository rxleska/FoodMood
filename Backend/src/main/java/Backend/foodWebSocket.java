package Backend;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import Backend.database.Food.Food;
import Backend.database.Food.FoodRepository;

@Controller
public class foodWebSocket implements WebSocketHandler
{
 
    @Autowired
    private FoodRepository foodRepository;

    private static Set<WebSocketSession> sessionSet = new HashSet<>(); //list of all sessions connected to the websocket

    public void sendManyFoods(List<Food> foodList) throws IOException
    {
        for(Food food: foodList)
        {
            sendFood(food);
        }
    }//loops for a list of foods and send them to the sendFood one by one

    public void sendFood(Food food) throws IOException
    {
        for(WebSocketSession session: sessionSet)
        {
            session.sendMessage(new TextMessage(food.toJson()));
        } // sends each food to all the sessions connected to the websocket
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionSet.add(session);
    }//config for when a new session is connected, adds to the list of sessions

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        session.sendMessage(message);
        sendFood(foodRepository.findById(Integer.parseInt(message.getPayload().toString())));
    }//config for when a message is received, sends the food with the id of the message to all the sessions and echos the message to the sender

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.sendMessage(new TextMessage("Error: " + exception.getMessage()));
    }// config for when an error occurs, sends the error to the session

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessionSet.remove(session);
        session.close();
    }//config for when a session is closed, removes the session from the list of sessions and closes the session for assurance

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }//does not support partial messages
}
