package unknowRule;

import java.util.ArrayList;

/**
 * 
 * 包含一些规则的定义
 * 
 * 
 * @2010-3-19
 */
public class UnknowGrammarDoc {

	/**
	 * An inner class of Ternary Search Trie that represents a node in the trie.
	 */
	public final class TSTNode {

		/** The key to the node. */
		public ArrayList<DocSpan> data = null;

		/** The relative nodes. */
		protected TSTNode loKID;

		protected TSTNode eqKID;

		protected TSTNode hiKID;

		/** The char used in the split. */
		protected DocType splitchar;

		/**
		 * Constructor method.
		 * 
		 * @param splitchar
		 *            The char used in the split.
		 */
		protected TSTNode(DocType splitchar) {
			this.splitchar = splitchar;
		}

		public String toString() {
			return "from UnknowGrammarPoi.TSTNode...toString() splitchar:"
					+ splitchar;
		}
	}

	/** The base node in the trie. */
	public TSTNode root;

	public void addProduct(ArrayList<DocType> key, ArrayList<DocSpan> lhs) {

		boolean isEquals = true;
		for (DocSpan span : lhs) {
			if (span.length > 1) {
				isEquals = false;
				break;
			}
		}

		if (isEquals) {
			int leftCount = 0;
			int rightCount = 0;
			for (DocType dy : key) {

				leftCount += dy.ordinal();

			}
			for (DocSpan sd : lhs) {

				rightCount += sd.type.ordinal();

			}

			if (leftCount <= rightCount) {

				System.out.println("leftCount->" + leftCount);
				System.out.println("rightCount->" + rightCount);
				System.out.println("您所写的规则加载失败");

				System.exit(-1);
			}
		}

		if (root == null) {
			root = new TSTNode(key.get(0));
		}
		TSTNode node = null;
		if (key.size() > 0 && root != null) {
			TSTNode currentNode = root;
			int charIndex = 0;
			while (true) {
				if (currentNode == null)
					break;
				int charComp = key.get(charIndex).compareTo(
						currentNode.splitchar);
				if (charComp == 0) {
					charIndex++;
					if (charIndex == key.size()) {
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
			ArrayList<DocSpan> occur2 = null;
			if (node != null) {
				occur2 = node.data;
			}
			if (occur2 != null) {
				return;
			}
			currentNode = getOrCreateNode(key);
			currentNode.data = lhs;
		}
	}

	public static class MatchRet {
		public int end;

		public ArrayList<DocSpan> lhs;

		public MatchRet(int e, ArrayList<DocSpan> d) {
			this.end = e;
			this.lhs = d;
		}

		public String toString() {
			return end + ":" + lhs;
		}
	}

	public MatchRet matchLong(ArrayList<DocToken> key, int offset,
			MatchRet matchRet) {
		if (key == null || root == null || "".equals(key)
				|| offset >= key.size()) {
			matchRet.end = offset;
			matchRet.lhs = null;
			return matchRet;
		}
		int ret = offset;
		ArrayList<DocSpan> retPOS = null;

		TSTNode currentNode = root;
		int charIndex = offset;
		while (true) {
			if (currentNode == null) {
				matchRet.end = ret;
				matchRet.lhs = retPOS;
				return matchRet;
			}
			int charComp = key.get(charIndex).type
					.compareTo(currentNode.splitchar);

			if (charComp == 0) {
				charIndex++;

				if (currentNode.data != null && charIndex > ret) {
					ret = charIndex;
					retPOS = currentNode.data;
				}
				if (charIndex == key.size()) {
					matchRet.end = ret;
					matchRet.lhs = retPOS;
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

	protected TSTNode getOrCreateNode(ArrayList<DocType> key)
			throws NullPointerException, IllegalArgumentException {
		if (key == null) {
			throw new NullPointerException(
					"attempt to get or create node with null key");
		}
		if ("".equals(key)) {
			throw new IllegalArgumentException(
					"attempt to get or create node with key of zero length");
		}
		if (root == null) {
			root = new TSTNode(key.get(0));
		}
		TSTNode currentNode = root;
		int charIndex = 0;
		while (true) {
			int charComp = key.get(charIndex).compareTo(currentNode.splitchar);
			if (charComp == 0) {
				charIndex++;
				if (charIndex == key.size()) {
					return currentNode;
				}
				if (currentNode.eqKID == null) {
					currentNode.eqKID = new TSTNode(key.get(charIndex));
				}
				currentNode = currentNode.eqKID;
			} else if (charComp < 0) {
				if (currentNode.loKID == null) {
					currentNode.loKID = new TSTNode(key.get(charIndex));
				}
				currentNode = currentNode.loKID;
			} else {
				if (currentNode.hiKID == null) {
					currentNode.hiKID = new TSTNode(key.get(charIndex));
				}
				currentNode = currentNode.hiKID;
			}
		}
	}

	public static void replace(ArrayList<DocToken> key, int offset,
			ArrayList<DocSpan> spans) {
		int j = 0;
		for (int i = offset; i < key.size(); ++i) {
			DocSpan span = spans.get(j);
			DocToken token = key.get(i); // PoiToken token
			StringBuilder newText = new StringBuilder();
			int newStart = token.start;
			int newEnd = token.end;
			DocType newType = span.type;

			for (int k = 0; k < span.length; ++k) {
				token = key.get(i + k);
				newText.append(token.termText);
				newEnd = token.end;
			}
			DocToken newToken = new DocToken(newStart, newEnd, newText
					.toString(), newType, span.weight);
			newToken.code = token.code;
			for (int k = 0; k < span.length; ++k) {
				key.remove(i);
			}
			key.add(i, newToken);
			j++;
			if (j >= spans.size()) {
				return;
			}
		}
	}

	/**
	 * 构造方法，用来定义一些规则
	 */
	public UnknowGrammarDoc() {
		ArrayList<DocSpan> lhs = new ArrayList<DocSpan>();
		ArrayList<DocType> rhs = new ArrayList<DocType>();
		//1
		rhs.add(DocType.Link);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(1, DocType.Other));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//2 
	    lhs = new ArrayList<DocSpan>();
	    rhs = new ArrayList<DocType>();
		rhs.add(DocType.Address);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(1, DocType.Address));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//3
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Prefix);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(1, DocType.Other));	
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//日期1
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		lhs.add(new DocSpan(8, DocType.Date));
		addProduct(rhs, lhs);
		
		//日期2
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Time);
		lhs.add(new DocSpan(7, DocType.Date));
		addProduct(rhs, lhs);
		
		//日期3
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Time);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		lhs.add(new DocSpan(7, DocType.Date));
		addProduct(rhs, lhs);
		
		//日期4
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Time);
		rhs.add(DocType.Time);
		lhs.add(new DocSpan(6, DocType.Date));
		addProduct(rhs, lhs);
		
		//日期5
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		lhs.add(new DocSpan(8, DocType.Date));
		addProduct(rhs, lhs);
		
		//日期6
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		lhs.add(new DocSpan(9, DocType.Date));
		addProduct(rhs, lhs);
		
		//日期7
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		lhs.add(new DocSpan(9, DocType.Date));
		addProduct(rhs, lhs);
		
		//日期8
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		lhs.add(new DocSpan(10, DocType.Date));
		addProduct(rhs, lhs);
		

		//日期9
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Time);
		rhs.add(DocType.Time);
		lhs.add(new DocSpan(2, DocType.Date));
		addProduct(rhs, lhs);
		

