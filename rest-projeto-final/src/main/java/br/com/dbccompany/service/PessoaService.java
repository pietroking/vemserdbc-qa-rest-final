package br.com.dbccompany.service;

import br.com.dbccompany.dto.RelatorioDTO;
import br.com.dbccompany.utils.Login;

import static io.restassured.RestAssured.*;

public class PessoaService {

    String baseUri = "http://vemser-dbc.dbccompany.com.br:39000/vemser/dbc-pessoa-api";

    String tokenAdm = new Login().authenticationAdmin();
    public RelatorioDTO[] buscarRealatorio(){

        RelatorioDTO[] result =
                given()
                        .header("Authorization", tokenAdm)
                .when()
                        .get(baseUri + "/pessoa/relatorio")
                .then()
                        .log().all()
                        .statusCode(200)
                        .extract().as(RelatorioDTO[].class)
                ;
        return result;
    }
}
