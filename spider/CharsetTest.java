package spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.mozilla.universalchardet.UniversalDetector;

public class CharsetTest {
	public static void main(String[] args) throws IOException {
		File file1=new File("D:\\test\\gb2312.txt");
		File file2=new File("D:\\test\\utf-8.txt");
		BufferedReader br1=new BufferedReader(new FileReader(file1));
		BufferedReader br2=new BufferedReader(new FileReader(file2));
		String context1=br1.readLine();
		String context2=br2.readLine();
		byte[] bytes1=context1.getBytes();
		byte[] bytes2=context2.getBytes();
		UniversalDetector detector = new UniversalDetector(null);
		//�����������ṩ����
		detector.handleData(bytes1, 0, bytes1.length);
		//֪ͨ�������������Ѿ�����
		detector.dataEnd();
		//ȡ�ü����ı�����
		System.out.println(detector.getDetectedCharset()+"  D:\\test\\gb2312.txt");

		//���ٴ�ʹ�ñ�������֮ǰ���ȵ���UniversalDetector.reset()
		detector.reset();
		//�����������ṩ����
		detector.handleData(bytes2, 0, bytes2.length);
		//֪ͨ�������������Ѿ�����
		detector.dataEnd();
		//ȡ�ü����ı�����
		System.out.println(detector.getDetectedCharset()+"  D:\\test\\utf-8.txt");
	}
}
