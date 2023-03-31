package tiduswr.servicos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalculadoraMockTest {

    @Mock
    private Calculadora calcMock;

    @Spy
    private Calculadora calcSpy;

    public void deveMostrarDiferencaEntreMockSpy(){
        /*Quando não sabe oque fazer o Mock retorna o valor padrão,
        e o Spy executa o método de fato, a desvantagem é que o Spy
        não pode 'espionar' Interfaces. O Mock tem algo parecido, no caso
        o thenCallRealMethod()*/
        when(calcMock.somar(1,8)).thenReturn(9);

        //CUIDADO!!!
        //Nessa notação o método somar é executado antes do Mock
        //when(calcSpy.somar(1,8)).thenReturn(9);
        doReturn(9).when(calcSpy).somar(1,8);

        //Mocando metóds void
        doNothing().when(calcSpy).imprime();

        calcMock.imprime();
        calcSpy.imprime();
    }

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
