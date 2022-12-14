package br.com.dbccompany.aceitacao;

import br.com.dbccompany.dto.*;
import br.com.dbccompany.service.ContatoService;
import br.com.dbccompany.service.EnderecoService;
import br.com.dbccompany.service.PessoaService;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        PessoaDTO resultService = service.buscarPessoasPorCpf(resultServiceteste.getCpf());
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

    @Test
    public void deveRetornarListaDePessoa(){
        PageDTOPessoaDTO resultService = service.buscarPessoas();
        assertThat(resultService.getPage(),Matchers.is(0));
    }

    @Test
    public void deveRetornarPessoaComListaCompleta() throws IOException {
        // Instanciando servi??o de Contatos e Endere??os
        ContatoService serviceContato = new ContatoService();
        EnderecoService serviceEndereco = new EnderecoService();
        //Adicionando Pessoa no banco de dados
        PessoaDTO resultServicePessoaAdd = service.postPessoa(jsonBodyPessoa);
        Assert.assertEquals(resultServicePessoaAdd.getNome().toUpperCase(), "joker".toUpperCase());
        // Adicionando Contato
        ContatoDTO resultServiceContatoAdd = serviceContato
                .criarContato(resultServicePessoaAdd.getIdPessoa(),jsonBodyContato);
        // Adicionando Endereco
        EnderecoDTO resultServiceEnderecoAdd = serviceEndereco
                .adicionarEndereco(resultServicePessoaAdd.getIdPessoa(),jsonBodyEndereco);
        //Testando Get:
        PessoaDTO[] resultServicePessoaGet = service
                .buscarPessoaListaCompleta(resultServicePessoaAdd.getIdPessoa());
        Assert.assertEquals(resultServicePessoaGet[0].getContatos()[0].getIdContato()
                ,resultServiceContatoAdd.getIdContato());
        Assert.assertEquals(resultServicePessoaGet[0].getEnderecos()[0].getLogradouro(),
                resultServiceEnderecoAdd.getLogradouro());
        //Limpando dados do servidor
        Response resultServicePessoaDelete = service.deletePessoa(resultServicePessoaAdd.getIdPessoa());
    }

    @Test
    public void deveRetornarPessoaComContatos() throws IOException{
        // Instanciando servi??o de Contatos e Endere??os
        ContatoService serviceContato = new ContatoService();
        //Adicionando Pessoa no banco de dados
        PessoaDTO resultServicePessoaAdd = service.postPessoa(jsonBodyPessoa);
        Assert.assertEquals(resultServicePessoaAdd.getNome().toUpperCase(), "joker".toUpperCase());
        // Adicionando Contato
        ContatoDTO resultServiceContatoAdd = serviceContato
                .criarContato(resultServicePessoaAdd.getIdPessoa(),jsonBodyContato);
        //Testando Get:
        PessoaDTO[] resultServicePessoaGet = service
                .buscarPessoaContato(resultServicePessoaAdd.getIdPessoa());
        Assert.assertEquals(resultServicePessoaGet[0].getContatos()[0].getIdContato()
                ,resultServiceContatoAdd.getIdContato());
        //Limpando dados do servidor
        Response resultServicePessoaDelete = service.deletePessoa(resultServicePessoaAdd.getIdPessoa());
    }
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