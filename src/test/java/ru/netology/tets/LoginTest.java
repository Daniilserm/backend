package ru.netology.tets;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.APIHelper;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

//    @AfterEach
//    void clean() {
//        SQLHelper.cleanCodes();
//    }
//
//    @AfterAll
//    static void cleanAll() {
//        SQLHelper.cleanData();
//    }


    @Test
    void shouldTransferMoneyBetweenOwnCardsSecondToFirst() {
        var auth = DataHelper.getAuthInfoValid();
        APIHelper.makeLogin(auth, 200);
        var verificationCode = SQLHelper.getVerificationCodeFor();
        var verificationInfo = new DataHelper.VerificationInfo(auth.getLogin(), verificationCode.getCode());
        var tokenInfo = APIHelper.getToken(verificationInfo, 200);
        var cardsBalance = APIHelper.getCardsBalance(tokenInfo.getToken(), 200);
        var firstCardBalance = cardsBalance.get(DataHelper.getFirstCardInfo().getTestId());
        var secondCardBalance = cardsBalance.get(DataHelper.getSecondCardInfo().getTestId());
        var amount = DataHelper.generateValidAmount(secondCardBalance);
        var transferInfo = new APIHelper.TransferInfo(
                DataHelper.getSecondCardInfo().getCardNumber(),
                DataHelper.getFirstCardInfo().getCardNumber(), amount);
        APIHelper.makeTransfer(tokenInfo.getToken(), transferInfo, 200);
        cardsBalance = APIHelper.getCardsBalance(tokenInfo.getToken(), 200);
        var actualFirstCardBalance = cardsBalance.get(DataHelper.getFirstCardInfo().getTestId());
        var actualSecondCardBalance = cardsBalance.get(DataHelper.getSecondCardInfo().getTestId());
        var expectedFirstCardBalance = firstCardBalance + amount;
        var expectedSecondCardBalance = secondCardBalance - amount;
        assertEquals(actualFirstCardBalance, expectedFirstCardBalance);
        assertEquals(actualSecondCardBalance, expectedSecondCardBalance);
    }


//    $("[data-test-id=login] input").setValue(registeredUser.getLogin());
//    $("[data-test-id=password] input").setValue(wrongPassword);
//    $(byText("Продолжить")).click();
//    $("[data-test-id=error-notification]")
//                .shouldHave(Condition.text("Неверно указан логин или пароль"), Duration.ofSeconds(15))
//            .shouldBe(Condition.visible);
//
//    var firstCardInfo = getFirstCardInfo();
//    var secondCardInfo = getSecondCardInfo();
//    var firstCardBalance = dashboardPage.getCardBalance(0);
//    var secondCardBalance = dashboardPage.getCardBalance(1);
//    var amount = generateValidAmount(secondCardBalance);
//    var transferPage = dashboardPage.selectCardTransfer(firstCardInfo);
//    dashboardPage = transferPage.validTransfer(String.valueOf(amount), secondCardInfo);
//    var actualFirstCardBalance = dashboardPage.getCardBalance(0);
//    var actualSecondCardBalance = dashboardPage.getCardBalance(1);
//    var expectedFirstCardBalance = firstCardBalance + amount;
//    var expectedSecondCardBalance = secondCardBalance - amount;
//    assertEquals(actualFirstCardBalance, expectedFirstCardBalance);
//    assertEquals(actualSecondCardBalance, expectedSecondCardBalance);

}
