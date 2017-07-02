package webdriver;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import javafx.beans.binding.BooleanExpression;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
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
    public void communalServices() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.navigate().to("https://www.tinkoff.ru/"); // Переходом по адресу https://www.tinkoff.ru/ загрузить стартовую страницу Tinkoff Bank.
        driver.findElement(By.cssSelector("[data-reactid=\"100\"]")).click(); // Из верхнего меню, нажатием на пункт меню “Платежи“, перейти на страницу “Платежи“.
        driver.findElement(By.cssSelector("[href=\"/payments/categories/kommunalnie-platezhi/\"]")).click(); // В списке категорий платежей, нажатием на пункт “Коммунальные платежи“, перейти на  страницу выбора поставщиков услуг.
        if (driver.findElement(By.xpath("//span[contains(text(),'Москве')]")).isDisplayed()) { // Убедиться, что текущий регион – “г. Москва” (в противном случае выбрать регион “г. Москва” из списка регионов).

            System.out.print("Москве");

        } else {
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("[class=\"ui-link payment-page__title_inner\"]")).click();
            Thread.sleep(2000);
            driver.findElement(By.xpath("//span[contains(text(),'г. Москва')]")).click();
        }

        Thread.sleep(2000);
        // Со страницы выбора поставщиков услуг, выбрать 1-ый из списка (Должен быть “ЖКУ-Москва”). Сохранить его наименование (далее “искомый”) и нажатием на соответствующий элемент перейти на страницу оплаты “ЖКУ-Москва“.
        String compay = driver.findElement(By.cssSelector("[class=\"ui-link ui-menu__link ui-menu__link_logo ui-menu__link_icons ui-menu__link_icons_active\"]")).getText();
        driver.findElement(By.cssSelector("[class=\"ui-link ui-menu__link ui-menu__link_logo ui-menu__link_icons ui-menu__link_icons_active\"]")).click();
        String urlmoscow = driver.getCurrentUrl();

        driver.findElement(By.cssSelector("[href=\"/zhku-moskva/oplata/\"]")).click(); // На странице оплаты, перейти на вкладку “Оплатить ЖКУ в Москве“.
        Thread.sleep(2000);

        // Выполнить проверки на невалидные значения для обязательных полей: проверить все текстовые сообщения об ошибке (и их содержимое), которые появляются под соответствующим полем ввода в результате ввода некорректных данных.

        driver.findElement(By.cssSelector("[data-reactid=\"91\"]")).click(); // Из верхнего меню, нажатием на пункт меню “Платежи“, перейти на страницу “Платежи“.
        Thread.sleep(2000);

        driver.findElement(By.cssSelector("[class=\"_3kceY\"]")).sendKeys(compay); // В строке быстрого поиска поставщика услуг ввести наименование искомого (ранее сохраненного).

        Thread.sleep(2000);
        String compaylist = driver.findElement(By.cssSelector("[class=\"_3WKsv _3KH0m HhZgX _2wQkF\"]")).getText();
        if(compay.equals(compaylist)) { // Убедиться, что в списке предложенных провайдеров искомый поставщик первый.
            driver.findElement(By.cssSelector("[class=\"_200uJ\"]")).click(); // Нажатием на элемент, соответствующий искомому, перейти на страницу “Оплатить ЖКУ в Москве“. Убедиться, что загруженная страница та же, что и страница, загруженная в результате шага (5).
            String urlmoscowlist = driver.getCurrentUrl();
        }else Thread.sleep(5000);
        Thread.sleep(2000);
        if(urlmoscow.equals(urlmoscow)){
            driver.findElement(By.cssSelector("[data-reactid=\"91\"]")).click(); // Из верхнего меню, нажатием на пункт меню “Платежи“, перейти на страницу “Платежи“.
            Thread.sleep(2000);
        }else Thread.sleep(5000);


        driver.findElement(By.cssSelector("[href=\"/payments/categories/kommunalnie-platezhi/\"]")).click(); // В списке категорий платежей, нажатием на пункт “Коммунальные платежи“, перейти на  страницу выбора поставщиков услуг.
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("[class=\"ui-link payment-page__title_inner\"]")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//span[contains(text(),'г. Санкт-Петербург')]")).click(); // В списке регионов выбрать “г. Санкт-Петербург”.
        Thread.sleep(2000);
    }
}




