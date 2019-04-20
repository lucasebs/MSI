package distributed.client;



import distributed.server.ImageReceiverServer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageReceiverClient implements Runnable {
    private DataInputStream input;

    public ImageReceiverClient(DataInputStream input){
        this.input = input;
    }

    @Override
    public void run() {
        try {

            while (true) {
                long stream_size = this.input.readLong();

                System.out.println("Esperando receber imagem de " + stream_size + " bytes");

                String nome = this.input.readUTF();

                System.out.println("Nome do arquivo " + nome);

                nome = nome + "_out";

                byte[] stream = new byte[16 * 1024];
                int count;
                int lidos = 0;
                ByteArrayOutputStream byte_out = new ByteArrayOutputStream();
                while (lidos < stream_size) {
                    count = this.input.read(stream, 0, 16 * 1024);
                    byte_out.write(stream, 0, count);
//                System.out.println("Recebeu " + count + " bytes");
                    lidos += count;
                }

                //criando a imagem a partir do array de stream de bytes
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(byte_out.toByteArray()));

                System.out.println("Imagem recebida...");

                File f2 = new File("video/" + nome + ".jpg");  //output file path
                ImageIO.write(img, "jpg", f2);
                System.out.println("Imagem salva...");
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageReceiverServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
