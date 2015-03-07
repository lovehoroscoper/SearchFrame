package enDep;

import java.util.ArrayList;
import java.util.HashMap;

//���������е�Ӣ����������������������ת��
public class TreeConverter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//�ִʵõ��Ĵ�����
		ArrayList<Token> seg = new ArrayList<Token>();
		seg.add(new Token("һ",0,1));
		seg.add(new Token("��",1,5));
		
		// a book �� �����ķ���
		// a -det-> book
		ArrayList<ArrayList<TreeNode>> depTree = new ArrayList<ArrayList<TreeNode>>();
		
		TreeNode a = new TreeNode(seg.get(0));
		TreeNode book = new TreeNode(seg.get(1));
		a.dominator = book; //��������֧����ǡ��顱
		a.relation = GrammaticalRelation.DETERMINER;
		
		TreeNode root = book;

		ArrayList<TreeNode> firstLevel = new ArrayList<TreeNode>();
		firstLevel.add(a);
		firstLevel.add(book);
		depTree.add(firstLevel);
		
		//String sent= getSentence(root,depTree);
		//System.out.println(sent);
		
		convert(root,depTree);
		String sent=TestDep.getSentence(root,depTree);
		System.out.println(sent);
	}
	
	static HashMap<String,String> quantityMap = new HashMap<String,String>();
	
	//�� ֻ �� ̨ �� �� �� �� λ ƪ �� �� ͷ �� �� �� �� �� �� �� ֧ �� �� �� ö �� �� �� �� �� �� �� ƥ �� �� �� Ƭ �� �� �� �� Ⱥ �� �� �� �� �� �� �� յ �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� ˿ �� �� ֦ �� �� �� �� �� Ȧ �� ͦ �� ˫ �� �� ��
	//�� �� Ƭ ƪ �� �� ֻ �� �� �� �� ֧ �� �� �� ̨ �� �� ͷ �� �� �� �� �� ƥ �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� յ λ �� �� �� �� �� �� �� �� ֦ ö ׮ �� �� �� �� �� �� �� �� �� �� �� Ա �� �� ҳ �� �� �� �� �� β �� �� �� �� �� �� Ļ �� �� �� �� �� �� �� �� Ԫ �� �� �� �� �� �� �� �� �� �� �� ��
	static{
		quantityMap.put("��", "��");
		quantityMap.put("��", "֧");
		quantityMap.put("����", "ƪ");
	}
	
	public static void convert(TreeNode root,ArrayList<ArrayList<TreeNode>> depTree){
		int level = 0;
		ArrayList<TreeNode> currentLevel = depTree.get(level);
		
		for(TreeNode n:currentLevel){
			if(n.relation == GrammaticalRelation.DETERMINER){
				//�����½ڵ�
				TreeNode newNode = new TreeNode (quantityMap.get(n.dominator.term),3);
				//�½ڵ���ԭ�в�
				newNode.dominator = n.dominator;
				int pos = currentLevel.indexOf(n);
				currentLevel.set(pos, newNode);
				
				//�����µĲ�
				ArrayList<TreeNode> newLevel = new ArrayList<TreeNode>();
				newNode.child = newNode.clone();
				n.dominator = newNode.child;
				newLevel.add(n);
				newLevel.add(newNode.child);
				depTree.add(newLevel);
			}
		}
	}

}
