package unknowRule;

import java.util.Iterator;

public class DocDicTypes {
	/** 属性 */
	private Node head, tail;

	/**
	 * 结点类
	 * 
	 *
	 * 
	 */
	public static class Node {
		/**
		 * 数据项
		 */
		public PoiTypeInf1 item;
		/**
		 * 下一个结点
		 */
		public Node next;

		/**
		 * 构造器
		 * 
		 * @param item
		 */
		Node(PoiTypeInf1 item) {
			this.item = item;
			next = null;
		}
	}

	/**
	 * poiType信息类，其中有PoiType表示类型，weight表示频率，code表示语义编码，即行政区划码
	 * 
	
	 * 
	 */
	public static class PoiTypeInf1 {
		public DocType pos; // 类型
		public int weight = 0; // 频率
		public long code; // 语义编码

		public PoiTypeInf1(DocType p, int f, long semanticCode) {
			
			pos = p;
			weight = f;
			code = semanticCode;
		}

		public String toString() {
			return "from PoiDicTypes.PoiTypeInf...toString() " + pos + ":"
					+ weight;
		}
		public DocType getPOIType1(){
			return pos;
		}
	}

	/**
	 * 构造方法
	 */
	public DocDicTypes() {
		head = null;
		tail = null;
	}

	/**
	 * 向链表结尾插入一个元素
	 * 
	 * @param item
	 */
	public void put(PoiTypeInf1 item) {
		Node t = tail;
		tail = new Node(item);
		if (head == null)
			head = tail;
		else
			t.next = tail;
	}

	/**
	 * 向链表中插入一个元素
	 * 
	 * @param item
	 */
	public void insert(PoiTypeInf1 item) {
		// one element
		if (head == tail) {
			if (head.item.pos.compareTo(item.pos) > 0) {
				Node t = head;
				head = new Node(item);
				head.next = t;
			} else {
				Node t = tail;
				tail = new Node(item);
				t.next = tail;
			}
			return;
		}
		Node t = head;

		while (t.next != null) {
			if (t.next.item.pos.compareTo(item.pos) > 0) {
				break;
			}
			t = t.next;
		}
		Node p = t.next;
		t.next = new Node(item);
		t.next.next = p;
	}

	/**
	 * 获得头
	 * 
	 * @return 此链表的头结点
	 */
	public Node getHead() {
		return head;
	}

	public PoiTypeInf1 findType(DocType toFind) {
		if (head == null)
			return null;
		Node t = head;
		while (t != null && t.item != null) {
			if (t.item.pos.equals(toFind)) {
				return t.item;
			}
			t = t.next;
		}

		return null;
	}

	/**
	 * @return int 返回链表的大小
	 */
	public int size() {
		int count = 0;

		Node t = head;
		while (t != null) {
			count++;
			t = t.next;
		}

		return count;
	}

	public int totalCost() {
		int cost = 0;

		Node t = head;
		while (t != null) {
			cost += t.item.weight;
			t = t.next;
		}

		return cost;
	}

	public Iterator<DocDicTypes.PoiTypeInf1> iterator() {
		return new LinkIterator(head);
	}

	/** Adapter to provide descending iterators via ListItr.previous */
	private class LinkIterator implements Iterator<DocDicTypes.PoiTypeInf1> {
		Node itr;

		public LinkIterator(Node begin) {
			itr = begin;
		}

		public boolean hasNext() {
			return itr != null;
		}

		public PoiTypeInf1 next() {
			Node cur = itr;
			itr = itr.next;
			return cur.item;
		}

		public void remove() {

		}
	}

	/**
	 * 覆盖的方法
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("from PoiDicTypes...toString() ");
		Node pCur = head;

		while (pCur != null) {
			buf.append(pCur.item.toString());
			buf.append(' ');
			pCur = pCur.next;
		}

		return buf.toString();
	}
}
