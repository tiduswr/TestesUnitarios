package tiduswr.servicos;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import tiduswr.entidades.Filme;
import tiduswr.entidades.Locacao;
import tiduswr.entidades.Usuario;
import org.junit.Test;
import tiduswr.exceptions.FilmeSemEstoqueException;
import tiduswr.exceptions.LocadoraException;

import java.util.Date;

import static tiduswr.utils.DataUtils.isMesmaData;
import static tiduswr.utils.DataUtils.obterDataComDiferencaDias;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Test
    public void testeLocacao() throws Exception{
        //cenario
        LocacaoService locacaoService = new LocacaoService();
        Usuario usuario = new Usuario("HARLLEM");
        Filme filme = new Filme("MATRIX",2,5.0);

        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //verificacao
        error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(5.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), CoreMatchers.is(true));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void testLocacaco_filmeSemEstoque() throws Exception{
        //cenario
        LocacaoService locacaoService = new LocacaoService();
        Usuario usuario = new Usuario("HARLLEM");
        Filme filme = new Filme("MATRIX",0,5.0);

        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);
    }

    @Test
    public void testLocacaco_filmeSemEstoque2(){
        //cenario
        LocacaoService locacaoService = new LocacaoService();
        Usuario usuario = new Usuario("HARLLEM");
        Filme filme = new Filme("MATRIX",0,5.0);

        //acao
        try {
            Locacao locacao = locacaoService.alugarFilme(usuario, filme);
            Assert.fail("Deveria ter lançado uma exceção!");
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Filme sem estoque!");
        }
    }

    @Test
    public void testLocacaco_filmeSemEstoque3(){
        //cenario
        LocacaoService locacaoService = new LocacaoService();
        Usuario usuario = new Usuario("HARLLEM");
        Filme filme = new Filme("MATRIX",0,5.0);

        Assert.assertThrows("Filme sem estoque!", Exception.class, () -> locacaoService.alugarFilme(usuario, filme));
    }

    @Test
    public void testLocacao_usuarioVazio(){
        //cenario
        LocacaoService locacaoService = new LocacaoService();
        Filme filme = new Filme("MATRIX",1,5.0);

        Assert.assertThrows("Usuário Vázio!", LocadoraException.class, () -> locacaoService.alugarFilme(null, filme));
    }

    @Test
    public void testLocacao_filmeVazio(){
        //cenario
        LocacaoService locacaoService = new LocacaoService();
        Usuario usuario = new Usuario("HARLLEM");

        Assert.assertThrows("Filme vázio!", LocadoraException.class, () -> locacaoService.alugarFilme(usuario, null));
    }

}
