package newsExtract;

import java.util.ArrayList;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Extractor {
	public static void main(String[] argv) throws Exception {
		DOMParser parser = new DOMParser(); // ����DOM������
		parser.parse("http://www.gongkong.com/Common/Details.aspx?c=1&m=7&l=8&Type=news&CompanyID=0-8F38-217A2BF31D51&Id=2011042111562900003"); // ������ҳ
		NewsInfo n = extractAll(parser.getDocument());// �Ӹ��ڵ㿪ʼ����
		System.out.println(n.toString());
	}

	public static String textExtractor(Node root) {
		// �����ı��ڵ�Ļ���ֱ�ӷ���
		if (root.getNodeType() == Node.TEXT_NODE) {
			return root.getNodeValue().trim();
		}
		if (root.getNodeType() == Node.ELEMENT_NODE) {
			Element elmt = (Element) root;
			// �����ű�
			if (elmt.getTagName().equals("STYLE")
					|| elmt.getTagName().equals("SCRIPT"))
				return "";

			NodeList children = elmt.getChildNodes();
			StringBuilder text = new StringBuilder();
			for (int i = 0; i < children.getLength(); i++) {
				text.append(textExtractor(children.item(i)));
			}
			return text.toString();
		}
		// ���������͵Ľڵ㣬���ؿ�ֵ
		return "";
	}

	static NewsInfo extractAll(Node documentNode) {
		NewsInfo needInfo = new NewsInfo();
		String infoSource = ""; /* ��Ϣ��Դ */
		String publishDate = ""; /* ����ʱ�� */
		String title = ""; /* ���� */
		String text = ""; /* ���� */
		String author = ""; /* ���� */
		int start = 0; // ���ĵĿ�ʼλ��
		int end = 0; // ���ĵĽ���λ��

		boolean findTextStartFlag = false; /* ��ʼ���ҵ����Ŀ�ͷ��־ */
		boolean findTextEndFlag = false; /* ��ʼ���ҵ����Ľ�β��־ */
		boolean findInfoSource = false; /* ��ʼ���ҵ���Ϣ��Դ��־ */
		boolean findPublishDate = false; /* ��ʼ���ҵ��������ڱ�־ */
		boolean findTextAuthor = false; /* ��ʼ���ҵ����߱�־ */

		/* ��ȡ���� */
		title = getClearTitle(documentNode);

		/* ȡ��DOM�����ڵ������ı� */
		String content = textExtractor(documentNode);

		/* ִ����Ϣ��ȡ ���ù���ʽ��ȡС���ı� */
		ArrayList<DocToken> ret = DocTagger.tag(content);
		if(ret==null)
			return null;

		for (int j = 0; j < ret.size(); j++) {
			DocToken token = ret.get(j);

			/* ��Ϣ��Դ */
			if (token.type.equals(DocType.InfoSource)
					&& (findInfoSource == false)) {
				infoSource = token.termText;
				findInfoSource = true; /* �ҵ���Ϣ��Դ��־ */
				continue;
			}

			/* ���� */
			if (token.type.equals(DocType.Author) && (findTextAuthor == false)) {
				author = token.termText;
				findTextAuthor = true; /* �ҵ����� */
				if (author.length() >= 4) {
					author = "";
					findTextAuthor = false;
				}
				continue;
			}

			/* ����ʱ�� */
			if (token.type.equals(DocType.PublishDate)
					&& (findPublishDate == false)) {
				publishDate = token.termText;
				findPublishDate = true; /* �ҵ��������ڱ�־ */
				continue;
			}

			/* ����ǰ׺ */
			if (token.type.equals(DocType.PrefixText)
					&& (findTextStartFlag == false)) {
				start = j;
				findTextStartFlag = true;
				continue;
			}

			/* ���ĺ�׺ */
			if (token.type.equals(DocType.SuffixText)
					&& (findTextEndFlag == false)
					&& (findTextStartFlag == true)) {
				end = j;
				findTextEndFlag = true;
				continue;
			}
		}

		text = "";
		if ((findTextStartFlag == true) && (findTextEndFlag == true)) {
			for (int j = start + 1; j < end - 1; j++) {
				text += ret.get(j).termText;
			}
		}

		// ȥ��ͷβ��һЩ����Ҫ���ַ�
		title = Entities.HTML40.unescape(title).trim();

		/* �������е�ת���ַ� */
		text = Entities.HTML40.unescape(text.toString()).trim();
		needInfo.title = title;
		needInfo.text = text;
		needInfo.author = author;
		needInfo.infoSource = infoSource;
		needInfo.publishDate = publishDate;

		return needInfo;
	}

	private static void getText(StringBuffer sb, Node node) {
		if (node.getNodeType() == Node.TEXT_NODE) {
			sb.append(node.getNodeValue());// ȡ�ý��ֵ������ʼ�������ǩ֮�����Ϣ
		}
		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				getText(sb, children.item(i));// �ݹ����DOM��
			}
		}
	}

	private static boolean getTitle(StringBuffer sb, Node node) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			if ("title".equalsIgnoreCase(node.getNodeName())) {
				getText(sb, node);
				return true;
			}
		}
		org.w3c.dom.NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				if (getTitle(sb, children.item(i))) {
					return true;
				}
			}
		}
		return false;
	}

	/* ��ȡ��ҳ���� */
	public static String getClearTitle(Node node) {
		int startPos = 0;
		int endPos = 0;
		String title = "";
		StringBuffer sb = new StringBuffer();
		getTitle(sb, node);

		ArrayList<DocToken> ret = DocTagger.tag(sb.toString().trim());

		boolean findTitleStartFlag = false;
		boolean findTitleEndFlag = false;
		for (int i = 0; i < ret.size(); ++i) {
			/* ���� */
			if (ret.get(i).type.equals(DocType.PrefixTitle)
					&& (findTitleStartFlag == false)
					&& (findTitleEndFlag == false)) {
				findTitleStartFlag = true; /* �ҵ����⿪ͷ��־ */
				startPos = i;
				continue;
			} else if (((ret.get(i).type.equals(DocType.SuffixTitle)) || (ret
					.get(i).type.equals(DocType.PublishDate)))
					&& (findTitleEndFlag == false)) {
				findTitleEndFlag = true; /* �ҵ������β��־ */
				endPos = i;
				continue;
			}
		}

		/* ���ݷ��ֱ���ı�־���д��� */
		if (findTitleStartFlag == true) {
			if (findTitleEndFlag == true) {
				/* ��title��start��־ ������ end��־ */
				for (int j = startPos + 1; j < endPos; j++) {
					if (!ret.get(j).type.equals(DocType.Start)
							&& !ret.get(j).type.equals(DocType.End)
							&& !ret.get(j).type.equals(DocType.Link)
							&& !ret.get(j).type.equals(DocType.PrefixTitle)
							&& !ret.get(j).type.equals(DocType.SuffixTitle)) {
						title += ret.get(j).termText;
					}
				}
			} else {
				/* ��title��start��־ ������ ��β */
				for (int j = startPos + 1; j < ret.size(); j++) {
					if (!ret.get(j).type.equals(DocType.Start)
							&& !ret.get(j).type.equals(DocType.End)
							&& !ret.get(j).type.equals(DocType.Link)
							&& !ret.get(j).type.equals(DocType.PrefixTitle)
							&& !ret.get(j).type.equals(DocType.SuffixTitle)) {
						title += ret.get(j).termText;
					}
				}
			}
		} else {
			if (findTitleEndFlag == true) {
				for (int j = 0; j < endPos; j++) {
					if (!ret.get(j).type.equals(DocType.Start)
							&& !ret.get(j).type.equals(DocType.End)
							&& !ret.get(j).type.equals(DocType.Link)
							&& !ret.get(j).type.equals(DocType.PrefixTitle)
							&& !ret.get(j).type.equals(DocType.SuffixTitle)) {
						title += ret.get(j).termText;
					}
				}
			} else {
				title = sb.toString().trim();
			}
		}

		return title;
	}
}
