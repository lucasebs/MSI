package centralized;

import javax.imageio.ImageIO;
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
    private Integer consumers;
//    private Integer imagesQuantity = 10;

    private Random r = new Random();

//    private String img_name = "sample_image";
    private String img_extension = ".jpg";

    public Producer(ArrayList<Buffer> buffers, ArrayList<Semaphore> free, ArrayList<Semaphore> blocked) {
        System.out.println("[ Producer - Builded ]");
        this.buffers = buffers;
        this.free = free;
        this.blocked = blocked;
        this.consumers = buffers.size();
    }

    @Override
    public void run() {
//        this.qtds = new int[this.consumers];

//        for (int i=0;i<this.consumers;i++ ) {
//            this.qtds[i] = r.nextInt(8);
//        };

//        System.out.println("[ Producer - Running ]");

        ArrayList<String> result = new ArrayList<String>();

        File folder = new File("src/input/samples/");
        File[] listOfFiles = folder.listFiles();

        for (int j = 0; j < listOfFiles.length; j++) {
            result.add(listOfFiles[j].getName());
        }



//        while (true) {
        int cont = 0;
        int i = 0;
        for (String r : result) {

            String img_name = r.split("\\.")[0];
//            if (cont >= this.imagesQuantity) {
//                break;
//            }

            Buffer buffer = buffers.get(i);

            try {
                free.get(i).acquire();
//                System.out.println("[ Producer : Block ]");

            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
            Image img = null;
//            System.out.println(cont-(this.imagesQuantity/2));
            try {
                File f = new File("src/input/samples/"+ img_name + this.img_extension);
                System.out.println(f);
//                img = new Image(ImageIO.read(f),this.img_name + "processed_ ",cont-(this.imagesQuantity/2));
                img = new Image(ImageIO.read(f),img_name + "_processed", 100);
            } catch (IOException e) {
                System.out.println("Erro: " + e);
            }
//            System.out.println(" - Adding image " + i + "...");
            buffer.insert(img);
            blocked.get(i).release();
//            System.out.println("[ Producer : Free ]");

            cont++;
            i = cont % this.consumers;
        }

        for (int h=0; h < this.consumers ;h++){
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
