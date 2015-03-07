package spider;

import java.net.*;
import java.io.*;

/**
 * ͨ��socket��HTTP GET����ͨ��"URL"������
 * 
 *
 */
public class SHead {
	public static void main(String[] args) throws IOException {
		String frontURL = "http://www.lietu.com/index.jsp";
		//"http://www.tendata.cn/";
		//"http://www.mysteel.com/";
	    URL u = new URL(frontURL);
	    
		Socket s = null;

		try {
			String host = u.getHost();// ������ ������ www.lietu.com
			System.out.println(host);
			String file = u.getPath();// ��ҳ·�� ������ /index.jsp
			System.out.println(file);
			int port = 80; // �˿ں�

			s = new Socket(host, port);

			OutputStream out = s.getOutputStream();
			PrintWriter outw = new PrintWriter(out, false);
			outw.print("HEAD " + file + " HTTP/1.0\r\n");
			//outw.print("User-Agent: Internet Explorer\r\n");
			//User-Agent", "Internet Explorer
			//outw.print("Accept: text/plain, text/html, text/*\r\n");
			outw.print("\r\n");
			outw.flush();

			InputStream in = s.getInputStream();
			InputStreamReader inr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(inr);
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}

		if (s != null) {
			try {
				s.close();
			} catch (IOException ioEx) {
			}
		}
	}
}
