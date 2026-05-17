import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grafo {
    public static final int INFINITO = 9999999;

    private Map<String, Integer> ciudadMap;
    private ArrayList<ArrayList<Integer>> matrizAdyacencia;
    private ArrayList<ArrayList<Integer>> matrizRecorridos;
    private int totalCiudades;

    public Grafo() {
        this.ciudadMap = new HashMap<>();
        this.matrizAdyacencia = new ArrayList<>();
        this.matrizRecorridos = new ArrayList<>();
    }

    // Inicializa y procesa el grafo desde las líneas del archivo
    public void cargarGrafo(List<String> lineasArchivo) {
        ciudadMap.clear();
        matrizAdyacencia.clear();
        matrizRecorridos.clear();

        // Identificar ciudades únicas
        for (String linea : lineasArchivo) {
            String[] datos = linea.split(",");
            String origen = datos[0].trim();
            String destino = datos[1].trim();

            if (!ciudadMap.containsKey(origen)) ciudadMap.put(origen, ciudadMap.size());
            if (!ciudadMap.containsKey(destino)) ciudadMap.put(destino, ciudadMap.size());
        }

        totalCiudades = ciudadMap.size();

        // Inicializar matriz de adyacencia
        for (int i = 0; i < totalCiudades; i++) {
            ArrayList<Integer> fila = new ArrayList<>();
            for (int j = 0; j < totalCiudades; j++) {
                fila.add(i == j ? 0 : INFINITO);
            }
            matrizAdyacencia.add(fila);
        }

        // Rellenar con los pesos del archivo 
        for (String linea : lineasArchivo) {
            String[] datos = linea.split(",");
            String origen = datos[0].trim();
            String destino = datos[1].trim();
            int distancia = Integer.parseInt(datos[2].trim());

            matrizAdyacencia.get(ciudadMap.get(origen)).set(ciudadMap.get(destino), distancia);
        }

        // Ejecutar algoritmo de optimización de rutas
        recalcularFloyd();
    }

    // Lógica del Algoritmo de Floyd
    public void recalcularFloyd() {
        matrizRecorridos.clear();

        // Reinicializar matriz de recorridos base
        for (int i = 0; i < totalCiudades; i++) {
            ArrayList<Integer> filaRecorrido = new ArrayList<>();
            for (int j = 0; j < totalCiudades; j++) {
                if (i == j || matrizAdyacencia.get(i).get(j) == INFINITO) {
                    filaRecorrido.add(-1);
                } else {
                    filaRecorrido.add(j);
                }
            }
            matrizRecorridos.add(filaRecorrido);
        }

        // Tres bucles de Floyd
        for (int k = 0; k < totalCiudades; k++) {
            for (int i = 0; i < totalCiudades; i++) {
                for (int j = 0; j < totalCiudades; j++) {
                    int distIntermedia = matrizAdyacencia.get(i).get(k) + matrizAdyacencia.get(k).get(j);
                    if (matrizAdyacencia.get(i).get(k) != INFINITO && matrizAdyacencia.get(k).get(j) != INFINITO) {
                        if (distIntermedia < matrizAdyacencia.get(i).get(j)) {
                            matrizAdyacencia.get(i).set(j, distIntermedia);
                            matrizRecorridos.get(i).set(j, matrizRecorridos.get(i).get(k));
                        }
                    }
                }
            }
        }
    }

    // Método para agregar o modificar conexiones dinámicamente en pantalla
    public void modificarConexion(String origen, String destino, int distancia) {
        if (ciudadMap.containsKey(origen) && ciudadMap.containsKey(destino)) {
            int idOrigen = ciudadMap.get(origen);
            int idDestino = ciudadMap.get(destino);
            matrizAdyacencia.get(idOrigen).set(idDestino, distancia);
            recalcularFloyd(); // Recalcular automáticamente
        } else {
            System.out.println("Una o ambas ciudades no existen en el mapa.");
        }
    }

    // Opción 1: Obtener la ruta más corta detallada
    public void mostrarRutaMasChorta(String origen, String destino) {
        if (!ciudadMap.containsKey(origen) || !ciudadMap.containsKey(destino)) {
            System.out.println("Error: Asegúrate de escribir correctamente los nombres.");
            return;
        }

        int idOrigen = ciudadMap.get(origen);
        int idDestino = ciudadMap.get(destino);
        int distancia = matrizAdyacencia.get(idOrigen).get(idDestino);

        if (distancia == INFINITO) {
            System.out.println("No existe ninguna ruta transitable entre " + origen + " y " + destino);
            return;
        }

        System.out.println(" -> Peso total de la ruta: " + distancia + " KM");
        System.out.print(" -> Itinerario: " + origen);

        int actual = idOrigen;
        while (actual != idDestino) {
            actual = matrizRecorridos.get(actual).get(idDestino);
            System.out.print(" -> " + obtenerNombrePorId(actual));
        }
        System.out.println();
    }

    // Opción 2: Cálculo del centro del grafo basado en excentricidades
    public String obtenerCentroGrafo() {
        int centroId = -1;
        int minExcentricidad = INFINITO;

        for (int i = 0; i < totalCiudades; i++) {
            int maxFila = 0;
            for (int j = 0; j < totalCiudades; j++) {
                if (i != j && matrizAdyacencia.get(i).get(j) > maxFila) {
                    maxFila = matrizAdyacencia.get(i).get(j);
                }
            }
            // El nodo con el valor máximo más pequeño de su fila es el centro
            if (maxFila < minExcentricidad && maxFila != INFINITO) {
                minExcentricidad = maxFila;
                centroId = i;
            }
        }
        return centroId != -1 ? obtenerNombrePorId(centroId) : "Indeterminado (Grafo desconectado)";
    }

    // Muestra la matriz en pantalla
    public void mostrarMatrizAdyacencia() {
        System.out.println("\n--- MATRIZ DE ADYACENCIA ACTUAL ---");
        for (int i = 0; i < totalCiudades; i++) {
            for (int j = 0; j < totalCiudades; j++) {
                int valor = matrizAdyacencia.get(i).get(j);
                System.out.print((valor == INFINITO ? "INF" : valor) + "\t");
            }
            System.out.println();
        }
    }

    private String obtenerNombrePorId(int id) {
        for (Map.Entry<String, Integer> entry : ciudadMap.entrySet()) {
            if (entry.getValue() == id) return entry.getKey();
        }
        return "";
    }
}