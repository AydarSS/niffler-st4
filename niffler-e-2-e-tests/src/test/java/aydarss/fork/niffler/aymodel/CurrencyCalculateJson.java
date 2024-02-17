package aydarss.fork.niffler.aymodel;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrencyCalculateJson(
        @JsonProperty("originalCurrency")
        CurrencyValues originalCurrency,
        @JsonProperty("desiredCurrency")
        CurrencyValues desiredCurrency,
        @JsonProperty("originalAmount")
        Double originalAmount,
        @JsonProperty("desiredAmount")
        Double desiredAmount) {

}
