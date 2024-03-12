package aydarss.fork.niffler.ayjupiter.ayannotation.aymeta;

import aydarss.fork.niffler.ayjupiter.DbUserCRUDExtension;
import aydarss.fork.niffler.ayjupiter.ayextension.ContextHolderExtension;
import aydarss.fork.niffler.ayjupiter.ayextension.MyDataBaseCreateUserExtension;
import io.qameta.allure.junit5.AllureJunit5;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({ContextHolderExtension.class, AllureJunit5.class, MyDataBaseCreateUserExtension.class})
public @interface MyGqlTest {
}
