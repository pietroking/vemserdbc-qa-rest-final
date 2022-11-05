package br.com.dbccompany.service;

import br.com.dbccompany.dto.LoginCreateDTO;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.*;

public class AuthService {
    String baseUri = "http://vemser-dbc.dbccompany.com.br:39000/vemser/dbc-pessoa-api";

    public String fazerLogin(String login,String senha){
        LoginCreateDTO data =  new LoginCreateDTO(login,senha);
        String result =
            given()
                .contentType(ContentType.JSON)
                .body(data)
            .when()
                .post(baseUri+"/auth")
            .then()
                .log().all()
                .extract().asString();
        return result;
    }
}
