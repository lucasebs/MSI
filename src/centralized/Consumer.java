package centralized;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Consumer implements  Runnable {
    private Buffer buffer;
    private Semaphore free;
    private Semaphore block;
    private Image img;
    private ArrayList<Long> tempos;
    private Long tempoMedio;

    public Consumer(Buffer buffer, Semaphore free, Semaphore block) {
        System.out.println("{ Consumer - Builded }");
        this.buffer = buffer;
        this.free = free;
        this.block = block;
    }

    @Override
    public void run() {
        System.out.println("{ Consumer - Running }");

        ProcessadorImagens proc = new ProcessadorImagens();

        while(true) {
            long inicio = System.currentTimeMillis();
            try {

                System.out.println("{ Consumer : Block }");
                block.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.img = buffer.remove();
            if (this.img.isEnd()){
                System.out.println("{ Consumer - Done }");
//                tempoMedio =
                break;
            }

            free.release();
            System.out.println("{ Consumer : Free }");
            System.out.println(" - Get image '" + this.img.getFile_name() + this.img.getBrilho() +  "' from Buffer...");

            BufferedImage img_out = proc.brilho(this.img.getImg(), this.img.getBrilho());
            System.out.println(" - Image '" + this.img.getFile_name() + this.img.getBrilho() +  "' processed...");

            try {

                File f = new File("src/output/" + this.img.getFile_name() + this.img.getBrilho() + ".jpg");
                ImageIO.write(img_out, "jpg", f);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(" - Image '" + this.img.getFile_name() + this.img.getBrilho() +  "' saved...");
            long fim = System.currentTimeMillis();
//            tempos.add(fim-inicio);
        }
    }
}
