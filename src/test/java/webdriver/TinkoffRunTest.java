package webdriver;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import javafx.beans.binding.BooleanExpression;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.datatransfer.Clipboard;
import java.util.concurrent.TimeUnit;

/**
 * Created by uwage on 02.07.2017.
 * Запуск производится через ChromeDriver. Проверка(перед запуском тестов) актуальной версии - через webdriver-manager.
 */

public class TinkoffRunTest {

    WebDriver driver;

    @BeforeClass
    public static void setupClass() {
        ChromeDriverManager.getInstance().setup();
    }

    @Before
    public void setupTest() {
        driver = new ChromeDriver();
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void communalServices() throws Exception {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
// Переходом по адресу https://www.tinkoff.ru/ загрузить стартовую страницу Tinkoff Bank.
        driver.navigate().to("https://www.tinkoff.ru/");

// Из верхнего меню, нажатием на пункт меню “Платежи“, перейти на страницу “Платежи“.
        driver.findElement(By.cssSelector("[data-reactid=\"100\"]")).click();

// В списке категорий платежей, нажатием на пункт “Коммунальные платежи“, перейти на  страницу выбора поставщиков услуг.
        driver.findElement(By.cssSelector("[href=\"/payments/categories/kommunalnie-platezhi/\"]")).click();

// Убедиться, что текущий регион – “г. Москва” (в противном случае выбрать регион “г. Москва” из списка регионов).
//        "//span[contains(text(),'Москве')]"
        if (driver.findElements(By.xpath("//span[contains(text(),'Москве')]")).size() != 0) {
            Thread.sleep(1000);
        } else {
            driver.findElement(By.cssSelector("[class=\"ui-link payment-page__title_inner\"]")).click();
            driver.findElement(By.xpath("//span[contains(text(),'г. Москва')]")).click();
        }

// Со страницы выбора поставщиков услуг, выбрать 1-ый из списка (Должен быть “ЖКУ-Москва”). Сохранить его наименование
// (далее “искомый”) и нажатием на соответствующий элемент перейти на страницу оплаты “ЖКУ-Москва“.
        String compay = driver.findElement(By.cssSelector
                ("[class=\"ui-link ui-menu__link ui-menu__link_logo ui-menu__link_icons ui-menu__link_icons_active\"]"))
                .getText();
        driver.findElement(By.cssSelector
                ("[class=\"ui-link ui-menu__link ui-menu__link_logo ui-menu__link_icons ui-menu__link_icons_active\"]"))
                .click();
        String urlmoscow = driver.getCurrentUrl();

// На странице оплаты, перейти на вкладку “Оплатить ЖКУ в Москве“.
        driver.findElement(By.cssSelector("[href=\"/zhku-moskva/oplata/\"]")).click();
        Thread.sleep(2000);

// Из верхнего меню, нажатием на пункт меню “Платежи“, перейти на страницу “Платежи“.
        driver.findElement(By.cssSelector("[data-reactid=\"91\"]")).click();

// В строке быстрого поиска поставщика услуг ввести наименование искомого (ранее сохраненного).
        driver.findElement(By.cssSelector("[class=\"_3kceY\"]")).sendKeys(compay);

// Убедиться, что в списке предложенных провайдеров искомый поставщик первый.
        String compaylist = driver.findElement(By.cssSelector("[class=\"_3WKsv _3KH0m HhZgX _2wQkF\"]")).getText();
        if(compay.equals(compaylist)) {
// Нажатием на элемент, соответствующий искомому, перейти на страницу “Оплатить ЖКУ в Москве“. Убедиться,
// что загруженная страница та же, что и страница, загруженная в результате шага (5).
            driver.findElement(By.cssSelector("[class=\"_200uJ\"]")).click();
            String urlmoscowlist = driver.getCurrentUrl();
        }else throw new Exception("Поставщики не совпадают!");

// Из верхнего меню, нажатием на пункт меню “Платежи“, перейти на страницу “Платежи“.
        if(urlmoscow.equals(urlmoscow)){
            driver.findElement(By.cssSelector("[data-reactid=\"91\"]")).click();
        }else throw new Exception("Загруженная страница не совпадает с шагом 5!");

// В списке категорий платежей, нажатием на пункт “Коммунальные платежи“, перейти на  страницу выбора поставщиков услуг.
        driver.findElement(By.cssSelector("[href=\"/payments/categories/kommunalnie-platezhi/\"]")).click();
        driver.findElement(By.cssSelector("[class=\"ui-link payment-page__title_inner\"]")).click();

// В списке регионов выбрать “г. Санкт-Петербург”.
        driver.findElement(By.xpath("//span[contains(text(),'г. Санкт-Петербург')]")).click();

// Убедится, что в списке поставщиков на странице выбора поставщиков услуг отсутствует искомый.
        if (driver.findElements(By.xpath(compay)).size() == 0) {
            driver.quit();
        } else throw new Exception("Найден некорректный элемент!");
    }

// Выполнить проверки на невалидные значения для обязательных полей: проверить все текстовые сообщения об ошибке
// (и их содержимое), которые появляются под соответствующим полем ввода в результате ввода некорректных данных.
    @Test
    public void invalidPayment() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.navigate().to("https://www.tinkoff.ru/zhku-moskva/oplata/");

// Код плательщика. Пустое значение
        driver.findElement(By.cssSelector("[for=\"payerCode\"]")).sendKeys("" + Keys.ENTER);
        driver.findElement(By.xpath("//div[contains(text(),'Поле обязательное')]")).isDisplayed();

// Код плательщика. Не полносутью заполнено
        driver.findElement(By.cssSelector("[for=\"payerCode\"]")).sendKeys("1" + Keys.ENTER);
        driver.findElement(By.xpath("//div[contains(text(),'Поле неправильно заполнено')]")).isDisplayed();

        driver.navigate().refresh();

// Период оплаты. Пустое значение
        driver.findElement(By.cssSelector("[name=\"provider-period\"]")).sendKeys("" + Keys.TAB);
        driver.findElement(By.xpath("//div[contains(text(),'Поле обязательное')]")).isDisplayed();

// Период оплаты. Не полносутью заполнено
        driver.findElement(By.cssSelector("[name=\"provider-period\"]")).sendKeys("1" + Keys.TAB);
        driver.findElement(By.xpath("//div[contains(text(),'Поле заполнено некорректно')]")).isDisplayed();

// Период оплаты. Некорректная дата
        driver.findElement(By.cssSelector("[name=\"provider-period\"]")).sendKeys("99999" + Keys.TAB);
        driver.findElement(By.xpath("//div[contains(text(),'Поле заполнено некорректно')]")).isDisplayed();


        driver.navigate().refresh();

// Код плательщика. Пустое значение
        driver.findElement(By.cssSelector("[class=\"ui-input__field ui-input__field_not-empty\"]")).sendKeys("" + Keys.TAB);
        driver.findElement(By.xpath("//div[contains(text(),'Поле обязательное')]")).isDisplayed();
// Код плательщика. Меньше минимума
       // driver.navigate().refresh();
       // driver.findElement(By.cssSelector("[data-reactid=\"269\"]")).sendKeys("9" + Keys.ALT);
      //  driver.findElement(By.xpath("//div[contains(text(),'Минимальная сумма перевода - 10')]")).isDisplayed();

// Код плательщика. Больше максимума
     //   driver.findElement(By.cssSelector("[class=\"ui-input__field ui-input__field_not-empty\"]")).sendKeys(Keys.BACK_SPACE + "15 001" + Keys.ENTER);
      //  driver.findElement(By.xpath("//div[contains(text(),'Максимальная сумма перевода - 15 000 \u20BD')]")).isDisplayed();
    }
    }






