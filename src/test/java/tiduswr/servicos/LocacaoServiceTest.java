package tiduswr.servicos;

import org.hamcrest.CoreMatchers;
import tiduswr.entidades.Filme;
import tiduswr.entidades.Locacao;
import tiduswr.entidades.Usuario;
import tiduswr.servicos.LocacaoService;
import tiduswr.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocacaoServiceTest {

    @Test
    public void teste() {
        //cenario
        LocacaoService locacaoService = new LocacaoService();
        Usuario usuario = new Usuario("HARLLEM");
        Filme filme = new Filme("MATRIX",2,5.0);

        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //verificacao
        assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(5.0)));
        assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.not(6.0)));
        assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
    }

}
