package aydarss.fork.niffler.aytest.grpc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import guru.qa.grpc.niffler.grpc.CategoriesRequest;
import guru.qa.grpc.niffler.grpc.CategoriesResponse;
import guru.qa.grpc.niffler.grpc.Category;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CategoryGrpcTest extends BaseSpendGrpcTest {

  @DisplayName("Проверим получение всех категорий")
  @Test
  void allCategoriesTest() {
    CategoriesRequest categoriesRequest = CategoriesRequest.newBuilder()
        .setUsername("duck")
        .build();
    CategoriesResponse categoriesResponse = blockingStubCategory.getAllCategories(
        categoriesRequest);

    assertTrue(
        categoriesResponse.getAllCategoriesList()
            .stream()
            .anyMatch(
                category -> category.getCategory().contains("Обучение") && category.getUsername()
                    .equals("duck")));
  }

  @DisplayName("Проверим добавление категории")
  @Test
  void addCategoryTest() {
    String randomCategory = UUID.randomUUID().toString();

    Category candidate = Category.newBuilder()
        .setCategory(randomCategory)
        .setUsername("rabbit")
        .build();

    Category created = blockingStubCategory.addCategory(candidate);

    assertAll(
        () -> assertTrue(!created.getId().equals("")),
        () -> assertEquals("rabbit", created.getUsername()),
        () -> assertEquals(randomCategory, created.getCategory())
    );
  }

}
