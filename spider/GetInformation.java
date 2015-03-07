package spider;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class GetInformation {
	public static void getInformation(String path){
		String strPages=loginGoogle(path);
//		System.out.println(strPages);
	}
	
	public static String loginGoogle(String url) {
		try {
			
			URL pageURL = new URL("http://gde24.ru/company/card/BgFdDh77avHPR4hCfx2Ls4mM/");
			System.out.println(pageURL);
			HttpURLConnection httpurlconnection = (HttpURLConnection) pageURL.openConnection();
			httpurlconnection.setRequestProperty("User-Agent","Internet Explorer");
			httpurlconnection.setRequestProperty("Host", pageURL.getHost());
			httpurlconnection.setRequestProperty("Accept-Language", "ru");
			httpurlconnection.setRequestProperty("Accept-Charset","utf-8;q=0.7,*;q=0.7n");
			httpurlconnection.setRequestProperty("content-type", "text/html");
			
		       // Read from the connection. Default is true.
			httpurlconnection.setDoOutput(true);
		       // Read from the connection. Default is true.
			httpurlconnection.setDoInput(true);

			java.io.InputStream urlStream = httpurlconnection.getInputStream();
			
			// ����һ��ʹ��Ĭ�ϴ�С���뻺�����Ļ����ַ�������
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream,"utf-8"));
			
			String line;
			// ��ȡ��ҳ����
			StringBuilder pageBuffer = new StringBuilder();// �̰߳�ȫ�Ŀɱ��ַ�����
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				pageBuffer.append(line);
			}
			urlStream.close();//������
			// ������ҳ����
//			System.out.println(pageBuffer.toString());
			return pageBuffer.toString();
			
		} catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){
		String strPages=loginGoogle("http://gde24.ru/company/card/BgFdDh77avHPR4hCfx2Ls4mM/");
//		System.out.println(strPages);
	}
}

