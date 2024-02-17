package aydarss.fork.niffler.aydb.ayrepository.ayspend;

import static aydarss.fork.niffler.aydb.Database.SPEND;

import aydarss.fork.niffler.aydb.EmfProvider;
import aydarss.fork.niffler.aydb.ayjpa.JpaService;
import aydarss.fork.niffler.aydb.aymodel.ayspend.CategoryEntity;
import aydarss.fork.niffler.aydb.aymodel.ayspend.SpendEntity;
import jakarta.persistence.NoResultException;
import java.util.Optional;

public class SpendRepositoryHibernate extends JpaService implements SpendRepository {

  public SpendRepositoryHibernate() {
    super(SPEND, EmfProvider.INSTANCE.emf(SPEND).createEntityManager());
  }

  @Override
  public SpendEntity createSpend(SpendEntity spendEntity) {
    persist(SPEND, spendEntity);
    return spendEntity;
  }

  @Override
  public CategoryEntity createCategory(CategoryEntity categoryEntity) {
    persist(SPEND, categoryEntity);
    return categoryEntity;
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUserIdAndCategory(String username,
      String category) {
    try {
      return Optional.of(entityManager(SPEND)
          .createQuery("""
              SELECT cat 
              FROM CategoryEntity cat 
              WHERE cat.category = :category
              AND cat.username = :username
              """, CategoryEntity.class)
          .setParameter("category", category)
          .setParameter("username", username)
          .getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
