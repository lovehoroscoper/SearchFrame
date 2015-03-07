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
 * ��������
 *
 *
 */
public class TopicCrawler {
    //����չ��URL����
	ArrayList toExtendsURL = new ArrayList();
	//�������ؼ��ʵ�����
	private static String[] keywords = new String[]{};
	//�������ؼ��ʵ�Ȩ��
	private static double[] w = new double[]{};
	//��ֵ
	private static final double e=0.01;
	//����ص�URL����
	ArrayList notRelativeURL = new ArrayList();
	//�Ѿ����ʵ�URL����
	HashSet visiterURLList = new HashSet();
	//����Ŀ¼
	final String downPath = "c:/chuxiong/crawler/";
    //���URL
	private void addExtendsURL(String url) {
		//��ӽ�Ҫ��չ��URL
		notRelativeURL.add(url);
	}
	//
	//���URL
	private void addNotRelativeURL(String url) {
		//��ӽ�Ҫ��չ��URL
		toExtendsURL.add(url);
	}
    //���캯��
	public TopicCrawler() {
		super();
		new File(downPath).mkdirs();
	}
    //����չ������ȡURL��������ҳ���ݣ����ҰѴ���ҳ��������ȡ��URL���뵽��չ������ȥ������ȡ��URL��ʱ��Ҫ������ضȼ��㣩
	private void deleterExtendsURL() {
		//�����ű�������ȡ�����Ƿ���URL
		String url = (String) toExtendsURL.iterator().next();
		//��ȡURL
		String Content = downloadPageContent(url);
		try {
			// ��ȡURL���Ӵ洢�����ű�����
			retrieveLinks(Content, new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//�����ű���ɾ���Ѿ���ȥ����URL
		toExtendsURL.remove(url);
		// ��ӣ������ű���ɾ����URL
		visiterURLList.add(url);
	}
    //������ҳ����ȡ���е�����
	private String downloadPageContent(String str) {
		try {
			//����STR����URL����
			URL pageUrl = new URL(str);
			// System.out.println(pageUrl.getFile());
			//������ҳ
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					pageUrl.openStream()));
			String line;
			//��ȡ��ҳ����
			StringBuffer pageBuffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				pageBuffer.append(line);
			}
			return pageBuffer.toString();
		} catch (Exception e) {
		}
		return null;
	}
    //�����ص���ҳ����������ȡURL
	private void retrieveLinks(String pageContents, URL pageUrl) {

		if (!"".equals(pageContents) && pageContents != null) {
			// ��ȡ�г����ӵ�URL
			Pattern p = Pattern.compile("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]",
					Pattern.CASE_INSENSITIVE);
			System.out.println(pageContents);
			Matcher m = p.matcher(pageContents);
			while (m.find()) {
				String link = m.group(1).trim();
				if (link.length() < 1) {
					continue;
				}

				// Skip links that are just page anchors.����
				if (link.charAt(0) == '#') {
					continue;
				}

				// Skip mailto links.����mailto
				if (link.indexOf("mailto:") != -1) {
					continue;
				}

				// ���� JavaScript links.
				if (link.toLowerCase().indexOf("javascript") != -1) {
					continue;
				}

				if (link.indexOf("://") == -1) {
					// Handle absolute URLs.�������URL
					if (link.charAt(0) == '/') {
						link = "http://" + pageUrl.getHost() + link;
						// Handle relative URLs.�������URL
					} else {
						String file = pageUrl.getFile();
						if (file.indexOf('/') == -1) {
							link = "http://" + pageUrl.getHost() + "/" + link;
						} else {
							String path = file.substring(0, file
									.lastIndexOf('/') + 1);
							link = "http://" + pageUrl.getHost() + path + link;
						}
					}
				}
				int index = link.indexOf('#');
				if (index != -1) {
					link = link.substring(0, index);
				}
				if (!visiterURLList.contains(link)) {
					// ���ҳ��������¾�ץ
					if (pageContents.indexOf("����") != -1) {
						addExtendsURL(link);
					}

				}
				//�����ӽ�����ضȷ��������С��Ԥ����ķ�ֵ���ͱ�����˵�
				if(!isRelative(pageContents)){
					addNotRelativeURL(link);
				}
			}
		}

	}
	//�����ȡ��ҳ��������	
    private boolean isRelative(String pageContents){
    	int[] x = new int[keywords.length];
    	String tempString = pageContents;
    	int k=-1;
    	for(int i=0;i<keywords.length;i++){
    		while((k=tempString.indexOf(keywords[i]))!=-1){
    			x[i]++;
    			tempString = tempString.substring(k+keywords[i].length());
    		}
    	}
    	double sum=0,sum1=0,sum2=0;
    	for(int i=0;i<x.length;i++){
    		sum+=x[i]*Math.pow(w[i], 2);
    		sum1+=Math.pow(x[i], 2)*Math.pow(w[i], 2);
    		sum2+=Math.pow(w[i], 2);
    	}
    	double cos = sum/(Math.sqrt(sum1)*Math.sqrt(sum2));
    	if(cos<e){
    		return false;
    	}
    	return true;
    }
	public void run() {

		while (toExtendsURL.size() > 0) {
			/*
			 * if(toExtendsURL.size()>10000){ return; }
			 */
			System.out.println("toExtendsURL��С" + toExtendsURL.size());
			printArray();
			deleterExtendsURL();
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
		Iterator to = toExtendsURL.iterator();
		while (to.hasNext()) {
			String url = (String) to.next();
			// System.out.println(toExtendsURL.size()+"���ű�ģգң̣�"+url);

			File file = new File("����" + System.currentTimeMillis() + ".txt");
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				FileWriter out = new FileWriter(file);
				out.write(toExtendsURL.size() + "���ű�ģգң̣�" + url);
				out.write("\t");
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Iterator visitor = visiterURLList.iterator();
		while (visitor.hasNext()) {
			String url = (String) visitor.next();
			try {
				writer.write(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			File file = new File("����" + System.currentTimeMillis() + ".txt");
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				FileWriter out = new FileWriter(file);
				out.write(visiterURLList.size() + "���ʱ�ģգң̣�" + url);
				out.write("\t");
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(visiterURLList.size()+"���ʱ�ģգң̣�"+url);
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
