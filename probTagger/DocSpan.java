package probTagger;

/**
 * 
     * ��������AddressType.type��Ͳ����䶨�����
     * 
     * @2010-3-18
 */

public class DocSpan {
	public int length;
	public PartOfSpeech type;
	
	public DocSpan(int l,PartOfSpeech t)
	{
		length = l;
		type = t;
	}
	
	public String toString()
	{
		return type+":"+length;
	}
}
