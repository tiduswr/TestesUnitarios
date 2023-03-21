package tiduswr.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tiduswr.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraTest {
    private Calculadora calc;

    @Before
    public void setup(){
        calc = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores(){
        //Cenário
        int a = 5;
        int b = 3;
        int expected = 8;

        //Ação
        int resultado = calc.somar(a,b);

        //Verificação
        Assert.assertEquals(expected, resultado);
    }

    @Test
    public void deveSubtrairDoisValores(){
        //Cenário
        int a = 5;
        int b = 3;
        int expected = 2;

        //Ação
        int resultado = calc.subtracao(a,b);

        //Verificação
        Assert.assertEquals(expected, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException{
        //Cenário
        int a = 6;
        int b = 2;
        int expected = 3;

        //Ação
        int resultado = calc.dividir(a,b);

        //Verificação
        Assert.assertEquals(expected, resultado);
    }

    @Test
    public void deveLancarExcecaoAoDividirPorZero(){
        //Verificação
        Assert.assertThrows(NaoPodeDividirPorZeroException.class, () -> calc.dividir(3,0));
    }

}
