package aydarss.fork.niffler.aydb.ayrepository;

import aydarss.fork.niffler.aydb.DataSourceProvider;
import aydarss.fork.niffler.aydb.Database;
import aydarss.fork.niffler.aydb.aymodel.Authority;
import aydarss.fork.niffler.aydb.aymodel.AuthorityEntity;
import aydarss.fork.niffler.aydb.aymodel.UserAuthEntity;
import aydarss.fork.niffler.aydb.aymodel.UserEntity;
import aydarss.fork.niffler.aydb.aysjdbc.AuthorityEntityRowMapper;
import aydarss.fork.niffler.aydb.aysjdbc.UserAuthEntityResultSetExtractor;
import aydarss.fork.niffler.aydb.aysjdbc.UserEntityRowMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

public class UserRepositorySJdbc implements UserRepository {

  private final TransactionTemplate authTxt;
  private final TransactionTemplate udTxt;
  private final JdbcTemplate authTemplate;
  private final JdbcTemplate udTemplate;

  private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public UserRepositorySJdbc() {
    JdbcTransactionManager authTm = new JdbcTransactionManager(
        DataSourceProvider.INSTANCE.dataSource(Database.AUTH)
    );
    JdbcTransactionManager udTm = new JdbcTransactionManager(
        DataSourceProvider.INSTANCE.dataSource(Database.USERDATA)
    );

    this.authTxt = new TransactionTemplate(authTm);
    this.udTxt = new TransactionTemplate(udTm);
    this.authTemplate = new JdbcTemplate(authTm.getDataSource());
    this.udTemplate = new JdbcTemplate(udTm.getDataSource());
  }

  @Override
  public UserAuthEntity createInAuth(UserAuthEntity user) {
    KeyHolder kh = new GeneratedKeyHolder();
    return authTxt.execute(status -> {
      authTemplate.update(con -> {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO \"user\" " +
                "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) "
                +
                "VALUES (?, ?, ?, ?, ?, ?)",
            PreparedStatement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, user.getUsername());
        ps.setString(2, pe.encode(user.getPassword()));
        ps.setBoolean(3, user.getEnabled());
        ps.setBoolean(4, user.getAccountNonExpired());
        ps.setBoolean(5, user.getAccountNonLocked());
        ps.setBoolean(6, user.getCredentialsNonExpired());
        return ps;
      }, kh);

      user.setId((UUID) kh.getKeys().get("id"));

      authTemplate.batchUpdate("INSERT INTO \"authority\" " +
          "(user_id, authority) " +
          "VALUES (?, ?)", new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
          ps.setObject(1, user.getId());
          ps.setString(2, user.getAuthorities().get(i).getAuthority().name());
        }

        @Override
        public int getBatchSize() {
          return user.getAuthorities().size();
        }
      });

