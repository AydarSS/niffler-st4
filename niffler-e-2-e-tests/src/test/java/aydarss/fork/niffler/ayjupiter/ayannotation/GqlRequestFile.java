package aydarss.fork.niffler.ayjupiter.ayannotation;




import aydarss.fork.niffler.ayjupiter.ayextension.GqlRequestResolver;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ExtendWith(GqlRequestResolver.class)
public @interface GqlRequestFile {
  String value();
}
