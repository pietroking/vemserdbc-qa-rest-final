package br.com.dbccompany.service;

import br.com.dbccompany.dto.ContatoDTO;
import br.com.dbccompany.dto.EnderecoListaDTO;
import br.com.dbccompany.utils.Login;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ContatoService {
    String baseUri = "http://vemser-dbc.dbccompany.com.br:39000/vemser/dbc-pessoa-api";
    String token = new Login().authenticationAdmin();

    public ContatoDTO[] pegarContatos(){
        ContatoDTO[] result =
            given()
                .header("Authorization", token)
            .when()
                .get(baseUri+"/contato")
            .then()
                .log().all()
                .extract().as(ContatoDTO[].class);
        return result;
    }
    public ContatoDTO[] pegarContatos(Integer idPessoa){
        ContatoDTO[] result =
            given()
                .pathParam("idPessoa",idPessoa)
                .header("Authorization", token)
            .when()
                .get(baseUri+"/contato/{idPessoa}")
            .then()
                .log().all()
                .extract().as(ContatoDTO[].class);
        return result;
    }
    public Response pegarContatoPorIdResponse(Integer idPessoa){
        Response result =
            given()
                .pathParam("idPessoa",idPessoa)
                .header("Authorization", token)
            .when()
                .get(baseUri+"/contato/{idPessoa}")
            .then()
                .log().all()
                .extract().response();
        return result;
    }

    public ContatoDTO criarContato(Integer idPessoa,String contato){
        ContatoDTO result =
            given()
                .contentType(ContentType.JSON)
                .body(contato)
                .pathParam("idPessoa",idPessoa)
                .header("Authorization",token)
            .when()
                .post(baseUri+"/contato/{idPessoa}")
            .then()
                .log().all()
                .extract().as(ContatoDTO.class);
        return result;
    }

    public Response deletarContato(Integer idContato){
        Response result =
            given()
                .pathParam("idContato",idContato)
                .header("Authorization",token)
            .when()
                    .delete(baseUri+"/contato/{idContato}")
            .then()
                .extract().response();
        return result;
    }

    public ContatoDTO editarContato(Integer idContato, String novoContato) {
        ContatoDTO result =
            given()
                .contentType(ContentType.JSON)
                .body(novoContato)
                .pathParam("idContato", idContato)
                .header("Authorization", token)
            .when()
                .put(baseUri + "/contato/{idContato}")
            .then()
                .log().all()
                .extract().as(ContatoDTO.class);
        return result;
    }
}