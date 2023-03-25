package tiduswr.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tiduswr.servicos.CalculadoraTest;
import tiduswr.servicos.CalculoValorLocacaoTest;
import tiduswr.servicos.LocacaoServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CalculadoraTest.class,
        CalculoValorLocacaoTest.class,
        LocacaoServiceTest.class
})
public class SuiteExecucao {}
