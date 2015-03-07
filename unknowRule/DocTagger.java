package unknowRule;

import java.io.*;
import java.util.*;

/**
 * ��ֺͱ�עһƪ�ĵ�
 * 
 *
 */
public class DocTagger
{
	public static ContextStatDoc contextStatPoi = ContextStatDoc.getInstance();
	// ��ʼ���ʵ�
	public static DicDoc dicPoi = DicDoc.getInstance();
	public static UnknowGrammarDoc grammar = UnknowGrammarDoc.getInstances();
	
	public static void tagUnknow(ArrayList<DocToken> tokens)
	{
		//�ϲ�δ֪��
		for(int i=0; i<tokens.size(); ++i)
		{
			DocToken token = tokens.get(i);
			if(token.type != null)
			{
				continue;
			}
			StringBuilder unknowText = new StringBuilder();
			int start = token.start;
			while(true)
			{
				unknowText.append(token.termText);
				tokens.remove(i);
				if(i>=tokens.size())
				{
					int end = token.end; //token.end
					tokens.add(i, new DocToken(start,end,unknowText.toString(),DocType.Unknow));
					break;
				}
				token = tokens.get(i);
				if(token.type != null)
				{
					int end = token.start;
					
					tokens.add(i, new DocToken(start,end,unknowText.toString(),DocType.Unknow));
					break;
				}
			}
		}
	}
	
	/**
	 * �ִʺ�������������һ�����Ʒֳ����ɸ�������Ĳ���
	 * @param word
	 * @return һ��ArrayList������ÿ��Ԫ����һ����
	 */
	public static ArrayList<DocToken> basicTag(String word) 
	{
		//��Ҫ�ִʵľ���
		
		System.out.println("basicTag->"+word);
		
		AdjListDoc g = getAdjList(word);
		int length = word.length();
		

//		 System.out.println(g.toString()+"++++++++");
		
		DocToken pt = new DocToken(length, length + 1, 0, "", DocType.End);

		DocTokenInf endToken = new DocTokenInf(pt.start, pt.end, pt.termText);
		
		DocTypeInf posInf = new DocTypeInf(DocType.End, 10, 0, endToken);
	
		DocTypes poiData = new DocTypes();
		
		poiData.put(posInf);
		
		endToken.setData(poiData);

		g.addEdge(endToken);

		ArrayList<DocToken> alpts = maxProb(g, endToken);
		
		tagUnknow(alpts);
		
		UnknowGrammarDoc.MatchRet matchRet = new UnknowGrammarDoc.MatchRet(0,null);
		
		int offset = 0;

		while (true)
		{
			matchRet = grammar.matchLong(alpts, offset, matchRet);
			if (matchRet.lhs != null)
			{ 
//			  ��ӡƥ����Ĺ��� ��ʼ
//				System.out.println("��ӡƥ����Ĺ��� ��ʼ->start");
//                for(int i = offset;i<(matchRet.end -1);++i)
//                {
//                    System.out.print(alpts.get(i).type+",");
//                }
//                System.out.print(alpts.get(matchRet.end - 1).type+"@");
//                for(DocSpan docSpan : matchRet.lhs)
//                {
//                    System.out.print(docSpan.length+","+docSpan.type+",");
//                }
//                System.out.println("��ӡƥ����Ĺ��� ->over");
//                //��ӡƥ����Ĺ���  ����
				UnknowGrammarDoc.replace(alpts, offset, matchRet.lhs);
				offset = 0;
			}
			else 
			{
				++offset;
				if (offset >= alpts.size())
					break;
			}
		}
//		System.out.println(alpts.toString()+"++++++++");
		return alpts;
	}
	
