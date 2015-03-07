package unknowRule;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
     *������ĵ�������
     * 
     * @2010-3-18
 */
public class DocTokenLinkedList {
	/**
	 * �����
	 
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
     * ��������β���һ��Ԫ��
     * @param item Ҫ��ӵĽ��
     */
    public void put(DocTokenInf item)
    {
    	Node n = new Node(item);
		n.next = head;
		head = n;
    }
    
    /**
     * ��ô������ͷ���
     * @return			�������ͷ���
     */
	public Node getHead()
	{
		return head;
	}
	
	/**
	 * ��ô�����Ĵ�С
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
     * ��ô�����ĵ�����
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
