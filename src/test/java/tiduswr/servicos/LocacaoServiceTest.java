package tiduswr.servicos;

import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import tiduswr.daos.LocacaoDAO;
import tiduswr.daos.LocacaoDaoFake;
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
import static tiduswr.builder.FilmeBuilder.umFilme;
import static tiduswr.builder.FilmeBuilder.umFilmeSemEstoque;
import static tiduswr.builder.UsuarioBuilder.*;
import static tiduswr.matchers.MatchersProprios.*;

//Para definir ordem nos testes(Não recomendado pois quebra a letra I do principio FIRST)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocacaoServiceTest {

    private LocacaoService locacaoService;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Before
    public void setup(){
        locacaoService = new LocacaoService();
        locacaoService.setLocacaoDAO(Mockito.mock(LocacaoDAO.class));
    }

    @Test
    //@Ignore
    public void deveAlugarFilme() throws Exception{
        //Não Executa no sbado
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = List.of(umFilme().comValor(5.0).agora());

        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(locacao.getDataLocacao(), isHoje());
        error.checkThat(locacao.getDataRetorno(), isHojePlusDays(1));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception{
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = List.of(umFilmeSemEstoque().agora());

        //acao
        locacaoService.alugarFilme(usuario, filmes);
    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario(){
        //cenario
        List<Filme> filmes = List.of(umFilme().agora());

        Assert.assertThrows("Usuário Vázio!", LocadoraException.class, () -> locacaoService.alugarFilme(null, filmes));
    }

    @Test
    public void naoDeveAlugarFilmeSemFilme(){
        //cenario
        Usuario usuario = umUsuario().agora();

        Assert.assertThrows("Filme vázio!", LocadoraException.class, () -> locacaoService.alugarFilme(usuario, null));
    }

    @Test
    //@Ignore
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
        //Só executa no sábado
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = List.of(umFilme().agora());

        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getDataRetorno(), caiEmUmaSegunda());
    }

}
