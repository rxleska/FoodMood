package Backend.Responses.UserResponses;

import Backend.Responses.responseInterface;

public record loginResp(boolean loggedIn,String status,int token) implements responseInterface{};