package Backend.Responses.UserResponses;

import Backend.Responses.responseInterface;

public record deleteUserResp(boolean deleted,String status) implements responseInterface{}