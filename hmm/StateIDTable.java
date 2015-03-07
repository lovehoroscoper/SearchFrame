package hmm;

import java.util.Hashtable;

/*description:����State��hash�洢���洢ֵΪInteger����State����ű���*/
public class StateIDTable {
	Hashtable<State,Integer> table;

	public StateIDTable() {
		table = new Hashtable<State,Integer>();
	}

	public void put(State obj, int i) {
		table.put(obj, new Integer(i));
	}

	public int get(State obj) {
		Integer i = (Integer) table.get(obj);
		if (i == null) {
			return 0;
		}
		return i.intValue();
	}
}
