package aydarss.fork.niffler.aymodel;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FriendJson(
    @JsonProperty("username")
    String username) {

}