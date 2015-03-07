package cnClassify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import cnClassify.TernarySearchTrie.WordRelation;

/**
 * �����ı����࣬���ùؼ��ʼ�Ȩ���෽��
 * 
 */
public class Classifier {
	TernarySearchTrie dic;
	String[] categories;

	private Classifier() { // ��ȡ�������
		String classfyRule = System.getProperty("user.dir")
				+ "/dic/classfyrule.txt";
		String categoryFile = System.getProperty("user.dir")
				+ "/dic/category.txt";

		categories = readcategory(categoryFile);
		
		dic = new TernarySearchTrie(classfyRule,
				categories);
	}
	
	private static Classifier categoryTrie = null;
	
	// ���ʵ��
	public static Classifier getInstance() {
		if (categoryTrie == null)
			categoryTrie = new Classifier();
		return categoryTrie;
	}

	public static String[] readcategory(String fileName) {
		InputStream file;
		try {
			file = new FileInputStream(new File(fileName));
			BufferedReader read = new BufferedReader(new InputStreamReader(
					file, "GBK"));
			String line;
			Set<String> catset = new HashSet<String>();
			while ((line = read.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				catset.add(st.nextToken());
			}
			String[] category = catset.toArray(new String[catset.size()]);
			Arrays.sort(category);
			return category;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getCategoryName(String sentence) {
		//��һ������洢һ����Ʒ����ĳһ������������
		int degrees[] = new int[categories.length];
		//����������
		int offset = 0;
		for (offset = 0; offset < sentence.length(); offset++) {
			// System.err.println("offset="+offset);
			WordRelation wr = dic.matchLong(sentence, offset);
			if (wr == null) {
				continue;
			}
			for (int i = 0; i < wr.degree.length; i++) {
				// System.err.println(wr.degree[i]);
				degrees[i] += wr.degree[i];
			}
			// System.out.println(sentence+" match:"+ret);
		}

		// ��Ʒ���ൽ�������������
		int index = 0;
		int maxDegree = degrees[0];
		for (int i = 1; i < degrees.length; i++) {
			if (maxDegree < degrees[i]) {
				maxDegree = degrees[i];
				index = i;
			}
		}

		// System.out.println(categories[index]);
		return categories[index]; //������������ȶ�Ӧ�����
	}

	public static void main(String[] args) {
		Classifier c = Classifier.getInstance();
		String category = c.getCategoryName("��������");
		System.out.print(category);
	}
}
