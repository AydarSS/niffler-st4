package aydarss.fork.niffler.ayjupiter.ayannotation;




import aydarss.fork.niffler.ayjupiter.ayconverter.GqlRequestConverter;
import aydarss.fork.niffler.ayjupiter.ayextension.GqlRequestResolver;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.converter.ConvertWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ConvertWith(GqlRequestConverter.class)
public @interface GqlRequestFileConverted {
  String value() default "";
}
