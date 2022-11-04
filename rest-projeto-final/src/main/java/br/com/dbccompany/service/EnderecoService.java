package br.com.dbccompany.service;

import br.com.dbccompany.dto.EnderecoDTO;
import br.com.dbccompany.dto.EnderecoListaDTO;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class EnderecoService {
    String baseUri = "http://vemser-dbc.dbccompany.com.br:39000/vemser/dbc-pessoa-api";
    public EnderecoListaDTO pegarEnderecos(String token){
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
    public EnderecoListaDTO pegarEnderecos(String token,Integer pagina){
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
    public EnderecoListaDTO pegarEnderecos(String token,int pageSize){
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

    public Response pegarEnderecosPorPais(String token){
            Response result =
                (Response) given()
                    .header("Authorization", token)
                .when()
                    .get(baseUri+"/endereco/retorna-por-pais")
                .then()
                    .log().all().extract().response();
        return result;
    }

    public EnderecoDTO[] pegarEnderecosPorPais(String token,String pais){
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

}
