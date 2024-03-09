package aydarss.fork.niffler.ayjupiter.ayannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyUser {

  Point value() default Point.INNER;

  enum Point {
    INNER, OUTER
  }
}
