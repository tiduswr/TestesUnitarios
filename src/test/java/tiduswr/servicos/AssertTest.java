package tiduswr.servicos;

import org.junit.Assert;
import org.junit.Test;
import tiduswr.entidades.Usuario;

public class AssertTest {

    @Test
    public void test(){
        Assert.assertTrue(true);
        Assert.assertFalse(false);
        Assert.assertEquals("Erro de comparação", 1,1);

        //Exemplos float
        Assert.assertEquals(0.51234,0.512, 0.001);
        Assert.assertEquals(Math.PI, 3.14, 0.01);

        //Exemplos int
        int i = 5;
        Integer i2 = 5;
        Assert.assertEquals(i, i2.intValue());

        //String
        Assert.assertEquals("bola", "bola");
        Assert.assertNotEquals("bola", "Bola");
        Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
        Assert.assertTrue("bola".startsWith("bo"));

        Usuario u1 = new Usuario("HARLLEM");
        Usuario u2 = new Usuario("HARLLEM");
        Usuario u3 = null;

        Assert.assertEquals(u1, u2);
        Assert.assertSame(u1,u1);
        Assert.assertNotSame(u1,u2);
        Assert.assertNull(u3);
        Assert.assertNotNull(u2);
    }

}
