package aydarss.fork.niffler.ayjupiter;

import aydarss.fork.niffler.ayjupiter.ayannotation.Friendship;
import aydarss.fork.niffler.ayjupiter.ayannotation.GenerateSpend;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTestUser {

  boolean fake() default false;

  String username() default "";

  String password() default "";

  GenerateCategory[] categoryValue() default {};

  GenerateSpend[] spendValue() default {};

  Friendship[] withFriends() default {};


}
