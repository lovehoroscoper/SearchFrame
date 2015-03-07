package trainHMM;

import java.util.HashMap;
import java.util.Map.Entry;

public class Word {
	HashMap<String,Integer> posFreqMap = new HashMap<String,Integer>();
	
	public String toString(){
		StringBuilder ret = new StringBuilder();
		 for(Entry<String,Integer> m: posFreqMap.entrySet()){
			 ret.append(m.getKey() + ":"+ m.getValue()+" ");
		 }
		 return ret.toString();
	}

	public String getFireProbability(CountPOS countPOS){
		StringBuilder ret = new StringBuilder();
		 for(Entry<String,Integer> m: posFreqMap.entrySet()){
			 //ĳ�ʵ�������Եķ�������� ĳ�ʳ���������Ե�Ƶ�� / ������Ե���Ƶ��
			 double prob = (double)m.getValue()/(double)(countPOS.getFreq(CorpusToDic.getPOSId(m.getKey())));
			 ret.append(m.getKey() + ":"+ prob +" ");
		 }
		 return ret.toString();
	}
	
	public Word() {
		super();
	}	

	public int addFreq(String pos) {
		Integer freq = posFreqMap.get(pos);
		if(freq == null)
		{
			freq = 1;
		}
		else
		{
			freq++;
		}
		posFreqMap.put(pos, freq);
		return freq;
	}
}
