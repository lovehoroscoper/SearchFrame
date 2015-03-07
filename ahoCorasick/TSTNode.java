package ahoCorasick;

import java.util.LinkedList;

public class TSTNode {
	
	protected TSTNode loNode; //��߽ڵ�
	
	protected TSTNode eqNode; //�м�ڵ�
	
	protected TSTNode hiNode; //�ұ߽ڵ�
	
	protected TSTNode ptNode; //��Ը��ڵ�
	
	/**  �ڵ��ֵ  */
	public POSInf data=null;// data���Կ��Դ洢��ԭ�ĺʹ��ԡ���Ƶ����ص���Ϣ
	
	protected char splitchar; //���ڵ��ʾ���ַ�
	
	protected TSTNode _failure;//ƥ��ʧ�ܺ���ת�Ľڵ�
	
	private LinkedList<String> _results;//�洢ģʽ�����������
	
	public LinkedList<String> getResult(){
		return this._results;
	}
	
	//��ģʽ���в���_results�е�ģʽ����ӽ���
	public void addResult(String result) {
		if (_results.contains(result))//����Ѿ�������ģʽ�����ӵ�ģʽ����
			return;
		_results.add(result);
	}
	
	public String getPath()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.splitchar);
		TSTNode parent = ptNode;
		while (parent!=null)
		{
			sb.append(parent.splitchar);
			parent = parent.ptNode;
		}
		return sb.reverse().toString();
	}
	
	/*public String getPath()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.splitchar);
		TSTNode parent = ptNode;
		TSTNode child = this;
		if(parent==null)
		{
			return String.valueOf(this.splitchar);
		}
		while (true)
		{
			//System.out.println("splitchar:"+parent.splitchar);
			if(parent.eqNode==child)
			{
				sb.append(parent.splitchar);
			}
			parent = parent.ptNode;
			child = parent;
			if(parent==null)
			{
				break;
			}
		}
		return sb.reverse().toString();
	}*/
	
	/**
	 *  ���췽��
	 *
	 *@param  splitchar  �ýڵ��ʾ���ַ�
	 */
	protected TSTNode(char splitchar, TSTNode parent) {
		this.splitchar = splitchar;
		this.ptNode = parent;
		_results = new LinkedList<String>();//�洢����û���ظ���ģʽ��������
	}
	public String toString()
	{
		return "splitchar:"+ splitchar;
	}
}
