package tiduswr.servicos;

import org.junit.jupiter.api.Test;
import tiduswr.entidades.Usuario;

import static org.junit.jupiter.api.Assertions.*;

public class AssertTest {

    @Test
    public void test(){
        assertTrue(true);
        assertFalse(false);
        assertEquals(1,1, "Erro de comparação");

        //Exemplos float
        assertEquals(0.51234,0.512, 0.001);
        assertEquals(Math.PI, 3.14, 0.01);

        //Exemplos int
        int i = 5;
        Integer i2 = 5;
        assertEquals(i, i2.intValue());

        //String
        assertEquals("bola", "bola");
        assertNotEquals("bola", "Bola");
        assertTrue("bola".equalsIgnoreCase("Bola"));
        assertTrue("bola".startsWith("bo"));

        Usuario u1 = new Usuario("HARLLEM");
        Usuario u2 = new Usuario("HARLLEM");
        Usuario u3 = null;

        assertEquals(u1, u2);
        assertSame(u1,u1);
        assertNotSame(u1,u2);
        assertNull(u3);
        assertNotNull(u2);
    }

}
