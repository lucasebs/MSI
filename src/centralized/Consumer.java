package centralized;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import processor.ProcessadorImagens;

public class Consumer implements  Runnable {
    private Buffer buffer;
    private Semaphore free;
    private Semaphore block;
    private Image img;
    private ArrayList<Long> times = new ArrayList<Long>();
    private Long tempoMedio;
    private String outputPath = "src/output/centralized/";

    public Consumer(Buffer buffer, Semaphore free, Semaphore block) {
        System.out.println("{ Consumer - Builded }");
        this.buffer = buffer;
        this.free = free;
        this.block = block;
    }

    @Override
    public void run() {
//        System.out.println("{ Consumer - Running }");

        ProcessadorImagens proc = new ProcessadorImagens();

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(this.outputPath + "log/pti_tpi2.txt", true));

            while(true) {
                try {

    //                System.out.println("{ Consumer : Block }");
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
    //            System.out.println("{ Consumer : Free }");
    //            System.out.println(" - Get image '" + this.img.getFile_name() + this.img.getBrilho() +  "' from Buffer...");

                long begin = System.currentTimeMillis();
                BufferedImage img_out = proc.brilho(this.img.getImg(), this.img.getBright());
    //            System.out.println(" - Image '" + this.img.getFile_name() + this.img.getBrilho() +  "' processed...");
                long end = System.currentTimeMillis();
                this.times.add(end-begin);

                System.out.println( "Processing Time per Image / Tempo de Processamento por Imagem" );
                System.out.println( this.times.get(this.times.size() - 1) + " Milliseconds / Milissegundos");


                writer.write('"' + this.img.getFile_name() + '"' + ';' +
                        String.valueOf(this.times.get(this.times.size() - 1)));
                writer.newLine();


                try {

                    File f = new File(this.outputPath + "images/" + this.img.getFile_name() + ".jpg");
                    ImageIO.write(img_out, "jpg", f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
    //            System.out.println(" - Image '" + this.img.getFile_name() + this.img.getBrilho() +  "' saved...");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
