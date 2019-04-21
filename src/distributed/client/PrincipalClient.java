package distributed.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrincipalClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList<Socket> sockets = new ArrayList<Socket>();
        ArrayList<Thread> threads = new ArrayList<Thread>();

        BufferedReader f = new BufferedReader(new FileReader("src/input/servers.txt"));

        int i = 0;
        String line;
        while ((line = f.readLine()) != null) {
            System.out.println(line);
            String[] info = line.split(",");

            Socket s = new Socket(InetAddress.getByName(info[0]), Integer.valueOf(info[1]));
            sockets.add(s);

            DataInputStream input = new DataInputStream(s.getInputStream());

            ImageReceiverClient imageReceiverClient = new ImageReceiverClient(input);
            Thread t = new Thread(imageReceiverClient);
            t.start();
            i++;
        }
        System.out.println("Enviando Imagem...");
        int cont = 0;
        for (int j = 0; j<=5; j++) {
            System.out.println("Enviando Imagem "+j+" para socket "+cont % sockets.size()+"...");
            Thread.sleep(1000);
            cont++;
            DataOutputStream output = new DataOutputStream(sockets.get(cont % sockets.size()).getOutputStream());

            File f1 = new File("src/input/3840_RIGHT_0212"+j+".jpg");

            InputStream in = new FileInputStream(f1);

            long length = f1.length();

            byte[] bytes = new byte[16 * 1024]; //criando o array de bytes usado para enviar os dados da imgagem

            //enviando a quantidade de bytes do arquivo
            output.writeLong(length);
            //enviadno nome do arquivo
            output.writeUTF("3840_RIGHT_0212"+j);
            //enviando o arquivo pela rede
            output.writeInt(100);
            int count = 0;
            //enquanto houver bytes para enviar, obtém do arquivo e manda pela rede
            while ((count = in.read(bytes, 0, bytes.length)) > 0) { //count recebe a qtd de bytes lidos do arquivo para serem enviados
                //  System.out.println("Enviou " + count + " bytes");
                try {
                    output.write(bytes, 0, count); //envia count bytes pela rede
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        cont = 0;
        for (int k = 0; k<=sockets.size(); k++) {
            DataOutputStream output = new DataOutputStream(sockets.get(cont % sockets.size()).getOutputStream());
            cont++;
            output.writeLong(0);
        }


        f.close();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(PrincipalClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

