package spider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ��ָ����ַץȡ��ץȡ����ָ���ؼ��ʵ���ҳ��
 * 
 * 
 *
 */
public class MyCrawler {
	ArrayList<String> todo = new ArrayList<String>();
	HashSet<String> visited = new HashSet<String>();
	final String downPath = "d:/crawler/";
	String interestWord = "����";//ץȡָ���ؼ��ʵ���ҳ

	public MyCrawler() {
		super();
		new File(downPath).mkdirs();
	}

	private String downloadPageContent(String strUrl) {
		try {
			// ������ַstrUrl����URL����
			URL pageUrl = new URL(strUrl);
			// System.out.println(pageUrl.getFile());
			// ������ҳ
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					pageUrl.openStream()));
			String line;
			// ��ȡ��ҳ����
			StringBuffer pageBuffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				pageBuffer.append(line);
			}
			return pageBuffer.toString();
		} catch (Exception e) {
		}
		return null;
	}

	private HashSet<String> retrieveLinks(String pageContents, URL pageUrl) {

		HashSet<String> hSLink = new HashSet<String>();
		if (!"".equals(pageContents) && pageContents != null) {
			// ��ȡ���г����ӵ�URL
			Pattern p = Pattern.compile("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]",
					Pattern.CASE_INSENSITIVE);
			System.out.println(pageContents);
			Matcher m = p.matcher(pageContents);
			while (m.find()) {
				String strLink = m.group(1).trim();
				if (strLink.length() < 1) {
					continue;
				}

				// �������ӣ�ֻ����ҳê,����ê
				if (strLink.charAt(0) == '#') {
					continue;
				}

				// ����mailto����,����mailto
				if (strLink.indexOf("mailto:") != -1) {
					continue;
				}

				// ���� JavaScript ����.
				if (strLink.toLowerCase().indexOf("javascript") != -1) {
					continue;
				}

				if (strLink.indexOf("://") == -1) {
					// �������URL
					if (strLink.charAt(0) == '/') {
						strLink = "http://" + pageUrl.getHost() + strLink;
						// �������URL
					} else {
						String file = pageUrl.getFile();
						if (file.indexOf('/') == -1) {
							strLink = "http://" + pageUrl.getHost() + "/"
									+ strLink;
						} else {
							String path = file.substring(0, file
									.lastIndexOf('/') + 1);
							strLink = "http://" + pageUrl.getHost() + path
									+ strLink;
						}
					}
				}
				int index = strLink.indexOf('#');
				if (index != -1) {
					strLink = strLink.substring(0, index);
				}
				if (!visited.contains(strLink)) {
					// ���ҳ�����ָ���ؼ��ʾ�ץȡ
					if (pageContents.indexOf(interestWord) != -1) {
						// addToDoURL(Link);
						hSLink.add(strLink);
					}
				}
			}
		}
		return hSLink;
	}

	public void run() {

		while (todo.size() > 0) {
			System.out.println("queueURL��С��" + todo.size());
			printArray();
			// ��Todo����������ȡ���Ƿ���URL
			String strUrl = todo.iterator().next();
			// ��ȡURL������
			String Content = downloadPageContent(strUrl);
			try {
				// ��ȡURL���Ӵ洢��Todo��������
				HashSet<String> newLinks = retrieveLinks(Content, new URL(strUrl));
				todo.addAll(newLinks);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			// ��Todo������ɾ���Ѿ�������URL
			todo.remove(strUrl);
			// ��Todo������ɾ����URL,��ӵ�Visited������
			visited.add(strUrl);
		}
	}

	public void printArray() {
		FileWriter visiter;
		BufferedWriter writer = null;
		try {
			visiter = new FileWriter(downPath + "viciter"
					+ System.currentTimeMillis() + ".txt");
			writer = new BufferedWriter(visiter);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<String> to = todo.iterator();
		while (to.hasNext()) {
			String url = to.next();
			// System.out.println(queueURL.size()+"Todo���еģգң̣�"+url);

			File file = new File("Todo����" + System.currentTimeMillis() + ".txt");
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				FileWriter out = new FileWriter(file);
				out.write(todo.size() + "Todo���еģգң̣�" + url);
				out.write("\t");
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Iterator<String> visitor = visited.iterator();
		while (visitor.hasNext()) {
			String url = (String) visitor.next();
			try {
				writer.write(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			File file = new File("���ʱ�" + System.currentTimeMillis() + ".txt");
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				FileWriter out = new FileWriter(file);
				out.write(visited.size() + "���ʱ�ģգң̣�" + url);
				out.write("\t");
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(visitedURLList.size()+"���ʱ�ģգң̣�"+url);
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		// �����Լ�������֩��
		MyCrawler crawler = new MyCrawler();
		// ������ڵ�ַ
		crawler.todo.add("http://www.sina.com");
		// ����
		crawler.run();
	}
}