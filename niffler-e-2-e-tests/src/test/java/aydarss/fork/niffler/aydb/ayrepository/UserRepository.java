package aydarss.fork.niffler.aydb.ayrepository;

import aydarss.fork.niffler.aydb.aymodel.AuthorityEntity;
import aydarss.fork.niffler.aydb.aymodel.UserAuthEntity;
import aydarss.fork.niffler.aydb.aymodel.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  UserAuthEntity createInAuth(UserAuthEntity user);

  Optional<UserAuthEntity> findByIdInAuth(UUID id);

  UserEntity createInUserdata(UserEntity user);

  Optional<UserEntity> findByIdInUserdata(UUID id);

  void deleteInAuthById(UUID id);

  void deleteInUserdataById(UUID id);

  UserAuthEntity updateUserInAuth(UserAuthEntity userAuthEntity);

  UserEntity updateUserInUserdata(UserEntity userEntity);

  List<AuthorityEntity> findAuthoritiesByUserId(UUID userId);

}
