package hmm;

import java.util.Hashtable;

/*description:�ַ���"cow"����״̬��hash�洢���洢ֵΪ����State*/
public class StateTable {
	Hashtable<String,State> table;

	public StateTable() {
		table = new Hashtable<String,State>();
	}

	public State get(String s) {
		s = s.toUpperCase();
		State st = table.get(s);
		if (st == null) {
			st = new State(s);
			table.put(s, st);
		}
		return st;
	}
}
