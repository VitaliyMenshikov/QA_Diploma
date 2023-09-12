package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DBHelper;
import ru.netology.data.DataHelper;
import ru.netology.page.MainPage;
import ru.netology.page.PaymentPage;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DBHelper.cleanDatabase;


public class PaymentAPITests {

    private static List<DBHelper.PaymentEntity> payments;
    private static List<DBHelper.CreditRequestEntity> credits;
    private static List<DBHelper.OrderEntity> orders;
    private static final String paymentUrl = "/payment";

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

    @DisplayName("Позитивный. Покупка тура с действующей карты, создание записи в таблице payment_entity")
    @Test
    public void shouldValidTestCardApprovedEntityAdded() {
        var cardInfo = DataHelper.getValidCardApproved();
        DBHelper.getBody(cardInfo, paymentUrl,200);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();

        assertEquals(1, payments.size());
        assertEquals(0, credits.size());
        assertEquals("APPROVED", DBHelper.getPaymentStatus());
    }

    @DisplayName("Позитивный. Покупка тура с действующей карты, создание записи в таблице orders")
    @Test
    public void shouldValidTestCardApprovedOrdersAdded() {
        var cardInfo = DataHelper.getValidCardApproved();
        DBHelper.getBody(cardInfo, paymentUrl, 200);
        orders = DBHelper.getOrders();

        assertEquals(1, orders.size());
        assertEquals(DBHelper.getBankIDForPayment(), DBHelper.getPaymentID());
    }

    @DisplayName("Позитивный. Покупка тура с недействующей карты, создание записи в таблице payment_entity")
    @Test
    public void shouldValidTestCardDeclinedEntityAdded() {
        var cardInfo = DataHelper.getValidCardDeclined();
        DBHelper.getBody(cardInfo, paymentUrl, 200);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();

        assertEquals(1, payments.size());
        assertEquals(0, credits.size());
        assertEquals("DECLINED", DBHelper.getPaymentStatus());
    }

    @DisplayName("Позитивный. Покупка тура с недействующей карты, создание записи в таблице orders")
    @Test
    public void shouldValidTestCardDeclinedOrdersAdded() {
        var cardInfo = DataHelper.getValidCardDeclined();
        DBHelper.getBody(cardInfo, paymentUrl, 200);
        orders = DBHelper.getOrders();

        assertEquals(1, orders.size());
        assertEquals(DBHelper.getBankIDForPayment(), DBHelper.getPaymentID());
    }

    @DisplayName("Отправка пустого POST запроса платежа")
    @Test
    public void shouldPOSTBodyEmpty() {
        var cardInfo = DataHelper.getAllEmpty();
        DBHelper.getBody(cardInfo, paymentUrl, 400);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();
        orders = DBHelper.getOrders();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @DisplayName("Отправка POST запроса платежа с пустым значением number")
    @Test
    public void shouldPOSTNumberEmpty() {
        var cardInfo = DataHelper.getCardEmpty();
        DBHelper.getBody(cardInfo, paymentUrl, 400);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();
        orders = DBHelper.getOrders();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @DisplayName("Отправка POST запроса платежа с пустым значением month")
    @Test
    public void shouldPOSTMonthEmpty() {
        var cardInfo = DataHelper.getMonthEmpty();
        DBHelper.getBody(cardInfo, paymentUrl, 400);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();
        orders = DBHelper.getOrders();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @DisplayName("Отправка POST запроса платежа с пустым значением year")
    @Test
    public void shouldPOSTYearEmpty() {
        var cardInfo = DataHelper.getYearEmpty();
        DBHelper.getBody(cardInfo, paymentUrl, 400);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();
        orders = DBHelper.getOrders();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @DisplayName("Отправка POST запроса платежа с пустым значением holder")
    @Test
    public void shouldPOSTHolderEmpty() {
        var cardInfo = DataHelper.getHolderEmpty();
        DBHelper.getBody(cardInfo, paymentUrl, 400);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();
        orders = DBHelper.getOrders();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @DisplayName("Отправка POST запроса платежа с пустым значением cvc")
    @Test
    public void shouldPOSTCvcEmpty() {
        var cardInfo = DataHelper.getCvcEmpty();
        DBHelper.getBody(cardInfo, paymentUrl, 400);
        payments = DBHelper.getPayments();
        credits = DBHelper.getCreditRequests();
        orders = DBHelper.getOrders();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @DisplayName("Покупка тура с действующей карты(ввод данных через форму), создание записи в таблице payment_entity")
    @Test
    public void shouldValidFormTestCardApprovedEntityAdded() {
        open("http://localhost:8080/");
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getValidCardApproved();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getSuccessNotification();

        assertEquals(1, payments.size());
        assertEquals(0, credits.size());
        assertEquals("APPROVED", DBHelper.getPaymentStatus());
    }

    @DisplayName("Покупка тура с недействующей карты(ввод данных через форму), создание записи в таблице payment_entity")
    @Test
    public void shouldValidFormTestCardDeclinedEntityAdded() {
        open("http://localhost:8080/");
        MainPage mainPage = new MainPage();
        var CardInfo = DataHelper.getValidCardDeclined();
        PaymentPage paymentPage = mainPage.paymentButtonClick();
        paymentPage.inputData(CardInfo);
        paymentPage.getErrorNotification();

        assertEquals(1, payments.size());
        assertEquals(0, credits.size());
        assertEquals("DECLINED", DBHelper.getPaymentStatus());
    }
}