		//日期10
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Time);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		lhs.add(new DocSpan(3, DocType.Date));
		addProduct(rhs, lhs);
		

		//日期11
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Time);
		lhs.add(new DocSpan(3, DocType.Date));
		addProduct(rhs, lhs);
		
		//日期12
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		lhs.add(new DocSpan(4, DocType.Date));
		addProduct(rhs, lhs);
		
		//日期13
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		lhs.add(new DocSpan(2, DocType.Date));
		addProduct(rhs, lhs);
		
		//日期14
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		lhs.add(new DocSpan(3, DocType.Date));
		addProduct(rhs, lhs);
		
		//日期15
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		lhs.add(new DocSpan(3, DocType.Date));
		addProduct(rhs, lhs);
		
		//日期16
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		lhs.add(new DocSpan(4, DocType.Date));
		addProduct(rhs, lhs);
		
//		//5
//		lhs = new ArrayList<DocSpan>();
//		rhs = new ArrayList<DocType>();
//		rhs.add(DocType.Unknow);
//		rhs.add(DocType.Time);
//		rhs.add(DocType.Time);
//		lhs.add(new DocSpan(3, DocType.Date));
//		addProduct(rhs, lhs);
//		//6
//		lhs = new ArrayList<DocSpan>();
//		rhs = new ArrayList<DocType>();
//		rhs.add(DocType.Time);
//		rhs.add(DocType.Time);
//		lhs.add(new DocSpan(2, DocType.Date));
//		addProduct(rhs, lhs);
		//7
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Prefix);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Prefix);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(1, DocType.Other));
		lhs.add(new DocSpan(3, DocType.Destination));
		addProduct(rhs, lhs);
		//8
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Prefix);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(1, DocType.Other));
		lhs.add(new DocSpan(3, DocType.Destination));
		addProduct(rhs, lhs);
		//9
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(3, DocType.Destination));
		addProduct(rhs, lhs);
		//10
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Other);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(3, DocType.Other));
		addProduct(rhs, lhs);
		//11
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(2, DocType.Other));
		addProduct(rhs, lhs);
		//12
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Suffix);
		rhs.add(DocType.Other);
		lhs.add(new DocSpan(2, DocType.Other));
		addProduct(rhs, lhs);
		//
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Start);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(1, DocType.Other));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//1
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(8, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//2
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Time);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(7, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//3
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Time);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(7, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//4
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Time);
		rhs.add(DocType.Time);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(6, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//5
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(8, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//6
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(9, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//7
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(9, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//8
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(10, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//9
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Time);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(3, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//10
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Time);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(3, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//11
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Suffix);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(2, DocType.Other));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//12
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Other);
		rhs.add(DocType.Time);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(4, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//13
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(2, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//14
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(3, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//15
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(3, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		//16
		lhs=new ArrayList<DocSpan>();
		rhs=new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Link);
		rhs.add(DocType.Other);
		rhs.add(DocType.Other);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Suffix);
		lhs.add(new DocSpan(4, DocType.Date));
		lhs.add(new DocSpan(2, DocType.Destination));
		addProduct(rhs, lhs);
		
	}

	private static UnknowGrammarDoc dicGrammar = new UnknowGrammarDoc();

	/**
	 * 
	 * @return the singleton of basic dictionary
	 */
	public static UnknowGrammarDoc getInstances() {
		return dicGrammar;
	}

}
