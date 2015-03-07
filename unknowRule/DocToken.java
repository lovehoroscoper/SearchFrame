package unknowRule;

/**
 * 
     * 表示按规则分类后的词对象
     * 
     * @2010-3-18
 */
public class DocToken {
	public String termText; //有意义的分词
	public DocType type;	//兴趣点的类型
	public int start ;		//开始标记
	public int end ;		//结束标记
	public long cost;		//权值
	public long code;		//行政区划码
	public int weight;   //权重
	
	public DocToken bestPrev;//最佳前驱
	
	public DocToken(DocTokenInf t) {
		this.code = t.bestTypeInf.code;
		this.type = t.bestTypeInf.pos;
		this.start = t.start;
		this.end = t.end;
		this.termText = t.termText;;
	}
	
	public DocToken(DocToken ptk,DocType type) {
		this.code = ptk.code;
		this.type =type;
		this.start = ptk.start;
		this.end = ptk.end;
		this.termText = ptk.termText;;
	}

	public DocToken(DocType typ) {
		type = typ;
	}
	public DocToken(int from ,int to, int cost ,String text,DocType type){
		this.start = from;
		this.end = to;
		this.cost = cost;
		this.termText = text;
		this.type = type;
	}
	public DocToken(int vertexFrom, int vertexTo, int c, String word, DocType typ ,int cde) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
		cost = c;
		type = typ;
		code = cde;
	}


	public DocToken(int from ,int to, String text,DocType type){
		this.start = from;
		this.end = to;
		this.termText = text;
		this.type = type;
	}
	public DocToken(int vertexFrom, int vertexTo, String word, DocTypeInf typeInf) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
		type = typeInf.pos;
		code = typeInf.code;
	}
	
	public DocToken(int from ,int to, String text,DocType type,int weight){
		this.start = from;
		this.end = to;
		this.termText = text;
		this.type = type;
		this.weight = weight;
	}
	
	public boolean match(DocType type) {
		if(type.equals(this.type)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getTermText() {
		return this.termText;
	}

	public String toString(){
		return "from PoiToken...toString() text:" + termText + " start:" + start + " end:" + end
		+ " cost:" + cost + " pos:" + type +" code:"+code;
	}
	
}