	/**
	 * 
	 * @param word
	 * @return AdjListDoc ��ʾһ���б�ͼ
	 */
	private static AdjListDoc getAdjList(String word) 
	{
		if (word == null || word.length() == 0) {
			return null;
		}

		int atomCount = word.length();
		// ���ƥ��Ľڵ���Ϣ
		DicDoc.MatchRet matchRet = new DicDoc.MatchRet(0, null);

		int start = 0;
		// ��ʼ����Dictionary�д���ɵ�ͼ
		AdjListDoc adjList = new AdjListDoc(atomCount + 2);

		// �����￪ʼ���зִ�
		while (true)
		{
			matchRet = dicPoi.matchLong(word, start, matchRet);
			// ƥ����
			if (matchRet.end > start) 
			{
				String termText = word.substring(start, matchRet.end);
	
				DocTokenInf tokenInf = new DocTokenInf(start, matchRet.end,termText);
				
				DocTypes poiTypes = new DocTypes();
				
				Iterator<DocDicTypes.PoiTypeInf1> nextIt = matchRet.posInf.iterator();
				
				while (nextIt.hasNext()) 
				{
					DocDicTypes.PoiTypeInf1 nextTypeInf = nextIt.next();
					DocType pos = nextTypeInf.getPOIType1();
					// �������ΪLINK���滻
					if (pos.equals(DocType.Link)) 
					{
						termText = "";
						tokenInf = new DocTokenInf(start, matchRet.end,termText);
					}
					

					poiTypes.put(new DocTypeInf(nextTypeInf, tokenInf));
				}
				tokenInf.setData(poiTypes);
				adjList.addEdge(tokenInf);
				start = matchRet.end;

			} 
			else
			{ // ûƥ����
				adjList.addEdge(new DocTokenInf(start, start + 1, word
						.substring(start, start + 1)));
				start++;
			}
			if (start >= atomCount) 
			{
				break;
			}
		}
		return adjList;
	}

	/**
	 * �ϲ�δ֪��
	 * @param tokens
	 */
	public static void mergeUnknow(ArrayList<DocTokenInf> tokens)
	{
		for(int i=0; i<tokens.size(); ++i)
		{
			DocTokenInf token = tokens.get(i);
			if(token.data != null)
			{
				continue;
			}
			StringBuilder unknowText = new StringBuilder();
			int start = token.start;
			while(true)
			{
				unknowText.append(token.termText);
				tokens.remove(i);
				if(i>=tokens.size())
				{
					int end = token.end; //token.end
					
					DocTokenInf unKnowTokenInf = new DocTokenInf(start,end,unknowText.toString());
					
					DocTypeInf item = new DocTypeInf(DocType.Unknow,10,0,unKnowTokenInf);
					DocTypes d = new DocTypes();
					d.put(item);
					unKnowTokenInf.setData(d);
					tokens.add(i, unKnowTokenInf);
					break;
				}
				token = tokens.get(i);
				if(token.data != null)
				{
					int end = token.start;
					
					DocTokenInf unKnowTokenInf = new DocTokenInf(start,end,unknowText.toString());
					
					DocTypeInf item = new DocTypeInf(DocType.Unknow,10,0,unKnowTokenInf);
					DocTypes d = new DocTypes();
					d.put(item);
					unKnowTokenInf.setData(d);
					tokens.add(i, unKnowTokenInf);
					break;
				}
			}
		}
	}
	
	/**
	 * ������п���ƥ���DocToken����
	 * 
	 * @param adjList
	 * @param endToken
	 * @return maxProb maxProb�е�����Ԫ�ض���������ƥ���ϵ�����
	 */
	public static ArrayList<DocToken> maxProb(AdjListDoc adjList,DocTokenInf endToken)
	{
		for (int index = 1; index < adjList.verticesNum; index++) 
		{
			Iterator<DocTokenInf> it = adjList.getAdjacencies(index);
			while (it.hasNext()) 
			{
				DocTokenInf pt = it.next();
				// ��ȡÿ��Token�����ǰ��
				getPrev(adjList, pt);
			}
		}

		ArrayList<DocTokenInf> al = new ArrayList<DocTokenInf>(adjList.verticesNum);
		for (DocTokenInf pt = endToken; pt != null; pt = pt.bestPrev)
		{
			al.add(pt);
		}
		Collections.reverse(al);
		mergeUnknow(al);
		hmm(al, endToken);
		ArrayList<DocToken> list = new ArrayList<DocToken>();
		for (int i = 0; i < al.size(); i++) 
		{
			DocTokenInf tokenInf = al.get(i);
			DocToken poiToken = new DocToken(tokenInf);
			list.add(poiToken);
		}
		return list;
	}


