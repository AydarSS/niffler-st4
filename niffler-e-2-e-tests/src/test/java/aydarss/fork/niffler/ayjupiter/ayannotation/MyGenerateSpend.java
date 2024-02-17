package aydarss.fork.niffler.ayjupiter.ayannotation;

import aydarss.fork.niffler.ayjupiter.DatabaseSpendExtension;
import aydarss.fork.niffler.ayjupiter.ayextension.MySpendResolverExtension;
import guru.qa.niffler.model.CurrencyValues;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({DatabaseSpendExtension.class, MySpendResolverExtension.class})
public @interface MyGenerateSpend {

  String username();

  String description();

  String category();

  double amount();

  CurrencyValues currency();
}
