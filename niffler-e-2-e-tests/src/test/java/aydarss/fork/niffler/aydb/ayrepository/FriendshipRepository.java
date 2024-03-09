package aydarss.fork.niffler.aydb.ayrepository;

import java.util.UUID;

public interface FriendshipRepository {


  void addFriendShip(UUID firstFriend, UUID secondFriend, boolean isPending);

}
