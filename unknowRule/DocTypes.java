package unknowRule;

import java.util.Iterator;

public class DocTypes implements Iterable<DocTypeInf>{
	/**
	 * �ڵ���
	 * 
	 *
	 */
	public static class Node
    {
		/**
		 * ������
		 */
		public DocTypeInf item;
		/**
		 * ��һ�����
		 */
		public Node next;
		/**
		 * ���캯��
		 * @param item PoiTypeInf����
		 */
        Node(DocTypeInf item)
          { this.item = item; next = null; }
    }
    
    private Node head, tail;
	
    /**
     * ���췽��
     */
    public DocTypes()
    {
    	head = null;
    	tail = null; 
    }
    
    /**
     * ��������β���һ�����
     * @param item
     */
    public void put(DocTypeInf item)
    {
    	//System.out.println(item.toString());
    	Node t = tail; tail = new Node(item);
        if (head == null) head = tail; else t.next = tail;
    }
    
    /**
     * ������������һ����㣬�Ѹ�������������ǰ����PoiType��СΪ����
     * @param item Ҫ��ӵĽ��
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
     * ��������ͷ���
     * @return   �����ͷ���
     */
	public Node getHead()
	{
		return head;
	}

    /**
     * @return int ��ǰ�����ж���Ԫ��
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
     * ���ش�����ĵ�����
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
     * ���ǵ�toString()����
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
