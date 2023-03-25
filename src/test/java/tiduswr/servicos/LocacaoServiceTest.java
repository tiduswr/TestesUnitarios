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
import tiduswr.utils.DataUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static tiduswr.matchers.MatchersProprios.*;
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
    //@Ignore
    public void deveAlugarFilme() throws Exception{
        //Não Executa no sbado
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("HARLLEM");
        List<Filme> filmes = List.of(new Filme("MATRIX",2,7.0),
                new Filme("SHREK",4,4.0));

        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValor(), is(equalTo(11.0)));
        error.checkThat(locacao.getDataLocacao(), isHoje());
        error.checkThat(locacao.getDataRetorno(), isHojePlusDays(1));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception{
        //cenario
        Usuario usuario = new Usuario("HARLLEM");
        List<Filme> filmes = List.of(new Filme("MATRIX",2,7.0),
                new Filme("SHREK",0,4.0),
                new Filme("AUTO DA COMPADECIDA",3,5.0));

        //acao
        locacaoService.alugarFilme(usuario, filmes);
    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario(){
        //cenario
        List<Filme> filmes = List.of(new Filme("MATRIX",2,7.0),
                new Filme("SHREK",4,4.0),
                new Filme("AUTO DA COMPADECIDA",3,5.0));

        Assert.assertThrows("Usuário Vázio!", LocadoraException.class, () -> locacaoService.alugarFilme(null, filmes));
    }

    @Test
    public void naoDeveAlugarFilmeSemFilme(){
        //cenario
        Usuario usuario = new Usuario("HARLLEM");

        Assert.assertThrows("Filme vázio!", LocadoraException.class, () -> locacaoService.alugarFilme(usuario, null));
    }

    @Test
    //@Ignore
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
        //Só executa no sábado
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Harllem");
        List<Filme> filmes = List.of(new Filme("MATRIX",2,4.0));

        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getDataRetorno(), caiEmUmaSegunda());
    }

}
