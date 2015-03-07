package unknowRule;

/**
 * 
     * ��������ϲ�����
     * 
     * @2010-3-18
 */
public class DocSpan {
	public int length;//�ϲ�����
	public DocType type;//�ϲ����������
	public int weight=0;//Ȩ��
	
	public DocSpan(int l,DocType t)
	{
		length = l;
		type = t;
	}
	
	public DocSpan(int l,DocType t,int weight)
	{
		length = l;
		type = t;
		this.weight=weight;
	}
	
	public String toString()
	{
		return type+":"+length;
	}
}
