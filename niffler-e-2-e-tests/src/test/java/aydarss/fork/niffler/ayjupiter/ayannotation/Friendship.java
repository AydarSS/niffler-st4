package aydarss.fork.niffler.ayjupiter.ayannotation;

import static aydarss.fork.niffler.ayjupiter.ayannotation.Friendship.FriendshipState.FRIENDS;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Friendship {
  boolean handle()  default true;

  FriendshipState friedshipState() default FRIENDS;

  enum FriendshipState {
    FRIENDS,
    INCOME_REQUEST,
    OUTCOME_REQUEST
  }
}
