package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.MySpendExtension;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class MySpendResolverExtension implements ParameterResolver {

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter()
        .getType()
        .isAssignableFrom(SpendJson.class);
  }

  @Override
  public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(MySpendExtension.NAMESPACE)
        .get(extensionContext.getUniqueId(), SpendJson.class);
  }
}
