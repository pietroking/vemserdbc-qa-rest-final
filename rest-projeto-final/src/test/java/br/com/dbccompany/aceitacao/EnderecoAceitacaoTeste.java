package br.com.dbccompany.aceitacao;
import br.com.dbccompany.dto.EnderecoDTO;
import br.com.dbccompany.dto.EnderecoListaDTO;
import br.com.dbccompany.dto.PessoaDTO;
import br.com.dbccompany.dto.ResponseDTO;
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
    int SC_OK = 200;

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
    public void testarRetornarListadeEnderecosSemAuth(){
        Response resultService = service.pegarEnderecosSemAuth();
        Assert.assertFalse(resultService.statusCode() == SC_OK);
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
    public void testarPegarEnderecoPorIdPessoaSemParametro(){
        Response resultService = service.pegarEnderecosPorIdPessoa();
        Assert.assertFalse(resultService.statusCode() == SC_OK);
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
    public void testarAdicionarEnderecoIdPessoaInvalido() throws IOException {
        String jsonBodyEndereco = lerJson("src/test/resources/data/enderecoTest.json");
        EnderecoDTO resultService = service.adicionarEndereco(8238114,jsonBodyEndereco);
        assertThat(resultService.getStatus(), Matchers.is("404"));
    }

    @Test
    public void testarEditarEndereco() throws IOException{
        // Criando pessoa no banco de dados
        PessoaService servicePessoa = new PessoaService();
        String jsonBodyPessoa = lerJson("src/test/resources/data/pessoaTest.json");
        PessoaDTO resultServicePessoa = servicePessoa.postPessoa(jsonBodyPessoa);
        Assert.assertEquals(resultServicePessoa.getNome().toUpperCase(), "joker".toUpperCase());
        // Criando Endereço no banco de dados
        String jsonBodyEndereco = lerJson("src/test/resources/data/enderecoTest.json");
        EnderecoDTO resultServiceEnderecoAdd = service.adicionarEndereco(resultServicePessoa.getIdPessoa(),jsonBodyEndereco);
        Assert.assertEquals(resultServiceEnderecoAdd.getPais(),"Brasil");
        // Editando endereço:
        EnderecoDTO payload = new EnderecoDTO(resultServicePessoa.getIdPessoa(),"COMERCIAL",
                "Rua Tal", "123","Ali perto", "78782828",
                "Buenos Aires","Buenos Aires","Argentina");
        EnderecoDTO resultService = service
                .editarEndereco(resultServiceEnderecoAdd.getIdEndereco(), payload).body().as(EnderecoDTO.class);
        assertThat(resultService.getPais(), Matchers.is("Argentina"));
        //Limpando dados:
        Response resultServicePessoaDelete = servicePessoa.deletePessoa(resultServicePessoa.getIdPessoa());
        Assert.assertEquals(resultServicePessoaDelete.statusCode(),SC_OK);
    }
    @Test
    public void  testarEditarEnderecoIdsInvalidos(){
        EnderecoDTO payload = new EnderecoDTO(184812,"COMERCIAL",
                "Rua Tal", "123","Ali perto", "78782828",
                "Buenos Aires","Buenos Aires","Argentina");
        EnderecoDTO resultService = service.editarEndereco(98123213, payload).body().as(EnderecoDTO.class);
        assertThat(resultService.getStatus(), Matchers.is("404"));
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
    public void testarPegarEnderecoPorIdPessoaInválido(){
        Response resultService = service.pegarEnderecosPorIdPessoaResult(994129939);
        assertThat(resultService.body().as(EnderecoDTO[].class),Matchers.is(Matchers.emptyArray()) );
        //Assert.assertFalse(resultService.getStatusCode() == SC_OK);
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
    @Test
    public void testarPegarEnderecoPorIdEnderecoInvalido(){
        EnderecoDTO resultService = service.pegarEnderecoPorIdEndereco(991291);
        assertThat(resultService.getStatus(),Matchers.is(Matchers.notNullValue()));
    }
    @Test
    public void testarDeletarEndereco() throws IOException{
        // Criando pessoa no banco de dados
        PessoaService servicePessoa = new PessoaService();
        String jsonBodyPessoa = lerJson("src/test/resources/data/pessoaTest.json");
        PessoaDTO resultServicePessoaAdd = servicePessoa.postPessoa(jsonBodyPessoa);
        Assert.assertEquals(resultServicePessoaAdd.getNome().toUpperCase(), "joker".toUpperCase());
        // Criando 2 Endereços no banco de dados
        String jsonBodyEndereco = lerJson("src/test/resources/data/enderecoTest.json");
        EnderecoDTO resultServiceEnderecoAdd = service
                .adicionarEndereco(resultServicePessoaAdd.getIdPessoa(),jsonBodyEndereco);
        EnderecoDTO resultServiceEnderecoAdd2 = service
                .adicionarEndereco(resultServicePessoaAdd.getIdPessoa(),jsonBodyEndereco);
        // Deletando um dos endereços
        Response resultServiceEnderecoDelete = service
                .removerEndereco(resultServiceEnderecoAdd.getIdEndereco());
        Assert.assertEquals(resultServiceEnderecoDelete.getStatusCode(),SC_OK);
        //Verificando se o outro contato persiste no banco
        EnderecoDTO[] resultServiceEnderecoGet = service
                .pegarEnderecosPorIdPessoa(resultServicePessoaAdd.getIdPessoa());
        assertThat(resultServiceEnderecoGet,Matchers.is(Matchers.arrayWithSize(1)));
        //limpando banco de dados
        Response resultServicePessoaDelete =  servicePessoa
                .deletePessoa(resultServicePessoaAdd.getIdPessoa());
        Assert.assertEquals(resultServicePessoaDelete.statusCode(), 200);
    }

    @Test
    public void testarDeletarEnderecoIdInvalido(){
        ResponseDTO resultService = service.removerEndereco(291312794).body().as(ResponseDTO.class);
        assertThat(resultService.getStatus(), Matchers.is("404"));
    }



}