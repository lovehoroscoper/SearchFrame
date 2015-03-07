package classify;

import java.util.List;
import java.util.Set;

/**
 * Trie tree
 * 
 * 
 * 
 */
public class Trie {

	public TrieNode<WordRelation> rootNode = new TrieNode<WordRelation>();

	public void add(String key, WordRelation value) {
		addNode(rootNode, key, 0, value);
	}

	public WordRelation find(String key) {
		return findKey(rootNode, key);
	}

	/**
	 * �ִ�����
	 * 
	 * @param prefix
	 *            Ҫ�ִʵ��ַ���
	 * @return
	 */
	public void analysisAll(String prefix, int[] degrees) {
		int index = 0;
		TrieNode<WordRelation> node = rootNode;
		while (index < prefix.length()) {
			char c = prefix.charAt(index);
			if (" ".equals(c) || ",".equals(c)) {
				index++;
				continue;
			}
			node = node.getChildren().get(c);
			if (node == null) {
				// ��ȡ��һ����������ĸ
				index = getMinIndex(index, prefix);
				node = rootNode;
			} else if (node.isTerminal()) {
				// �ж���һ���ַ��Ƿ�����ĸ
				if (index + 1 < prefix.length()) {
					c = prefix.charAt(index + 1);
					if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
						index++;
						continue;
					}
				}
				WordRelation wr = node.getNodeValue();
				degrees[wr.getIndex()] = degrees[wr.getIndex()]
						+ wr.getDegree();
			}
			if (index == -1) {
				return;
			}
			index++;
		}
		return;
	}

	/*
	 * ȡ����һ���������ַ�
	 */
	// TODO: Ч��̫��
	private static int getMinIndex(int index, String prefix) {
		// char ch [] ={'.',',',' '};
		for (; index < prefix.length(); index++) {
			char c = prefix.charAt(index);
			if (c == '.') {
				return index;
			} else if (c == ',') {
				return index;
			} else if (c == ' ') {
				return index;
			}
		}
		return index;
	}

	private WordRelation findKey(TrieNode<WordRelation> currNode, String key) {
		Character c = key.charAt(0);
		if (currNode.getChildren().containsKey(c)) {
			TrieNode<WordRelation> nextNode = currNode.getChildren().get(c);
			if (key.length() == 1) {
				if (nextNode.isTerminal()) {
					return nextNode.getNodeValue();
				}
			} else {
				return findKey(nextNode, key.substring(1));
			}
		}

		return null;
	}

	/*
	 * �ݹ���� ����key�ֳ��ַ��ӽڵ����Trie
	 */
	private void addNode(TrieNode<WordRelation> currNode, String key, int pos,
			WordRelation value) {
		Character c = key.charAt(pos);
		TrieNode<WordRelation> nextNode = currNode.getChildren().get(c);

		if (nextNode == null) {
			nextNode = new TrieNode<WordRelation>();
			nextNode.setNodeKey(c);
			if (pos < key.length() - 1) {
				addNode(nextNode, key, pos + 1, value);
			} else {
				nextNode.setNodeValue(value);
				nextNode.setTerminal(true);
			}
			currNode.getChildren().put(c, nextNode);
		} else {
			if (pos < key.length() - 1) {
				addNode(nextNode, key, pos + 1, value);
			} else {
				nextNode.setNodeValue(value);
				nextNode.setTerminal(true);
			}
		}
	}

	// ��ô����һ������Ӧ�ò��ÿ�ȱ����ķ���
	public void findKey(List<WordRelation> si, TrieNode<WordRelation> currNode,
			String key) {
		Character c = key.charAt(0);
		if (currNode.getChildren().containsKey(c)) {
			TrieNode<WordRelation> nextNode = currNode.getChildren().get(c);
			if (key.length() == 1) {
				addItem(si, nextNode);
			} else {
				findKey(si, nextNode, key.substring(1));
			}
		}

	}

	private List<WordRelation> addItem(List<WordRelation> si,
			TrieNode<WordRelation> nextNode) {
		Set<Character> cset = nextNode.getChildren().keySet();
		if (nextNode.isTerminal()) {
			si.add(nextNode.getNodeValue());
		}
		for (Character c : cset) {
			addItem(si, nextNode.getChildren().get(c));
		}
		return si;
	}

}