package tiduswr.servicos;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tiduswr.daos.LocacaoDAO;
import tiduswr.entidades.Filme;
import tiduswr.entidades.Locacao;
import tiduswr.entidades.Usuario;
import tiduswr.exceptions.FilmeSemEstoqueException;
import tiduswr.exceptions.LocadoraException;
import tiduswr.utils.DataUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static tiduswr.builder.FilmeBuilder.umFilme;
import static tiduswr.builder.FilmeBuilder.umFilmeSemEstoque;
import static tiduswr.builder.LocacaoBuilder.umaLocacao;
import static tiduswr.builder.UsuarioBuilder.umUsuario;
import static tiduswr.matchers.MatchersProprios.*;

//Para definir ordem nos testes anote eles com @Order() e o valor index(ex: @Order(1))
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
//Execução de testes em paralelo
@Execution(ExecutionMode.CONCURRENT)
public class LocacaoServiceTest {

    @InjectMocks @Spy
    private LocacaoService locacaoService;

    @Mock
    private SpcService spcService;
    @Mock
    private LocacaoDAO locacaoDAO;
    @Mock
    private EmailService emailService;

    @Test
    //@Ignore
    public void deveAlugarFilme() throws Exception{
        //cenario
        Date sampleDate = DataUtils.obterData(31,3,2023);
        Date sampleDatePlusOneDay = DataUtils.adicionarDias(sampleDate, 1);
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = List.of(umFilme().comValor(5.0).agora());
        doReturn(sampleDate).when(locacaoService).generateHoje();

        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        assertAll(
                () -> assertThat(locacao.getValor(), is(equalTo(5.0))),
                () -> assertThat(locacao.getDataLocacao(), is(sampleDate)),
                () -> assertThat(locacao.getDataRetorno(), is(sampleDatePlusOneDay))
        );
    }

    @Test
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception{
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = List.of(umFilmeSemEstoque().agora());

        //acao
        assertThrows(FilmeSemEstoqueException.class, () -> locacaoService.alugarFilme(usuario, filmes));
    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario(){
        //cenario
        List<Filme> filmes = List.of(umFilme().agora());

        assertThrows(LocadoraException.class, () -> locacaoService.alugarFilme(null, filmes), "Usuário Vázio!");
    }

    @Test
    public void naoDeveAlugarFilmeSemFilme(){
        //cenario
        Usuario usuario = umUsuario().agora();

        assertThrows(LocadoraException.class, () -> locacaoService.alugarFilme(usuario, null), "Filme vázio!");
    }

    @Test
    //@Ignore
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Date algumSabado = DataUtils.obterData(1,4,2023);
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = List.of(umFilme().agora());
        doReturn(algumSabado).when(locacaoService).generateHoje();

        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getDataRetorno(), caiEmUmaSegunda());
    }

    @Test
    public void naoDeveAlugarFilmeParaNegativadoSpc() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = List.of(umFilme().agora());
        when(spcService.possuiNegativacao(any(Usuario.class))).thenReturn(true);

        //acao
        assertThrows(LocadoraException.class,
                () -> locacaoService.alugarFilme(usuario, filmes));

        //verificacao
        verify(spcService).possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas(){
        //cenario
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("Usuario 2").agora();
        Usuario usuario3 = umUsuario().comNome("Usuario 3").agora();

        List<Locacao> locacoes = List.of(
                umaLocacao().atrasado().comUsuario(usuario).agora(),
                umaLocacao().comUsuario(usuario2).agora(),
                umaLocacao().atrasado().comUsuario(usuario3).agora(),
                umaLocacao().atrasado().comUsuario(usuario3).agora());
        when(locacaoDAO.obterLocacoesPendentes()).thenReturn(locacoes);

        //acao
        locacaoService.notificarAtrasos();

        //verificacao
        verify(emailService).notificarAtraso(usuario);

        verify(emailService, times(3)).notificarAtraso(any(Usuario.class));
        verify(emailService, atLeast(2)).notificarAtraso(usuario3);

        //Garantir que o email não foi enviado para o usuario 2
        verify(emailService, never()).notificarAtraso(usuario2);

        //Garantir que não foi enviado nenhum outro email alem dos 3
        verifyNoMoreInteractions(emailService);

        //Garantir que não usado o serviço abaixo(não tem haver com o código, é só uma curiosidade)
        verifyNoInteractions(spcService);
    }

    @Test
    public void deveTratarErroSPC() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = List.of(umFilme().agora());
        when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrófica"));

        //acao
        assertThrows(LocadoraException.class,() -> locacaoService.alugarFilme(usuario, filmes), "Problemas com SPC, tente novamente");
    }

    @Test
    public void deveProrrogarUmaLocacao(){
        //cenario
        final int DAYS = 3;
        Locacao locacao = umaLocacao().agora();

        //acao
        locacaoService.prorrogarLocacao(locacao, DAYS);

        //verificacao
        ArgumentCaptor<Locacao> argCaptor = ArgumentCaptor.forClass(Locacao.class);
        verify(locacaoDAO).salvar(argCaptor.capture());
        Locacao locCaptured = argCaptor.getValue();

        assertAll("Teste da nova Locacao",
                () -> assertThat(locCaptured.getValor(), is(locacao.getValor() * DAYS)),
                () -> assertThat(locCaptured.getDataLocacao(), isHoje()),
                () -> assertThat(locCaptured.getDataRetorno(), isHojePlusDays(DAYS))
        );

    }

    /*
    Se um método é muito complexo para precisar de um teste, seria melhor
    considerar extrai-lo para um servico ou classe separada e testa-lo
    independentemente pois geralmente métodos privados são detalhes
    especificos de implementação de uma classe, e ele ja deveria ser
    testado na classe publica em que foi usado.
     */
    @Test
    public void deveTestarMetodoPrivado() throws Exception {
        //cenario
        Double expectedValue = 4.0;
        List<Filme> filmes = List.of(umFilme().agora());

        //acao
        Class<LocacaoService> clazz = LocacaoService.class;
        Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
        metodo.setAccessible(true);
        Double valor = (Double) metodo.invoke(locacaoService, filmes);

        //verificacao
        assertThat(valor, is(expectedValue));
    }

}
