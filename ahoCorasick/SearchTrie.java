package ahoCorasick;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SearchTrie {
	private static TSTNode root=null;

	private static SearchTrie searchTrie;

	public static SearchTrie getInit(String[] dicWord) {
		if (searchTrie == null) {
			return new SearchTrie(dicWord);
		}
		return searchTrie;
	}

	public static SearchTrie getInstance() {
		if (searchTrie == null) {
			return new SearchTrie("SDIC.txt");
		}
		return searchTrie;
	}
	
	public SearchTrie(String fileName) {
		FileReader filereader = null;
		try {
			filereader = new FileReader(fileName);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader read = new BufferedReader(filereader);
		String key = null;
		try {
			while ((key = read.readLine()) != null) {
				key = (new StringTokenizer(key)).nextToken();
				if (root == null) {
					root = new TSTNode(key.charAt(0), null);
				}
				
				if (key.length() > 0 && root != null) {
					TSTNode currentNode = creatTSTNode(key);
					currentNode.addResult(key);// �洢ģʽ�����������
				}
			}
			
			// ���������ĸ��ڵ������µ�һ�����ڵ�
			TSTNode root_parent = new TSTNode(' ', null);
			root.ptNode = root_parent;
			root_parent.eqNode = root;
			root = root_parent;
		} catch (IOException e) {
			System.out.println(e);
		}
		
		addFailure();
	}

	// �����������ķ���
	public SearchTrie(String[] dicWord) {
		buildTree(dicWord);
		// ���ƥ��ʧ�ܺ���ת�Ľڵ�
		addFailure();
	}

	private void buildTree(String[] dicWord) {
		for (String key : dicWord) {
			if (root == null) {
				root = new TSTNode(key.charAt(0), null);
			}

			if (key.length() > 0 && root != null) {
				TSTNode currentNode = creatTSTNode(key);
				currentNode.addResult(key);// �洢ģʽ�����������
			}
		}
	}

	// ��ÿ���ڵ����ʧ�ܵĽڵ�
	private void addFailure() {
		ArrayList<TSTNode> firstNodes = this.getEqNodes(root);
		System.out.println(firstNodes.size());
		//System.out.println("path:"+firstNodes.get(0).getPath());
		ArrayList<TSTNode> nodes = new ArrayList<TSTNode>();
		// ѭ����������ڵ��ͬ���ڵ� ���ʧ�ܵĽڵ�Ϊ���ڵ�
		int i=0;
		for (TSTNode node : firstNodes) {
			System.out.println("path:"+i+":"+node.getPath());
			//node.ptNode = root; // ָ�����ڵ�
			//node._failure = null; // ָ��ʧ�ܺ���ת�Ľڵ�
			ArrayList<TSTNode> trans = this.getEqNodes(node.eqNode);
			for (TSTNode t : trans) {
				System.out.println("add:"+t.getPath());
			}
			nodes.addAll(trans);
			++i;
		}
		
		while (nodes.size() != 0) {
			ArrayList<TSTNode> newNodes = new ArrayList<TSTNode>();
			for (TSTNode nd : nodes) {
				TSTNode r = nd.ptNode._failure;
				
				char c = nd.splitchar;
				// ����ڸ��ڵ�ʧ�ܺ���ת�ĺ��ӽڵ���û���ҵ��ýڵ�
				while (r != null && !containsTransition(r,c))
					r = r._failure;
				
				if (r == null)
				{
					r = root;
				}
				else
				{
					System.out.println("r:" + r.getPath());
				}
				
				nd._failure = getTransition(r,c);
				if(nd._failure!=null)
				{
					System.out.println("nd"+nd.getPath()+" nd._failure:" + nd._failure.getPath());
					for (String result : nd._failure.getResult())
					{
						System.out.println("nd"+nd.getPath()+" add result:"+result);
						nd.addResult(result);
					}
				}

				for (TSTNode child : this.getEqNodes(nd.eqNode))
					newNodes.add(child);
			}
			nodes = newNodes;

			for (TSTNode t : nodes) {
				System.out.println(t.getPath());
			}
		}
		root._failure = null;
	}

	//����һ�����
	public TSTNode creatTSTNode(String key)throws NullPointerException,IllegalArgumentException {
		if(key==null){
			throw new NullPointerException("��ָ���쳣");
		}
		int charIndex = 0;
		TSTNode currentNode = root;
		if (root == null) {
			root = new TSTNode(key.charAt(0), null);
		}
		TSTNode parentNode = null; // ��Ը��ڵ�
		while (true) {
			int compa = (key.charAt(charIndex) - currentNode.splitchar);
			if (compa == 0) {
				charIndex++;
				if (charIndex == key.length()) {
					return currentNode;				
				}
				if (currentNode.eqNode == null) {
					currentNode.eqNode = new TSTNode(key.charAt(charIndex),
							currentNode);
				}
				parentNode = currentNode;
				currentNode = currentNode.eqNode;
			} else if (compa < 0) {
				if (currentNode.loNode == null) {
					currentNode.loNode = new TSTNode(key.charAt(charIndex),
							parentNode);
				}
				currentNode = currentNode.loNode;
			} else {
				if (currentNode.hiNode == null) {
					currentNode.hiNode = new TSTNode(key.charAt(charIndex),
							parentNode);
				}
				currentNode = currentNode.hiNode;
			}
		}				
	}

	public TSTNode getTransition(TSTNode currentNode, char cmpChar) {
		while (true) {
			if (currentNode == null) {
				return null;
			}
			int charComp = cmpChar - currentNode.splitchar;
			if (charComp == 0) {
				return currentNode;
			} else if (charComp < 0) {
				currentNode = currentNode.loNode;
			} else {
				currentNode = currentNode.hiNode;
			}
		}
	}

	// �������ı����ҹؼ��ʼ��ϵĹ���
	public StringSearchResult[] findAll(String text) {
		ArrayList<StringSearchResult> ret = new ArrayList<StringSearchResult>();
		TSTNode ptr = root;
		int index = 0;

		while (index < text.length()) {
			TSTNode trans = null;
			while (trans == null) {
				trans = getTransition(ptr, text.charAt(index));
				if (ptr == null)
					break;
				if (trans == null) {
					// ƥ��ʧ�ܺ󣬴�ʧ��������ƥ��
					ptr = ptr._failure;
					if(ptr!=null)
						System.out.println("ptr:" + ptr.getPath());
				}
			}
			if (trans != null)
				ptr = trans;
			
			if(ptr != null)
			{
				System.out.println("size:" + ptr.getResult().size());
	
				for (String found : ptr.getResult())
				{
					System.out.println("found:" + found);
					ret.add(new StringSearchResult(index - found.length() + 1,
							found));
				}
			}
			else
			{
				ptr = root;
			}
			index++;
		}

		return ret.toArray(new StringSearchResult[ret.size()]);
	}

	public String depthSearch(TSTNode node, StringBuilder ret, int deapth) {
		if (node != null) {
			for (int i = 0; i < deapth; i++)
				ret.append("|  ");
			ret.append(node.getPath() + "\n");
			
			if (node.loNode != null) {
				depthSearch(node.loNode, ret, deapth + 1);
			}
			if (node.eqNode != null) {
				depthSearch(node.eqNode, ret, deapth + 1);
			}
			if (node.hiNode != null) {
				depthSearch(node.hiNode, ret, deapth + 1);
			}
			/*ret.append("|����");
			ret.append(node.splitchar + "\n");
			if (node.loNode != null) {
				depthSearch(node.loNode, ret, deapth + 1);
			}
			if (node.eqNode != null) {
				depthSearch(node.eqNode, ret, deapth + 1);
			}
			if (node.hiNode != null) {
				depthSearch(node.hiNode, ret, deapth + 1);
			}*/
		}
		return ret.toString();
	}

	// ��ӡ��״ͼ��
	public String toString() {
		StringBuilder ret = new StringBuilder();
		return depthSearch(root, ret, 0);
	}

	// ���ݸýڵ�Ļ�ȡ��ýڵ�ͬ�������нڵ�
	public ArrayList<TSTNode> getEqNodes(TSTNode tstNode) {
		ArrayList<TSTNode> childNodes = new ArrayList<TSTNode>();
		if (tstNode != null) {
			ArrayList<TSTNode> nodes = new ArrayList<TSTNode>();
			nodes.add(tstNode);//��ӽڵ�
			childNodes = nodes;
			while (true) {
				ArrayList<TSTNode> newlist = new ArrayList<TSTNode>();
				for (TSTNode node : nodes) {
					if (node.loNode != null) {
						newlist.add(node.loNode);// �����ڵ�
					}
					if (node.hiNode != null) {
						newlist.add(node.hiNode);// ����ҽڵ�
					}
				}
				nodes = newlist;
				if (newlist.size() == 0) {
					return childNodes;
				}
				childNodes.addAll(nodes);
			}
		}
		return childNodes;
	}

	// ����ĳһ�ڵ���Ҹýڵ�ĸ��ڵ�ƥ��ʧ�ܺ�Ľڵ����Ƿ���ڸýڵ�
	private boolean containsTransition(TSTNode tstNode, char cmpChar) {
		int charComp;
		while (true) {
			if (tstNode == null) {
				return false;
			}
			charComp = cmpChar - tstNode.splitchar;
			if (charComp == 0) {
				return true;
			} else if (charComp < 0) {
				tstNode = tstNode.loNode;
			} else {
				tstNode = tstNode.hiNode;
			}
		}
	}

}
