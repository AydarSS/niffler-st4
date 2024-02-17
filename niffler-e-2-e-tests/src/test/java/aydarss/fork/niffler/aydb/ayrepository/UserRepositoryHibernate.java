package aydarss.fork.niffler.aydb.ayrepository;

import static aydarss.fork.niffler.aydb.Database.AUTH;
import static aydarss.fork.niffler.aydb.Database.USERDATA;

import aydarss.fork.niffler.aydb.EmfProvider;
import aydarss.fork.niffler.aydb.ayjpa.JpaService;
import aydarss.fork.niffler.aydb.aymodel.AuthorityEntity;
import aydarss.fork.niffler.aydb.aymodel.UserAuthEntity;
import aydarss.fork.niffler.aydb.aymodel.UserEntity;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRepositoryHibernate extends JpaService implements UserRepository {

  private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public UserRepositoryHibernate() {
    super(
        Map.of(
            AUTH, EmfProvider.INSTANCE.emf(AUTH).createEntityManager(),
            USERDATA, EmfProvider.INSTANCE.emf(USERDATA).createEntityManager()
        )
    );
  }

  @Override
  public UserAuthEntity createInAuth(UserAuthEntity user) {
    String originalPassword = user.getPassword();
    user.setPassword(pe.encode(originalPassword));
    persist(AUTH, user);
    user.setPassword(originalPassword);
    return user;
  }

  @Override
  public Optional<UserAuthEntity> findByIdInAuth(UUID id) {
    return Optional.of(entityManager(AUTH).find(UserAuthEntity.class, id));
  }

  @Override
  public UserEntity createInUserdata(UserEntity user) {
    persist(USERDATA, user);
    return user;
  }

  @Override
  public Optional<UserEntity> findByIdInUserdata(UUID id) {
    return Optional.of(entityManager(USERDATA).find(UserEntity.class, id));
  }

  @Override
  public void deleteInAuthById(UUID id) {
    UserAuthEntity toBeDeleted = findByIdInAuth(id).get();
    remove(AUTH, toBeDeleted);
  }

  @Override
  public void deleteInUserdataById(UUID id) {
    UserEntity toBeDeleted = findByIdInUserdata(id).get();
    remove(USERDATA, toBeDeleted);
  }

  @Override
  public UserAuthEntity updateUserInAuth(UserAuthEntity userAuthEntity) {
    return userAuthEntity;
  }

  @Override
  public UserEntity updateUserInUserdata(UserEntity userEntity) {
    return userEntity;

  }

  @Override
  public List<AuthorityEntity> findAuthoritiesByUserId(UUID userId) {
    return null;
  }
}