      return user;
    });
  }

  @Override
  public Optional<UserAuthEntity> findByIdInAuth(UUID id) {
    try {
      return Optional.ofNullable(
          authTemplate.query(
              "SELECT * " +
                  "FROM \"user\" u " +
                  "LEFT JOIN \"authority\" a ON u.id = a.user_id " +
                  "where u.id = ?",
              UserAuthEntityResultSetExtractor.instance,
              id
          )
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public UserEntity createInUserdata(UserEntity user) {
    KeyHolder kh = new GeneratedKeyHolder();
    udTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO \"user\" (username, currency) VALUES (?, ?)",
          PreparedStatement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      return ps;
    }, kh);

    user.setId((UUID) kh.getKeys().get("id"));
    return user;
  }

  @Override
  public Optional<UserEntity> findByIdInUserdata(UUID id) {
    try {
      return Optional.ofNullable(
          udTemplate.queryForObject(
              "SELECT * FROM \"user\" WHERE id = ?",
              UserEntityRowMapper.instance,
              id
          )
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public void deleteInAuthById(UUID id) {
    authTxt.execute(status -> {
      authTemplate.update("DELETE FROM \"authority\" WHERE user_id = ?", id);
      authTemplate.update("DELETE FROM \"user\" WHERE id = ?", id);
      return null;
    });
  }

  @Override
  public void deleteInUserdataById(UUID id) {
    udTxt.execute(status -> {
      udTemplate.update("DELETE FROM friendship WHERE friend_id = ?", id);
      udTemplate.update("DELETE FROM friendship WHERE user_id = ?", id);
      udTemplate.update("DELETE FROM \"user\" WHERE id = ?", id);
      return null;
    });
  }

  @Override
  public UserAuthEntity updateUserInAuth(UserAuthEntity userAuthEntity) {
    List<AuthorityEntity> authorityEntities = findAuthoritiesByUserId(userAuthEntity.getId());

    Set<Authority> authoritiesFromDb = mapAuthorityEntitiesToAuthority(authorityEntities);
    Set<Authority> authoritiesCandidate = mapAuthorityEntitiesToAuthority(
        userAuthEntity.getAuthorities());

    Set<Authority> tempSetAuthorityEntitiesForDelete = new HashSet<>(authoritiesFromDb);
    tempSetAuthorityEntitiesForDelete.removeAll(authoritiesCandidate);

    Set<Authority> tempSetAuthorityEntitiesForInsert = new HashSet<>(authoritiesCandidate);
    tempSetAuthorityEntitiesForInsert.removeAll(authoritiesFromDb);

    authTxt.execute(status -> {
      if (!tempSetAuthorityEntitiesForDelete.isEmpty()) {
        authTemplate.batchUpdate("""
                DELETE FROM "authority" WHERE  user_id = ? AND authority = ?""",
            new BatchPreparedStatementSetter() {
              @Override
              public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, userAuthEntity.getId());
                ps.setString(2, tempSetAuthorityEntitiesForDelete.stream().toList().get(i).name());
              }

              @Override
              public int getBatchSize() {
                return tempSetAuthorityEntitiesForDelete.size();
              }
            });
      }

      if (!tempSetAuthorityEntitiesForInsert.isEmpty()) {
        authTemplate.batchUpdate("""
                INSERT INTO "authority" (user_id, authority) VALUES (?, ?)""",
            new BatchPreparedStatementSetter() {
              @Override
              public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, userAuthEntity.getId());
                ps.setString(2, tempSetAuthorityEntitiesForInsert.stream().toList().get(i).name());
              }

              @Override
              public int getBatchSize() {
                return tempSetAuthorityEntitiesForInsert.size();
              }
            });
      }

      authTemplate.update("""
                UPDATE "user"
                SET username = ?,
                    password = ?,
                    enabled = ?,
                    account_non_expired = ?,
                    account_non_locked = ?,
                    credentials_non_expired = ?
              WHERE id = ?
              """, userAuthEntity.getUsername(),
          pe.encode(userAuthEntity.getPassword()),
          userAuthEntity.getEnabled(),
          userAuthEntity.getAccountNonExpired(),
          userAuthEntity.getAccountNonLocked(),
          userAuthEntity.getCredentialsNonExpired(),
          userAuthEntity.getId());
      return null;
    });
    return userAuthEntity;
  }

  @Override
  public UserEntity updateUserInUserdata(UserEntity userEntity) {
    udTemplate.update("""
            UPDATE "user"
            SET username = ?,
                currency = ?,
                firstname = ?,
                surname = ?,
                photo = ?
            WHERE id = ?
             """, userEntity.getUsername(),
        userEntity.getCurrency().name(),
        userEntity.getFirstname(),
        userEntity.getSurname(),
        userEntity.getPhoto(),
        userEntity.getId());
    return userEntity;
  }

  @Override
  public List<AuthorityEntity> findAuthoritiesByUserId(UUID userId) {
    return authTemplate.query("""
            SELECT id,
                   user_id,
                   authority
            FROM "authority"
            WHERE  user_id = ?
            """, AuthorityEntityRowMapper.instance,
        userId);
  }

  private Set<Authority> mapAuthorityEntitiesToAuthority(List<AuthorityEntity> authorityEntities) {
    return authorityEntities
        .stream()
        .map(authority -> authority.getAuthority())
        .collect(Collectors.toSet());
  }
}