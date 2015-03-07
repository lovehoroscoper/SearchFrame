package unknowRule;

/**
 * ת�Ƹ���
 * 
 *
 */
public class ContextStatDoc 
{
	private static int[][] transProbs;

	private static ContextStatDoc cs = new ContextStatDoc();

	public static ContextStatDoc getInstance() {
		return cs;
	}

	private ContextStatDoc() 
	{
		int matrixLength = DocType.values().length;
		transProbs = new int[matrixLength][];

		for (int i = 0; i < DocType.values().length; ++i) 
		{
			transProbs[i] = new int[matrixLength];
		}

		addTrob(DocType.Start, DocType.Address, 8);
		addTrob(DocType.Address, DocType.Unknow, -10);
		addTrob(DocType.Unknow, DocType.Address, -10);
		addTrob(DocType.Address, DocType.End, -10);
		addTrob(DocType.Address, DocType.GuillemetStart, -10);
		addTrob(DocType.Link, DocType.Address, -10);
		addTrob(DocType.Address, DocType.Link, -10);
	}

	/**
	 * �������PoiTypeʵ��֮���ת�Ƹ���
	 * 
	 * @param prevǰ���
	 * @param cur��ǰ���
	 * @param valǰ��㵽��ǰ����ת�Ƹ���
	 */
	public void addTrob(DocType prev, DocType cur, int val) {
		transProbs[prev.ordinal()][cur.ordinal()] = val;
	}

	/**
	 * �������PoiTypeʵ��֮���ת�Ƹ���,������Ĳ�����PoiType����
	 * 
	 * @param prevǰ���
	 * @param cur��ǰ���
	 * @return ǰ��㵽��ǰ����ת�Ƹ���
	 */
	public int getContextPossibility(DocType prev, DocType cur) {
		return transProbs[prev.ordinal()][cur.ordinal()];
	}

	/**
	 * �������PoiTypeʵ��֮���ת�Ƹ���,������Ĳ�����PoiType����
	 * 
	 * @param prevǰ���
	 * @param cur��ǰ���
	 * @return ǰ��㵽��ǰ����ת�Ƹ���
	 */
	// public int getContextPossibility(PoiTokenInf1 prev, PoiTokenInf1 cur){
	// if(prev.data == null || cur.data == null)
	// {
	// return 0;
	// }
	// int maxProb = Integer.MIN_VALUE;
	//		
	// for(PoiTypeInf1 prevInf:prev.data)
	// {
	// for(PoiTypeInf1 curInf:cur.data)
	// {
	// int prob = transProbs[prevInf.pos.ordinal()][curInf.pos.ordinal()];
	// if (prob > maxProb)
	// {
	// maxProb = prob;
	// }
	// }
	// }
	// if(maxProb == Integer.MIN_VALUE)
	// {
	// maxProb = 0;
	// }
	// return maxProb;
	// }
	public int getContextPossibility(DocTokenInf prev, DocTokenInf cur) {
		if (prev.data == null || cur.data == null) {
			return 0;
		}
		int maxProb = Integer.MIN_VALUE;

		for (DocTypeInf prevInf : prev.data) {
			for (DocTypeInf curInf : cur.data) {
				int prob = transProbs[prevInf.pos.ordinal()][curInf.pos
						.ordinal()];
				if (prob > maxProb) {
					maxProb = prob;
				}
			}
		}
		if (maxProb == Integer.MIN_VALUE) {
			maxProb = 0;
		}
		return maxProb;
	}
}
