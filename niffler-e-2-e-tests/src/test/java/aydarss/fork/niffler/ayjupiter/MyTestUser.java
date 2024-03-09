package aydarss.fork.niffler.ayjupiter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(DbUserCRUDExtension.class)
public @interface MyTestUser {
  boolean fake() default false;

  String username() default "";

  String password() default "";

}
