package hmm;

import java.util.Vector;

public class SymbolList {
	Vector<Symbol> list;

	public SymbolList() {
		list = new Vector<Symbol>();
	}

	public int size() {
		return list.size();
	}
	/*��Symbol����洢�ڸ����±�ֵ���б���
	 * @param index ���б��±�ֵ�� sym Symbol����
	 * @return no return value
	 */
	public void set(int index, Symbol sym) {
		list.setElementAt(sym, index);
	}
	/*��Symbol����洢���б���
	 * @param  sym Symbol����
	 * @return no return value
	 */
	public void add(Symbol sym) {
		list.addElement(sym);
	}

	/*�ڸ����±�ֵ���б��У�����Symbol����
	 * @param  index ���б��±�ֵ
	 * @return Symbol����
	 */
	public Symbol get(int index) {
		return (Symbol) list.elementAt(index);
	}

}
