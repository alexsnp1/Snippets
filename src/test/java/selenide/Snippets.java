package selenide;
import com.codeborne.selenide.*;
import org.openqa.selenium.*;

import java.io.*;
import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

// this is not a full list, just the most common
public class Snippets {
    void browser_command_examples() {
        open("https://google.com");
        open("/customer/orders");     // -Dselenide.baseUrl=http://123.23.23.1
        open("/", AuthenticationType.BASIC,
                new BasicAuthCredentials("", "user", "password"));
        // ЭТО ИСПОЛЬЗУЕТСЯ ДЛЯ АУТЕНФИКАЦИИ НА САЙТЕ КОГДА ЕСТЬ ВСПЛЫВАЮЩЕЕ ОКНО С ЛОГИНОМ И ПАРОЛЕМ
        // ЕСЛИ НЕ НАДО ВО ВСПЛЫВАЮЩЕМ ОКНЕ ВВОДИТЬ ДОМЕН, ОСТАВЛЯЕМ ПОЛЕ ПУСТЫМ

        Selenide.back(); // КНОПКА ПЕРЕЙТИ НАЗАД
        Selenide.refresh(); // КНОПКА ОБНОВИТЬ СТРАНИЦУ

        Selenide.clearBrowserCookies(); // ЧИСТИМ КУКИ (ПОСЛЕ ЭТОГО НУЖНА КОМАНДА РЕФРЕШ)
        Selenide.clearBrowserLocalStorage(); // ЧИСТИМ ЛОКАШ СТОРАДЖ (ТОЖЕ РЕФРЕШ)
        executeJavaScript("sessionStorage.clear();"); //ЧИСТИМ СЕШОН СТОРАДЖ (РЕФРЕШ)

        Selenide.confirm(); // ВЫЛЕЗАЕТ АЛЕРТ С КНОПКОЙ ОК
        Selenide.dismiss(); // ВЫЛЕЗАЕТ АЛЕРТ С КНОПКАМИ ОК И ОТМЕНА

        Selenide.closeWindow(); // ЗАКРЫВАЕТ ОКНО БРАУЗЕРА
        Selenide.closeWebDriver(); // ЗАКРЫВАЕТ ВСЕ ОКНА БРАУЗЕРА
        Selenide.switchTo().window("The Internet"); //ПЕРЕЙТИ В ДРУГОЕ ОКНО БРАУЗЕРА

        Selenide.switchTo().frame("new"); //ПЕРЕЙТИ ВО ФРЕЙМ (ПОЧТИ НЕ ИСПОЛЬЗУЕТСЯ)
        Selenide.switchTo().defaultContent(); // ВОЗВРАЩАЕМСЯ В ДЕФОЛТНОЕ ОКНО С ФРЕЙМА КАКОГО-ТО
        // СЕЛЕНИД НЕ МОЖЕТ ИСКАТЬ ВО ФРЕЙМАХ ТЕКСТ, НАДО СНАЧАЛА БЫТЬ ВНУТРИ ФРЕЙМА, ТОГДА ОН НАЙДЕТ


        var cookie = new Cookie("foo", "bar");
        WebDriverRunner.getWebDriver().manage().addCookie(cookie);
        // ДОБАВИТЬ КУКИ (РЕФРЕШ НУЖЕН ПОСЛЕ)

    } //КОМАНДЫ БРАУЗЕРА

