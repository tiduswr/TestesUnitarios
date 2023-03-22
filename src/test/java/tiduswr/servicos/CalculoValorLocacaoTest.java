package tiduswr.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tiduswr.entidades.Filme;
import tiduswr.entidades.Locacao;
import tiduswr.entidades.Usuario;
import tiduswr.exceptions.FilmeSemEstoqueException;
import tiduswr.exceptions.LocadoraException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    private LocacaoService locacaoService;

    @Parameterized.Parameter
    public List<Filme> filmes;

    @Parameterized.Parameter(value = 1)
    public Double valorLocacao;

    @Parameterized.Parameter(value = 2)
    public String nomeTeste;

    //Amostras
    public static final Filme FILME1 = new Filme("MATRIX",2,4.0);
    public static final Filme FILME2 = new Filme("SHREK",4,4.0);
    public static final Filme FILME3 = new Filme("AUTO DA COMPADECIDA",3,4.0);
    public static final Filme FILME4 = new Filme("DUMB GUYS",5,4.0);
    public static final Filme FILME5 = new Filme("A CULPA É DAS ESTRELAS",7,4.0);
    public static final Filme FILME6 = new Filme("MAIS QUE AMIGOS, FRIENDS",10,4.0);
    public static final Filme FILME7 = new Filme("BREAKING BAD 2 - O RETORNO DE FLYNN",13,4.0);

    @Before
    public void setup(){
        locacaoService = new LocacaoService();
    }

    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object[]> getParametros(){
        return Arrays.asList(new Object[][]{
            {Arrays.asList(FILME1,FILME2), 8.0, "2 Filmes sem desconto"},
            {Arrays.asList(FILME1,FILME2,FILME3), 11.0, "3 Filmes 25%"},
            {Arrays.asList(FILME1,FILME2,FILME3, FILME4), 13.0, "4 Filmes 50%"},
            {Arrays.asList(FILME1,FILME2,FILME3, FILME4, FILME5), 14.0, "5 Filmes 75%"},
            {Arrays.asList(FILME1,FILME2,FILME3, FILME4, FILME5, FILME6), 14.0, "6 Filmes 100%"},
            {Arrays.asList(FILME1,FILME2,FILME3, FILME4, FILME5, FILME6, FILME7), 18.0, "7 Filmes sem desconto a partir do 7th filme"},
        });
    }

    @Test
    public void devePagarFilmeComDesconto() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("HARLLEM");

        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

        //verificação
        Assert.assertEquals(valorLocacao, resultado.getValor());
    }

}
