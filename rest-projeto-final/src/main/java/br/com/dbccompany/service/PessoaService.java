package br.com.dbccompany.service;

import br.com.dbccompany.dto.PageDTOPessoaDTO;
import br.com.dbccompany.dto.PessoaDTO;
import br.com.dbccompany.dto.RelatorioDTO;
import br.com.dbccompany.dto.ResponseDTO;
import br.com.dbccompany.utils.Login;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

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

    public PageDTOPessoaDTO[] buscarPessoas(){

        PageDTOPessoaDTO[] result =
                given()
                        .header("Authorization", tokenAdm)
                .when()
                        .get(baseUri + "/pessoa")
                .then()
                        .log().all()
                        .statusCode(200)
                        .extract().as(PageDTOPessoaDTO[].class)
                ;
        return result;
    }

    public PessoaDTO postPessoa(String requstBody){

        PessoaDTO result =
                given()
                        .header("Authorization", tokenAdm)
                        .contentType(ContentType.JSON)
                        .body(requstBody)
                .when()
                        .post(baseUri + "/pessoa")
                .then()
                        .log().all()
                        .extract().as(PessoaDTO.class)
                ;
        return result;
    }

    public ResponseDTO postPessoaError(String requstBody){

        ResponseDTO result =
                given()
                        .header("Authorization", tokenAdm)
                        .contentType(ContentType.JSON)
                        .body(requstBody)
                .when()
                        .post(baseUri + "/pessoa")
                .then()
                        .log().all()
                        .extract().as(ResponseDTO.class)
                ;
        return result;
    }

    public Response deletePessoa(String idPessoa){

         Response result =
                 (Response) given()
                         .header("Authorization", tokenAdm)
                         .pathParam("idPessoa", idPessoa)
                 .when()
                         .delete(baseUri + "/pessoa/{idPessoa}")
                 .then()
                         .log().all()
                         .statusCode(200)
                         .extract().response()
                 ;
        return result;
    }

}
