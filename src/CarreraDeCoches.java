import java.util.Random;

class Coche implements Runnable {
    private String nombre;
    private int distanciaRecorrida;
    private static final int DISTANCIA_META = 100;
    private static boolean ganadorAnunciado = false;
    private static String ganador = null;
    private static final Object lock = new Object();

    public Coche(String nombre) {
        this.nombre = nombre;
        this.distanciaRecorrida = 0;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (distanciaRecorrida < DISTANCIA_META) {
            // Avance aleatorio entre 1 y 10 unidades
            int avance = random.nextInt(10) + 1;
            distanciaRecorrida += avance;

            // Imprimir progreso
            System.out.println(nombre + " ha avanzado " + distanciaRecorrida + " unidades.");

            // Verificar si el coche ha cruzado la meta
            if (distanciaRecorrida >= DISTANCIA_META) {
                synchronized (lock) {
                    if (!ganadorAnunciado) {
                        ganadorAnunciado = true;
                        ganador = nombre;
                        System.out.println(nombre + " ha cruzado la meta y es el ganador!");
                    }
                }
            }

            // Simular un pequeño retraso
            try {
                Thread.sleep(random.nextInt(500)); // Espera entre 0 y 500 ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class CarreraDeCoches {
    public static void main(String[] args) {
        int numCoches = 5;
        Thread[] hilosCoches = new Thread[numCoches];

        // Crear e iniciar los hilos de los coches
        for (int i = 0; i < numCoches; i++) {
            Coche coche = new Coche("Coche " + (i + 1));
            hilosCoches[i] = new Thread(coche);
            hilosCoches[i].start();
        }

        // Esperar a que todos los hilos terminen
        for (int i = 0; i < numCoches; i++) {
            try {
                hilosCoches[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("La carrera ha terminado.");
    }
}