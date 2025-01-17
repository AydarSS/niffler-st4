package aydarss.fork.niffler.aydb.aysjdbc;

import aydarss.fork.niffler.aydb.aymodel.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;


public class UserEntityRowMapper implements RowMapper<UserEntity> {

  public static final UserEntityRowMapper instance = new UserEntityRowMapper();

  @Override
  public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserEntity user = new UserEntity();
    user.setId(rs.getObject("id", UUID.class));
    user.setUsername(rs.getString("username"));
    user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
    user.setFirstname(rs.getString("firstname"));
    user.setSurname(rs.getString("surname"));
    user.setPhoto(rs.getBytes("photo"));
    return user;
  }
}
