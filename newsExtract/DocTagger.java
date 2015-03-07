/*
 * Created on 2004-8-26
 *
 */
package newsExtract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import newsExtract.DocTypes.DocTypeInf;

import newsExtract.UnknowGrammar.MatchRet;
/**
 * �ִʺͱ�ע
 */
public class DocTagger {
	public static ContextStatDoc contexStatAddress = ContextStatDoc.getInstance();
	public static DicDoc dictAddress = DicDoc.getInstance(); // ��ʼ���ʵ�
	public static UnknowGrammar grammar = UnknowGrammar.getInstance();
	static long minValue = Long.MIN_VALUE / 2;

	/**
	 * ����ڵ�i�����ǰ���ڵ�
	 * 
	 * @param adjList �зִ�ͼ
	 * @param i �ڵ���
	 */
	public static void getPrev(AdjList g, int i,DocTokenInf[] prevNode,long[] prob) {
		Iterator<DocTokenInf> it = g.getPrev(i);
		long maxFee = minValue;
		DocTokenInf maxID = null;

		// ����������к�ѡ�ʣ��õ�ǰ���ʼ��ϣ�������ѡ���ǰ����
		while (it.hasNext()) {
			DocTokenInf itr = it.next();
			long currentCost = prob[itr.start] + itr.cost;
			//currentCost += contexStatAddress.getContextPossibility(itr, i);
			if (currentCost > maxFee) {
				maxID = itr;
				maxFee = currentCost;
			}
		}
		prob[i] = maxFee;
		prevNode[i] = maxID;
		return;
	}

	/**
	 * ������п���ƥ���DocToken����
	 * 
	 * @param g
	 * @param endToken
	 * @return maxProb maxProb�е�����Ԫ�ض���������ƥ���ϵ�����
	 */
	public static ArrayList<DocToken> maxProb(AdjList g) {
		System.out.println(g==null);
		DocTokenInf[] prevNode = new DocTokenInf[g.verticesNum];
		long[] prob = new long[g.verticesNum];
		for (int index = 1; index < g.verticesNum; index++) {
			getPrev(g, index,prevNode,prob);
		}

		//for (int i = 1; i < prevNode.length; i++) {
		//	if(prevNode[i] == null)
		//		continue;
		//	System.out.println("i: "+i+"    "+prevNode[i].toString());
		//}
		
		ArrayList<DocTokenInf> ret = new ArrayList<DocTokenInf>(g.verticesNum);
		for (int i = (g.verticesNum-1); i>0; i = prevNode[i].start)// ��������ȡ�ʺ�ѡ��
		{
			ret.add(prevNode[i]);
			//System.out.println("ret "+prevNode[i]);
		}

		Collections.reverse(ret);
		mergeUnknow(ret);

		System.out.println("ret.size() "+ret.size());
		for (int i = 0; i < ret.size(); i++) {
		// Iterator<AddTypeInf> it = ret.get(i).data.iterator();
		 System.out.println("data:"+ret.get(i).toString());
		// System.out.println(g.toString());
		}

		DocType[] bestTag = hmm(ret);
		//for (int i = 0; i < bestTag.length; i++) {
			// Iterator<AddTypeInf> it = ret.get(i).data.iterator();
		//	 System.out.println("tag:"+bestTag[i]);
			// System.out.println(g.toString());
		//}
		
		ArrayList<DocToken> list = new ArrayList<DocToken>();
		for (int i = 0; i < ret.size(); i++) {
			DocTokenInf tokenInf = ret.get(i);
			// System.out.println("maxProb:----------" + tokenInf.data.size());
			DocToken addressToken = new DocToken(tokenInf.start,tokenInf.end,tokenInf.termText,bestTag[i]);
			list.add(addressToken);
		}
		return list;
	}

