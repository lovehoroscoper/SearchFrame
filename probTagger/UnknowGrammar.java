package probTagger;

import java.util.ArrayList;

/**
 * δ��¼����ʶ�����
 * 
 * 
 * @2010-3-23
 */
public class UnknowGrammar {

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
		protected PartOfSpeech splitchar;

		/**
		 * Constructor method.
		 * 
		 *@param splitchar
		 *            The char used in the split.
		 */
		protected TSTNode(PartOfSpeech splitchar) {
			this.splitchar = splitchar;
		}

		public String toString() {
			return "splitchar:" + splitchar;
		}
	}

	/** The base node in the trie. */
	public TSTNode root;

	public void addProduct(ArrayList<PartOfSpeech> key,
			ArrayList<DocSpan> lhs) {
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
				// occur2.insert(pi);
				return;
			}
			currentNode = getOrCreateNode(key);
			currentNode.data = lhs;
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

		// System.out.println("enter");
		TSTNode currentNode = root;
		int charIndex = offset;
		while (true) {
			if (currentNode == null) {
				// System.out.println("ret "+ret);
				matchRet.end = ret;
				matchRet.lhs = retPOS;
				return matchRet;
			}
			int charComp = key.get(charIndex).type
					.compareTo(currentNode.splitchar);

			if (charComp == 0) {
				// System.out.println("comp:"+key.get(charIndex).type);
				charIndex++;

				if (currentNode.data != null && charIndex > ret) {
					ret = charIndex;
					retPOS = currentNode.data;
					// System.out.println("ret pos:"+retPOS);
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

	public static void replace(ArrayList<DocToken> key, int offset,
			ArrayList<DocSpan> spans) {
		int j = 0;
		for (int i = offset; i < key.size(); ++i) {
			DocSpan span = spans.get(j);
			DocToken token = key.get(i);
			StringBuilder newText = new StringBuilder();
			int newStart = token.start;
			int newEnd = token.end;
			PartOfSpeech newType = span.type;

			for (int k = 0; k < span.length; ++k) {
				token = key.get(i + k);
				newText.append(token.termText);
				newEnd = token.end;
			}
			DocToken newToken = new DocToken(newStart, newEnd, newText
					.toString(), newType);

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
	 * Returns the node indexed by key, creating that node if it doesn't exist,
	 * and creating any required intermediate nodes if they don't exist.
	 * 
	 *@param key
	 *            A <code>String</code> that indexes the node that is returned.
	 *@return The node object indexed by key. This object is an instance of an
	 *         inner class named <code>TernarySearchTrie.TSTNode</code>.
	 *@exception NullPointerException
	 *                If the key is <code>null</code>.
	 *@exception IllegalArgumentException
	 *                If the key is an empty <code>String</code>.
	 */
	protected TSTNode getOrCreateNode(ArrayList<PartOfSpeech> key)
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

	public static class MatchRet {
		public int end;
		public ArrayList<DocSpan> lhs;

		public MatchRet(int e, ArrayList<DocSpan> d) {
			end = e;
			lhs = d;
		}

		public String toString() {
			return end + ":" + lhs;
		}
	}

	private UnknowGrammar() {
		// 1
		/*ArrayList<DocSpan> lhs = new ArrayList<DocSpan>();
		ArrayList<PartOfSpeech> rhs = new ArrayList<PartOfSpeech>(); // right-hand

		// ==============================================������ʡ�Ĳ���
		// ������
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Municipality);
		rhs.add(PartOfSpeech.SuffixCity);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(2, PartOfSpeech.Municipality));

		addProduct(rhs, lhs);

		// �����г������߱�����߱���
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Municipality);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.End);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Municipality));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(1, PartOfSpeech.End));

		addProduct(rhs, lhs);
		
		// �й������Ͼ��н���ʡ
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.SuffixProvince);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(2, PartOfSpeech.Province));

		addProduct(rhs, lhs);
		// �й����ս����н���ʡ
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.SuffixProvince);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(2, PartOfSpeech.Province));

		addProduct(rhs, lhs);
		// �й���������������������ʡ
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.SuffixProvince);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(2, PartOfSpeech.Province));

		addProduct(rhs, lhs);
		// �й���������������������ʡ
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixProvince);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(2, PartOfSpeech.Province));

		addProduct(rhs, lhs);
		// ����֣���к���ʡ
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.SuffixIndicationFacility);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixProvince);
		lhs.add(new DocSpan(3, PartOfSpeech.Province));

		addProduct(rhs, lhs);

		// ����֣���к���ʡ
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.SuffixProvince);
		lhs.add(new DocSpan(2, PartOfSpeech.Province));

		addProduct(rhs, lhs);

		// =============================�������в���
		
		// �й������Ͼ���
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCity);
		lhs.add(new DocSpan(2, PartOfSpeech.City));
		

		addProduct(rhs, lhs);
		// �й������Ͼ���
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCity);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.City));
		

		addProduct(rhs, lhs);

		// �й������Ͼ����Ͼ���
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCity);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(2, PartOfSpeech.City));

		addProduct(rhs, lhs);
		// �й������Ͼ�����ϼ���Ͼ���
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCity);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(2, PartOfSpeech.City));

		addProduct(rhs, lhs);
		// �㶫ʡ��ݸ����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCity);
		rhs.add(PartOfSpeech.SuffixCity);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.Other));

		addProduct(rhs, lhs);

		// �㶫ʡ��ݸ����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Other);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Other));
		lhs.add(new DocSpan(3, PartOfSpeech.Street));

		addProduct(rhs, lhs);

		// ����ʡ֣����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCity);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.City));

		addProduct(rhs, lhs);

		// =================================��������������
		// �����г�����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.County));

		addProduct(rhs, lhs);

		// �����д�����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.County));

		addProduct(rhs, lhs);
		//		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// �й����ճ����н���ʡ�����������
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.SuffixProvince);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(2, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// �й������綫��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);

		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// �й� ���� �����л�ɽ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// �㶫ʡ�����а�����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// ������
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// ����������
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Other);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Other));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// �й����պ����غ�����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// �й����ճ������±������ճ������±���
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// �й����������л�ɽ�������л�ɽ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// ��ԭ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(3, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// ����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(3, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// ������
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.SuffixStreet);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// ������֣����̨��Ͷ��������·�ϲ�
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixCounty);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// ��ˮ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// ����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.SuffixCity);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// �й� ���� �Ͼ��� �껨̨���������Ǻӹ�ҵ԰8��
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// ������
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// ========================����������
		// ʯ����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixBuildingNo);
		rhs.add(PartOfSpeech.SuffixTown);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(3, PartOfSpeech.Town));
		addProduct(rhs, lhs);

		// ��ƽ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixTown);
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		addProduct(rhs, lhs);

		// =================================�����ǽֵ��ŵ�

		// ��ɽ���ÿ�����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixDistrict);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.District));
		addProduct(rhs, lhs);
		// ����������
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixDistrict);
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);

		// ������
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		//		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 2��¥
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.SuffixBuildingUnit);
		lhs.add(new DocSpan(2, PartOfSpeech.BuildingUnit));
		addProduct(rhs, lhs);
		// ��·
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.SuffixStreet));
		addProduct(rhs, lhs);
		// ��·
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.SuffixStreet));
		addProduct(rhs, lhs);
		// ѧԺ·
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// ����ҽԺ
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// ̨�Ǵ���
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// // �ķ����
		// lhs = new ArrayList<AddressSpan>();
		// rhs = new ArrayList<AddressType>(); // right-hand .
		//		
		// rhs.add(AddressType.County);
		// rhs.add(AddressType.SuffixLandMark);
		// lhs.add(new AddressSpan(2, AddressType.LandMark));
		// addProduct(rhs, lhs);
		// �幫�ﴦ
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.SuffixIndicationPosition);
		lhs.add(new DocSpan(2, PartOfSpeech.IndicationPosition));
		addProduct(rhs, lhs);
		// �Ĵ�
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.SuffixBuildingNo);
		lhs.add(new DocSpan(2, PartOfSpeech.BuildingNo));
		addProduct(rhs, lhs);

		// distract
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.DetailDesc);
		rhs.add(PartOfSpeech.SuffixDistrict);
		lhs.add(new DocSpan(3, PartOfSpeech.District));
		addProduct(rhs, lhs);

		// �����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.SuffixTown);
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		addProduct(rhs, lhs);

		// �㶫ʡ��ݸ�г�����107���������Ƶ�б����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);

		// ����ʡ�Ͼ����½ֿں��䱱·��ʯ��24��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(3, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// ��ݸ�к�����º�ɳ·������·��ֱ���100��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// �������山����·�����˽�84�ź���һ֧·����С������
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// ����ʡ�Ͼ��иߴ��ؿ�������ó��998��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixDistrict);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixCounty);
		rhs.add(PartOfSpeech.No);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.SuffixDistrict));
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		lhs.add(new DocSpan(2, PartOfSpeech.No));
		addProduct(rhs, lhs);
		// �㶫ʡ��ݸ�к����Ҿߴ�����ʼҾߴ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// ����ʡ�Ͼ��н����������򴾻���ί��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.End);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(3, PartOfSpeech.DetailDesc));
		lhs.add(new DocSpan(1, PartOfSpeech.End));
		addProduct(rhs, lhs);

		// ������������������������(���˱����ſ�)
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.StartSuffix);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(2, PartOfSpeech.RelatedPos));
		lhs.add(new DocSpan(1, PartOfSpeech.StartSuffix));
		lhs.add(new DocSpan(2, PartOfSpeech.DetailDesc));
		addProduct(rhs, lhs);

		// ���������������������»����մ����ϲ�(119��ѧ����)
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.LandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.District));
		lhs.add(new DocSpan(1, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);

		// ɳ����̫¡��ҵ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.District);
		lhs.add(new DocSpan(3, PartOfSpeech.District));
		addProduct(rhs, lhs);

		// ������
		// lhs = new ArrayList<AddressSpan>();
		// rhs = new ArrayList<AddressType>(); // right-hand .
		//
		// rhs.add(AddressType.RelatedPos);
		// rhs.add(AddressType.District);
		// lhs.add(new AddressSpan(2, AddressType.County));
		// addProduct(rhs, lhs);

		// ������
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// ����ɽ��ҵ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.SuffixDistrict);
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);

		// �����´�
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixVillage);
		lhs.add(new DocSpan(2, PartOfSpeech.Village));
		addProduct(rhs, lhs);

		// �ڶ���ҵ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.SuffixDistrict);
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);

		// ��԰�´�
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.SuffixVillage);
		lhs.add(new DocSpan(2, PartOfSpeech.Village));
		addProduct(rhs, lhs);

		// �����г�����ϼ����66��Զ���¸���A��908��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Symbol);
		lhs.add(new DocSpan(1, PartOfSpeech.No));
		lhs.add(new DocSpan(3, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(1, PartOfSpeech.Symbol));
		addProduct(rhs, lhs);
		// �껨̨��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.County);
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// ��Ԣ����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Village);
		lhs.add(new DocSpan(2, PartOfSpeech.Village));
		addProduct(rhs, lhs);

		// �ۿ�·
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.SuffixDistrict);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// //�·���·
		// lhs = new ArrayList<AddressSpan>();
		// rhs = new ArrayList<AddressType>(); // right-hand .
		//
		// rhs.add(AddressType.Unknow);
		// rhs.add(AddressType.RelatedPos);
		// lhs.add(new AddressSpan(2, AddressType.Street));
		// addProduct(rhs, lhs);

		// ѧǰ·
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		//
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// �������й�ƽ·�������ƺ�·39��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.SuffixCounty);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// �㶫ʡ��ݸ��������ɽ��·��ֶ���9��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(3, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// ��ݸ�к�������ɽ·576��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// �������������ɳ���ͬ7��Ժ(�������ŵ���A��)
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// ��������������ƽ�ﶫ��С���ű�(������������)
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.RelatedPos);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(1, PartOfSpeech.RelatedPos));
		addProduct(rhs, lhs);

		// �㶫ʡ�����а�������԰��·��ֱ̩�ֶ�2��2������Ӣ�����903
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.RelatedPos));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// �㶫ʡ�����дӻ���̫ƽ��̫ƽ���ü���������
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.District);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);

		// �㶫ʡ�����з�خ����ʯ������Ǹڴ��3��10��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.Village));
		lhs.add(new DocSpan(3, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// �������������ĵ���·1��(��ѧԺ��·)
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.StartSuffix);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.District));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.No));
		lhs.add(new DocSpan(1, PartOfSpeech.StartSuffix));
		lhs.add(new DocSpan(1, PartOfSpeech.RelatedPos));
		lhs.add(new DocSpan(1, PartOfSpeech.SuffixLandMark));
		lhs.add(new DocSpan(1, PartOfSpeech.IndicationPosition));
		addProduct(rhs, lhs);
		// ����������Ӫ��·88��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// ����բ�ڴ嶫ݸ�绯���Ž�����ҵ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.SuffixStreet);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Village);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Village));
		addProduct(rhs, lhs);
		// ����բ�ڴ嶫ݸ�绯���Ž�����ҵ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.District);
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Village));
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);
		// ����ʡ�Ͼ��иߴ��ش�Ϫ������·288��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// ��������Ϫ�س�������Ȫ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.Town);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		addProduct(rhs, lhs);
		// ������������̴Ӫ�����
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixCounty);
		rhs.add(PartOfSpeech.Town);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		addProduct(rhs, lhs);
		// ������������˫����ʯ�ݿ���
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCity);
		rhs.add(PartOfSpeech.Town);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		addProduct(rhs, lhs);

		// �����кϴ����к�����������97��
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixTown);
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		addProduct(rhs, lhs);

		// ����ʡ�Ͼ�����ˮ�ش��Ž�29��3¥
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixBuildingUnit);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(3, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// ����ʡ֣���лݼ���������������·2��
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixDistrict);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(3, PartOfSpeech.District));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// �山��������԰��ʳ��
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// �����з�ɽ��������򺫴�Ӵ�
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.SuffixIndicationFacility);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.Village);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(3, PartOfSpeech.Town));
		lhs.add(new DocSpan(2, PartOfSpeech.Village));
		addProduct(rhs, lhs);
		// �����з�ɽ����������ȼҷش�
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.SuffixIndicationFacility);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Village);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(3, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Village));
		addProduct(rhs, lhs);
		// �����к�������ׯ����3��¥
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.BuildingUnit);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(3, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(1, PartOfSpeech.BuildingUnit));
		addProduct(rhs, lhs);
		// ����բ�ڴ嶫ݸ�绯���Ž�����ҵ��
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.District);
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Village));
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);
		// ��¥���ݳ��Ŵ������㳡�����������ڵ�ͼ
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.LandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);

		// �㶫ʡ��ݸ��������ɽ��·��ֶ���9��
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.District));
		lhs.add(new DocSpan(3, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// �㶫ʡ�����а���������·��������17��
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// �㶫ʡ�����к�������ҵ����Ͻ��һ��29��
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.RelatedPos));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// �㶫ʡ�����к�����̩������ɹ��2��13B
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixVillage);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Village));
		addProduct(rhs, lhs);
		// �㶫ʡ�����������������·3�ŵ۾�ԷC��14E��
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// ������ѧԺ·���ⱱ��8��
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(3, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);

		// ���������������꽭����·
		// 2010.5.25
		// lhs = new ArrayList<AddressSpan>();
		// rhs = new ArrayList<AddressType>(); // right-hand .
		// rhs.add(AddressType.Province);
		// rhs.add(AddressType.County);
		// rhs.add(AddressType.County);
		// rhs.add(AddressType.Street);
		// lhs.add(new AddressSpan(1, AddressType.Province));
		// lhs.add(new AddressSpan(1, AddressType.County));
		// lhs.add(new AddressSpan(2, AddressType.Street));
		// addProduct(rhs, lhs);
		// ������ɳƺ����������ٽ�װ�γ�14-5��
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.LandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.District));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// �й� ���� �����б����� ����ǰ����Ϫ�����ϣ����˲ִ����޹�˾��
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.StartSuffix);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.LandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.StartSuffix));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// �й� ���� �����б����� ���˹�ҵ��
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixDistrict);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);
		// �й� ���� ������������ �⽭��ʢ��ͷ�����D��16��
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.LandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// ��ݸ�ж��Ǵ�����д���2¥
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixStreet);
		rhs.add(PartOfSpeech.LandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// ����ʡ�Ͼ����������Ͼ���ɽ��·301��
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.District));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// ����֣���к���ʡ֣�����Ϲؽ����ֶ���38��
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(3, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// �㶫ʡ��ݸ�д���ɽ����ƽ�¸����
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.End);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(3, PartOfSpeech.Village));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(1, PartOfSpeech.End));
		addProduct(rhs, lhs);

		// ��ݸ�ж�������԰�´��г�·20��
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixCounty);
		rhs.add(PartOfSpeech.Village);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Village));
		addProduct(rhs, lhs);
		// �����з�̨���Ұ�����������26��¥1��Ԫ301��
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.BuildingUnit);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.District));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(1, PartOfSpeech.BuildingUnit));
		addProduct(rhs, lhs);

		// �����������ع�ҵ������
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixCounty);
		rhs.add(PartOfSpeech.SuffixDistrict);
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.SuffixDistrict));
		addProduct(rhs, lhs);
		// �������������������̴��
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Village);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Village));
		addProduct(rhs, lhs);
		// ������������·�ٻ��Ŷ�(����ׯ)
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.StartSuffix);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.Town);
		lhs.add(new DocSpan(1, PartOfSpeech.StartSuffix));
		lhs.add(new DocSpan(1, PartOfSpeech.RelatedPos));
		lhs.add(new DocSpan(1, PartOfSpeech.DetailDesc));
		addProduct(rhs, lhs);
		// ������ѧԺ��·68�ż�������C������¥111��
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.No));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// �й� ���� ������ �������Ǵ��49-51��
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCity);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// ������վ������������
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.LandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// ����ʡ֣���йܳ��������ﱤ������103��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.Village);

		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.RelatedPos));
		lhs.add(new DocSpan(3, PartOfSpeech.Village));
		addProduct(rhs, lhs);
		// ��¥����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.SuffixStreet);

		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// �ӻ���
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCity);
		rhs.add(PartOfSpeech.Street);

		lhs.add(new DocSpan(2, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// ������վ
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixIndicationFacility);

		lhs.add(new DocSpan(2, PartOfSpeech.SuffixIndicationFacility));
		addProduct(rhs, lhs);
		
		// ��վ
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixIndicationFacility);

		lhs.add(new DocSpan(2, PartOfSpeech.SuffixIndicationFacility));
		addProduct(rhs, lhs);

		// ����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixBuildingUnit);

		lhs.add(new DocSpan(2, PartOfSpeech.SuffixBuildingUnit));
		addProduct(rhs, lhs);
		

		//�Ƽ���ѧ����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.SuffixBuildingUnit);

		lhs.add(new DocSpan(2, PartOfSpeech.BuildingUnit));
		addProduct(rhs, lhs);
		// һ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.SuffixLandMark);

		lhs.add(new DocSpan(2, PartOfSpeech.SuffixLandMark));
		addProduct(rhs, lhs);
		// ����
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixLandMark);

		lhs.add(new DocSpan(2, PartOfSpeech.SuffixLandMark));
		addProduct(rhs, lhs);

		//�컪԰ һ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.SuffixLandMark);

		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		//����̫��������
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.SuffixDistrict);

		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);
		//�����ж������Ϻ��ش�ֻ����ֶ���c��һ��
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);

		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(3, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		
		//11-A
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.Symbol);

		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(2, PartOfSpeech.No));
		addProduct(rhs, lhs);*/

	}

	private static UnknowGrammar dicGrammar = new UnknowGrammar();

	/**
	 * 
	 * @return the singleton of basic dictionary
	 */
	public static UnknowGrammar getInstance() {
		return dicGrammar;
	}
}
