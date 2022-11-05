package br.com.dbccompany.aceitacao;

import br.com.dbccompany.dto.ContatoDTO;
import br.com.dbccompany.dto.PessoaDTO;
import br.com.dbccompany.service.ContatoService;
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
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;

public class ContatoAceitacaoTeste {
    Integer SC_OK = 200;
    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    ContatoService service = new ContatoService();

    @Test
    public void testarPegarContatos(){
        ContatoDTO[] resultService = service.pegarContatos();
    }

    @Test
    public void testarAdicionarContato() throws IOException {
        //Pegando Massas de dados:
        String jsonBodyPessoa = lerJson("src/test/resources/data/pessoaTest.json");
        String jsonBodyContato = lerJson("src/test/resources/data/contatoTest.json");
        // Instanciando serviço de pessoas
        PessoaService servicePessoa = new PessoaService();
        //Adicionando Pessoa no banco de dados
        PessoaDTO resultServicePessoaAdd = servicePessoa.postPessoa(jsonBodyPessoa);
        RestAssured.defaultParser = Parser.JSON;
        Assert.assertEquals(resultServicePessoaAdd.getNome().toUpperCase(), "joker".toUpperCase());
        // Adicionando Contato
        ContatoDTO resultServiceContatoAdd = service.criarContato(resultServicePessoaAdd.getIdPessoa(),jsonBodyContato);
        Assert.assertTrue(resultServiceContatoAdd.getDescricao().equals("casa"));
        //Deletando Pessoa do banco de dados
        Response resultServicePessoaDelete = servicePessoa.deletePessoa(resultServicePessoaAdd.getIdPessoa());
        Assert.assertTrue(resultServicePessoaDelete.getStatusCode() == SC_OK);
    }


    @Test
    public void testarPegarContatoPorIdPessoa() throws IOException {
        //Pegando Massas de dados:
        String jsonBodyPessoa = lerJson("src/test/resources/data/pessoaTest.json");
        String jsonBodyContato = lerJson("src/test/resources/data/contatoTest.json");
        // Instanciando serviço de pessoas
        PessoaService servicePessoa = new PessoaService();
        //Adicionando Pessoa no banco de dados
        PessoaDTO resultServicePessoaAdd = servicePessoa.postPessoa(jsonBodyPessoa);
        RestAssured.defaultParser = Parser.JSON;
        Assert.assertEquals(resultServicePessoaAdd.getNome().toUpperCase(), "joker".toUpperCase());
        // Adicionando Contatos
        ContatoDTO resultServiceContatoAdd = service.criarContato(resultServicePessoaAdd.getIdPessoa(),jsonBodyContato);
        ContatoDTO resultServiceContatoAdd2 = service.criarContato(resultServicePessoaAdd.getIdPessoa(),jsonBodyContato);
        Assert.assertTrue(resultServiceContatoAdd.getDescricao().equals("casa"));
        Assert.assertTrue(resultServiceContatoAdd2.getDescricao().equals("casa"));
        //PesquisandoContatosPorId
        ContatoDTO[] resultServiceContatoGet = service.pegarContatos(resultServicePessoaAdd.getIdPessoa());
        assertThat(resultServiceContatoGet, Matchers.hasItemInArray(resultServiceContatoAdd2));
        assertThat(resultServiceContatoGet,Matchers.hasItemInArray(resultServiceContatoAdd));

        //Deletando Pessoa do banco de dados
        Response resultServicePessoaDelete = servicePessoa.deletePessoa(resultServicePessoaAdd.getIdPessoa());
        Assert.assertTrue(resultServicePessoaDelete.getStatusCode() == SC_OK);

    }

