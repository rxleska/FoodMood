package Backend.Responses.UserResponses;

import Backend.Responses.responseInterface;

public record checkTokenResp(boolean loggedIn,String status) implements responseInterface{};