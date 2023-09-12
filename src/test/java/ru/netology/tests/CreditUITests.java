package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DBHelper;
import ru.netology.data.DataHelper;
import ru.netology.page.CreditPage;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DBHelper.cleanDatabase;

public class CreditUITests {
    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080/");
    }

    @AfterAll
    static void tearDown() {
        cleanDatabase();
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    //Positive tests
    @Epic(value = "UI-тесты")
    @Feature(value = "Оплата тура в кредит")
    @Story(value = "Позитивный. Покупка тура в кредит с действующей карты (номер с пробелами)")
    @Test
    public void shouldValidCreditTestCardApproved() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getValidCardApproved();
        CreditPage creditPage = mainPage.creditButtonClick();
        creditPage.inputData(CardInfo);
        creditPage.getSuccessNotification();
        assertEquals("APPROVED", DBHelper.getCreditStatus());
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Оплата тура в кредит")
    @Story(value = "Позитивный. Покупка тура в кредит с действующей карты (номер без пробелов)")
    @Test
    public void shouldValidCreditTestCardApprovedWithoutSpaces() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getValidCardApprovedWithoutSpaces();
        CreditPage creditPage = mainPage.creditButtonClick();
        creditPage.inputData(CardInfo);
        creditPage.getSuccessNotification();
        assertEquals("APPROVED", DBHelper.getCreditStatus());
    }

    @Epic(value = "UI-тесты")
    @Feature(value = "Оплата тура в кредит")
    @Story(value = "Позитивный. Покупка тура в кредит с недействующей карты (номер с пробелами)")
    @Test
    public void shouldValidCreditTestCardDeclined() {
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getValidCardDeclined();
        CreditPage creditPage = mainPage.creditButtonClick();
        creditPage.inputData(CardInfo);
        creditPage.getErrorNotification();
        assertEquals("DECLINED", DBHelper.getCreditStatus());
    }
}
