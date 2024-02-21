package aydarss.fork.niffler.aydb.aylogging;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;
import java.util.Objects;
import lombok.SneakyThrows;

public class JsonAllureAppender {

  private final String templateName = "json.ftl";
  private final AttachmentProcessor<AttachmentData> attachmentProcessor = new DefaultAttachmentProcessor();

  @SneakyThrows
  public void logJson(Object o, String message) {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    String json = Objects.nonNull(o) ? ow.writeValueAsString(o) : "null loging";

    JsonAttachment attachment = new JsonAttachment(message, json);
    attachmentProcessor.addAttachment(attachment, new FreemarkerAttachmentRenderer(templateName));
  }

}
