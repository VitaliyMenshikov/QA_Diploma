package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DBHelper;
import ru.netology.data.DataHelper;
import ru.netology.page.CreditPage;
import ru.netology.page.MainPage;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DBHelper.cleanDatabase;

public class CreditAPITests {

    private static List<DBHelper.PaymentEntity> payments;
    private static List<DBHelper.CreditRequestEntity> credits;
    private static List<DBHelper.OrderEntity> orders;
    private static final String creditUrl = "/credit";

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeAll
    public static void setUp() {
        cleanDatabase();
    }

    @AfterEach
    public void tearDown() {
        cleanDatabase();
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Epic(value = "Тестирование API")
    @Feature(value = "Тур в кредит с карты")
    @Story(value = "Позитивный. Тур в кредит с действующей карты, создание записи в таблице credit_request_entity")
    @Test
    public void shouldValidTestCreditCardApprovedEntityAdded() {
        var cardInfo = DataHelper.getValidCardApproved();
        DBHelper.getBody(cardInfo, creditUrl,200);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();

        assertEquals(0, payments.size());
        assertEquals(1, credits.size());
        assertEquals("APPROVED", DBHelper.getCreditStatus());
    }

    @Epic(value = "Тестирование API")
    @Feature(value = "Тур в кредит с карты")
    @Story(value = "Позитивный. Тур в кредит с действующей карты, создание записи в таблице orders")
    @Test
    public void shouldValidTestCreditCardApprovedOrdersAdded() {
        var cardInfo = DataHelper.getValidCardApproved();
        DBHelper.getBody(cardInfo, creditUrl, 200);
        orders = DBHelper.getOrders();

        assertEquals(1, orders.size());
        assertEquals(DBHelper.getBankIDForCredit(), DBHelper.getCreditID());
    }

    @Epic(value = "Тестирование API")
    @Feature(value = "Тур в кредит с карты")
    @Story(value = "Позитивный. Тур в кредит с недействующей карты, создание записи в таблице credit_request_entity")
    @Test
    public void shouldValidTestCreditCardDeclinedEntityAdded() {
        var cardInfo = DataHelper.getValidCardDeclined();
        DBHelper.getBody(cardInfo, creditUrl, 200);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();

        assertEquals(0, payments.size());
        assertEquals(1, credits.size());
        assertEquals("DECLINED", DBHelper.getCreditStatus());
    }

    @Epic(value = "Тестирование API")
    @Feature(value = "Тур в кредит с карты")
    @Story(value = "Позитивный. Тур в кредит с недействующей карты, создание записи в таблице orders")
    @Test
    public void shouldValidTestCreditCardDeclinedOrdersAdded() {
        var cardInfo = DataHelper.getValidCardDeclined();
        DBHelper.getBody(cardInfo, creditUrl, 200);
        orders = DBHelper.getOrders();

        assertEquals(1, orders.size());
        assertEquals(DBHelper.getBankIDForCredit(), DBHelper.getCreditID());
    }

    @Epic(value = "Тестирование API")
    @Feature(value = "Тур в кредит с карты")
    @Story(value = "Отправка пустого POST запроса кредита")
    @Test
    public void shouldCreditPOSTBodyEmpty() {
        var cardInfo = DataHelper.getAllEmpty();
        DBHelper.getBody(cardInfo, creditUrl, 400);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();
        orders = DBHelper.getOrders();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @Epic(value = "Тестирование API")
    @Feature(value = "Тур в кредит с карты")
    @Story(value = "Отправка POST запроса кредита с пустым значением number")
    @Test
    public void shouldCreditPOSTNumberEmpty() {
        var cardInfo = DataHelper.getCardEmpty();
        DBHelper.getBody(cardInfo, creditUrl, 400);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();
        orders = DBHelper.getOrders();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @Epic(value = "Тестирование API")
    @Feature(value = "Тур в кредит с карты")
    @Story(value = "Отправка POST запроса кредита с пустым значением month")
    @Test
    public void shouldCreditPOSTMonthEmpty() {
        var cardInfo = DataHelper.getMonthEmpty();
        DBHelper.getBody(cardInfo, creditUrl, 400);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();
        orders = DBHelper.getOrders();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @Epic(value = "Тестирование API")
    @Feature(value = "Тур в кредит с карты")
    @Story(value = "Отправка POST запроса кредита с пустым значением year")
    @Test
    public void shouldCreditPOSTYearEmpty() {
        var cardInfo = DataHelper.getYearEmpty();
        DBHelper.getBody(cardInfo, creditUrl, 400);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();
        orders = DBHelper.getOrders();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @Epic(value = "Тестирование API")
    @Feature(value = "Тур в кредит с карты")
    @Story(value = "Отправка POST запроса кредита с пустым значением holder")
    @Test
    public void shouldCreditPOSTHolderEmpty() {
        var cardInfo = DataHelper.getHolderEmpty();
        DBHelper.getBody(cardInfo, creditUrl, 400);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();
        orders = DBHelper.getOrders();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @Epic(value = "Тестирование API")
    @Feature(value = "Тур в кредит с карты")
    @Story(value = "Отправка POST запроса кредита с пустым значением cvc")
    @Test
    public void shouldCreditPOSTCvcEmpty() {
        var cardInfo = DataHelper.getCvcEmpty();
        DBHelper.getBody(cardInfo, creditUrl, 400);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();
        orders = DBHelper.getOrders();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @Epic(value = "Обращение к БД через форму орплаты")
    @Feature(value = "Тур в кредит с карты")
    @Story(value = "Тур в кредит с действующей карты(ввод данных через форму), создание записи в таблице credit_request_entity")
    @Test
    public void shouldValidTestFormCreditCardApprovedEntityAdded() {
        open("http://localhost:8080/");
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getValidCardApproved();
        CreditPage creditPage = mainPage.creditButtonClick();
        creditPage.inputData(CardInfo);
        creditPage.getSuccessNotification();

        assertEquals(0, payments.size());
        assertEquals(1, credits.size());
        assertEquals("APPROVED", DBHelper.getCreditStatus());
    }

    @Epic(value = "Обращение к БД через форму орплаты")
    @Feature(value = "Тур в кредит с карты")
    @Story(value = "Тур в кредит с недействующей карты(ввод данных через форму), создание записи в таблице credit_request_entity")
    @Test
    public void shouldValidTestFormCreditCardDeclinedEntityAdded() {
        open("http://localhost:8080/");
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getValidCardDeclined();
        CreditPage creditPage = mainPage.creditButtonClick();
        creditPage.inputData(CardInfo);
        creditPage.getErrorNotification();

        assertEquals(0, payments.size());
        assertEquals(1, credits.size());
        assertEquals("DECLINED", DBHelper.getCreditStatus());
    }
}