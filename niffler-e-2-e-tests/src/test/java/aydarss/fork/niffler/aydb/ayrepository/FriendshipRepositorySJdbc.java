package aydarss.fork.niffler.aydb.ayrepository;

import aydarss.fork.niffler.aydb.DataSourceProvider;
import aydarss.fork.niffler.aydb.Database;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;

public class FriendshipRepositorySJdbc implements FriendshipRepository {

  private final JdbcTemplate udTemplate;


  public FriendshipRepositorySJdbc() {
    JdbcTransactionManager udTm = new JdbcTransactionManager(
        DataSourceProvider.INSTANCE.dataSource(Database.USERDATA)
    );
    this.udTemplate = new JdbcTemplate(udTm.getDataSource());
  }

  @Override
  public void addFriendShip(UUID firstFriend, UUID secondFriend, boolean isPending) {
    udTemplate.update("insert into friendship (user_id, friend_id, pending) values (?,?,?)",
        firstFriend,
        secondFriend,
        isPending);
  }
}
