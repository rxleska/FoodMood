package Backend.Responses.UserResponses;

import Backend.Responses.responseInterface;

public record logoutResp(boolean loggedIn,String status) implements responseInterface{};