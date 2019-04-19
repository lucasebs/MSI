import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class Principal {

    public static void main(String[] args) {
        ArrayList<Buffer> buffers = new ArrayList<Buffer>();
        ArrayList<Semaphore> livres = new ArrayList<Semaphore>();
        ArrayList<Semaphore> ocupados = new ArrayList<Semaphore>();
        ArrayList<Consumer> consumers = new ArrayList<Consumer>();
        ArrayList<Thread> threads = new ArrayList<Thread>();
        Integer quantidadeThreads = 3;


        for (int i=0;i<quantidadeThreads;i++) {
            Semaphore livre = new Semaphore(10);
            Semaphore ocupado = new Semaphore(0);
            Buffer buffer = new Buffer();
            Consumer c = new Consumer(buffer,livre,ocupado);
            Thread tc = new Thread(c);

            livres.add(livre);
            ocupados.add(ocupado);
            buffers.add(buffer);
            consumers.add(c);
            threads.add(tc);
        }

        Producer p = new Producer(buffers,livres, ocupados);
        Thread tp = new Thread(p);

        long inicio = System.currentTimeMillis();
        tp.start();
        for (int i=0;i<quantidadeThreads;i++) {
            threads.get(i).start();
        }

        for (int i = 0; i < quantidadeThreads ; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long fim = System.currentTimeMillis();

        System.out.println( "Tempo de execução: " + (fim-inicio) + " milissegundos" );

    }
}
