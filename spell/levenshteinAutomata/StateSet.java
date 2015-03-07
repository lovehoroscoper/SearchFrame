package spell.levenshteinAutomata;

import java.util.BitSet;

public class StateSet {
	public BitSet table; // λ����

	/**
	 * ����һ��״̬����
	 * 
	 * @param n
	 *            ��ȷ�ַ���
	 * @param e
	 *            �����ַ���
	 */
	public StateSet(int n, int e) {
		table = new BitSet((n + 1) * (e + 1));
	}

	/**
	 * ״̬�����Ƿ����ĳ��״̬
	 * @param o ״̬���
	 * @return
	 */
	public boolean contains(int o) {
		return table.get(o);
	}

	/**
	 * ����״̬�����Ƿ���ڽ���
	 * @param s Ҫ�жϵ�����һ��״̬����
	 * @return
	 */
	public boolean containsAny(StateSet s) {
		for (int state = s.table.nextSetBit(0); state >= 0; state = s.table
				.nextSetBit(state + 1)) {
			if (table.get(state))
				return true;

		}
		return false;
	}

	/**
	 * ����һ��״̬��״̬����
	 * @param s
	 */
	public void add(int s) {
		table.set(s);
	}

	/**
	 * ����һ��״̬�����е�����״̬����ǰ״̬����
	 * @param s
	 */
	public void add(StateSet s) {
		for (int state = s.table.nextSetBit(0); state >= 0; state = s.table
				.nextSetBit(state + 1)) {
			table.set(state);
		}
	}

	@Override
	public String toString() { 
		StringBuilder sb = new StringBuilder();
		
		//�����ǰ״̬�����а�����״̬���
		for (int state = table.nextSetBit(0); state >= 0; state = table
				.nextSetBit(state + 1)) {
			sb.append(state + "\t");
		}
		return sb.toString();
	}
}
