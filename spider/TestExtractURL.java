package spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestExtractURL {

	public static void main(String[] args) throws Exception {
		
		String pageContents = "<a href='http://www.lietu.com'>����</a>";
		String email = "123com";
		System.out.println(email.matches("\\d+.*"));
		Pattern p = Pattern.compile("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(pageContents);
		while (m.find()) {//��ȡ��ҳ�е�����
			String link = m.group(1).trim();
			System.out.println(link);
		}
	}
}
