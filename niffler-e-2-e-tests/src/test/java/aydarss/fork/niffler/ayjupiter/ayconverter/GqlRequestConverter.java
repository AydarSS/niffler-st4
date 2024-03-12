package aydarss.fork.niffler.ayjupiter.ayconverter;

import aydarss.fork.niffler.ayjupiter.ayannotation.GqlRequestFile;
import aydarss.fork.niffler.ayjupiter.ayannotation.GqlRequestFileConverted;
import aydarss.fork.niffler.ayjupiter.ayextension.GqlRequestResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.model.gql.GqlRequest;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

public class GqlRequestConverter implements ArgumentConverter {


  private static final ObjectMapper om = new ObjectMapper();
  private final ClassLoader cl = GqlRequestConverter.class.getClassLoader();

  @Override
  public Object convert(Object o, ParameterContext parameterContext) throws ArgumentConversionException {
    if(!(o instanceof String) || !parameterContext.isAnnotated(GqlRequestFileConverted.class)) {
      throw new RuntimeException("Cannot convert to GqlRequest");
    }
    try (InputStream is = cl.getResourceAsStream((String) o)) {
      return om.readValue(is.readAllBytes(), GqlRequest.class);
    } catch (IOException e) {
      throw new ArgumentConversionException(e.getMessage());
    }
  }
}
