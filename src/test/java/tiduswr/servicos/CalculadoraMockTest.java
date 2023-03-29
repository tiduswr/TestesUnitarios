package tiduswr.servicos;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class CalculadoraMockTest {
    @Test
    public void teste(){
        Calculadora calc = Mockito.mock(Calculadora.class);

        ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
        when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);

        assertThat(calc.somar(1, 8), is(5));

        /*Para pegar os valores ele deve ser chamado depois de executar o método
         pois antes disso ele funciona apenas como um coringa*/
        List<Integer> args = argCapt.getAllValues();
    }
}
