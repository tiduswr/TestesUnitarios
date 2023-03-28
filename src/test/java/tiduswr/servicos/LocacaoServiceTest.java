package tiduswr.servicos;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tiduswr.daos.LocacaoDAO;
import tiduswr.entidades.Filme;
import tiduswr.entidades.Locacao;
import tiduswr.entidades.Usuario;
import tiduswr.exceptions.FilmeSemEstoqueException;
import tiduswr.exceptions.LocadoraException;
import tiduswr.utils.DataUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
public class LocacaoServiceTest {

    @InjectMocks
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
        //Não Executa no sbado
        Assumptions.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = List.of(umFilme().comValor(5.0).agora());

        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(locacao.getValor(), is(equalTo(5.0)));
        assertThat(locacao.getDataLocacao(), isHoje());
        assertThat(locacao.getDataRetorno(), isHojePlusDays(1));
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
        //Só executa no sábado
        Assumptions.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY),
                () -> "deveDevolverNaSegundaAoAlugarNoSabado(): Falhou por nao ser sabado.");

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = List.of(umFilme().agora());

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

}
