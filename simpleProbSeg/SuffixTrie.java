package simpleProbSeg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import unigrambigramSeg.WordType;


public class SuffixTrie {

	public final class TSTNode {
		public WordType data = null;

		protected TSTNode loNode;
		protected TSTNode eqNode;
		protected TSTNode hiNode;

		public char splitChar;

		public TSTNode(char key) {
			this.splitChar = key;
		}

		public String toString() {
			return "data  ��" + data + "   spliter��" + splitChar;
		}
	}

	public static class SuffixRet {
		public ArrayList<WordType> values;
		public int nextIndex; // ��¼�´�ƥ��Ŀ�ʼλ��

		public SuffixRet(ArrayList<WordType> values, int nextIndex) {
			this.values = values;
			this.nextIndex = nextIndex;
		}

	}

	private static final String DIC_LOCATION = "/SDIC.txt";

	private static SuffixTrie dic = null;

	public TSTNode root;

	public int wordCount;

	public static SuffixTrie getInstance() {
		if (dic == null)
			synchronized (DIC_LOCATION) {
				dic = new SuffixTrie(SuffixTrie.class.getResourceAsStream(DIC_LOCATION));
			}
		return dic;
	}

	private SuffixTrie(InputStream input) {
		try {
			BufferedReader read = new BufferedReader(new InputStreamReader(input, "GBK"));
			String line;
			try {
				while ( ((line = read.readLine()) != null)) {
					if("".equals(line)) 
							continue;
					
					StringTokenizer st = new StringTokenizer(line,"\t");
					String key = st.nextToken();
					int freq;
					try {
						freq = Integer.parseInt(st.nextToken());
					} catch (NumberFormatException e) {
						freq = 1;
					}
					addWord(key,freq);
					wordCount += freq;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*try {
			List<String> lines = IOUtils.readLines(input, "GBK");
			for (String line : lines) {
				if (StringUtils.isBlank(line))
					continue;
				String[] split = line.split("\t");
				String key = split[0];
				int freq = NumberUtils.toInt(split[1], 1);
				addWord(key, freq);
				wordCount += freq;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	public void addWord(String key, int freq) {
		if (key==null)
			throw new IllegalArgumentException();
		int charIndex = key.length() - 1;
		if (root == null)
			root = new TSTNode(key.charAt(charIndex));
		TSTNode currNode = root;
		while (true) {
			int compa = (key.charAt(charIndex) - currNode.splitChar);
			if (compa == 0) {
				charIndex--;
				if (charIndex < 0) {
					currNode.data = new WordType(key, freq);
					break;
				}
				if (currNode.eqNode == null)
					currNode.eqNode = new TSTNode(key.charAt(charIndex));
				currNode = currNode.eqNode;
			} else if (compa < 0) {
				if (currNode.loNode == null)
					currNode.loNode = new TSTNode(key.charAt(charIndex));
				currNode = currNode.loNode;
			} else {
				if (currNode.hiNode == null)
					currNode.hiNode = new TSTNode(key.charAt(charIndex));
				currNode = currNode.hiNode;
			}
		}
	}

	public void matchAll(String sentence, int offset,ArrayList<WordType> ret) {
		ret.clear(); //��շ��������еĴ�
		if ("".equals(sentence) || root == null || offset < 0)
			return;

		// ƥ��Ӣ��
		WordType enWord = matchEnglish(sentence, offset);
		if (enWord != null) {
			ret.add(enWord);
			return;
		}
		// ƥ������
		WordType dgWord = matchDigit(sentence, offset);
		if (dgWord != null) {
			ret.add(dgWord);
			return;
			// tokens.add(new CnToken(dgWord.word, offset - dgWord.word.length() + 1, offset + 1, dgWord.freq));
			// tokens.addAll(matchAll(sentence,offset - dgWord.word.length()));
		}
		TSTNode currentNode = root;
		int charIndex = offset;
		while (true) {
			if (currentNode == null) {// ��ǰ�ڵ�Ϊ��,˵��ʵ����Ҳ�����Ӧ�Ĵ�,�򽫵����ַ��
				if(ret.size() == 0)
					ret.add(new WordType(sentence.charAt(offset) + "", 1));
				return;
			}
			int charComp = sentence.charAt(charIndex) - currentNode.splitChar;

			if (charComp == 0) {
				if (currentNode.data != null) {
					ret.add(currentNode.data) ; // ��ѡ�ƥ���
				}
				if (charIndex <= 0) {
					return; // �Ѿ�ƥ����
				}
				charIndex--;
				currentNode = currentNode.eqNode;
			} else if (charComp < 0) {
				currentNode = currentNode.loNode;
			} else {
				currentNode = currentNode.hiNode;
			}
		}
	}

	private WordType matchEnglish(String sentence, int offset) {
		int index = offset;
		// ƥ��Ӣ��
		for (; index >= 0; index--) {
			char c = sentence.charAt(index);
			if (!(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z'))
				break;
		}
		if (index != offset)
			return new WordType(sentence.substring(++index, offset + 1), 1);
		return null;
	}

	private WordType matchDigit(String sentence, int offset) {
		int index = offset;
		// ƥ������
		for (; index >= 0; index--) {
			char c = sentence.charAt(index);
			if (!(c >= '0' && c <= '9'))
				break;
		}

		if (index != offset)
			return new WordType(sentence.substring(++index, offset + 1), 1);
		return null;
	}

	public static void main(String[] args) {
		SuffixTrie trie = SuffixTrie.getInstance();
		//System.out.println(trie.matchLong("��ѧ��abc�����", 5).word);
		//SuffixRet suffixRet = trie.matchAll("��������123",7);
		//System.out.println(suffixRet.values);
		//System.out.println(suffixRet.nextIndex);
		
		String txt = "��������123";
		ArrayList<WordType> ret = new ArrayList<WordType>();
		for (int index = 1; index <txt.length();++index) {
			trie.matchAll(txt, index,ret);
			for (WordType word : ret) {
				double logProb = Math.log(word.freq) - Math.log(trie.wordCount);
				System.out.println(word+":"+logProb);
			}
		}
		
	}
}
