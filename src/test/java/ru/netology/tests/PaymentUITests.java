package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DBHelper;
import ru.netology.data.DataHelper;
import ru.netology.page.MainPage;
import ru.netology.page.PaymentPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DBHelper.cleanDatabase;

public class PaymentUITests {
    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080/");
    }

    @AfterAll
    public static void tearDown() {
        cleanDatabase();
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    // Positive tests
    @Epic(value = "UI-тесты")
    @Feature(value = "Оплата тура картой")
    @Story(value = "Позитивный. Покупка тура с действующей карты (номер с пробелами)")
    @Test
    public void shouldValidTestCardApproved() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getValidCardApproved();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getSuccessNotification();
        assertEquals("APPROVED", DBHelper.getPaymentStatus());
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Оплата тура картой")
    @Story(value = "Позитивный. Покупка тура с действующей карты (номер без пробелов)")
    @Test
    public void shouldValidTestCardApprovedWithoutSpaces() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getValidCardApprovedWithoutSpaces();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getSuccessNotification();
        assertEquals("APPROVED", DBHelper.getPaymentStatus());
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Оплата тура картой")
    @Story(value = "Позитивный. Покупка тура с недействующей карты (номер с пробелами)")
    @Test
    public void shouldValidTestCardDeclined() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getValidCardDeclined();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getErrorNotification();
        assertEquals("DECLINED", DBHelper.getPaymentStatus());
    }

    //Negative tests/Validation
    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Номер карты 11 цифр")
    @Test
    public void shouldNumberField11char() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getRandomCard11char();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверный формат");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Номер карты 20 цифр")
    @Test
    public void shouldNumberField20char() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getRandomCard20char();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getErrorNotification();
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Номер карты 16 цифр")
    @Test
    public void shouldNumberField16char() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getRandomCard16char();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getErrorNotification();
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Номер карты 19 цифр")
    @Test
    public void shouldNumberField19char() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getRandomCard19char();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getErrorNotification();
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Номер карты символы")
    @Test
    public void shouldNumberFieldSymbols() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getRandomCardSymbols();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверный формат");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Номер карты пустое")
    @Test
    public void shouldNumberFieldEmpty() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getCardEmpty();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Поле обязательно для заполнения");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Месяц число больше 12")
    @Test
    public void shouldMonthFieldMore12() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getInvalidMonthOver12();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверно указан срок действия карты");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Месяц число 00")
    @Test
    public void shouldMonthFieldNull() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getInvalidMonthNull();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверно указан срок действия карты");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Месяц 1 число")
    @Test
    public void shouldMonthField1char() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getMonth1char();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getSuccessNotification();
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Месяц математические символы")
    @Test
    public void shouldMonthFieldSymbols() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getInvalidMonthSymbols();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверный формат");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Месяц меньше текущего")
    @Test
    public void shouldMonthFieldLessCurrent() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getInvalidMonthLessCurrent();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверно указан срок действия карты");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Месяц пустое")
    @Test
    public void shouldMonthFieldEmpty() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getMonthEmpty();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Поле обязательно для заполнения");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Год меньше текущего")
    @Test
    public void shouldYearFieldLessCurrent() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getInvalidYearLessCurrent();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Истёк срок действия карты");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Год число 00")
    @Test
    public void shouldYearFieldNull() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getYearNull();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Истёк срок действия карты");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Год пустое")
    @Test
    public void shouldYearFieldEmpty() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getYearEmpty();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Поле обязательно для заполнения");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Владелец с пробелом в середине")
    @Test
    public void shouldHolderFieldWithSpaceMiddle() {
        MainPage mainPage = new MainPage();
        var cardInfo = DataHelper.getValidHolderWithSpaceMiddle();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(cardInfo);
        paymentPage.getSuccessNotification();
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Владелец с дефисом в середине")
    @Test
    public void shouldHolderFieldWithDashMiddle() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getValidHolderWithDashMiddle();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getSuccessNotification();
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Владелец с дефисом в начале")
    @Test
    public void shouldHolderFieldWithDashFirst() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getHolderWithDashFirst();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверный формат");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Владелец с дефисом в конце")
    @Test
    public void shouldHolderFieldWithDashEnd() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getHolderWithDashEnd();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверный формат");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Владелец с пробелом в начале")
    @Test
    public void shouldHolderFieldWithSpaceFirst() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getHolderWithSpaceFirst();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверный формат");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Владелец с пробелом в конце")
    @Test
    public void shouldHolderFieldWithSpaceEnd() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getHolderWithSpaceEnd();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверный формат");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Владелец нижний регистр")
    @Test
    public void shouldHolderFieldLowercase() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getHolderLowercase();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getSuccessNotification();
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Владелец кириллицей")
    @Test
    public void shouldHolderFieldRu() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getHolderRu();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверный формат");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Владелец латиницей и числами")
    @Test
    public void shouldHolderFieldNumbers() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getHolderNumbers();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверный формат");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Владелец латиницей и спецсимволами")
    @Test
    public void shouldHolderFieldSymbols() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getHolderSymbols();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверный формат");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле Владелец пустое")
    @Test
    public void shouldHolderFieldEmpty() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getHolderEmpty();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Поле обязательно для заполнения");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле CVC/CVV 2 цифры")
    @Test
    public void shouldCVCField2char() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getInvalidCvc2char();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверный формат");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле CVC/CVV 4 цифры")
    @Test
    public void shouldCVCField4char() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getInvalidCvc4char();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getSuccessNotification();
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле CVC/CVV символами")
    @Test
    public void shouldCVCFieldSymbols() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getInvalidCvcSymbols();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Неверный формат");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Поле CVC/CVV пустое")
    @Test
    public void shouldCVCFieldEmpty() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getCvcEmpty();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getInputInvalid("Поле обязательно для заполнения");
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Проверка валидации")
    @Story(value = "Все поля пустые")
    @Test
    public void shouldAllFieldsEmpty() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getAllEmpty();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        $(byText("Номер карты")).parent().$(".input__sub").shouldBe(visible).
                shouldHave(text("Поле обязательно для заполнения"));
        $(byText("Месяц")).parent().$(".input__sub").shouldBe(visible).
                shouldHave(text("Поле обязательно для заполнения"));
        $(byText("Год")).parent().$(".input__sub").shouldBe(visible).
                shouldHave(text("Поле обязательно для заполнения"));
        $(byText("Владелец")).parent().$(".input__sub").shouldBe(visible).
                shouldHave(text("Поле обязательно для заполнения"));
        $(byText("CVC/CVV")).parent().$(".input__sub").shouldBe(visible).
                shouldHave(text("Поле обязательно для заполнения"));
    }
}