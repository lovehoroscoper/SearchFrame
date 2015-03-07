package hmm;

import java.util.HashMap;

/*description:���ܵ�״̬����cow,duck����start��end*/
public class State {
	public String name;
	HashMap<Symbol,Double> emits;//������ʣ���ProbTable��ʽ�洢
	HashMap<State,Double> linksto;//ת�Ƹ��ʣ���ProbTable��ʽ�洢

	public State(String s) {
		name = s;
		emits = new HashMap<Symbol,Double>();
		linksto = new HashMap<State,Double>();
	}

	public void addSymbol(Symbol sym, double prob) {
		emits.put(sym, prob);
	}

	public double emitprob(Symbol sym) {
		Double val = emits.get(sym);
		if(val==null){
			return 0;
		}
		return val.doubleValue();
	}

	public void addLink(State st, double prob) {
		linksto.put(st, (Double)prob);
	}

	public double transprob(State st) {
		Double val = linksto.get(st);
		if(val==null){
			return 0;
		}
		return val.doubleValue();
	}

}
