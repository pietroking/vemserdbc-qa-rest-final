package br.com.dbccompany.aceitacao;
import br.com.dbccompany.dto.EnderecoDTO;
import br.com.dbccompany.dto.EnderecoListaDTO;
import br.com.dbccompany.service.EnderecoService;
import br.com.dbccompany.utils.Login;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;

public class EnderecoAceitacaoTeste {
    Login login = new Login();
    EnderecoService service = new EnderecoService();

    @Test
    public void testarRetornarListaDeEnderecos(){
        Integer page = 0;
        String token = login.authenticationAdmin();
        EnderecoListaDTO resultService = service.pegarEnderecos(token);
        Assert.assertTrue(resultService.getPage().equals(page));
    }

    @Test
    public void testarRetornarListaDeEnderecosPassandoPagina(){
        Integer page = 12;
        String token = login.authenticationAdmin();
        EnderecoListaDTO resultService = service.pegarEnderecos(token,page);
        Assert.assertSame(resultService.getPage() ,page);
    }
    @Test
    public void testarRetornarListaDeEnderecosPassandoTamanhoDaPagina(){
        int pageSize = 10;
        String token = login.authenticationAdmin();
        EnderecoListaDTO resultService = service.pegarEnderecos(token,pageSize);
        Assert.assertTrue(resultService.getSize().intValue() == pageSize);
    }

    @Test
    public void testarpegarEnderecoPorPaisSemParametro(){
        String token = login.authenticationAdmin();
        Response resultService = service.pegarEnderecosPorPais(token);
        int code = 400;
        Assert.assertTrue(resultService.statusCode() == code);
    }

    @Test
    public void testarPegarEnderecoPorPais(){
        String token = login.authenticationAdmin();
        String paisTeste = "Brasil";
        EnderecoDTO[] resultService = service.pegarEnderecosPorPais(token,paisTeste);
        if (resultService.length > 0){
            Assert.assertTrue(Arrays.stream(resultService).allMatch(el->el.getPais().equals(paisTeste)));
        }
    }

    @Test
    public void testarPegarEnderecoPorIdPessoa(){
        String token = login.authenticationAdmin();
    }

}