package aydarss.fork.niffler.ayapi.ayspend;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SpendApi {

  @POST("/addSpend")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @GET("/spends")
  Call<List<SpendJson>> getSpends(@Query("username") String username,
      @Query("filterCurrency") CurrencyValues filterCurrency,
      @Query("from") Date from,
      @Query("to") Date to);

  @GET("/statistic")
  Call<List<StatisticJson>> getStatistic(@Query("username") String username,
      @Query("filterCurrency") CurrencyValues filterCurrency,
      @Query("from") Date from,
      @Query("to") Date to);


  @PATCH("/editSpend")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @DELETE("/deleteSpends")
  void deleteSpends(@Query("username") String username,
      @Query("ids") List<String> ids);

}
