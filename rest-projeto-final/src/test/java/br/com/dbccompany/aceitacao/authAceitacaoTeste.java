package br.com.dbccompany.aceitacao;

import br.com.dbccompany.service.AuthService;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class authAceitacaoTeste {
    AuthService service = new AuthService();

    @Test
    public void testarUsuarioComLoginSenhaValidos(){
        String resultService = service.fazerLogin("grupo04qa","1234");
        String assertString = "Bearer";
        Assert.assertTrue(resultService.contains(assertString));
    }

    @Test
    public void testarUsuarioComLoginSenhaInvalidos(){
        String resultService = service.fazerLogin("quiwr8y19273uinuis","wqpeijiq0wej02134");
        String assertString = "Bearer";
        Assert.assertFalse(resultService.contains(assertString));
    }
    @Test
    public void testarUsuarioComLoginValidoSenhaInvalida(){
        String resultService = service.fazerLogin("grupo04qa","1u920310729ud234");
        String assertString = "Bearer";
        Assert.assertFalse(resultService.contains(assertString));
    }
}
