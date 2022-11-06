package br.com.dbccompany.aceitacao;
import br.com.dbccompany.dto.EnderecoDTO;
import br.com.dbccompany.dto.EnderecoListaDTO;
import br.com.dbccompany.dto.PessoaDTO;
import br.com.dbccompany.service.EnderecoService;
import br.com.dbccompany.service.PessoaService;
import br.com.dbccompany.utils.Login;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.hamcrest.core.Every;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;

public class EnderecoAceitacaoTeste {
    Login login = new Login();
    EnderecoService service = new EnderecoService();

    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test
    public void testarRetornarListaDeEnderecos(){
        Integer page = 0;
        EnderecoListaDTO resultService = service.pegarEnderecos();
        Assert.assertTrue(resultService.getPage().equals(page));
    }

    @Test
    public void testarRetornarListaDeEnderecosPassandoPagina(){
        Integer page = 12;
        EnderecoListaDTO resultService = service.pegarEnderecos(page);
        Assert.assertSame(resultService.getPage() ,page);
    }
    @Test
    public void testarRetornarListaDeEnderecosPassandoTamanhoDaPagina(){
        int pageSize = 10;
        EnderecoListaDTO resultService = service.pegarEnderecos(pageSize);
        Assert.assertTrue(resultService.getSize().intValue() == pageSize);
    }

    @Test
    public void testarPegarEnderecoPorPaisSemParametro(){
        Response resultService = service.pegarEnderecosPorPais();
        int code = 400;
        Assert.assertTrue(resultService.statusCode() == code);
    }

    @Test
    public void testarPegarEnderecoPorPais(){
        String paisTeste = "Brasil";
        EnderecoDTO[] resultService = service.pegarEnderecosPorPais(paisTeste);
        Assert.assertEquals(resultService[0].getPais(),paisTeste);
        Assert.assertEquals(resultService[resultService.length-1].getPais(),paisTeste);
    }

    @Test
    public void testarPegarEnderecoPorPaisInexistente(){
        String paisTeste = "Bansfdoij231";
        EnderecoDTO[] resultService = service.pegarEnderecosPorPais(paisTeste);
        assertThat(resultService, Matchers.is(Matchers.emptyArray()) );
    }

    @Test
    public void testarAdicionarEndereco() throws IOException {
        // Criando pessoa no banco de dados
        PessoaService servicePessoa = new PessoaService();
        String jsonBodyPessoa = lerJson("src/test/resources/data/pessoaTest.json");
        PessoaDTO resultServicePessoa = servicePessoa.postPessoa(jsonBodyPessoa);
        Assert.assertEquals(resultServicePessoa.getNome().toUpperCase(), "joker".toUpperCase());
        // Criando Endereço no banco de dados
        String jsonBodyEndereco = lerJson("src/test/resources/data/enderecoTest.json");
        EnderecoDTO resultService = service.adicionarEndereco(resultServicePessoa.getIdPessoa(),jsonBodyEndereco);
        Assert.assertTrue(resultService.getIdPessoa().equals(resultServicePessoa.getIdPessoa()));
        Assert.assertEquals(resultService.getPais(),"Brasil");
        //Limpando banco de dados:
        Response resultServicePessoaDelete =  servicePessoa.deletePessoa(resultService.getIdPessoa());
        Assert.assertEquals(resultServicePessoaDelete.statusCode(), 200);
    }

    @Test
    public void testarPegarEnderecoPorIdPessoa() throws IOException{
        // Criando pessoa no banco de dados
        PessoaService servicePessoa = new PessoaService();
        String jsonBodyPessoa = lerJson("src/test/resources/data/pessoaTest.json");
        PessoaDTO resultServicePessoa = servicePessoa.postPessoa(jsonBodyPessoa);
        Assert.assertEquals(resultServicePessoa.getNome().toUpperCase(), "joker".toUpperCase());
        // Criando Endereços no banco de dados
        String jsonBodyEndereco = lerJson("src/test/resources/data/enderecoTest.json");
        EnderecoDTO resultService = service.adicionarEndereco(resultServicePessoa.getIdPessoa(),jsonBodyEndereco);
        EnderecoDTO resultService2 = service.adicionarEndereco(resultServicePessoa.getIdPessoa(),jsonBodyEndereco);
        // Pegando endereco utilizando id pessoa
        EnderecoDTO[] resultServiceGet = service.pegarEnderecosPorIdPessoa(resultServicePessoa.getIdPessoa());
        assertThat(resultServiceGet,Matchers.is(Matchers.arrayWithSize(2)));
        assertThat(resultServiceGet[0].getPais(),Matchers.is("Brasil"));
        //Limpando banco de dados:
        Response resultServicePessoaDelete =  servicePessoa.deletePessoa(resultService.getIdPessoa());
        Assert.assertEquals(resultServicePessoaDelete.statusCode(), 200);
    }

    @Test
    public void testarPegarEnderecoPorIdEndereco() throws IOException{
        // Criando pessoa no banco de dados
        PessoaService servicePessoa = new PessoaService();
        String jsonBodyPessoa = lerJson("src/test/resources/data/pessoaTest.json");
        PessoaDTO resultServicePessoaAdd = servicePessoa.postPessoa(jsonBodyPessoa);
        Assert.assertEquals(resultServicePessoaAdd.getNome().toUpperCase(), "joker".toUpperCase());
        // Criando Endereços no banco de dados
        String jsonBodyEndereco = lerJson("src/test/resources/data/enderecoTest.json");
        EnderecoDTO resultServiceEnderecoAdd = service
                .adicionarEndereco(resultServicePessoaAdd.getIdPessoa(),jsonBodyEndereco);
        EnderecoDTO resultServiceEnderecoAdd2 = service
                .adicionarEndereco(resultServicePessoaAdd.getIdPessoa(),jsonBodyEndereco);
        // testando pegar endereco pelo id dele
        EnderecoDTO resultServiceEnderecoGet = service
                .pegarEnderecoPorIdEndereco(resultServiceEnderecoAdd.getIdEndereco());
        EnderecoDTO resultServiceEnderecoGet2 = service
                .pegarEnderecoPorIdEndereco(resultServiceEnderecoAdd2.getIdEndereco());
        Assert.assertEquals(resultServiceEnderecoGet.getCep(),resultServiceEnderecoAdd.getCep());
        Assert.assertEquals(resultServiceEnderecoGet2.getLogradouro(),
                resultServiceEnderecoAdd2.getLogradouro());
        // limpando banco de dados
        Response resultServicePessoaDelete =  servicePessoa
                .deletePessoa(resultServicePessoaAdd.getIdPessoa());
        Assert.assertEquals(resultServicePessoaDelete.statusCode(), 200);
    }
}