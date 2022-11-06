package br.com.dbccompany.service;

import br.com.dbccompany.dto.EnderecoDTO;
import br.com.dbccompany.dto.EnderecoListaDTO;
import br.com.dbccompany.utils.Login;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class EnderecoService {
    String token = new Login().authenticationAdmin();
    String baseUri = "http://vemser-dbc.dbccompany.com.br:39000/vemser/dbc-pessoa-api";
    public EnderecoListaDTO pegarEnderecos(){
        EnderecoListaDTO result =
            given()
                    .header("Authorization", token)
            .when()
                .get(baseUri+"/endereco")
            .then()
                    .log().all()
                    .extract().as(EnderecoListaDTO.class);
        return result;
    }
    public EnderecoListaDTO pegarEnderecos(Integer pagina){
        EnderecoListaDTO result =
            given()
                .header("Authorization", token)
                .queryParam("pagina",pagina)
            .when()
                .get(baseUri+"/endereco")
            .then()
                .log().all()
                .extract().as(EnderecoListaDTO.class);
        return result;
    }
    public EnderecoListaDTO pegarEnderecos(int pageSize){
        EnderecoListaDTO result =
                given()
                    .header("Authorization", token)
                    .queryParam("tamanhoDasPaginas",pageSize)
                .when()
                    .get(baseUri+"/endereco")
                .then()
                    .log().all()
                    .extract().as(EnderecoListaDTO.class)
                ;
        return result;
    }

    public EnderecoDTO pegarEnderecoPorIdEndereco(Integer idEndereco){
        EnderecoDTO result =
            given()
                .header("Authorization", token)
                .pathParam("idEndereco",idEndereco)
            .when()
                .get(baseUri+"/endereco/{idEndereco}")
            .then()
                .log().all()
                .extract().as(EnderecoDTO.class)
            ;
        return result;
    }

    public Response pegarEnderecosPorPais(){
            Response result =
                given()
                    .header("Authorization", token)
                .when()
                    .get(baseUri+"/endereco/retorna-por-pais")
                .then()
                    .log().all().extract().response();
        return result;
    }

    public EnderecoDTO[] pegarEnderecosPorPais(String pais){
        EnderecoDTO[] result =
            given()
                .header("Authorization", token)
                .queryParam("País",pais)
            .when()
                .get(baseUri+"/endereco/retorna-por-pais")
            .then()
                .log().all().extract().as(EnderecoDTO[].class);
        return result;
    }

    public Response pegarEnderecosPorIdPessoa(){
        Response result =
            given()
                .header("Authorization", token)
            .when()
                .get(baseUri+"/endereco/retorna-por-id-pessoa")
            .then()
                .log().all()
                .extract().response();
        return result;
    }

    public EnderecoDTO[] pegarEnderecosPorIdPessoa(Integer idPessoa){
        EnderecoDTO[] result =
            given()
                .header("Authorization", token)
                .queryParam("idPessoa",idPessoa)
            .when()
                .get(baseUri+"/endereco/retorna-por-id-pessoa")
            .then()
                .log().all()
                .extract().as(EnderecoDTO[].class);
        return result;
    }
    public EnderecoDTO adicionarEndereco(Integer idPessoa,String body){
        EnderecoDTO result =
            given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .pathParam("idPessoa",idPessoa)
                .queryParam("idPessoa", idPessoa)
                .body(body)
            .when()
                .post(baseUri+"/endereco/{idPessoa}")
            .then()
                .log().all()
                .extract().as(EnderecoDTO.class)
            ;
        return result;
    }
    public Response removerEndereco(Integer idEndereco){
        Response result =
        given()
            .header("Authorization", token)
            .pathParam("idEndereco",idEndereco)
        .when()
            .delete(baseUri+"/endereco/{idEndereco}")
        .then()
            .log().all()
            .extract().response();
        return result;
    }

}
