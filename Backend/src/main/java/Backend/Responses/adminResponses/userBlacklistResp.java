package Backend.Responses.adminResponses;

import Backend.Responses.responseInterface;

public record userBlacklistResp(boolean success, String status) implements responseInterface{};