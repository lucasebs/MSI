import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Producer implements Runnable {
    private ArrayList<Buffer> buffers;
    private ArrayList<Semaphore> livres;
    private ArrayList<Semaphore> ocupados;
    private Integer consumidores;
    private Integer quantidadeImagens;

    private Random r = new Random();

    public Producer(ArrayList<Buffer> buffers, ArrayList<Semaphore> livres, ArrayList<Semaphore> ocupados) {
        System.out.println("Producer criado...");
        this.buffers = buffers;
        this.livres = livres;
        this.ocupados = ocupados;
        this.consumidores = buffers.size();
    }

    @Override
    public void run() {
//        this.qtds = new int[this.consumidores];

//        for (int i=0;i<this.consumidores;i++ ) {
//            this.qtds[i] = r.nextInt(8);
//        };

        System.out.println("Producer rodando...");

        int cont = 0;
        this.quantidadeImagens = 50;
        int i = 0;

        while (true) {
            if (cont >= this.quantidadeImagens) {
                break;
            }

            Buffer buffer = buffers.get(i);

            try {
                livres.get(i).acquire();
                System.out.println("Producer ocupa...");

            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
            Image img = null;
            System.out.println(cont-(this.quantidadeImagens/2));
            try {
                File f = new File("imagem_exemplo" + ".jpg");
                img = new Image(ImageIO.read(f),"imagem_exemplo_processada_",cont-(this.quantidadeImagens/2));
            } catch (IOException e) {
                System.out.println("Erro: " + e);
            }
            System.out.println("Adicionando Imagem " + i );
            buffer.insert(img);
            ocupados.get(i).release();
            System.out.println("Producer libera...");

            cont++;
            i = cont % this.consumidores;
        }

        for (int h=0; h<consumidores;h++){
            Image img = new Image();
            try {
                livres.get(h).acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Buffer buffer = buffers.get(h);
            buffer.insert(img);
            ocupados.get(h).release();
        }
    }
}