	/**
	 * ������������Ĺ�ϵ�ж�
	 * 
	 * @param preCode
	 * @param nextCode
	 * @return boolean true��ʾ�й���������ͬһ��ʡ��false��ʾû�й�����������ͬһ��ʡ
	 */
	public static boolean compCode(long preCode, long nextCode) 
	{
		if (preCode == 0 || nextCode == 0) 
		{
			return false;  
		}
		String preCodeStr = preCode + "";
		String nextCodeStr = nextCode + "";
		// ����ж�Ϊ����ʡ����û����
		if (!preCodeStr.substring(0, 2).equals(nextCodeStr.substring(0, 2))) 
		{
			return false;
		} 
		else if (!preCodeStr.substring(2, 4).equals(nextCodeStr.substring(2, 4))) 
		{
			if ("00".equals(preCodeStr.substring(2, 4))|| "00".equals(nextCodeStr.substring(2, 4))) 
			{
				return true;
			} 
			else 
			{
				return false;
			}
		} else if (!preCodeStr.substring(4, 6).equals(nextCodeStr.substring(4, 6))) 
		{
			if ("00".equals(preCodeStr.substring(4, 6))|| "00".equals(nextCodeStr.substring(4, 6))) 
			{
				return true;
			}
			else
			{
				return false;
			}
		} else if (!preCodeStr.substring(6, 9).equals(nextCodeStr.substring(6, 9)))
		{
			if ("000".equals(preCodeStr.substring(6, 9))|| "000".equals(nextCodeStr.substring(6, 9))) 
			{
				return true;
			} 
			else 
			{
				return false;
			}
		} else if (!preCodeStr.substring(9, 12).equals(nextCodeStr.substring(9, 12)))
		{
			if ("000".equals(preCodeStr.substring(9, 12))|| "000".equals(nextCodeStr.substring(9, 12)))
			{
				return true;
			}
			else 
			{
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * �������巽��
	 * 
	 * @param ret
	 *            Ҫ��������ļ���
	 * @param endToken
	 *            ���һ��token���
	 */
	public static void hmm(ArrayList<DocTokenInf> ret, DocTokenInf endToken) 
	{
		Iterator<DocTypeInf> startIt = ret.get(0).data.iterator();
		while (startIt.hasNext()) 
		{
			DocTypeInf startTypeInf = startIt.next();
			int prob = 1;
			startTypeInf.prob = prob;
		}
		for (int stage = 1; stage < ret.size(); stage++) 
		{
			DocTokenInf nexInf = ret.get(stage);
			if (nexInf.data == null) 
			{
				continue;
			}
			Iterator<DocTypeInf> nextIt = nexInf.data.iterator();
			while (nextIt.hasNext()) 
			{
				DocTypeInf nextTypeInf = nextIt.next();
				DocTokenInf preInf = ret.get(stage - 1);
				if (preInf.data == null) 
				{
					continue;
				}
				Iterator<DocTypeInf> preIt = preInf.data.iterator();
				while (preIt.hasNext()) 
				{
					DocTypeInf preTypeInf = preIt.next();
					int trans = 0;
					if ((preTypeInf.code == 0 && nextTypeInf.code == 0)|| preTypeInf.code == 0 || nextTypeInf.code == 0) 
					{
						trans = contextStatPoi.getContextPossibility(preTypeInf.pos, nextTypeInf.pos);
					}
					else 
					{
						if (compCode(preTypeInf.code, nextTypeInf.code)) 
						{
							trans = 100;
						}
						else
						{
							trans = -10;
						}
					}
					double prob = preTypeInf.prob;
					prob = prob + trans + nextTypeInf.weight;
					if (nextTypeInf.prob <= prob) 
					{
						nextTypeInf.prob = prob;
						nextTypeInf.bestPre = preTypeInf;
					}
					else if (nextTypeInf.bestPre == null) 
					{
						nextTypeInf.bestPre = preTypeInf;
					}
				}
			}
		}

		DocTypeInf i = endToken.data.getHead().item;
		while (i != null)
		{
			i.parent.bestTypeInf = i;
			i = i.bestPre;
		}
	}

	/**
	 * ���token�����ǰ��
	 * 
	 * @param adjList
	 * @param pt
	 */
	public static void getPrev(AdjListDoc adjList, DocTokenInf pt) 
	{
		Iterator<DocTokenInf> it = adjList.getAdjacencies(pt.start);
		long maxFee = Long.MIN_VALUE;
		DocTokenInf bestPrev = null;
		// ����������к�ѡ�ʣ��õ�ǰ���ʼ��ϣ�������ѡ���ǰ����
		while (it.hasNext()) 
		{
			DocTokenInf token = it.next();
			long currentCost = token.cost;
			currentCost += contextStatPoi.getContextPossibility(token, pt);

			if (currentCost > maxFee) {
				bestPrev = token;
				maxFee = currentCost;
			}
		}

		if (maxFee != Long.MIN_VALUE) 
		{
			pt.cost += maxFee;
		} else
		{
			pt.cost = maxFee;
		}
		pt.bestPrev = bestPrev;

		return;
	}
	
	/**
	 * ѡȡ�ı���matterȨ�ش��
	 
	 */
//	public static String getMatterMax(ArrayList<DocToken> pts)
//	{
//		DicDoc dicDoc = DicDoc.getInstance();
//		int weight = -1;
//		int max = 0;
//		int var = 0;
//		String maxMatter = "";
//		
//		for(DocToken token:pts)
//		{
//			if(token.type == DocType.Matter)
//			{
//				var = dicDoc.getWeight(token.termText);
////				System.out.println("~~~~~~~~~token.termTex:"+token.termText+":"+var);
//				if(var>max){
//					max = var;
//					maxMatter = token.termText;
////					System.out.println(maxMatter+"~~~~~~~~~~~~~~~~~~~maxMatter");
//					//return 
//				}			
//		}
//			
//		}
//
//		return maxMatter;
//	}

	public static Set<String> loadDic(String path) {
		Set<String> list = new HashSet<String>();
		try {
			File file = new File(path);
			FileReader isr = new FileReader(file);
			BufferedReader buf = new BufferedReader(isr);
			String line = "";
			while ((line = buf.readLine()) != null) {
				list.add(line);
			}
			buf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	     * ��������
	     * @param str comments of the String parameter.
	     * @return int
	     *
	 */
	public static String filterStr(String key){
		if(key.indexOf("��")!=-1)
		{
		String str1 = key.substring(0, key.indexOf("��"));
			String strok = "";
			if (str1.lastIndexOf("��") != -1) {
				strok = key.substring(str1.lastIndexOf("��") + 1, key.length());
			} else if (str1.lastIndexOf("��") != -1) {
				if (str1.lastIndexOf(" ") != -1) {
					if (str1.lastIndexOf(" ") > str1.lastIndexOf("��")) {
						strok = key.substring(str1.lastIndexOf(" "), key.length());
					} else {
						strok = key.substring(str1.lastIndexOf("��"), key.length());
					}
				} else {
					strok = key.substring(str1.lastIndexOf("��") + 1, key.length());
				}
			} else {
				strok = key.substring(str1.lastIndexOf(" "), key.length());
			}
			return strok;
		}else{return key;}
	}

}
