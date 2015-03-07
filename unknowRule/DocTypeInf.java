package unknowRule;

/**
 * �ڵ�
 */
public class DocTypeInf {
	public DocType pos;   //����
    public int weight=0;   //Ƶ��
    public long code; //�������
    public double prob=0;//�ۻ�����
    public DocTypeInf bestPre;//���ǰ��
    public DocTokenInf parent;//���ڵ�
    
    public DocTypeInf(DocType p,int f,long semanticCode,DocTokenInf parent)
    {
    	pos=p;
    	weight = f;
    	code = semanticCode;
    	this.parent = parent;
    }
    
    public DocTypeInf(DocTypeInf addTypeInf,DocTokenInf p)
    {
    	pos=addTypeInf.pos;
    	weight = addTypeInf.weight;
    	code = addTypeInf.code;
    	this.parent = p;
    }
    
    public DocTypeInf(DocDicTypes.PoiTypeInf1 addTypeInf,DocTokenInf p)
    {
    	pos=addTypeInf.pos;
    	weight = addTypeInf.weight;
    	code = addTypeInf.code;
    	this.parent = p;
    }
    
    public String toString()
	{
    	return "from PoiTypeInf...toString() "+pos+":weight["+weight+"]:prob["+prob+"]";
    }
}
