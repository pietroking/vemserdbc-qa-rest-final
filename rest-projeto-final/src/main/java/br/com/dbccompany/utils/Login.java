package br.com.dbccompany.utils;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;

public class Login {

    String baseUri = "http://vemser-dbc.dbccompany.com.br:39000/vemser/dbc-pessoa-api";

    public String authenticationAdmin(){

        String result =
                given()
                        .log().all()
                        .contentType(ContentType.JSON)
                        .body("{\"login\" : \"grupo04qa\", \"senha\" : \"1234\"}")
                .when()
                        .post(baseUri + "/auth")
                .then()
                        .log().all()
                        .statusCode(200)
                        .extract().asString()
                ;
        return result;
    }
}