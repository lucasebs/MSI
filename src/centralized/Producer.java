package centralized;

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
    private ArrayList<Semaphore> free;
    private ArrayList<Semaphore> blocked;
    private Integer consumidores;
    private Integer quantidadeImagens = 10;

    private Random r = new Random();

    private String img_name = "sample_image";
    private String img_extension = ".jpg";

    public Producer(ArrayList<Buffer> buffers, ArrayList<Semaphore> free, ArrayList<Semaphore> blocked) {
        System.out.println("[ Producer - Builded ]");
        this.buffers = buffers;
        this.free = free;
        this.blocked = blocked;
        this.consumidores = buffers.size();
    }

    @Override
    public void run() {
//        this.qtds = new int[this.consumidores];

//        for (int i=0;i<this.consumidores;i++ ) {
//            this.qtds[i] = r.nextInt(8);
//        };

//        System.out.println("[ Producer - Running ]");

        int cont = 0;
        int i = 0;

        while (true) {
            if (cont >= this.quantidadeImagens) {
                break;
            }

            Buffer buffer = buffers.get(i);

            try {
                free.get(i).acquire();
//                System.out.println("[ Producer : Block ]");

            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
            Image img = null;
//            System.out.println(cont-(this.quantidadeImagens/2));
            try {
                File f = new File("src/input/"+ this.img_name + this.img_extension);
//                img = new Image(ImageIO.read(f),this.img_name + "processed_ ",cont-(this.quantidadeImagens/2));
                img = new Image(ImageIO.read(f),this.img_name + "_processed_", cont);
            } catch (IOException e) {
                System.out.println("Erro: " + e);
            }
//            System.out.println(" - Adding image... " + i );
            buffer.insert(img);
            blocked.get(i).release();
//            System.out.println("[ Producer : Free ]");

            cont++;
            i = cont % this.consumidores;
        }

        for (int h=0; h<consumidores;h++){
            Image img = new Image();
            try {
                free.get(h).acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Buffer buffer = buffers.get(h);
            buffer.insert(img);
            blocked.get(h).release();
        }

        System.out.println("[ Producer - Done ]");

    }
}
