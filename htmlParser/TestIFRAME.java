package htmlParser;

import java.io.IOException;
import java.net.URL;

import org.htmlparser.Node;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.nodes.TagNode;
import org.htmlparser.util.ParserException;

public class TestIFRAME {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ParserException
	 */
	public static void main(String[] args) throws ParserException, IOException {
		String path = "http://tianya.cn";
		URL urlPage = new URL(path);
		Lexer lexer = new Lexer(urlPage.openConnection()); // ������ҳ
		Node node;
		while (null != (node = lexer.nextNode())) {
			if (!(node instanceof TagNode)) {// �ж�node�Ƿ�TagNode���͵Ľڵ�
				continue;
			}
			
			TagNode tagNode = (TagNode) node; // ǿ��ת������
			String name = tagNode.getTagName();// ���������ж��Ƿ�IFRAME
			if (name.equals("IFRAME") && !tagNode.isEndTag()) {
				String newURL = tagNode.getAttribute("src");
				URL u = new URL(new URL(path), newURL);// ȡ��URL�ľ��Ե�ַ
				System.out.println(u.toString());
			}
		}

	}

}