    @Test
    public void testarDeletarContato() throws IOException{
        //Pegando Massas de dados:
        String jsonBodyPessoa = lerJson("src/test/resources/data/pessoaTest.json");
        String jsonBodyContato = lerJson("src/test/resources/data/contatoTest.json");
        // Instanciando serviço de pessoas
        PessoaService servicePessoa = new PessoaService();
        //Adicionando Pessoa no banco de dados
        PessoaDTO resultServicePessoaAdd = servicePessoa.postPessoa(jsonBodyPessoa);
        RestAssured.defaultParser = Parser.JSON;
        Assert.assertEquals(resultServicePessoaAdd.getNome().toUpperCase(), "joker".toUpperCase());
        // Adicionando 2 Contatos iguais
        ContatoDTO resultServiceContatoAdd = service.criarContato(resultServicePessoaAdd.getIdPessoa(),jsonBodyContato);
        ContatoDTO resultServiceContatoAdd2 = service.criarContato(resultServicePessoaAdd.getIdPessoa(),jsonBodyContato);
        Assert.assertTrue(resultServiceContatoAdd.getDescricao().equals("casa"));
        Assert.assertTrue(resultServiceContatoAdd2.getDescricao().equals("casa"));
        //Deletando Contatos
        Response resultServiceContatoDelete = service.deletarContato(resultServiceContatoAdd.getIdContato());
        Assert.assertTrue(resultServiceContatoDelete.statusCode() == SC_OK);
        //Verificando se o outro contato continuou lá:
        ContatoDTO[] reultServiceContatoGet = service.pegarContatos(resultServicePessoaAdd.getIdPessoa());
        assertThat(reultServiceContatoGet,Matchers.hasItemInArray(resultServiceContatoAdd2));
        //Limpando Banco de dados Excluindo Pessoa
        Response resultServicePessoaDelete = servicePessoa.deletePessoa(resultServicePessoaAdd.getIdPessoa());
        Assert.assertTrue(resultServicePessoaDelete.getStatusCode() == SC_OK);
    }

    @Test
    public void testarEditarContato() throws IOException{
        //Pegando Massas de dados:
        String jsonBodyPessoa = lerJson("src/test/resources/data/pessoaTest.json");
        String jsonBodyContato = lerJson("src/test/resources/data/contatoTest.json");
        String jsonBodyContatoEdit = lerJson("src/test/resources/data/contatoTest2.json");
        // Instanciando serviço de pessoas
        PessoaService servicePessoa = new PessoaService();
        //Adicionando Pessoa no banco de dados
        PessoaDTO resultServicePessoaAdd = servicePessoa.postPessoa(jsonBodyPessoa);
        RestAssured.defaultParser = Parser.JSON;
        Assert.assertEquals(resultServicePessoaAdd.getNome().toUpperCase(), "joker".toUpperCase());
        // Adicionando 2 Contatos iguais
        ContatoDTO resultServiceContatoAdd = service.criarContato(resultServicePessoaAdd.getIdPessoa(),jsonBodyContato);
        ContatoDTO resultServiceContatoAdd2 = service.criarContato(resultServicePessoaAdd.getIdPessoa(),jsonBodyContato);
        Assert.assertTrue(resultServiceContatoAdd.getDescricao().equals("casa"));
        Assert.assertTrue(resultServiceContatoAdd2.getDescricao().equals("casa"));
        //Editando um dos contatos:
        ContatoDTO resultServiceContatoEdit = service.editarContato(resultServiceContatoAdd2.getIdContato(),jsonBodyContatoEdit);
        Assert.assertTrue(resultServiceContatoEdit.getTipoContato().equals("COMERCIAL"));
        // Colocando o id no contato editado
        resultServiceContatoEdit.setIdContato(resultServiceContatoAdd2.getIdContato());
        //Verificando Banco de dados se o outro contato ficou normal:
        ContatoDTO[] reultServiceContatoGet = service.pegarContatos(resultServicePessoaAdd.getIdPessoa());
        assertThat(reultServiceContatoGet,Matchers.hasItemInArray(resultServiceContatoAdd));
        Assert.assertTrue(Arrays.stream(reultServiceContatoGet).anyMatch(el-> el.getTelefone().equals("(71)4002-8922")));
        //Limpando Banco de dados Excluindo Pessoa
        Response resultServicePessoaDelete = servicePessoa.deletePessoa(resultServicePessoaAdd.getIdPessoa());
        Assert.assertTrue(resultServicePessoaDelete.getStatusCode() == SC_OK);
    }


}