	/**
	 * 
	 * @param word
	 * @return AdjListDoc ��ʾһ���б�ͼ
	 */
	public static AdjList getAdjList(String addressStr) {
		if (addressStr == null || addressStr.length() == 0) {
			return null;
		}
		
		System.out.println("address:"+addressStr);
		int atomCount;

		// ���ƥ��Ľڵ���Ϣ
		// int fee;
		int start = 0;
		atomCount = addressStr.length();

		AdjList g = new AdjList(atomCount + 1); // ��ʼ����Dictionary�д���ɵ�ͼ

		while (true) // �����￪ʼ���зִ�
		{
			ArrayList<DicDoc.MatchRet> matchRet = dictAddress.matchAll(
					addressStr, start);
			if (matchRet.size() > 0) {// ƥ����
				// fee = 100;
				for (DicDoc.MatchRet ret : matchRet) {
					String termText = addressStr.substring(start, ret.end);
					DocTokenInf tokenInf = new DocTokenInf(start,
							ret.end, termText,ret.posInf);					
					
					g.addEdge(tokenInf);
				}
				start++;
			} else { // ûƥ����
				// fee = -10;
				g.addEdge(new DocTokenInf(start, start + 1, addressStr
						.substring(start, start + 1),null));
				start++;
			}
			if (start >= atomCount) {
				break;
			}
		}

		return g;
	}

	/**
	 * �������巽��
	 * 
	 * @param ret
	 *            Ҫ��������ļ���
	 * @return ���Ƶı�ע��������
	 */
	public static DocType[] hmm(ArrayList<DocTokenInf> ret) {
		DocTypes startType = new DocTypes();
		startType.put(new DocTypeInf(DocType.Start,1,0));
		ret.add(0, new DocTokenInf(-1,0,"Start",startType));
		
		DocTypes endType = new DocTypes();
		endType.put(new DocTypeInf(DocType.End,100,100));
		ret.add(new DocTokenInf(-1,0,"End",endType));
		
		for (int i = 0; i < ret.size(); i++) {
			// Iterator<AddTypeInf> it = ret.get(i).data.iterator();
			 System.out.println(i+" hmm:"+ret.get(i).toString());
			// System.out.println(g.toString());
		}
		
		int stageLength = ret.size();//��ʼ�׶κͽ����׶�
		int[][] prob = new int[stageLength][];//�ۻ�����
		for(int i = 0;i<stageLength;++i)
		{
			prob[i] = new int[DocType.values().length];
			for(int j=0;j<DocType.values().length;++j)
				prob[i][j] = Integer.MIN_VALUE;
		}
		
		DocType[][] bestPre = new DocType[stageLength][];//���ǰ����Ҳ����ǰһ����ע��ʲô
		for(int i = 0;i<ret.size();++i)
		{
			bestPre[i] = new DocType[DocType.values().length];
		}
		
		prob[0][DocType.Start.ordinal()]=1;
		
		for (int stage = 1; stage < stageLength; stage++) {
			DocTokenInf nexInf = ret.get(stage);
			if (nexInf.data == null) {
				continue;
			}
			System.out.println("stage:"+stage);
			Iterator<DocTypeInf> nextIt = nexInf.data.iterator();
			while (nextIt.hasNext()) {
				DocTypeInf nextTypeInf = nextIt.next();

				DocTokenInf preInf = ret.get(stage - 1);
				if (preInf.data == null) {
					continue;
				}

				Iterator<DocTypeInf> preIt = preInf.data.iterator();

				while (preIt.hasNext()) {
					DocTypeInf preTypeInf = preIt.next();
					// ��һ����㵽��һ������ת�Ƹ���
					int trans = contexStatAddress.getContextPossibility(preTypeInf.pos,
									nextTypeInf.pos);
					int currentprob = prob[stage - 1][preTypeInf.pos.ordinal()];
					currentprob = currentprob + trans + nextTypeInf.weight;
					if (prob[stage][nextTypeInf.pos.ordinal()] <= currentprob) {
						System.out.println("find new max:"+preTypeInf.pos);
						prob[stage][nextTypeInf.pos.ordinal()] = currentprob;
						bestPre[stage][nextTypeInf.pos.ordinal()] = preTypeInf.pos;
					}
				}
			}
		}
		
		DocType endTag = DocType.End;

		System.out.println("stageLength "+stageLength);
		DocType[] bestTag = new DocType[stageLength];
		for(int i = (stageLength-1);i>1;i--)
		{
			System.out.println("i "+i+" endTag:"+endTag);
			bestTag[i-1]=bestPre[i][endTag.ordinal()];
			endTag = bestTag[i-1];
		}
		DocType[] resultTag = new DocType[stageLength-2];
	    System.arraycopy(bestTag, 1, resultTag, 0, resultTag.length);
	    
		ret.remove(stageLength-1);
		ret.remove(0);
		return resultTag;
	}

