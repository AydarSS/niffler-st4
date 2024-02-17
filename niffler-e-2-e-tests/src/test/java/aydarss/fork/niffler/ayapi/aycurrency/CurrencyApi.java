package aydarss.fork.niffler.ayapi.aycurrency;

import aydarss.fork.niffler.aymodel.CurrencyCalculateJson;
import aydarss.fork.niffler.aymodel.CurrencyJson;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CurrencyApi {

  @GET("/getAllCurrencies")
  Call<List<CurrencyJson>> getAllCurrencies();

  @POST("/calculate")
  Call<CurrencyCalculateJson> getAllCurrenciesCalculate(
      @Body CurrencyCalculateJson currencyCalculate);

}
