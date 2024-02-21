package aydarss.fork.niffler.aydb.aysjdbc;

import aydarss.fork.niffler.aydb.aymodel.Authority;
import aydarss.fork.niffler.aydb.aymodel.AuthorityEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class AuthorityEntityRowMapper implements RowMapper<AuthorityEntity> {

  public static final AuthorityEntityRowMapper instance = new AuthorityEntityRowMapper();

  @Override
  public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    AuthorityEntity authorityEntity = new AuthorityEntity();
    authorityEntity.setId(rs.getObject(1, UUID.class));
    authorityEntity.setAuthority(Enum.valueOf(Authority.class, rs.getString(3)));

    return authorityEntity;
  }
}