package rule;

import java.util.HashSet;

/**
 * rule
 *
 * 
 */
public class Rule {
	public String[] antecedent;//�ؼ���
	public String consequent;//������
	
	public Rule (String[] antd,String classLabel)
	{
		antecedent = antd;
		consequent = classLabel;
	}
    
    /**
     * �����Ƿ񸲸Ƿ����ĵ�
     * 
     * @param doc ������ʵ��
     * @return �����������������򷵻�true
     */
    public boolean covers(HashSet<String> doc){
    	for(int i=0;i<antecedent.length;++i)
    	{
    		if(!doc.contains(antecedent[i]))
    		{
    			return false;
    		}
    	}
    	return true;
    }
}
