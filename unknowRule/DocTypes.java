package unknowRule;

import java.util.Iterator;

public class DocTypes implements Iterable<DocTypeInf>{
	/**
	 * 节点类
	 * 
	 *
	 */
	public static class Node
    {
		/**
		 * 数据项
		 */
		public DocTypeInf item;
		/**
		 * 下一个结点
		 */
		public Node next;
		/**
		 * 构造函数
		 * @param item PoiTypeInf类型
		 */
        Node(DocTypeInf item)
          { this.item = item; next = null; }
    }
    
    private Node head, tail;
	
    /**
     * 构造方法
     */
    public DocTypes()
    {
    	head = null;
    	tail = null; 
    }
    
    /**
     * 向此链表结尾添加一个结点
     * @param item
     */
    public void put(DocTypeInf item)
    {
    	//System.out.println(item.toString());
    	Node t = tail; tail = new Node(item);
        if (head == null) head = tail; else t.next = tail;
    }
    
    /**
     * 向此链表中添加一个结点，已给定参数和链表当前结点的PoiType大小为依据
     * @param item 要添加的结点
     */
    void insert(DocTypeInf item)
    {
    	//one element
    	if (head == tail)
    	{
    		if(head.item.pos.compareTo(item.pos)>0)
    		{
    	    	Node t = head;
    	    	head = new Node(item);
    	        head.next = t;
    	    }
    		else
    		{
    	    	Node t = tail;
    	    	tail = new Node(item);
    	        t.next = tail;
    	     }
    		return;
		}
    	Node t = head;
    	
    	while(t.next!= null)
    	{
    		if (t.next.item.pos.compareTo(item.pos)>0)
    			break;
	    	t = t.next;
    	}
    	Node p = t.next;
    	t.next = new Node(item);
    	t.next.next = p;
    }
    
    /**
     * 获得链表的头结点
     * @return   链表的头结点
     */
	public Node getHead()
	{
		return head;
	}

    /**
     * @return int 当前链表有多少元素
     */
    public int size()
    {
    	int count=0;
    	
    	Node t = head;
    	while(t!= null)
    	{
    		count++;
	    	t = t.next;
    	}
    	
    	return count;
    }

    public int totalCost()
    {
    	int cost=0;
    	
    	Node t = head;
    	while(t!= null)
    	{
    		cost+=t.item.weight;
	    	t = t.next;
    	}
    	
    	return cost;
    }
    
    /**
     * 返回此链表的迭代器
     */
    public Iterator<DocTypeInf> iterator() {
        return new LinkIterator(head);
    }

    /** Adapter to provide descending iterators via ListItr.previous */
    private class LinkIterator implements Iterator<DocTypeInf> {
        Node itr;
    	public LinkIterator(Node begin)
    	{
    		itr = begin;
    	}
    	
        public boolean hasNext() {
            return itr!=null;
        }
        
        public DocTypeInf next() {
        	Node cur = itr;
        	itr = itr.next;
            return cur.item;
        }
        
        public void remove() {
        }
    }
    
    /**
     * 覆盖的toString()方法
     */
    public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("from PoiTypes...toString() ");
		Node pCur=head;
		
		while(pCur!=null)
		{
			buf.append(pCur.item.toString());
			buf.append(' ');
			pCur=pCur.next;
		}

		return buf.toString();
	}
}
