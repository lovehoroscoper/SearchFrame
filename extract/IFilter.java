package extract;

import java.io.File;

//�����ĵ�
public interface IFilter {
	String getTitle(File file);//���ر���
	String getBody(File file);//��������
	IDocument getDocument(File file);//����ȫ��������Ϣ
}
