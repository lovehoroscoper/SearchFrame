package spider;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

public class ReadImage {
    public static void download(String imageUrl,String imageFileName)throws Exception   {
          URL url=new URL(imageUrl);
          
          Image src = javax.imageio.ImageIO.read(url); //����Image����
          int wideth = src.getWidth(null); //�õ�Դͼ��
          int height = src.getHeight(null); //�õ�Դͼ��
          BufferedImage tag = new BufferedImage(wideth / 1, height / 1,
                                                BufferedImage.TYPE_INT_RGB);
          //������С���ͼ
          tag.getGraphics().drawImage(src, 0, 0, wideth / 1, height / 1, null);
          File file = new File(imageFileName); //������ļ���
          ImageIO.write(tag,"jpg", file);
    }
    public static void main(String[] args) throws Exception {
        ReadImage.download("http://www.51766.com/img/jhzdd/1167040251883.bmp",
                                "D:/HH.jpg");
      }
}