    void selectors_examples() {
        $("div").click();
        element("div").click();
        // ИЩЕМ БЛОК ДИВ И КЛИКАЕМ ПО НЕМУ

        $("div", 2).click(); // ИЩЕТ ТРЕТИЙ ДИВ (НУМЕРАЦИЯ С НУЛЯ)

        $x("//h1/div").click();
        $(byXpath("//h1/div")).click();
        //ТО ЖЕ САМОЕ С ИКСПАСОМ

        $(byText("full text")).click(); // ПОИСК ТАКОГО ТЕКСТ
        $(withText("ull tex")).click(); // ПОИСК ЧАСТИ ТЕКСТА

        $(byTagAndText("div", "full text")); //В ТЕГЕ ТЕКСТ
        $(withTagAndText("div", "ull text")); // В ТЕГЕ ЧАСТИЧНО ТЕКСТ

        $("").parent();// ОБРАЩЕНИЕ К РОДИТЕЛЬСКОМУ ЭЛЕМЕНТУ
        $("").sibling(1); // БЛИЗНЕЦ. НАШЛИ ЭЛЕМЕНТ, НИЖЕ БУДЕТ 0, ДАЛЬШЕ 1. 22:37 МИНУТА
        $("").preceding(1); //ТО ЖЕ САМОЕ, ТОЛЬКО ВВЕРХ БУДЕТ 0, ЕЩЕ ВЫШЕ 1
        $("").closest("div");
        $("").ancestor("div"); // the same as closest // НЕПОНЯТНО
        $("div:last-child"); //ПОИСК САМОГО ПОСЛЕДНЕГО РЕБЕНКА (24:01 МИНУТА)

        $("div").$("h1").find(byText("abc")).click();
        // ПРИМЕР - НАХОДИМ ПЕРВЫЙ ДИВ, ВНУТРИ НЕГО ПЕРВЫЙ Н1, ВНУТРИ НЕГО ТЕКСТ АВС И КЛИКАЕМ ПО НЕМУ

        $(byAttribute("abc", "x")).click();
        $("[abc=x]").click();
        // ДВА ВАРИАНТА ОДНОГО И ТОТ ЖЕ. ИЩЕМ АТРИБУТ АВС СО ЗНАЧЕНИЕ Х И КЛИКАЕМ

        $(byId("mytext")).click();
        $("#mytext").click();
        // ДВА ВАРИАНТА ОДНОГО И ТОТ ЖЕ. ИЩЕМ АЙДИ МАЙ ТЕКСТ И КЛИКАЕМ
        // ВТОРОЙ ВАРИАНТ НЕ РАБОТАЕТ ЕСЛИ АЙДИ НАЧИНАЕТСЯ С ЦИФРЫ

        $(byClassName("red")).click();
        $(".red").click();
        // ДВА ВАРИАНТА ОДНОГО И ТОТ ЖЕ. ИЩЕМ КЛАСС РЕД И КЛИКАЕМ ПО НЕМУ

    } // СЕЛЕКТОРЫ

    void actions_examples() {
        $("").click();
        $("").doubleClick();
        $("").contextClick(); // ТО ЖЕ САМОЕ ЧТО РАЙТ КЛИК

        $("").hover(); // ПОДНЕСТИ МЫШКУ НО НЕ НАЖИМАТЬ

        $("").setValue("text"); // ЗАПИСЫВАЕМ ТЕКСТ В ПОЛЕ ДЛЯ ВВОДА
        $("").append("text"); // ДОБАВЛЯЕТ ЗНАЧЕНИЕ НОВОЕ К СТАРОМУ
        $("").clear();
        $("").setValue(""); // clear
        //ОБЕ ЭТИ КОМАНДЫ ОЧИЩАЮТ ПОЛЕ

        $("div").sendKeys("c"); // ПОСЫЛАТЬ КЛАВИШИ НА ОПРЕДЕЛЕННЫЙ ЭЛЕМЕНТ (ЕСЛИ НЕ ИППУТ)
        actions().sendKeys("c").perform(); //АБСТРАКТНО ПОСЫЛАТЬ КЛАВИШИ
        actions().sendKeys(Keys.chord(Keys.CONTROL, "f")).perform(); // НАЖАТЬ КЛАВИШИ Ctrl + F
        $("html").sendKeys(Keys.chord(Keys.CONTROL, "f")); // ТО ЖЕ САМОЕ

        $("").pressEnter();
        $("").pressEscape();
        $("").pressTab();
        // НАЖАТЬ КАКУЮ-ЛИБО КНОПКУ


        // complex actions with keybord and mouse, example
        actions().moveToElement($("div")).clickAndHold().moveByOffset(300, 200).release().perform();
        // ПЕРЕДВИНУТЬ МЫШКУ К ЭЛЕМЕНТУ "div", нажать на кнопку мыши и не отпускать ее, передвинуть курсор
        // на 300 пикселей слева направо и на 200 сверху вниз, отпустить клавишу мыши, выполнить все действия (типа закончить как я понял)
        //ПЕРЕДВИНУТЬ ОДИН ЭЛЕМЕНТ К ДРУГОМУ ЭЛЕМЕНТУ
        actions().clickAndHold($("#column-a")).moveToElement($("#column-b")).release().perform();
        //ИЛИ ТАК
        //$("#column-a").dragAndDropTo($("#column-b"));

        // old html actions don't work with many modern frameworks
        $("").selectOption("dropdown_option");
        // работает с дропдаунами старыми
        //новые дропдауны работают двумя кликами.
        $("").selectRadio("radio_options"); //работа с радиобаттонами (кружками). То же самое с чекбоксами
        // МОЖНО ЭТО ДЕЛАТЬ ПРОСТО ЧЕРЕЗ ОБЫЧНЫЙ $("label[for='hobbies-checkbox-2']").click();

    } // ДЕЙСТВИЯ

