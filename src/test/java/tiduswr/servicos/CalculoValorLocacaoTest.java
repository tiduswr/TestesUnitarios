package tiduswr.servicos;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static tiduswr.builder.FilmeBuilder.umFilme;
import static tiduswr.builder.UsuarioBuilder.umUsuario;

@ExtendWith(MockitoExtension.class)
@Execution(ExecutionMode.CONCURRENT)
public class CalculoValorLocacaoTest {

    @InjectMocks @Spy
    private LocacaoService locacaoService;
    @Mock
    private LocacaoDAO locacaoDAO;
    @Mock
    private SpcService spcService;

    @ParameterizedTest(name = "{2}")
    @MethodSource("getParametros")
    void devePagarFilmeComDesconto(List<Filme> filmes, double valorLocacao, String testName)
            throws FilmeSemEstoqueException, LocadoraException, InterruptedException {
        //cenario
        Thread.sleep(5000);
        Usuario usuario = umUsuario().agora();
        doReturn(DataUtils.obterData(1,4,2023)).when(locacaoService).generateHoje();

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
