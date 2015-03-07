package unigrambigramSeg;

public class TestBigramMap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		BigramMap bm = new BigramMap(10,0);
		bm.put(19, 9);
		bm.put(18, 8);
		bm.put(16, 7);
		bm.put(17, 6);
		bm.put(1, 16);
		bm.put(2, 12);
		bm.put(3, 13);
		System.out.println(bm);
		System.out.println(bm.id);
		System.out.println(bm.indexOf(88));
		System.out.println(bm.indexOf(2));
		System.out.println(bm.indexOf(19));
		System.out.println(bm.get(19));
		
		for(int i=0; i<bm.prevIds.length; i++) {
			System.out.print(bm.prevIds[i] + "\t");
		}
		
		System.out.println();
		
		for(int i=0; i<bm.freqs.length; i++) {
			System.out.print(bm.freqs[i] + "\t");
		}
		
		System.out.println();
		
		bm.buildArray(bm.prevIds, bm.freqs);	//完全二叉树
		System.out.println(bm.find(2));
		System.out.println(bm.find(3));
		System.out.println(bm.find(19));
	}
}