    void assertions_examples() {
        $("").shouldBe(visible);
        $("").shouldNotBe(visible);
        $("").shouldHave(text("abc"));
        $("").shouldNotHave(text("abc"));
        $("").should(appear);
        $("").shouldNot(appear);
        //shouldBe shouldHave и should работают абсолютно одинаково (чисто косметика)


        //КОГДА НУЖНО УВЕЛИЧИТЬ ИЛИ УМЕНЬШИТЬ ТАЙМАУТ ПРОВЕРКИ. БАЗОВО 4 СЕКУНДЫ
        $("").shouldBe(visible, Duration.ofSeconds(30));

    } // ПРОВЕРКИ

    void conditions_examples() {
        $("").shouldBe(visible); // ДОЛЖНО БЫТЬ ВИДИМЫМ
        $("").shouldBe(hidden); // ДОЛЖНО БЫТЬ НЕВИДИМЫМ


        // ПРОВЕРКА ТЕКСТА
        $("").shouldHave(text("abc")); // ЕСТЬ ЛИ ТАКОЙ ТЕКСТ
        $("").shouldHave(exactText("abc")); // ТОЛЬКО ТАКОЙ ТЕКСТ И НИЧЕГО БОЛЬШЕ
        $("").shouldHave(textCaseSensitive("abc")); // ПРОВЕРЯЕТ БОЛЬШИЕ МАЛЕНЬКИЕ БУКВЫ
        $("").shouldHave(exactTextCaseSensitive("abc")); //ТО ЖЕ САМОЕ ТОЛЬКО ТАКОЙ ТЕКСТ И БОЛЬШЕ НИЧЕГО
        $("").should(matchText("[0-9]abc$")); // ПРОВЕРИТЬ ВАЛИДАЦИЮ (ДАТА, ЕМЕЙЛ)

        $("").shouldHave(cssClass("red")); // ПРОВЕРЯЕТ СОДЕРЖИТ ЛИ ЭЛЕМЕНТ ДАННЫЙ КЛАСС red
        $("").shouldHave(cssValue("font-size", "12")); // МОЖНО ПРОВЕРИТЬ СТИЛИ

        //ПРОВЕРКИ ПОЛЕЙ ДЛЯ ВВОДА (ИНПУТОВ):
        $("").shouldHave(value("25"));
        $("").shouldHave(exactValue("25")); //ИМЕННО ТАКУЮ СТРОКУ ИЩЕТ
        $("").shouldBe(empty);// ПРОВЕРКА ЧТО ПОЛЕ ПУСТОЕ

        //ПРОВЕРКИ АТРИБУТА
        $("").shouldHave(attribute("disabled")); // ПРОВЕРЯЕМ ЕСТЬ ЛИ АТРИБУТ
        $("").shouldHave(attribute("name", "example")); // ПРОВЕРЯЕМ ЗНАЧЕНИЕ АТРИБУТА
        $("").shouldHave(attributeMatching("name", "[0-9]abc$"));

        $("").shouldBe(checked); // ПРОВЕРКА ДЛЯ ЧЕКБОКСОВ

        // Warning! Only checks if it is in DOM, not if it is visible! You don't need it in most tests!
        $("").should(exist); // СУЩЕСТВУЕТ ЛИ ЭЛЕМЕНТ

        // Warning! Checks only the "disabled" attribute! Will not work with many modern frameworks
        $("").shouldBe(disabled); // ПРОВЕРЯЕТ СТОИТ ЛИ ПЕРЕД ЭЛЕМЕНТОМ disabled
        $("").shouldBe(enabled);
    } // СОСТОЯНИЕ

