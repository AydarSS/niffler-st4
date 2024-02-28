package aydarss.fork.niffler.aypage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

public class ProfilePage extends BasePage<ProfilePage> {

  public static final String URL = CFG.frontUrl() + "/profile";

  private final SelenideElement nameInput = $("input[name='firstname']");
  private final SelenideElement surnameInput = $("input[name='surname']");
  private final SelenideElement submitBtn = $("button[type='submit']");

  private final SelenideElement categoryNameInput = $("input[name='category']");
  private final SelenideElement createCategoryBtn = $(".add-category__input-container button");

  private final ElementsCollection categories = $$(".categories__list .categories__item");


  @Step("Ввести в поле name {name}")
  public ProfilePage setName(String name){
    nameInput.setValue(name);
    return this;
  }

  @Step("Ввести в поле surname {surname}")
  public ProfilePage setSurname(String surname){
    surnameInput.setValue(surname);
    return this;
  }

  @Step("Нажимаем submit")
  public ProfilePage submitName(){
    submitBtn.click();
    return this;
  }

  @Step("Вводим в поле категорию {name}")
  public ProfilePage setCategoryName(String name) {
    categoryNameInput.setValue(name);
    return this;
  }

  @Step("Нажимаем добавить категорию")
  public ProfilePage addCategoryBtnClick(){
    createCategoryBtn.click();
    return this;
  }

  @Step("Проверим, что в списке категорий есть {category}")
  public ProfilePage categoriesListContans(String category) {
    categories.texts().contains(category);
    return this;
  }


}
