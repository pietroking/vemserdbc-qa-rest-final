package br.com.dbccompany.aceitacao;

import br.com.dbccompany.dto.*;
import br.com.dbccompany.service.EnderecoService;
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
    EnderecoService serviceEndereco = new EnderecoService();

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

    //    Assert.assertEquals(resultService[0].getNomePessoa().toUpperCase(), "Maicon Machado Gerardi".toUpperCase());
    }

    @Test
    public void deveRetornarRelatorioPessoaPorId(){
        PessoaDTO resultService = service.postPessoa(jsonBodyPessoa);
        RestAssured.defaultParser = Parser.JSON;

        RelatorioDTO[] resultService2 = service.buscarRealatorioPorId(resultService.getIdPessoa());

        Assert.assertEquals(resultService2[0].getNomePessoa().toUpperCase(), resultService.getNome().toUpperCase());
        service.deletePessoa(resultService.getIdPessoa());
    }

    @Test
    public void deveRetornarRelatorioPessoaPorIdInexistente(){

        RelatorioDTO[] resultService2 = service.buscarRealatorioPorId(99999);
        assertThat(resultService2,Matchers.is(Matchers.emptyArray()));

//        Assert.assertEquals(resultService2[0].getNomePessoa().toUpperCase(), "Joker".toUpperCase());
    }

    @Test
    public void deveRetornarListaDePessoaPorNome(){

        PessoaDTO resultServiceteste = service.postPessoa(jsonBodyPessoa);
        PessoaDTO resultServiceteste2 = service.postPessoa(jsonBodyPessoa);
        RestAssured.defaultParser = Parser.JSON;
        PessoaDTO[] resultService = service.buscarPessoasPorNome(resultServiceteste.getNome());

        Assert.assertEquals(resultService[0].getNome().toUpperCase(), resultServiceteste.getNome().toUpperCase());
        Assert.assertEquals(resultService[resultService.length-1].getNome().toUpperCase(), resultServiceteste.getNome().toUpperCase());

        service.deletePessoa(resultServiceteste.getIdPessoa());
        service.deletePessoa(resultServiceteste2.getIdPessoa());
    }

    @Test
    public void deveRetornarListaDePessoaPorNomeInvalido(){

        PessoaDTO[] resultService = service.buscarPessoasPorNome("HAHAHAHA");

        assertThat(resultService, Matchers.is(Matchers.emptyArray()));
    }

    @Test
    public void deveRetornarPessoaPorCpf(){

        PessoaDTO resultServiceteste = service.postPessoa(jsonBodyPessoa);
        RestAssured.defaultParser = Parser.JSON;
        PessoaDTO resultService = service.buscarPessoasPorCpf(resultServiceteste.getCpf());

//        Assert.assertEquals(resultService.getStatus(), "200");
        Assert.assertEquals(resultService.getNome().toUpperCase(), resultServiceteste.getNome().toUpperCase());

        service.deletePessoa(resultServiceteste.getIdPessoa());
    }

    @Test
    public void deveRetornarPessoaPorCpfMaisDeUmCpfIgual(){

        PessoaDTO resultServiceteste = service.postPessoa(jsonBodyPessoa);
        PessoaDTO resultServiceteste2 = service.postPessoa(jsonBodyPessoa);
        RestAssured.defaultParser = Parser.JSON;
        ResponseDTO resultService = service.buscarPessoasPorCpf(resultServiceteste.getCpf());

        Assert.assertEquals(resultService.getStatus(), "500");

        service.deletePessoa(resultServiceteste.getIdPessoa());
        service.deletePessoa(resultServiceteste2.getIdPessoa());
    }

    @Test
    public void deveRetornarListaDePessoaComEnderecos(){

        PessoaDTO resultServiceteste = service.postPessoa(jsonBodyPessoaReplace);
        RestAssured.defaultParser = Parser.JSON;
        serviceEndereco.adicionarEndereco(resultServiceteste.getIdPessoa(), jsonBodyEndereco);
        PessoaDTO[] resultService = service.buscarPessoasComEnderecos(resultServiceteste.getIdPessoa());

        Assert.assertEquals(resultService[0].getEnderecos()[0].getCidade().toUpperCase(), "HAHAHAHA".toUpperCase());

        service.deletePessoa(resultServiceteste.getIdPessoa());
    }

    @Test
    public void deveRetornarListaDePessoaComEnderecosComIdInexistente(){

        ResponseDTO resultService = service.buscarPessoasComEnderecosIdInexistente(9999999);

        Assert.assertEquals(resultService.getStatus(), "404");
    }

//    @Test
//    public void deveRetornarListaDePessoaPeriodoDataNascimento(){
//
//        PessoaDTO resultServiceteste = service.postPessoa(jsonBodyPessoa);
//        PessoaDTO resultServiceteste2 = service.postPessoa(jsonBodyPessoa);
//        RestAssured.defaultParser = Parser.JSON;
//        PessoaDTO[] resultService = service.buscarPessoasPorDataNascimento("1999-09-09", "1999-09-09");
//
//        Assert.assertEquals(resultService[0].getNome().toUpperCase(), resultServiceteste.getNome().toUpperCase());
//        Assert.assertEquals(resultService[resultService.length-1].getNome().toUpperCase(), resultServiceteste.getNome().toUpperCase());
//
//        service.deletePessoa(resultServiceteste.getIdPessoa());
//        service.deletePessoa(resultServiceteste2.getIdPessoa());
//    }

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
    public void deveAtualizarPessoaErrado(){

        PessoaDTO resultService = service.postPessoa(jsonBodyPessoa);
        RestAssured.defaultParser = Parser.JSON;
        PessoaDTO resultService2 = service.putPessoa(jsonBodyPessoaErro, resultService.getIdPessoa());
        Assert.assertEquals(resultService2.getStatus(), "400");

        service.deletePessoa(resultService.getIdPessoa());
    }

    @Test
    public void deveDeletarPessoa(){

        PessoaDTO resultService = service.postPessoa(jsonBodyPessoa);

        Response resultService2 =  service.deletePessoa(resultService.getIdPessoa());
        Assert.assertEquals(resultService2.statusCode(), 200);
    }

    @Test
    public void deveDeletarPessoaIdInexistente(){
        Response resultService2 =  service.deletePessoa(987456321);
        Assert.assertEquals(resultService2.statusCode(), 404);
    }

}