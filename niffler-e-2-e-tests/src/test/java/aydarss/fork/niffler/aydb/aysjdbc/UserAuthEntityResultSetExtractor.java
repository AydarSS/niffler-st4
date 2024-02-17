package aydarss.fork.niffler.aydb.aysjdbc;

import aydarss.fork.niffler.aydb.aymodel.Authority;
import aydarss.fork.niffler.aydb.aymodel.AuthorityEntity;
import aydarss.fork.niffler.aydb.aymodel.UserAuthEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;


public class UserAuthEntityResultSetExtractor implements ResultSetExtractor<UserAuthEntity> {

  public static final UserAuthEntityResultSetExtractor instance = new UserAuthEntityResultSetExtractor();

  @Override
  public UserAuthEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
    UserAuthEntity user = new UserAuthEntity();
    boolean userProcessed = false;
    while (rs.next()) {
      if (!userProcessed) {
        user.setId(rs.getObject(1, UUID.class));
        user.setUsername(rs.getString(2));
        user.setPassword(rs.getString(3));
        user.setEnabled(rs.getBoolean(4));
        user.setAccountNonExpired(rs.getBoolean(5));
        user.setAccountNonLocked(rs.getBoolean(6));
        user.setCredentialsNonExpired(rs.getBoolean(7));
        userProcessed = true;
      }

      AuthorityEntity authority = new AuthorityEntity();
      authority.setId(rs.getObject(8, UUID.class));
      authority.setAuthority(
          Objects.isNull(rs.getString(10)) ? null : Authority.valueOf(rs.getString(10)));
      user.getAuthorities().add(authority);
    }
    return user;
  }
}
