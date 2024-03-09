package aydarss.fork.niffler.ayjupiter.ayannotation;

import aydarss.fork.niffler.ayjupiter.ayextension.SpendExtension;
import aydarss.fork.niffler.ayjupiter.ayextension.SpendResolverExtension;
import guru.qa.niffler.model.CurrencyValues;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({SpendExtension.class, SpendResolverExtension.class})
public @interface GenerateSpend {

  String username() default "";

  String description();

  String category();

  double amount();

  CurrencyValues currency();
}
