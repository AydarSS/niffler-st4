package aydarss.fork.niffler.ayjupiter.aydto;

import guru.qa.niffler.model.CurrencyValues;
import java.util.Date;
import java.util.UUID;

public record SpendDto(
    UUID id,
    String username,
    CurrencyValues currency,
    Date spendDate,
    Double amount,
    String description,
    CategoryDto category
) {

}
