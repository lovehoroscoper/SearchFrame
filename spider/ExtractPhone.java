package spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractPhone {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String example = "�绰����:81727660.";
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(example);
		// ������еĳ���
		while (matcher.find()) {
			System.out.println("��ȡ�ĵ绰����: ");
			System.out.println(example.substring(matcher.start(), matcher.end()));
		}
	}

}
