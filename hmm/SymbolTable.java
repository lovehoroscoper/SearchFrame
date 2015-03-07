package hmm;

import java.util.Hashtable;

/*description:�ַ���"moo"�ȵ�hash�洢���洢ֵΪ����Symbol*/
public class SymbolTable {
	/*�����ַ���"moo"�ȣ������Ӧһ��Symbol����*/
	Hashtable<String,Symbol> table;

	public SymbolTable() {
		table = new Hashtable<String,Symbol>();
	}
	/*
	 * @param s �ַ���"moo"��
	 * @return �����ַ���"moo"��,����Symbol����
	 */
	public Symbol intern(String s) {
		s = s.toLowerCase();
		Symbol sym = table.get(s);
		if (sym == null) {
			sym = new Symbol(s);
			table.put(s, sym);
		}
		return sym;
	}
}
