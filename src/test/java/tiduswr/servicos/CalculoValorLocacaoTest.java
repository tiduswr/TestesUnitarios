package tiduswr.servicos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tiduswr.daos.LocacaoDAO;
import tiduswr.entidades.Filme;
import tiduswr.entidades.Locacao;
import tiduswr.entidades.Usuario;
import tiduswr.exceptions.FilmeSemEstoqueException;
import tiduswr.exceptions.LocadoraException;
import tiduswr.utils.DateFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static tiduswr.builder.FilmeBuilder.umFilme;
import static tiduswr.builder.UsuarioBuilder.umUsuario;

@ExtendWith(MockitoExtension.class)
public class CalculoValorLocacaoTest {

    @InjectMocks
    private LocacaoService locacaoService;
    @Mock
    private LocacaoDAO locacaoDAO;
    @Mock
    private SpcService spcService;
    @Mock
    private DateFactory dateFactory;

    @BeforeEach
    public void setup(){
        /*Lenient serve para definir um comportamento padrão para todos os testes
        precisa ser usado pois ao definir um comportamento aqui o mockito vai entender
        como se esse comportamento foi definido mas não usado, e lançara a exceção UnnecessaryStubbingException*/
        lenient().when(dateFactory.generateHoje()).thenReturn(new Date());
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("getParametros")
    void devePagarFilmeComDesconto(List<Filme> filmes, double valorLocacao, String testName)
            throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = umUsuario().agora();

        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

        //verificação
        assertEquals(valorLocacao, resultado.getValor());
    }

    private static Collection<Object[]> getParametros() {
        Filme FILME1 = umFilme().agora();
        Filme FILME2 = umFilme().agora();
        Filme FILME3 = umFilme().agora();
        Filme FILME4 = umFilme().agora();
        Filme FILME5 = umFilme().agora();
        Filme FILME6 = umFilme().agora();
        Filme FILME7 = umFilme().agora();
        return Arrays.asList(new Object[][]{
                {Arrays.asList(FILME1,FILME2), 8.0, "2 Filmes sem desconto"},
                {Arrays.asList(FILME1,FILME2,FILME3), 11.0, "3 Filmes 25%"},
                {Arrays.asList(FILME1,FILME2,FILME3, FILME4), 13.0, "4 Filmes 50%"},
                {Arrays.asList(FILME1,FILME2,FILME3, FILME4, FILME5), 14.0, "5 Filmes 75%"},
                {Arrays.asList(FILME1,FILME2,FILME3, FILME4, FILME5, FILME6), 14.0, "6 Filmes 100%"},
                {Arrays.asList(FILME1,FILME2,FILME3, FILME4, FILME5, FILME6, FILME7), 18.0, "7 Filmes sem desconto a partir do 7th filme"},
        });
    }
}
