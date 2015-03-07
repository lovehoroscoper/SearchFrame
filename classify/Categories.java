package classify;

import java.util.Arrays;
import java.util.Set;

public class Categories {
	public static String catNames []; //һ��������
	
	static{
		CrawlerCategory crawler = new CrawlerCategory();
		Set<String> set =crawler.readerDocument();
		catNames =new String [set.size()];
		catNames = set.toArray(catNames);
		Arrays.sort(catNames);
	}
	
	//����һ������������
	public static int[] createDegress(){
		return new int[catNames.length];
	}
}
