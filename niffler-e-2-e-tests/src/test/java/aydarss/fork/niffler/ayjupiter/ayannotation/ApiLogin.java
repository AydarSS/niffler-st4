package aydarss.fork.niffler.ayjupiter.ayannotation;

import aydarss.fork.niffler.ayjupiter.MyTestUser;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiLogin {
  String username() default "";

  String password() default "";

  MyTestUser user() default @MyTestUser(fake = true);

}