	public static void mergeUnknow(ArrayList<DocTokenInf> tokens) {
		// �ϲ�δ֪��
		for (int i = 0; i < tokens.size(); ++i) {
			DocTokenInf token = tokens.get(i);
			if (token.data != null) {
				continue;
			}
			//System.out.println("mergeUnknow:"+token);
			StringBuilder unknowText = new StringBuilder();
			int start = token.start;
			while (true) {
				unknowText.append(token.termText);
				tokens.remove(i);
				if (i >= tokens.size()) {
					int end = token.end; // token.end

					DocTypes item = new DocTypes();
					item.put(new DocTypeInf(DocType.Unknow, 10, 0));
					DocTokenInf unKnowTokenInf = new DocTokenInf(start,
							end, unknowText.toString(),item);
					tokens.add(i, unKnowTokenInf);
					break;
				}
				token = tokens.get(i);
				if (token.data != null) {
					int end = token.start;

					DocTypes item = new DocTypes();
					item.put(new DocTypeInf(DocType.Unknow, 10, 0));
					DocTokenInf unKnowTokenInf = new DocTokenInf(start,
							end, unknowText.toString(),item);
					tokens.add(i, unKnowTokenInf);
					break;
				}
			}
		}
	}

	/**
	 * �ִʺ�������������һ�����Ʒֳ����ɸ�������Ĳ���
	 * 
	 * @param word
	 * @return һ��ArrayList������ÿ��Ԫ����һ����
	 */
	public static ArrayList<DocToken> basicTag(String addressStr) // �ִ�
	{
		AdjList g = getAdjList(addressStr);
		ArrayList<DocToken> tokens = maxProb(g);
		return tokens;
	}

	public static ArrayList<DocToken> tag(String addressStr) // �ִ�
	{
		AdjList g = getAdjList(addressStr);
		if(g==null)
			return null;
		System.out.println("AdjList...");
		System.out.println(g);
		ArrayList<DocToken> tokens = maxProb(g);
		//���ӿ�ʼ�ͽ����ڵ�
		DocToken startToken = new DocToken(-1,0,"Start",DocType.Start);
		tokens.add(0, startToken);
		DocToken endToken = new DocToken(g.verticesNum-1,g.verticesNum,"End",DocType.End);
		tokens.add(endToken);

		//for(AddressToken tk:tokens){
		// System.out.println(tk.toString());
		//}

		// δ��¼��ʶ��
		MatchRet matchRet = new MatchRet(0, null);
		int offset = 0;
		while (true) {
			matchRet = grammar.matchLong(tokens, offset, matchRet);

			// System.out.println(matchRet.lhs);
			// System.out.println(matchRet.end);
			if (matchRet.lhs != null) {
				UnknowGrammar.replace(tokens, offset, matchRet.lhs);
				// System.out.println("replace:"+matchRet.lhs);
				offset = 0;
			} else {
				++offset;
				if (offset >= tokens.size())
					break;
			}
		}
		return tokens;
	}

	public static void main(String[] args) {
		String addressStr = //"ɽ��ʡ̫ԭ�и�����32��";
			//"̫ԭ�а��¶���49��";
		//"�����1��";
		//"�Ϻ���¬����˼��·113���������";
			//"�Ϻ�����ɽ��һ·1250�ź�";
			//"��·175Ū44��602";
			//"�㶫ʡ��ݸ��ݸ��·������������·";
			//"�й������г��������山·��2��ӯ�������̳�һ��109��Ԫ";
			//"ɽ��ʡ��ǿ�г�ɽ��112��";
			//"�Ĵ�ʡ�����н��ؿ����򶫺�·�¶�1��";
			//"�㶫ʡ�����дӻ���������ӱ�·28��";
			//"�㶫ʡ�����дӻ��������";
			//"�����г�������ׯ������A��1402";
			//"�����з�̨����������·29�ŵ���5��������ҤվB������";
			//"�ӱ�ʡ����������������";
			//"�Ϻ��к�����Ĵ���·2146��";
			//"�Ϻ��к��������·69��(��ŷ��·)";
			//"������������ƽ������16¥217��";
			//"�����д��˹�ҵ������̩�л�԰3-15-204";
			//"�й� ���� ������ ��̨���������·�»�����1��";
			//"��";
			"����ʡʯ������������";
		ArrayList<DocToken> ret =  DocTagger.tag(addressStr);
		
        for (int i =0; i <ret.size() ; ++i)
        {
        	DocToken token = ret.get(i);
        	System.out.println("token:"+token);
        }
	}
}
