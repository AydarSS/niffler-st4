package aydarss.fork.niffler.ayjupiter;

import aydarss.fork.niffler.aydb.aymodel.ayspend.CategoryEntity;
import aydarss.fork.niffler.aydb.aymodel.ayspend.SpendEntity;
import aydarss.fork.niffler.aydb.ayrepository.ayspend.SpendRepository;
import aydarss.fork.niffler.aydb.ayrepository.ayspend.SpendRepositoryJdbc;
import aydarss.fork.niffler.ayjupiter.aydto.CategoryDto;
import aydarss.fork.niffler.ayjupiter.aydto.SpendDto;
import java.util.Optional;

public class DatabaseSpendExtension extends MySpendExtension {

  SpendRepository spendRepository = new SpendRepositoryJdbc();

  @Override
  SpendDto createSpend(SpendDto spendDto) {

    SpendEntity createdSpendEntity = spendRepository.createSpend(spendDtoToEntity(spendDto));

    return spendEntityToDto(createdSpendEntity);
  }

  @Override
  CategoryDto createCategory(CategoryDto categoryDto) {
    Optional<CategoryEntity> categoryEntity =
        spendRepository.findCategoryByUserIdAndCategory(
            categoryDto.userName(),
            categoryDto.category());

    if (categoryEntity.isPresent()) {
      return new CategoryDto(categoryEntity.get().getId(),
          categoryEntity.get().getUsername(),
          categoryEntity.get().getCategory());
    }

    CategoryEntity categoryEntityCreated = spendRepository.createCategory
        (categoryDtoToEntity(categoryDto));

    return categoryEntityToDto(categoryEntityCreated);
  }

  private SpendEntity spendDtoToEntity(SpendDto spendDto) {
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setId(spendDto.category().id());
    categoryEntity.setCategory(spendDto.category().category());
    categoryEntity.setUsername(spendDto.category().userName());

    SpendEntity spendEntity = new SpendEntity();
    spendEntity.setId(spendDto.id());
    spendEntity.setUsername(spendDto.username());
    spendEntity.setCurrency(spendDto.currency());
    spendEntity.setSpendDate(spendDto.spendDate());
    spendEntity.setAmount(spendDto.amount());
    spendEntity.setDescription(spendDto.description());
    spendEntity.setCategory(categoryEntity);

    return spendEntity;
  }

  private SpendDto spendEntityToDto(SpendEntity spendEntity) {
    return new SpendDto(spendEntity.getId(),
        spendEntity.getUsername(),
        spendEntity.getCurrency(),
        spendEntity.getSpendDate(),
        spendEntity.getAmount(),
        spendEntity.getDescription(),
        new CategoryDto(spendEntity.getCategory().getId(),
            spendEntity.getCategory().getUsername(),
            spendEntity.getCategory().getCategory()));
  }

  private CategoryEntity categoryDtoToEntity(CategoryDto categoryDto) {
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setId(categoryDto.id());
    categoryEntity.setCategory(categoryDto.category());
    categoryEntity.setUsername(categoryDto.userName());
    return categoryEntity;
  }

  private CategoryDto categoryEntityToDto(CategoryEntity categoryEntity) {
    return new CategoryDto(categoryEntity.getId(), categoryEntity.getUsername(),
        categoryEntity.getCategory());
  }
}
