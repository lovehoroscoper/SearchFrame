package unknowRule;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
     *单词类的单链表类
     * 
     * @2010-3-18
 */
public class DocTokenLinkedList {
	/**
	 * 结点类
	 
	 *
	 */
	public static class Node
    {
		public DocTokenInf item; 
		public Node next;
        Node(DocTokenInf item) { 
        	this.item = item; 
        	next = null; 
        }
    }
    
    private Node head;

    public DocTokenLinkedList()
    {
    	head = null;
    }
    
    /**
     * 向此链表结尾添加一个元素
     * @param item 要添加的结点
     */
    public void put(DocTokenInf item)
    {
    	Node n = new Node(item);
		n.next = head;
		head = n;
    }
    
    /**
     * 获得此链表的头结点
     * @return			此链表的头结点
     */
	public Node getHead()
	{
		return head;
	}
	
	/**
	 * 获得此链表的大小
	 * @return int
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
    
    /**
     * 获得此链表的迭代器
     */
    public Iterator<DocTokenInf> iterator() {
        return new LinkIterator(head);
    }

    private class LinkIterator implements Iterator<DocTokenInf> {
        Node itr;
    	public LinkIterator(Node begin)
    	{
    		itr = begin;
    	}
    	
        public boolean hasNext() {
            return itr!=null;
        }
        
        public DocTokenInf next() {
        	if(itr == null)
        	{
        		throw new NoSuchElementException();
        	}
        	Node cur = itr;
        	itr = itr.next;
            return cur.item;
        }
        
        public void remove() {
        }
    }
    
    public String toString()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("from PoiTokenLinkedList...toString() ");
		Node pCur=head;
		
		while(pCur!=null)
		{
			buf.append(pCur.item.toString());
			buf.append('\t');
			pCur=pCur.next;
		}

		return buf.toString();
	}
}
