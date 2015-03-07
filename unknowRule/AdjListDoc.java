package unknowRule;

import java.util.Iterator;

/**
 * 
     *邻接表表示的图
     * 
     * @2010-3-18
 */
public class AdjListDoc {
	// AdjList的图的结构
	private DocTokenLinkedList [] list;
	
	/**
	 * 顶点的数量
	 */
	public int verticesNum ;
	
	/**
	 * Constructor: Allocates right amount of space
	 */
	public AdjListDoc (int verticesNum){
		
		this.verticesNum = verticesNum;
		
		list = new DocTokenLinkedList [verticesNum];
		
		// initialize all of the linked lists in the array
		for(int i=0; i<list.length; i++){
		
			list[i] = new DocTokenLinkedList();
		
		}
		
		DocTokenInf startPtInf = new DocTokenInf(-1,0,"");
		
		DocTypes pt = new DocTypes();
		
		DocTypeInf inf = new DocTypeInf(DocType.Start, 10, 0, startPtInf);
		
		pt.put(inf);
		
		startPtInf.setData(pt);
		
		addEdge(startPtInf);
	}

	/**
	 * add an edge to the graph
	 */
	public void addEdge(DocTokenInf newEdge) {
		
		list[newEdge.end].put(newEdge);	
		
	}
	
	/**
	 * Returns an iterator that contains the Edges leading to adjacent nodes
	 */
	public Iterator<DocTokenInf> getAdjacencies(int vertex){
		
		DocTokenLinkedList ll = list[vertex];
		
		if(ll == null)
		
			return null;
		
		return ll.iterator();
	}
	
	/**
	 * Return the cost of two nodes
	 */
	public double getCost(int from ,int to){
		
		if(from == to){
		
			return 0.0;
		
		}
		DocTokenLinkedList ptll = list[to];
		
		if(ptll != null ){
		
			Iterator<DocTokenInf> it = ptll.iterator();
			
			while (it.hasNext() ){
			
				DocTokenInf pt = it.next();
				
				if(pt.start == from){
				
					return pt.cost;
				
				}
			}
		}
		
		return Double.POSITIVE_INFINITY;
	}
	
	public String toString(){
		
		String temp = "";
		
		for(int i=0; i<this.verticesNum;i++){
		
			temp += "node:"+i+": "+list[i].toString();
			
			Iterator<DocTokenInf> it = list[i].iterator();
			
			if(! it.hasNext()){
			
				System.out.println("node:"+i);
			
			}
			
			while (it.hasNext()){
			
				DocTokenInf pt = it.next();
				
				temp += pt.toString();
			}
			
			temp +="\n";
		}
		
		return temp ;
	}
}
