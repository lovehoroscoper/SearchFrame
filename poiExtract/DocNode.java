package poiExtract;

import java.util.ArrayList;

public class DocNode {
    public DocNode(int newStart, int newEnd, String string, DocType newType) {
		// TODO Auto-generated constructor stub
	}
	public String termText; //��
    public DocType type;    //����
    public int start;       //��ʼλ��
    public int end;         //����λ��
    public long cost;       //����
    public ArrayList<DocNode> children = new ArrayList<DocNode>(); //���ӽڵ�
}
