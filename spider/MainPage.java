package spider;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainPage {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String companyName = "��ɿƼ���չ���޹�˾";
		String searchWord = URLEncoder.encode(companyName, "utf-8");//���ر����Ĳ�ѯ��

		URL companyURL = new URL("http://www.google.com.hk/search?hl=en&newwindow=1&safe=strict&site=&source=hp&q="+searchWord+"&oq="+searchWord+"&aq=f&aqi=&aql=&gs_l=hp.7...4374.9464.0.33510.5.5.0.0.0.0.0.0..0.0...0.0.qLGoENSLM5c&btnI=1");
		
		System.out.println(companyURL);
		HttpURLConnection connection = (HttpURLConnection)companyURL.openConnection(); 
		connection.addRequestProperty("User-Agent", "Mozilla/4.76");
		connection.setConnectTimeout(15000);
		connection.setReadTimeout(15000);
		connection.setInstanceFollowRedirects(false);
		connection.connect();

		System.out.println(connection.getResponseCode());
		System.out.println(connection.getHeaderField("Location"));
	}

}
