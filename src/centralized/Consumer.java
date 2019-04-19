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
    private Semaphore livre;
    private Semaphore ocupado;
    private Image img;
    private ArrayList<Long> tempos;
    private Long tempoMedio;

    public Consumer(Buffer buffer, Semaphore livre, Semaphore ocupado) {
        System.out.println("Consumer criado...");
        this.buffer = buffer;
        this.livre = livre;
        this.ocupado = ocupado;
    }

    @Override
    public void run() {
        System.out.println("Consumer rodando...");

        ProcessadorImagens proc = new ProcessadorImagens();

        while(true) {
            long inicio = System.currentTimeMillis();
            try {

                System.out.println("Consumer - ocupa...");
                ocupado.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.img = buffer.remove();
            if (this.img.isEnd()){
                System.out.println("Consumer encerado...");
//                tempoMedio =
                break;
            }

            livre.release();
            System.out.println("Consumer - libera...");
            System.out.println("Imagem " + this.img.getFile_name() + this.img.getBrilho() +  " retirada do Buffer...");

            BufferedImage img_out = proc.brilho(this.img.getImg(), this.img.getBrilho());
            System.out.println("Imagem " + this.img.getFile_name() + this.img.getBrilho() +  " processada...");

            try {

                File f = new File("test/" + this.img.getFile_name() + this.img.getBrilho() + ".jpg");
                ImageIO.write(img_out, "jpg", f);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Imagem " + this.img.getFile_name() + this.img.getBrilho() +  " salva...");
            long fim = System.currentTimeMillis();
            tempos.add(fim-inicio);
        }
    }
}
