import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Jacob Gordon
 * @version 1.0
 * @date 5/27/19
 **/
public class Main {

    public static void main(String[] args) {
        Main m = new Main();
        m.filterImages();
    }

    public void filterImages() {
        BufferedImage img = ImageUtils.getImage("nut.png");
        BufferedImage img2 = ImageUtils.getImage("nut2.png");

        Main m = new Main();
        m.createWindow();
        m.addImage(img);

        Scanner s = new Scanner(System.in);
        int k = 2;
        while (true) {
            String str = s.nextLine();
            Integer[][][] imgData = ImageUtils.getRGBMatrixFromImage(img);
            Integer[][] newImgData = ImageUtils.preformRGBConvolutionPadded(imgData, ImageUtils.getFiler(str));
            newImgData = ImageUtils.operationMaxPooling(newImgData, 5, 5, 2);

            System.out.print("IMG LENGTH: " + MatrixUtils.getMatrixLength(imgData[0]));
            System.out.print("NEW MATRIX LENGTH: " + MatrixUtils.getMatrixLength(newImgData));

            try {
                File f = ImageUtils.writeFileFromRGBMatrix("nut" + k++ + ".png", newImgData);
                BufferedImage myImg = ImageIO.read(f);
                if (myImg != null) {
                    m.addImage(myImg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    JFrame frame;
    public void createWindow() {
        frame = new JFrame("Jupiter 1.0");
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void addImage(BufferedImage img) {
        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        frame.pack();
        frame.repaint();
    }
}
