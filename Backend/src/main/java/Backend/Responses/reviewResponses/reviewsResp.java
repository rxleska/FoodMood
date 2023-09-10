package Backend.Responses.reviewResponses;

import java.util.List;
import Backend.Responses.*;
public record reviewsResp(List<reviewRec> reviews) implements responseInterface{}
