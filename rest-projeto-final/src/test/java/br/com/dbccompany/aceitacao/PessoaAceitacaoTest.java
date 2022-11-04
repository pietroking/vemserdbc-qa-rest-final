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
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PessoaAceitacaoTest {

    PessoaService service = new PessoaService();

    public PessoaAceitacaoTest() throws IOException {
    }

    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }
    String jsonBodyPessoa = lerJson("src/test/resources/data/pessoaTest.json");
    String jsonBodyContato = lerJson("src/test/resources/data/contatoTest.json");
    String jsonBodyEndereco = lerJson("src/test/resources/data/enderecoTest.json");

    @Test
    public void deveRetornarRelatorioPessoa(){

        RelatorioDTO[] resultService = service.buscarRealatorio();

        Assert.assertEquals(resultService[0].getNomePessoa().toUpperCase(), "Maicon Machado Gerardi".toUpperCase());
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

//        service.deletePessoa(resultService.getIdPessoa());
    }

    @Test
    public void deveDeletarPessoa(){

//        RestAssured.defaultParser = Parser.JSON;

        ResponseDTO resultService =  service.deletePessoa("72");

        Assert.assertEquals(resultService.getStatus(), "200");
    }
}