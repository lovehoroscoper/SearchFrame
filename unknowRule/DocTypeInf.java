package unknowRule;

/**
 * 节点
 */
public class DocTypeInf {
	public DocType pos;   //类型
    public int weight=0;   //频率
    public long code; //语义编码
    public double prob=0;//累积概率
    public DocTypeInf bestPre;//最佳前驱
    public DocTokenInf parent;//父节点
    
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
