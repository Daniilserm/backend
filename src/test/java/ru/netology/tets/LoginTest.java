package ru.netology.tets;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.APIHelper;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

    @AfterEach
    void clean() {
        SQLHelper.cleanCodes();
    }

    @AfterAll
    static void cleanAll() {
        SQLHelper.cleanData();
    }


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

}
