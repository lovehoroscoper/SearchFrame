package spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DFAMinmal {
	/**
	* ����һ��DFA D
	* @param d DFA״̬ת����
	* @param S ״̬����
	* @param E �����ַ���
	* @param s ��ʼ״̬
	* @param F ����״̬��
	* @return һ��DFA D', ����D������ͬ������, ������״̬������
	*/
	public int[][] convert(int[][] d, Set<Integer> S, char[] E, int s, Set<Integer> F)
	{
	   // �����ý���״̬��ͷǽ���״̬�黮�� PI
	   Set<Set<Integer>> PI = new HashSet<Set<Integer>>();
	   // ���� S - F
	   S.removeAll(F);
	   PI.add(F);
	   PI.add(S);
	  
	   // ���, ��PInew = PI
	   Set<Set<Integer>> PInew = new HashSet<Set<Integer>>();
	   // TODO ���ǳ���ƴ���������
	   PInew.addAll(PI);
	  
	   while (true)
	   {
	    // ��PI�е�ÿ����G
	    for (Iterator<Set<Integer>> it = PI.iterator(); it.hasNext(); )
	    {
	     List<Object> groupList = new ArrayList<Object>();
	     Set<Integer> G = it.next();
	     // ʹ���ַ������G��Ԫ��    
	    
	     // �����ַ����ÿ������a
	     for (int i = 0; i < E.length; i++)
	     {
	      Map<Integer, Set<Integer>> groupMap = new HashMap<Integer, Set<Integer>>();
	      char a = E[i];
	      for (Iterator<Integer> sIt = G.iterator(); sIt.hasNext(); )
	      {
	       int stat = sIt.next();
	       // ��״̬S���� ����a�ܹ������״̬
	       int tar = d[stat][a];
	       // ��ȡĿ��״̬��PI�е�λ��
	       int idx = getElelementIdx(PI, tar);
	       Set<Integer> group = groupMap.get(idx);
	       if (group == null)
	       {
	        group = new HashSet<Integer>();
	        groupMap.put(idx, group);
	       }     
	       group.add(stat);      
	      }
	     
	      groupList.add(groupMap);
	     }
	    
	     // ��PInew�н�G�滻Ϊ��G���з���õ�����ЩС��
	     PInew.remove(G);
	     PInew.addAll(setListPoly(groupList));
	    

	    }
	    // �ж�2���������Ƿ����
	    if (!isTwoSetGrpEqual(PI, PInew))
	    {
	     PI.clear();
	     PI.addAll(PInew);
	    } else
	    {
	     break;
	    }
	   }  
	  
	   // TODO ����4
	   for (Iterator<Set<Integer>> it = PI.iterator(); it.hasNext(); )
	   {
	    Set<Integer> set = it.next();
	    // ��S1��PI����ĳ��G�Ĵ���
	    for (Iterator<Integer> sIt = set.iterator(); sIt.hasNext(); )
	    {
	     int s1 = sIt.next();
	     while (sIt.hasNext())
	     {
	      // ��S1�滻SWP
	      int swp = sIt.next();
	      for (int i = 0; i < d.length; i++)
	      {
	       for (int j = 0; j < d[i].length; j++)
	       {
	        if (d[i][j] == swp)
	        {
	         d[i][j] = s1;
	        }
	       }
	      }
	      // ɾ��SWP��ת������
	      d[swp] = new int[]{};
	     }
	    }
	   
	   }
	  
	   return d;
	}


	/**
	* ��ȡĳ��Ԫ���ڼ������е�����
	* @param set
	* @param element
	* @return
	*/
	private int getElelementIdx(Set<Set<Integer>> set, int element)
	{
	   int idx = 0;
	   for (Iterator<Set<Integer>> it = set.iterator(); it.hasNext(); )
	   {
	    Set<Integer> g = it.next();
	    if (g.contains(element))
	    {
	     // TODO ���HASHCODE �Ƿ�����˼��ϵ�λ��
	     return idx;
	    }
	    idx++;
	   }
	   return -1;
	}


	// ���㼯����ۺϵĽ��
	@SuppressWarnings("unchecked")
	private Set<Set<Integer>> setListPoly(List<Object> oriSetList)
	{
	   Set<Set<Integer>> result = new HashSet<Set<Integer>>();
	   if (oriSetList.size() > 0)
	   {
	    // ��ȡ��һ��������
	    Map<Integer, Set<Integer>> groupMap = (Map<Integer, Set<Integer>>)oriSetList.get(0);
	    for (Iterator<Integer> it = groupMap.keySet().iterator(); it.hasNext(); )
	    {
	     result.add(groupMap.get(it.next()));
	    }
	   
	    for (int i = 1; i < oriSetList.size(); i++)
	    {
	     // ��ȡ�м伯��
	     Map<Integer, Set<Integer>> midMap = (Map<Integer, Set<Integer>>)oriSetList.get(i);
	     List<Set<Integer>> midSetList = new ArrayList<Set<Integer>>();
	     for (Iterator<Integer> it = midMap.keySet().iterator(); it.hasNext(); )
	     {
	      midSetList.add(midMap.get(it.next()));
	     }
	    
	     // ��ʼ����
	     // ������
	     List<Set<Integer>> calcResult = new ArrayList<Set<Integer>>();    
	     for (Iterator<Set<Integer>> it = result.iterator(); it.hasNext(); )
	     {
	      Set<Integer> srcSet = it.next();
	      for (int k = 0; k < midSetList.size(); k++)
	      {
	       // ����2�����ϵĽ���
	       Set<Integer> mixed = getSetMixed(srcSet, midSetList.get(k));
	       // ��������Ϊ��
	       if (!mixed.isEmpty())
	       {
	        // ����������
	        calcResult.add(mixed);
	       }
	      }
	     }
	    
	     // ���������滻result
	     result.clear();
	     result.addAll(calcResult);    
	    }
	   }
	  
	   return result;
	}

	// ����������ϵĽ���
	private Set<Integer> getSetMixed(Set<Integer> set1, Set<Integer> set2)
	{
	   Set<Integer> mixed = new HashSet<Integer>();
	   for (Iterator<Integer> it = set1.iterator(); it.hasNext(); )
	   {
	    int emu = it.next();
	    if (set2.contains(emu))
	    {
	     mixed.add(emu);
	    }
	   }
	  
	   return mixed;
	}


	/**
	* �ж�2���������Ƿ����
	* @param setGrp1
	* @param setGrp2
	* @return
	*/
	private boolean isTwoSetGrpEqual(Set<Set<Integer>> setGrp1, Set<Set<Integer>> setGrp2)
	{
	   boolean same = false;
	   int matchCounts = 0;
	   if (setGrp1.size() == setGrp2.size())
	   {
	    for (Iterator<Set<Integer>> it = setGrp1.iterator(); it.hasNext(); )
	    {
	     Set<Integer> set1 = it.next();
	     for (Iterator<Set<Integer>> it2 = setGrp2.iterator(); it2.hasNext(); )
	     {
	      Set<Integer> set2 = it2.next();
	      if (set2.equals(set1))
	      {
	       matchCounts++;
	      }
	     }
	    }
	   
	    if (matchCounts == setGrp1.size())
	    {
	     same = true;
	    }
	   }
	   return same;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		   // DFA��ת����
		   int[][] d = {{}, {2, 3}, {2, 4}, {2, 3}, {2, 5}, {2, 3}};
		   // ����״̬����
		   Set<Integer> S = new HashSet<Integer>();
		   S.add(1);
		   S.add(2);
		   S.add(3);
		   S.add(4);
		   S.add(5);
		   // �����ַ�
		   char[] E = {0, 1};
		   int s = 1;
		   Set<Integer> F = new HashSet<Integer>();
		   F.add(5);
		  
		   DFAMinmal a339 = new DFAMinmal();
		   int[][] DFA  =a339.convert(d, S, E, s, F);
		   for(int i=0;i<DFA.length;++i)
		   {
			   for(int j=0;j<DFA[i].length;++j)
				   System.out.print(DFA[i][j]+" ");
			   System.out.println();
		   }
	}

}
