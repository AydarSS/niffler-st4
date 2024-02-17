package aydarss.fork.niffler.ayjupiter.ayannotation;

import aydarss.fork.niffler.ayjupiter.ayconverter.AllureIdConverter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.params.converter.ConvertWith;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(AllureIdConverter.class)
public @interface AllureIdParam {
}
