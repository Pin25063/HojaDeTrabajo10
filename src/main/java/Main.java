import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sn = new Scanner(System.in);
        Grafo grafo = new Grafo();

        try {
            // Lectura inicial del archivo 
            List<String> lineasArchivo = FileHelper.readFile("src/main/resources/guategrafo.txt");
            grafo.cargarGrafo(lineasArchivo);
            grafo.mostrarMatrizAdyacencia();

            int opcion = 0;
            do {
                System.out.println("\n========== MENÚ DE LOGÍSTICA (COVID-19) ==========");
                System.out.println("1. Buscar ruta más corta entre dos ciudades");
                System.out.println("2. Encontrar el centro logístico (Centro del grafo)");
                System.out.println("3. Modificar red vial (Interrupción o nueva conexión)");
                System.out.println("4. Salir");
                System.out.print("Seleccione una opción: ");

                opcion = sn.nextInt();
                sn.nextLine(); // Limpiar el salto de línea del buffer

                switch (opcion) {
                    case 1:
                        System.out.print("\nIngrese la ciudad de origen: ");
                        String origen = sn.nextLine().trim();
                        System.out.print("Ingrese la ciudad de destino: ");
                        String destino = sn.nextLine().trim();
                        grafo.mostrarRutaMasChorta(origen, destino);
                        break;

                    case 2:
                        System.out.println("\nLa ubicación óptima para las oficinas centrales es: " + grafo.obtenerCentroGrafo());
                        break;

                    case 3:
                        System.out.println("\nSubmenú de modificación de red:");
                        System.out.println("  a) Reportar bloqueo/interrupción de tráfico");
                        System.out.println("  b) Registrar nueva conexión / habilitar paso");
                        System.out.print("Selección (a/b): ");
                        String subOp = sn.nextLine().trim().toLowerCase();

                        System.out.print("Ingrese ciudad origen: ");
                        String o = sn.nextLine().trim();
                        System.out.print("Ingrese ciudad destino: ");
                        String d = sn.nextLine().trim();

                        if (subOp.equals("a")) {
                            grafo.modificarConexion(o, d, Grafo.INFINITO); // Interrupción equivale a INFINITO 
                            System.out.println("Tránsito interrumpido con éxito.");
                        } else if (subOp.equals("b")) {
                            System.out.print("Ingrese la distancia en KM: ");
                            int km = sn.nextInt();
                            grafo.modificarConexion(o, d, km);
                            System.out.println("Conexión actualizada con éxito.");
                        } else {
                            System.out.println("Opción inválida.");
                        }
                        break;

                    case 4:
                        System.out.println("\nCerrando el sistema de distribución logística nacional. ¡Hasta pronto!");
                        break;

                    default:
                        System.out.println("\nOpción no válida.");
                }
            } while (opcion != 4);

        } catch (IOException e) {
            System.out.println("\nError crítico: No se pudo leer el archivo 'guategrafo.txt'. Verifique su ubicación.");
        } finally {
            sn.close();
        }
    }
}