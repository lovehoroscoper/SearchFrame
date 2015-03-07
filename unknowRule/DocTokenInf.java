package unknowRule;

/**
 * 
     * 最大概率切分用的单词类
     * 
     * @2010-3-18
 */
public class DocTokenInf {
	public String termText;
	public DocTypes data;
	public int start;
	public int end;
	public long cost;
	public DocTokenInf bestPrev;
	public DocTypeInf bestTypeInf;

	public DocTokenInf(int vertexFrom, int vertexTo, String word) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;

	}

	public void setData(DocTypes d) {
		data = d;
		cost = d.totalCost();
	}

	public DocToken getToken() {
		if (bestTypeInf != null) {
			return new DocToken(start, end, termText, bestTypeInf);
		}
		if (data == null) {
			return new DocToken(start, end, termText, (DocType) null);
		}

		return new DocToken(start, end, termText, data.getHead().item);
	}

	public String toString() {
		return "from PoiTokenInf...toString() text:" + termText + " start:"
				+ start + " end:" + end + " cost:" + cost + " data:" + data;
	}
}
