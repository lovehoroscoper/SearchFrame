package newsExtract;

import org.cyberneko.html.parsers.DOMParser;

public class TestTitle {

	public static void main(String[] argv) throws Exception {
		DOMParser parser = new DOMParser(); // ����DOM������
		parser.parse("http://www.gongkong.com/Common/Details.aspx?c=1&m=7&l=8&Type=news&CompanyID=0-8F38-217A2BF31D51&Id=2011042111562900003"); // ������ҳ
		//NewsInfo n = Extractor.extractAll(parser.getDocument());// �Ӹ��ڵ㿪ʼ����
		
		String title = Extractor.getClearTitle(parser.getDocument());
		System.out.println(title);
	}
}
