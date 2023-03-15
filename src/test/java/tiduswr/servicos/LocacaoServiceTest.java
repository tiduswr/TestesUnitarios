package tiduswr.servicos;

import tiduswr.entidades.Filme;
import tiduswr.entidades.Locacao;
import tiduswr.entidades.Usuario;
import tiduswr.servicos.LocacaoService;
import tiduswr.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

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
        Assert.assertEquals(5.0, locacao.getValor(), 0.0);
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
    }

}
