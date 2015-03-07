package unknowRule;

/**
 * 
     * ��ʾ����������Ĵʶ���
     * 
     * @2010-3-18
 */
public class DocToken {
	public String termText; //������ķִ�
	public DocType type;	//��Ȥ�������
	public int start ;		//��ʼ���
	public int end ;		//�������
	public long cost;		//Ȩֵ
	public long code;		//����������
	public int weight;   //Ȩ��
	
	public DocToken bestPrev;//���ǰ��
	
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
