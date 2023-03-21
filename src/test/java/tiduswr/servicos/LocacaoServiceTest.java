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
    public void deveAlugarFilme() throws Exception{
        //cenario
        Usuario usuario = new Usuario("HARLLEM");
        List<Filme> filmes = List.of(new Filme("MATRIX",2,7.0),
                new Filme("SHREK",4,4.0));

        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(11.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), CoreMatchers.is(true));
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
    public void devePagar75PorcentoNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("HARLLEM");
        Double expected = 11.0d;
        List<Filme> filmes = List.of(new Filme("MATRIX",2,4.0),
                new Filme("SHREK",4,4.0),
                new Filme("AUTO DA COMPADECIDA",3,4.0));

        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

        //verificação
        Assert.assertEquals(expected, resultado.getValor());
    }

    @Test
    public void devePagar50PorcentoNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("HARLLEM");
        Double expected = 13.0d;
        List<Filme> filmes = List.of(new Filme("MATRIX",2,4.0),
                new Filme("SHREK",4,4.0),
                new Filme("AUTO DA COMPADECIDA",3,4.0),
                new Filme("DUMB GUYS",5,4.0));

        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

        //verificação
        Assert.assertEquals(expected, resultado.getValor());
    }

    @Test
    public void devePagar75PorcentoNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("HARLLEM");
        Double expected = 14.0d;
        List<Filme> filmes = List.of(new Filme("MATRIX",2,4.0),
                new Filme("SHREK",4,4.0),
                new Filme("AUTO DA COMPADECIDA",3,4.0),
                new Filme("DUMB GUYS",5,4.0),
                new Filme("A CULPA É DAS ESTRELAS",7,4.0));

        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

        //verificação
        Assert.assertEquals(expected, resultado.getValor());
    }

    @Test
    public void devePagar100PorcentoNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("HARLLEM");
        Double expected = 14.0d;
        List<Filme> filmes = List.of(new Filme("MATRIX",2,4.0),
                new Filme("SHREK",4,4.0),
                new Filme("AUTO DA COMPADECIDA",3,4.0),
                new Filme("DUMB GUYS",5,4.0),
                new Filme("A CULPA É DAS ESTRELAS",7,4.0),
                new Filme("MAIS QUE AMIGOS, FRIENDS",10,4.0));

        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

        //verificação
        Assert.assertEquals(expected, resultado.getValor());
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Harllem");
        List<Filme> filmes = List.of(new Filme("MATRIX",2,4.0));

        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        System.out.println(resultado.getDataRetorno());
        boolean isSegunda = DataUtils.verificarDiaSemana(resultado.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(isSegunda);
    }

}
