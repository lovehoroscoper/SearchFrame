package spider;

import java.net.MalformedURLException;
import java.net.URL;

public class TestURL {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		URL fromUrl = new URL("http://www.lietu.com/news/");
		String url = "../index.html";//��ó����ӵ�ֵ
		String newUrl = (new URL(fromUrl, url)).toString();//ת��String����
		System.out.println(newUrl);
	}
}
