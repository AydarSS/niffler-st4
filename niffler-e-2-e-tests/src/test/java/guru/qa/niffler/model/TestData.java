package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import guru.qa.niffler.jupiter.annotation.UserQueue;

public record TestData(
    @JsonIgnore String password,
    @JsonIgnore String friendName,
    @JsonIgnore UserQueue.UserType userTypeQueue
) {
}