    void collections_examples() {

        $$("div"); // ИЩЕТ ВСЕ ДИВЫ
        $$x("//div"); // by XPath

        // СЕЛЕКТОРЫ
        $$("div").filterBy(text("123")).shouldHave(size(1));
        // ИЗ ЭТИХ ДИВОВ ОСТАВЛЯЕМ ТОЛЬКО ТЕ В КОТОРЫХ ЕСТЬ ТЕКСТ 123 (КОЛЛЕКЦИЯ СОЗДАЕТСЯ)
        $$("div").excludeWith(text("123")).shouldHave(size(1));
        //ИЗ ЭТИХ ДИВОВ ОСТАВЛЯЕМ ТОЛЬКО ТЕ В КОТОРЫХ НЕТ ТЕКСТА 123

        // РАБОТА С КОЛЛЕКЦИЕЙ КОТОРУЮ МЫ СОЗДАЛИ
        $$("div").first().click(); //ВЗЯТЬ ВСЕ ЭЛЕМЕНТЫ ДИВ И ВЗЯТЬ ИЗ НИХ ПЕРВЫЙ (НУЛЕВОЙ)
        elements("div").first().click(); // СИНОНИМ
        // $("div").click();
        $$("div").last().click();
        $$("div").get(1).click(); // ЭТО ВТОРОЙ ЭЛЕМЕНТ ПОТОМУ ЧТО НАЧИНАЕТСЯ С НУЛЯ
        $("div", 1).click(); // same as previous
        $$("div").findBy(text("123")).click(); //  НАХОДИТ ПЕРВЫЙ ЭЛЕМЕНТ (ЭТО КАК Filterby+first)


        // ПРОВЕРКИ
        $$("").shouldHave(size(0)); // ПРОВЕРКА НА РАЗМЕР (ПРО НОЛЬ ТИПА ЧТО НЕТУ, НЕ НАЙДЕНО)
        $$("").shouldBe(CollectionCondition.empty); // the same

        // ПРОВЕРКА НА КОЛИЧЕСТВО ТЕКСТА В КОЛЛЕКЦИИ (ДОЛЖНО БЫТЬ ВОТ ТОЛЬКО АЛЬФА БЕТА ГАММА
        // НЕ ДОЛЖНО БЫТЬ ЧЕГО-ТО ЧЕТВЕРТОГО
        // НО МОЖНО НАПРИМЕР АЛЬФА-ЦЕНТАВРА БЕТА ГАММА
        $$("").shouldHave(texts("Alfa", "Beta", "Gamma"));
        $$("").shouldHave(exactTexts("Alfa", "Beta", "Gamma")); // ТОЛЬКО ТАКОЙ ТЕКСТ 1:1

        // ИГНОРИРУЕТ ОЧЕРЕДНОСТЬ ЭЛЕМЕНТОВ
        $$("").shouldHave(textsInAnyOrder("Beta", "Gamma", "Alfa"));
        $$("").shouldHave(exactTextsCaseSensitiveInAnyOrder("Beta", "Gamma", "Alfa"));

        // НУЖНО УБЕДИТЬСЯ ЧТО В КОЛЛЕКЦИИ ЕСТЬ ХОТЯ БЫ ОДИН ЭЛЕМЕНТ С ТЕКСТОМ ГАММА
        $$("").shouldHave(itemWithText("Gamma"));

        $$("").shouldHave(sizeGreaterThan(0)); // РАЗМЕР ДОЛЖЕН БЫТЬ БОЛЬШЕ НУЛЯ
        $$("").shouldHave(sizeGreaterThanOrEqual(1)); // РАЗМЕР БОЛЬШЕ ИЛИ = 1
        $$("").shouldHave(sizeLessThan(3));// РАЗМЕР МЕНЬШЕ 3
        $$("").shouldHave(sizeLessThanOrEqual(2)); // РАЗМЕР МЕНЬШЕ ИЛИ = 2


    } //КОЛЛЕКЦИИ. ПОЗВОЛЯЮТ ИСКАТЬ МНОГО ЭЛЕМЕНТОВ УДОВЛЕТВОРЯЮЩИХ ОДНОМУ И ТОМУ ЖЕ УСЛОВИЮ

    void file_operation_examples() throws FileNotFoundException {

        File file1 = $("a.fileLink").download(); // РАБОТАЕТ ТОЛЬКО ЕСЛИ ЕСТЬ <a href=".."> ЛИНК
        File file2 = $("div").download(DownloadOptions.using(FileDownloadMode.FOLDER));
        // ВОТ ВТОРАЯ ОТЛИЧНАЯ, РАБОТАЕТ ПОЧТИ ВСЕГДА

        // upload. ТО ЕСТЬ ВГРУЗИТЬ ЧТО-ТО
        File file = new File("src/test/resources/readme.txt"); //ФАЙЛ БЕРЕМ И ДОБАВЛЯЕМ В resources
        $("#file-upload").uploadFile(file);
        $("#file-upload").uploadFromClasspath("readme.txt"); // ТУТ НЕ ЗАБЫВАЕМ ИМЯ ФАЙЛА
        $("uploadButton").click(); // И КЛИК ЧТОБЫ СДЕЛАТЬ ВГРУЗКУ
    } // ОПЕРАЦИИ С ФАЙЛАМИ

    void javascript_examples() {
        executeJavaScript("alert('selenide')"); // ПРОСТО ОБЫЧНЫЙ ЗАПУСК ДЖАВАСКРИПТА
        executeJavaScript("alert(arguments[0]+arguments[1])", "abc", 12);
        long fortytwo = executeJavaScript("return arguments[0]*arguments[1];", 6, 7);

    } // ЗАПУСК ДЖАВАСКРИПТА
}
