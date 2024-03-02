package guru.qa.niffler.service;

import guru.qa.grpc.niffler.grpc.CategoriesRequest;
import guru.qa.grpc.niffler.grpc.CategoriesResponse;
import guru.qa.grpc.niffler.grpc.Category;
import guru.qa.grpc.niffler.grpc.NifflerCategoryServiceGrpc;
import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import io.grpc.stub.StreamObserver;
import java.util.List;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@GrpcService
public class CategoryGrpcService extends NifflerCategoryServiceGrpc.NifflerCategoryServiceImplBase {

  private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);
  private static final int MAX_CATEGORIES_SIZE = 7;
  private final CategoryRepository categoryRepository;

  @Autowired
  public CategoryGrpcService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public void getAllCategories(CategoriesRequest request,
      StreamObserver<CategoriesResponse> responseObserver) {
    List<Category> categories = categoryRepository.findAllByUsername(request.getUsername())
        .stream()
        .map(categoryEntity -> Category.newBuilder()
            .setCategory(categoryEntity.getCategory())
            .setId(categoryEntity.getId().toString())
            .setUsername(categoryEntity.getUsername())
            .build())
        .toList();

    responseObserver.onNext(CategoriesResponse.newBuilder()
        .addAllAllCategories(categories)
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void addCategory(Category request, StreamObserver<Category> responseObserver) {
    final String username = request.getUsername();
    final String categoryName = request.getCategory();

    if (categoryRepository.findAllByUsername(username).size() > MAX_CATEGORIES_SIZE) {
      LOG.error("### Can`t add over than 7 categories for user: " + username);
      responseObserver.onError(
          new RuntimeException("### Can`t add over than 7 categories for user: " + username));
    }

    CategoryEntity ce = new CategoryEntity();
    ce.setCategory(categoryName);
    ce.setUsername(username);
    try {
      CategoryEntity saved = categoryRepository.save(ce);
      responseObserver.onNext(Category.newBuilder()
          .setCategory(saved.getCategory())
          .setId(saved.getId().toString())
          .setUsername(saved.getUsername())
          .build());
      responseObserver.onCompleted();
    } catch (DataIntegrityViolationException e) {
      LOG.error("### Error while creating category: " + e.getMessage());
      responseObserver.onError(new RuntimeException(categoryName + "' already exists"));
    }
  }
}
