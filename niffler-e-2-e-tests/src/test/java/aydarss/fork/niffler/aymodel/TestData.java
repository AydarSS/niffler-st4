package aydarss.fork.niffler.aymodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record TestData(
    @JsonIgnore String password,
    @JsonIgnore String friendName
) {
}
