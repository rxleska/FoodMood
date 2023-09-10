package Backend;

import java.io.IOException;
import java.util.ArrayList;
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

import Backend.database.Review.Review;
import Backend.database.Review.ReviewRepository;


@Controller
public class reviewWebSocket implements WebSocketHandler
{
    @Autowired
    private ReviewRepository reviewRepository;//probably don't need this, because you are providing the reviews that are going to be sent.

    private static Set<WebSocketSession> sessionSet = new HashSet<>(); //list of all sessions connected to the websocket

    public void sendReview(Review review) throws IOException
    {
        for(WebSocketSession session: sessionSet)
        {
            List<Review> reviews = reviewRepository.findAll();//this will grab all of the reviews, which are not being used
            String msg = (review.getFood().getId() + ":" + review.getProfile().getId());
            session.sendMessage(new TextMessage(msg));
        } // sends a corresponding foodId and profileId for each review to all the sessions connected to the websocket
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionSet.add(session);
    }//config for when a new session is connected, adds to the list of sessions

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        session.sendMessage(message);
    }//config for when a message is received, echos the message to the sender

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
