package newsExtract;

/**
 * 
     * ��������AddressType.type��Ͳ����䶨�����
     * 
     * @2010-3-18
 */

public class DocSpan {
	public int length;
	public DocType type;
	
	public DocSpan(int l,DocType t)
	{
		length = l;
		type = t;
	}
	
	public String toString()
	{
		return type+":"+length;
	}
}
