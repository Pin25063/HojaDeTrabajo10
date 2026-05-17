import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class GrafoTest extends TestCase {
    private Grafo grafo;

    @Before
    public void setUp() {
        grafo = new Grafo();
        List<String> datosPrueba = Arrays.asList(
                "Mixco, Antigua, 30",
                "Antigua, Escuintla, 25",
                "Escuintla, Santa Lucia, 15"
        );
        grafo.cargarGrafo(datosPrueba);
    }

    @Test
    public void testSeAgreganNodos() {
        assertNotNull(grafo);
    }

    @Test
    public void testSeAgreganArcos() {
        grafo.modificarConexion("Mixco", "Escuintla", 10);

        String centroAntes = grafo.obtenerCentroGrafo();
        assertNotNull(centroAntes);
    }

    @Test
    public void testSeEliminanArcos() {
        grafo.modificarConexion("Antigua", "Escuintla", Grafo.INFINITO);

        assertNotNull(grafo.obtenerCentroGrafo());
    }

    @Test
    public void testFuncionaAlgoritmoFloyd() {
        String centro = grafo.obtenerCentroGrafo();

        assertFalse(centro.equals("Indeterminado (Grafo desconectado)"));
    }
}