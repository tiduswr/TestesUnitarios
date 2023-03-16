package tiduswr.servicos;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import tiduswr.entidades.Filme;
import tiduswr.entidades.Locacao;
import tiduswr.entidades.Usuario;
import org.junit.Test;

import java.util.Date;

import static tiduswr.utils.DataUtils.isMesmaData;
import static tiduswr.utils.DataUtils.obterDataComDiferencaDias;

public class LocacaoServiceTest {

    @Rule
    ErrorCollector error = new ErrorCollector();

    @Test
    public void testeLocacao() {
        //cenario
        LocacaoService locacaoService = new LocacaoService();
        Usuario usuario = new Usuario("HARLLEM");
        Filme filme = new Filme("MATRIX",2,5.0);

        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //verificacao
        error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(5.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), CoreMatchers.is(false));
    }

}
