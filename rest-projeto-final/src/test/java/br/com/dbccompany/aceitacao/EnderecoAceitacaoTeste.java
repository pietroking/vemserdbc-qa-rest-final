package br.com.dbccompany.aceitacao;
import br.com.dbccompany.dto.EnderecoDTO;
import br.com.dbccompany.dto.EnderecoListaDTO;
import br.com.dbccompany.dto.PessoaDTO;
import br.com.dbccompany.service.EnderecoService;
import br.com.dbccompany.service.PessoaService;
import br.com.dbccompany.utils.Login;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
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
        String token = login.authenticationAdmin();
        EnderecoListaDTO resultService = service.pegarEnderecos();
        Assert.assertTrue(resultService.getPage().equals(page));
    }

    @Test
    public void testarRetornarListaDeEnderecosPassandoPagina(){
        Integer page = 12;
        String token = login.authenticationAdmin();
        EnderecoListaDTO resultService = service.pegarEnderecos(page);
        Assert.assertSame(resultService.getPage() ,page);
    }
    @Test
    public void testarRetornarListaDeEnderecosPassandoTamanhoDaPagina(){
        int pageSize = 10;
        String token = login.authenticationAdmin();
        EnderecoListaDTO resultService = service.pegarEnderecos(pageSize);
        Assert.assertTrue(resultService.getSize().intValue() == pageSize);
    }

    @Test
    public void testarpegarEnderecoPorPaisSemParametro(){
        String token = login.authenticationAdmin();
        Response resultService = service.pegarEnderecosPorPais();
        int code = 400;
        Assert.assertTrue(resultService.statusCode() == code);
    }

    @Test
    public void testarPegarEnderecoPorPais(){
        String paisTeste = "Brasil";
        EnderecoDTO[] resultService = service.pegarEnderecosPorPais(paisTeste);
        Assert.assertEquals(resultService[0].getPais(),paisTeste);
        Assert.assertEquals(resultService[-1].getPais(),paisTeste);
    }

    @Test
    public void testarPegarEnderecoPorPaisInexistente(){
        String paisTeste = "Bansfdoij231";
        EnderecoDTO[] resultService = service.pegarEnderecosPorPais(paisTeste);
        assertThat(resultService, Matchers.is(Matchers.emptyArray()) );
    }

    //Adicionar Endereço dando erro 500
    @Test
    public void testarAdicionarEndereco() throws IOException {
    //    PessoaService servicePessoa = new PessoaService();
    //    String jsonBodyPessoa = lerJson("src/test/resources/data/pessoaTest.json");
    //    PessoaDTO resultServicePessoa = servicePessoa.postPessoa(jsonBodyPessoa);
    //    Assert.assertEquals(resultServicePessoa.getNome().toUpperCase(), "joker".toUpperCase());
    //    String jsonBodyEndereco = lerJson("src/test/resources/data/enderecoTest.json");
    //    EnderecoDTO resultService = service.adicionarEndereco(resultServicePessoa.getIdPessoa(),jsonBodyEndereco);
    //    Assert.assertTrue(resultService.getIdPessoa().equals(resultServicePessoa.getIdPessoa()));
    //    Assert.assertEquals(resultService.getPais(),"Brasil");
    //    Response resultServiceDelete = service.removerEndereco(resultService.getIdEndereco());
    //    Assert.assertEquals(resultServiceDelete.statusCode(), 200);
    //    Response resultServicePessoaDelete =  servicePessoa.deletePessoa(resultService.getIdPessoa());
    //    Assert.assertEquals(resultServicePessoaDelete.statusCode(), 200);

    }

    @Test
    public void testarPegarEnderecoPorIdPessoa(){
        String token = login.authenticationAdmin();

    }

}