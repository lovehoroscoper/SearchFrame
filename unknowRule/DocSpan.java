package unknowRule;

/**
 * 
     * 用来定义合并规则
     * 
     * @2010-3-18
 */
public class DocSpan {
	public int length;//合并长度
	public DocType type;//合并后的新类型
	public int weight=0;//权重
	
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
