package unknowRule;

import java.io.*;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * 词典类
 * 
 *
 */
public class DicDoc {

	private Properties propertie;
	private InputStream inputFile;
	private static String dicDir;
	public static class MatchRet {
		int end;
		DocDicTypes posInf;

		public MatchRet(int end, DocDicTypes posInf) {
			this.end = end;
			this.posInf = posInf;
		}

		public String toString() {
			return "end: " + this.end + "  type: " + posInf;
		}
	}

	/**
	 * 三叉树中的结点

	 *
	 */
	public final class TSTNode {

		/** The key to the node. */
		public DocDicTypes data = null;

		/** The relative nodes. */
		protected TSTNode loKID;
		protected TSTNode eqKID;
		protected TSTNode hiKID;

		/** The char used in the split. */
		protected char splitchar;

		/**
		 * Constructor method.
		 * 
		 *@param splitchar
		 *            The char used in the split.
		 */
		public TSTNode(char splitchar) {
			this.splitchar = splitchar;
		}

		public String toString() {
			return "splitchar : " + splitchar;
		}

	}

	/** The base node in the trie. */
	public TSTNode root;

	/**
	 * 获得文件名称
	 * @return
	 */
	public static String getDir() {

		String dir = dicDir;
		if (dir == null) {
			dir = "D:/txt/";
		} else if (!dir.endsWith("/")) {
			dir += "/";
		}
		return dir;
	}
	
	public static void main(String[] args) {
		System.out.println(dicDir);
	}

	/**
	 * 获得一个DicPois实例
	 */
	private static DicDoc instance = new DicDoc();

	private DicDoc() {
		propertie = new Properties();
		try {
			inputFile = this.getClass().getClassLoader().getResourceAsStream("conf.properties");
			propertie.load(inputFile);
			dicDir = propertie.getProperty("dicDir");
		} catch (FileNotFoundException ex) {
			System.out.println("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("装载文件--->失败!");
			ex.printStackTrace();
		} finally {
			try {
				inputFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		addWord(" ", 0, DocType.Link, 10);
		addWord("　",0,DocType.Other,10);
		addWord("(", 0, DocType.StartBraket, 1000);
		addWord(")", 0, DocType.EndBraket, 1000);
		addWord("[", 0, DocType.StartBraket, 1000);
		addWord("]", 0, DocType.EndBraket, 1000);
		addWord("［", 0, DocType.StartBraket, 1000);
		addWord("］", 0, DocType.EndBraket, 1000);
		addWord("{", 0, DocType.StartBraket, 1000);
		addWord("}", 0, DocType.EndBraket, 1000);
		addWord("“", 0, DocType.GuillemetStart, 1000);
		addWord("”", 0, DocType.GuillemetEnd, 1000);
		addWord("（", 0, DocType.StartBraket, 1000);
		addWord("）", 0, DocType.EndBraket, 1000);
		addWord("<", 0, DocType.StartBraket, 1000);
		addWord(">", 0, DocType.EndBraket, 1000);
		addWord("【", 0, DocType.StartBraket, 1000);
		addWord("】", 0, DocType.EndBraket, 1000);
		addWord(":", 0, DocType.GuillemetStart, 1000);
		addWord("：", 0, DocType.GuillemetStart, 1000);
		addWord("，", 0, DocType.GuillemetStart, 1000);
		addWord(",", 0, DocType.GuillemetStart, 1000);
		addWord("《", 0, DocType.GuillemetStart, 1000);
		addWord("》", 0, DocType.GuillemetEnd, 1000);
		//加载字典
		load("link.txt",DocType.Link,1);
		load("other.txt", DocType.Other, 1);
		load("suffix.txt", DocType.Suffix, 1);
		load("prefix.txt", DocType.Prefix);
		load("time.txt", DocType.Time, 1);
		load("address.txt", DocType.Address, 1);
		load("country.txt", DocType.Address, 1);
		load("province.txt", DocType.Address, 1);
		load("city.txt", DocType.Address, 1);
		
	}

	/**
	 * Constructs a Ternary Search Trie and loads data from a <code>File</code>
	 * into the Trie. The file is a normal text document, where each line is of
	 * the form word : integer.
	 * 
	 *@param file
	 *            The <code>File</code> with the data to load into the Trie.
	 *@exception IOException
	 *                A problem occured while reading the data.
	 */
	public void load(String dic, DocType type, int weight) {

		String line = null;
		long code = 0;

		try {
			InputStream file = null;
			if (dicDir == null) {
				file = getClass().getResourceAsStream(getDir() + dic);
			} else{
				file = new FileInputStream(new File(getDir() + dic));
			}
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(file, "gbk"));
			while ((line = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ":");
				String key = "";
				if (st.hasMoreTokens()) {
					key = st.nextToken();
				}
				if (st.hasMoreTokens()) {
					try {
						code = Long.parseLong(st.nextToken());
					} catch (NumberFormatException e) {
						code = 0;
					}
				}
				addWord(key, code, type, weight);
			}
			in.close();
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println("not find dictionary file:" + dic);
		} catch (java.util.NoSuchElementException e) {
			System.out.println("format error:" + line);
		}
	}
	
	public void load(String dic, DocType type) {
		String line = null;
		int weight = 0;

		try {
			InputStream file = null;
			if (dicDir == null) {
				file = getClass().getResourceAsStream(getDir() + dic);
			} else
				file = new FileInputStream(new File(getDir() + dic));
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(file, "gbk"));
			while ((line = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ":");
				String key = "";
				if (st.hasMoreTokens()) {
					key = st.nextToken();
				}
				if (st.hasMoreTokens()) {
					try {
						weight = Integer.parseInt(st.nextToken());
					} catch (NumberFormatException e) {
						System.out.println(e);
					}
				}
				addWord(key, 0l, type, weight);
			}
			in.close();
		} catch (IOException e) {
			System.out.println("not find dictionary file:" + dic);
		} catch (java.util.NoSuchElementException e) {
			System.out.println("format error:" + line);
		}
	}

	private void addWord(String key, long code, DocType type, int w) {
//		if(type.equals(POIType1.Link))
//			key="~";
		if (root == null) {
			root = new TSTNode(key.charAt(0));
		}

		DocDicTypes.PoiTypeInf1 pi = new DocDicTypes.PoiTypeInf1(type, w, code);

		TSTNode node = null;
		if (key.length() > 0 && root != null) {
			TSTNode currentNode = root;
			int charIndex = 0;
			while (true) {
				if (currentNode == null)
					break;
				int charComp = (key.charAt(charIndex) - currentNode.splitchar);
				if (charComp == 0) {
					charIndex++;
					if (charIndex == key.length()) {
						node = currentNode;
						break;
					}
					currentNode = currentNode.eqKID;
				} else if (charComp < 0) {
					currentNode = currentNode.loKID;
				} else {
					currentNode = currentNode.hiKID;
				}
			}
			DocDicTypes occur2 = null;
			if (node != null) {
				occur2 = node.data;
			}
			if (occur2 != null) {
				occur2.insert(pi);
				return;
			}
			currentNode = getOrCreateNode(key);

			DocDicTypes occur3 = currentNode.data;
			if (occur3 != null) {
				occur3.insert(pi);
			} else {
				DocDicTypes occur = new DocDicTypes();
				occur.put(pi);
				currentNode.data = occur;
			}
		}
	}

	public static int matchEnglish(int start, String sentence) {
		int i = start;
		int count = sentence.length();
		while (i < count) {
			char c = sentence.charAt(i);
			if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
				++i;
			} else {
				break;
			}
		}

		return i;
	}

