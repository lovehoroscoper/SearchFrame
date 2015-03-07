package spider;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImgResize {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String inFile = "";
		String outFile = "";
		// ��inFile����ͼƬ�ļ�
		Image image = Toolkit.getDefaultToolkit().getImage(inFile);
		MediaTracker mediaTracker = new MediaTracker(new Container());
		mediaTracker.addImage(image, 0);
		mediaTracker.waitForID(0);
		// �ӿ�Ⱥ͸߶Ȳ�����������ͼ��С
		int thumbWidth = Integer.parseInt(args[2]);
		int thumbHeight = Integer.parseInt(args[3]);
		double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;
		if (thumbRatio < imageRatio) {
			thumbHeight = (int) (thumbWidth / imageRatio);
		} else {
			thumbWidth = (int) (thumbHeight * imageRatio);
		}
		// ��ԭͼ��̬���ŵ��µ�����ͼ��С
		BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
		// ������ͼ���浽OUTFILE
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(outFile));
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
		int quality = Integer.parseInt(args[4]);
		quality = Math.max(0, Math.min(quality, 100));
		param.setQuality((float) quality / 100.0f, false);
		encoder.setJPEGEncodeParam(param);
		encoder.encode(thumbImage);
		out.close();
	}

}
