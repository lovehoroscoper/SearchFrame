package rule;

import bayes.FMMSegment;

import java.util.ArrayList;
import java.util.HashSet;

/**
* ���ڹ���ķ�����
*/
public class RuleClassifier 
{
	private static FMMSegment spliter = new FMMSegment();
	private static ArrayList<Rule> decisionList = new ArrayList<Rule>();//����Ĺ��򼯺�

	/**
	* Ĭ�ϵĹ���������ʼ������
	*/
	public RuleClassifier()
	{
		Rule r = new Rule(new String[]{"��ͣ"},"�ƾ�");
		decisionList.add(r);
		
		r = new Rule(new String[]{"����"},"����");
		decisionList.add(r);
	}

	/**
	* �Ը������ı����з���
	* @param text �������ı�
	* @return ������
	*/
	public String classify(String text) 
	{
		ArrayList<String> terms = spliter.getWords(text);//���ķִʴ���
		HashSet<String> instance = new HashSet<String>();
		instance.addAll(terms);
		
		for (int i = 0; i < decisionList.size(); i++) {
			Rule rule = decisionList.get(i);
			if (rule.covers(instance))
				return rule.consequent;
		}
		return "";
	}
	
	public static void main(String[] args)
	{
		String text ="NBA����";
		RuleClassifier classifier = new RuleClassifier();//������������
		String result = classifier.classify(text);//���з���
		System.out.println("��������["+result+"]���");
	}
}