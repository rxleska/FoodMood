package Backend.Responses.UserResponses;

import java.util.Date;

import Backend.Responses.responseInterface;
import java.util.List;

public record userResp(Long id, String username, Date lastLogin, boolean banStatus,List<Integer> reviews,boolean isAdmin) implements responseInterface{
    
}
