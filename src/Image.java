import java.awt.image.BufferedImage;

public class Image {
    private BufferedImage img;
    private String file_name;
    private int brilho;
    private final boolean ending_image;

    public Image(BufferedImage img, String file_name, int brilho) {
        this.img = img;
        this.file_name = file_name;
        this.brilho = brilho;
        this.ending_image = false;
    }
    public Image(){
        this.ending_image = true;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public int getBrilho() {
        return this.brilho;
    }

    public void setBrilho(int brilho) {
        this.brilho = brilho;
    }

    public boolean isEnd() {
        return ending_image;
    }
}