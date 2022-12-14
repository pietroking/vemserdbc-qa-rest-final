package br.com.dbccompany.service;

import br.com.dbccompany.dto.*;
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

    public RelatorioDTO[] buscarRealatorioPorId(Integer idPessoa){

        RelatorioDTO[] result =
                given()
                        .header("Authorization", tokenAdm)
                        .queryParam("idPessoa", idPessoa)
                .when()
                        .get(baseUri + "/pessoa/relatorio")
                .then()
                        .log().all()
                        .statusCode(200)
                        .extract().as(RelatorioDTO[].class)
                ;
        return result;
    }

    public PageDTOPessoaDTO buscarPessoas(){
        PageDTOPessoaDTO result =
                given()
                        .header("Authorization", tokenAdm)
                .when()
                    .get(baseUri + "/pessoa")
                .then()
                    .log().all()
                    .extract().as(PageDTOPessoaDTO.class)
                ;
        return result;
    }

    public PessoaDTO[] buscarPessoasPorNome(String nome){

        PessoaDTO[] result =
                given()
                        .header("Authorization", tokenAdm)
                        .queryParam("nome", nome)
                .when()
                        .get(baseUri + "/pessoa/byname")
                .then()
                        .log().all()
                        .statusCode(200)
                        .extract().as(PessoaDTO[].class)
                ;
        return result;
    }

    public PessoaDTO buscarPessoasPorCpf(String cpf){

        PessoaDTO result =
                given()
                        .header("Authorization", tokenAdm)
                        .pathParam("cpf", cpf)
                .when()
                        .get(baseUri + "/pessoa/{cpf}/cpf")
                .then()
                        .log().all()
                        .extract().as(PessoaDTO.class)
                ;
        return result;
    }

    public PessoaDTO[] buscarPessoasPorDataNascimento(String data, String dtFinal){

        PessoaDTO[] result =
                given()
                        .header("Authorization", tokenAdm)
                        .queryParam("data", data)
                        .queryParam("dtFinal", dtFinal)
                .when()
                        .get(baseUri + "/pessoa/data-nascimento")
                .then()
                        .log().all()
                        .extract().as(PessoaDTO[].class)
                ;
        return result;
    }

    public PessoaDTO[] buscarPessoasComEnderecos(Integer idPessoa){

        PessoaDTO[] result =
                given()
                        .header("Authorization", tokenAdm)
                        .queryParam("idPessoa", idPessoa)
                .when()
                        .get(baseUri + "/pessoa/lista-com-enderecos")
                .then()
                        .log().all()
                        .extract().as(PessoaDTO[].class)
                ;
        return result;
    }

    public ResponseDTO buscarPessoasComEnderecosIdInexistente(Integer idPessoa){

        ResponseDTO result =
                given()
                        .header("Authorization", tokenAdm)
                        .queryParam("idPessoa", idPessoa)
                .when()
                        .get(baseUri + "/pessoa/lista-com-enderecos")
                .then()
                        .log().all()
                        .extract().as(ResponseDTO.class)
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

    public PessoaDTO putPessoa(String requstBody, Integer idPessoa){

        PessoaDTO result =
                given()
                        .header("Authorization", tokenAdm)
                        .contentType(ContentType.JSON)
                        .body(requstBody)
                        .pathParam("idPessoa", idPessoa)
                .when()
                        .put(baseUri + "/pessoa/{idPessoa}")
                .then()
                        .log().all()
                        .extract().as(PessoaDTO.class)
                ;
        return result;
    }

    public Response deletePessoa(Integer idPessoa){

         Response result =
                 (Response) given()
                         .header("Authorization", tokenAdm)
                         .pathParam("idPessoa", idPessoa)
                 .when()
                         .delete(baseUri + "/pessoa/{idPessoa}")
                 .then()
                         .log().all()
                         .extract().response()
                 ;
        return result;
    }

    public PessoaDTO[] buscarPessoaListaCompleta(Integer idPessoa){
        PessoaDTO[] result =
            given()
                .header("Authorization", tokenAdm)
                    .queryParam("idPessoa",idPessoa)
            .when()
                .get(baseUri+"/pessoa/lista-completa")
            .then()
                .log().all()
                    .extract().as(PessoaDTO[].class);
        return result;
    }

    public PessoaDTO[] buscarPessoaListaCompleta(){
        PessoaDTO[] result =
            given()
                .header("Authorization", tokenAdm)
            .when()
                .get(baseUri+"/pessoa/lista-completa")
            .then()
                .log().all()
                .extract().as(PessoaDTO[].class);
        return result;
    }
    public PessoaDTO[] buscarPessoaContato(Integer idPessoa){
        PessoaDTO[] result =
            given()
                .header("Authorization", tokenAdm)
                .queryParam("idPessoa",idPessoa)
            .when()
                .get(baseUri+"/pessoa/lista-com-contatos")
            .then()
                .log().all()
                .extract().as(PessoaDTO[].class);
        return result;
    }

    public PessoaDTO[] buscarPessoaContato(){
        PessoaDTO[] result =
            given()
                .header("Authorization", tokenAdm)
            .when()
                .get(baseUri+"/pessoa/lista-com-contatos")
            .then()
                .log().all()
                .extract().as(PessoaDTO[].class);
        return result;
    }

}
