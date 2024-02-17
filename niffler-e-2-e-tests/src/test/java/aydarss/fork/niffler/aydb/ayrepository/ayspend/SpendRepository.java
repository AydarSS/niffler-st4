package aydarss.fork.niffler.aydb.ayrepository.ayspend;

import aydarss.fork.niffler.aydb.aymodel.ayspend.CategoryEntity;
import aydarss.fork.niffler.aydb.aymodel.ayspend.SpendEntity;
import java.util.Optional;

public interface SpendRepository {

  SpendEntity createSpend(SpendEntity spendEntity);

  CategoryEntity createCategory(CategoryEntity categoryEntity);

  Optional<CategoryEntity> findCategoryByUserIdAndCategory(String username, String category);
}
