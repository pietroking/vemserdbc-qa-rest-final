package br.com.dbccompany.aceitacao;

import br.com.dbccompany.dto.PageDTOPessoaDTO;
import br.com.dbccompany.dto.PessoaDTO;
import br.com.dbccompany.dto.RelatorioDTO;
import br.com.dbccompany.dto.ResponseDTO;
import br.com.dbccompany.service.PessoaService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;

public class PessoaAceitacaoTest {

    PessoaService service = new PessoaService();

    public PessoaAceitacaoTest() throws IOException {
    }

    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }
    String jsonBodyPessoa = lerJson("src/test/resources/data/pessoaTest.json");
    String jsonBodyPessoaReplace = lerJson("src/test/resources/data/pessoaReplaceTest.json");
    String jsonBodyPessoaErro = lerJson("src/test/resources/data/pessoaSemNome.json");
    String jsonBodyContato = lerJson("src/test/resources/data/contatoTest.json");
    String jsonBodyEndereco = lerJson("src/test/resources/data/enderecoTest.json");

    @Test
    public void deveRetornarRelatorioPessoa(){

        RelatorioDTO[] resultService = service.buscarRealatorio();

        Assert.assertEquals(resultService[0].getNomePessoa().toUpperCase(), "Maicon Machado Gerardi".toUpperCase());
    }

    @Test
    public void deveRetornarRelatorioPessoaPorId(){
        PessoaDTO resultService = service.postPessoa(jsonBodyPessoa);
        RestAssured.defaultParser = Parser.JSON;

        RelatorioDTO[] resultService2 = service.buscarRealatorioPorId(resultService.getIdPessoa());

        Assert.assertEquals(resultService2[0].getNomePessoa().toUpperCase(), "Joker".toUpperCase());
        service.deletePessoa(resultService.getIdPessoa());
    }

//    @Test
//    public void deveRetornarListaDePessoa(){
//
//        PageDTOPessoaDTO[] resultService = service.buscarPessoas();
//
//        Assert.assertEquals(resultService, "Maicon Machado Gerardi".toUpperCase());
//    }

    @Test
    public void deveAddPessoa(){

        PessoaDTO resultService = service.postPessoa(jsonBodyPessoa);
        RestAssured.defaultParser = Parser.JSON;
        Assert.assertEquals(resultService.getNome().toUpperCase(), "joker".toUpperCase());

        service.deletePessoa(resultService.getIdPessoa());
    }

    @Test
    public void deveAddPessoaSemNome(){

        ResponseDTO resultService = service.postPessoaError(jsonBodyPessoaErro);
        RestAssured.defaultParser = Parser.JSON;

        Assert.assertEquals(resultService.getStatus(), "400");
        assertThat(resultService.getErrors().size(), Matchers.is(1));
    }

    @Test
    public void deveAtualizarPessoa(){

        PessoaDTO resultService = service.postPessoa(jsonBodyPessoa);
        RestAssured.defaultParser = Parser.JSON;
        PessoaDTO resultService2 = service.putPessoa(jsonBodyPessoaReplace, resultService.getIdPessoa());
        Assert.assertEquals(resultService2.getNome().toUpperCase(), "harleyquinn".toUpperCase());

        service.deletePessoa(resultService.getIdPessoa());
    }

    @Test
    public void deveDeletarPessoa(){

        PessoaDTO resultService = service.postPessoa(jsonBodyPessoa);

        Response resultService2 =  service.deletePessoa(resultService.getIdPessoa());
        Assert.assertEquals(resultService2.statusCode(), 200);
    }
}