package tiduswr.servicos;

import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.runners.MethodSorters;
import tiduswr.entidades.Filme;
import tiduswr.entidades.Locacao;
import tiduswr.entidades.Usuario;
import tiduswr.exceptions.FilmeSemEstoqueException;
import tiduswr.exceptions.LocadoraException;

import java.util.Date;
import java.util.List;

import static tiduswr.utils.DataUtils.isMesmaData;
import static tiduswr.utils.DataUtils.obterDataComDiferencaDias;

//Para definir ordem nos testes(Não recomendado pois quebra a letra I do principio FIRST)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocacaoServiceTest {

    private LocacaoService locacaoService;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Before
    public void setup(){
        locacaoService = new LocacaoService();
    }

    @Test
    public void testeLocacao() throws Exception{
        //cenario
        Usuario usuario = new Usuario("HARLLEM");
        List<Filme> filmes = List.of(new Filme("MATRIX",2,7.0),
                new Filme("SHREK",4,4.0),
                new Filme("AUTO DA COMPADECIDA",3,5.0));

        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(16.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), CoreMatchers.is(true));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void testLocacaco_filmeSemEstoque() throws Exception{
        //cenario
        Usuario usuario = new Usuario("HARLLEM");
        List<Filme> filmes = List.of(new Filme("MATRIX",2,7.0),
                new Filme("SHREK",0,4.0),
                new Filme("AUTO DA COMPADECIDA",3,5.0));

        //acao
        locacaoService.alugarFilme(usuario, filmes);
    }

    @Test
    public void testLocacaco_filmeSemEstoque2(){
        //cenario
        Usuario usuario = new Usuario("HARLLEM");
        List<Filme> filmes = List.of(new Filme("MATRIX",2,7.0),
                new Filme("SHREK",4,4.0),
                new Filme("AUTO DA COMPADECIDA",0,5.0));

        //acao
        try {
            locacaoService.alugarFilme(usuario, filmes);
            Assert.fail("Deveria ter lançado uma exceção!");
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Filme sem estoque!");
        }
    }

    @Test
    public void testLocacaco_filmeSemEstoque3(){
        //cenario
        Usuario usuario = new Usuario("HARLLEM");
        List<Filme> filmes = List.of(new Filme("MATRIX",0,7.0),
                new Filme("SHREK",4,4.0),
                new Filme("AUTO DA COMPADECIDA",3,5.0));

        Assert.assertThrows("Filme sem estoque!", Exception.class, () -> locacaoService.alugarFilme(usuario, filmes));
    }

    @Test
    public void testLocacao_usuarioVazio(){
        //cenario
        List<Filme> filmes = List.of(new Filme("MATRIX",2,7.0),
                new Filme("SHREK",4,4.0),
                new Filme("AUTO DA COMPADECIDA",3,5.0));

        Assert.assertThrows("Usuário Vázio!", LocadoraException.class, () -> locacaoService.alugarFilme(null, filmes));
    }

    @Test
    public void testLocacao_filmeVazio(){
        //cenario
        Usuario usuario = new Usuario("HARLLEM");

        Assert.assertThrows("Filme vázio!", LocadoraException.class, () -> locacaoService.alugarFilme(usuario, null));
    }

}
