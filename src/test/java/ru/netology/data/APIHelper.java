package ru.netology.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;


public class APIHelper {

    @Value
    public static class TokenInfo {
        String token;
    }

    @Value
    public static class CardInfo {
        String id;
        String number;
        Integer balance;
    }

    @Value
    public static class TransferInfo {
        String from;
        String to;
        int amount;
    }

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void makeLogin(DataHelper.AuthInfo authInfo, Integer statusCode) {
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(authInfo)
                .when() // "когда"
                .post("/api/auth") // на какой путь относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(statusCode);
    }

    public static TokenInfo getToken(DataHelper.VerificationInfo verificationInfo, Integer statusCode) {
        return given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(verificationInfo)
                .when() // "когда"
                .post("/api/auth/verification") // на какой путь относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(statusCode)
                .extract()
                .body()
                .as(TokenInfo.class);
    }

    public static Map<String, Integer> getCardsBalance(String token, Integer statusCode) {
        CardInfo[] cardsInfo = given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/cards")
                .then().log().all()
                .statusCode(statusCode)
                .extract()
                .body()
                .as(CardInfo[].class);
        Map<String, Integer> cardsBalance = new HashMap<>();
        for (CardInfo cardInfo : cardsInfo) {
            cardsBalance.put(cardInfo.getId(), cardInfo.getBalance());
        }
        return cardsBalance;
    }

    public static void makeTransfer(String token, TransferInfo transferInfo, Integer statusCode) {
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .header("Authorization", "Bearer " + token)
                .body(transferInfo)
                .when() // "когда"
                .post("/api/transfer") // на какой путь относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(statusCode);
    }
}