	private TSTNode getOrCreateNode(String key) {

		if (key == null) {
			throw new NullPointerException(
					"attempt to get or create node with null key");
		}
		if ("".equals(key)) {
			throw new IllegalArgumentException(
					"attempt to get or create node with key of zero length");
		}

		if (root == null) {
			root = new TSTNode(key.charAt(0));
		}

		TSTNode currentNode = root;

		int charIndex = 0;

		//创建一个三叉树
		while (true) {
			int charComp = key.charAt(charIndex) - currentNode.splitchar;

			if (charComp == 0) {

				charIndex++;
				if (charIndex == key.length()) {
					return currentNode;
				}

				if (currentNode.eqKID == null) {
					currentNode.eqKID = new TSTNode(key.charAt(charIndex));
				}

				currentNode = currentNode.eqKID;
			} else if (charComp < 0) {
				if (currentNode.loKID == null) {
					currentNode.loKID = new TSTNode(key.charAt(charIndex));
				}

				currentNode = currentNode.loKID;
			} else {
				if (currentNode.hiKID == null) {
					currentNode.hiKID = new TSTNode(key.charAt(charIndex));
				}

				currentNode = currentNode.hiKID;
			}
		}
	}

	public static DicDoc getInstance() {
		return instance;
	}

	public static void reload() {

		if (instance != null) {
			instance = null;
		}
		instance = new DicDoc();
	}

	/**
	 * 最大长度匹配分词
	 * @param word要分的字符串
	 * @param start从哪里开始分
	 * @param matchRet返回的最大匹配的结果，用matchRet封装
	 * @return matchRet
	 */
	public MatchRet matchLong(String word, int start, MatchRet matchRet) {

		if (word == null || "".endsWith(word) || root == null
				|| start >= word.length()) {
			matchRet.end = start;
			matchRet.posInf = null;
			return matchRet;
		}

		int ret = start;
		DocDicTypes pdt = new DocDicTypes();
		TSTNode currentNode = root;
		int charIndex = start;

		while (true) {
			if (currentNode == null) {
				matchRet.end = ret;
				matchRet.posInf = pdt;
				return matchRet;
			}

			int charComp = word.charAt(charIndex) - currentNode.splitchar;

			if (charComp == 0) {
				charIndex++;

				if (currentNode.data != null && ret < charIndex) {
					ret = charIndex;
					pdt = currentNode.data;
				}
				if (charIndex == word.length()) {
					matchRet.end = ret;
					matchRet.posInf = pdt;
					return matchRet;
				}
				currentNode = currentNode.eqKID;
			} else if (charComp < 0) {
				currentNode = currentNode.loKID;
			} else {
				currentNode = currentNode.hiKID;
			}
		}
	}
	
	public int getWeight(String word) {
		if (word == null || "".endsWith(word) || root == null) {
			return -1;
		}

		int ret = -1;
		TSTNode currentNode = root;
		int charIndex = 0;

		while (true) {
//			System.out.println(currentNode);
			if (currentNode == null) {
				return ret;
			}

			int charComp = word.charAt(charIndex) - currentNode.splitchar;
//			System.out.println();

			if (charComp == 0) {
				charIndex++;

				if (currentNode.data != null && charIndex == word.length()) {
					ret = currentNode.data.getHead().item.weight;
					return ret;
				}
				currentNode = currentNode.eqKID;
			} else if (charComp < 0) {
				currentNode = currentNode.loKID;
			} else {
				currentNode = currentNode.hiKID;
			}
		}
	}

}
