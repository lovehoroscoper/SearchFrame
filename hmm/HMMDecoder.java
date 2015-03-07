package hmm;

import java.util.ArrayList;

/*description:HMM�Ľ�����*/
public class HMMDecoder {
	ArrayList<State> states;// ���п��ܵ�״̬��{start��cow��duck��end}

	public ArrayList<Node>[] al;// ϡ��Ķ�ά����ڵ� ����̬����ά��ʾ��״̬���кţ�
	// ��̬����ά��ʾ��״̬��Node�ڵ�
	public ArrayList<Symbol> symbols;// ��״̬����

	public HMMDecoder() {
		states = new ArrayList<State>();
	}

	public void addStartState(State st) {
		states.add(st);
	}

	public void addNormalState(State st) {
		states.add(st);
	}

	public void addEndState(State st) {
		states.add(st);
	}

	// for debugging.
	public void show() {
		for (int i = 0; i < al.length; i++) {
			for (Node node : al[i]) {
				System.out.print(node.tag.name + " " + node.prob + " ");
			}
			System.out.println();
		}
	}

	// initialize for decoding
	public void initialize(ArrayList<Symbol> syms) {
		symbols = syms;
		// ��ʼ����ά����
		al = new ArrayList[symbols.size()];
		for (int i = 0; i < symbols.size(); i++) {
			al[i] = new ArrayList<Node>(states.size());
		}
		// ��ӳ�ʼ�ڵ㣬����start������ʲ�Ϊ��Ľڵ���ӵ�al[0]��
		for (int j = 0; j < states.size(); j++) {
			double emitProb = states.get(j).emitprob(symbols.get(0));
			if (emitProb > 0) {
				System.out.println(states.get(j).name + " emit:"
						+ symbols.get(0).name + " " + emitProb);
				Node tmNode = new Node(states.get(j));
				tmNode.prob = emitProb;
				tmNode.bestPrev = null;
				al[0].add(tmNode);
			}
		}
		// ���Ի������ڵ�
		for (int i = 1; i < symbols.size(); i++) {
			for (int j = 0; j < states.size(); j++) {
				double emitProb = states.get(j).emitprob(symbols.get(i));
				if (emitProb > 0) {
					System.out.println(states.get(j).name + " emit:"
							+ symbols.get(i).name + " " + emitProb);
					Node tmNode = new Node(states.get(j));
					tmNode.prob = 0.0;
					tmNode.bestPrev = new Node(states.get(0));
					al[i].add(tmNode);
				}
			}
		}
	}

	// ǰ���ۻ�����
	public void proceed_decoding() {
		int stage, i0, i1;
		Symbol sym1;
		State s0;
		State s1;
		double prob = 0.0;
		double emitprob;
		show();

		// stage��ʾ��״̬��ţ�����start״̬
		for (stage = 1; stage < al.length; stage++) {
			// i1��ʾ��״̬���stage��Ӧ����״̬�ڵ�ά�����
			for (i1 = 0; i1 < al[stage].size(); i1++) {
				// i0��ʾi1��һ��״̬ά����״̬�ڵ���
				for (i0 = 0; i0 < al[stage - 1].size(); i0++) {
					System.out.println("i0:" + i0 + " i1:" + i1 + " stage:"
							+ stage);
					sym1 = symbols.get(stage);
					// ��һ��״̬ά����״̬�ڵ�
					Node prevNode = al[stage - 1].get(i0);
					// ��ǰ��״̬ά����״̬�ڵ�
					Node nextNode = al[stage].get(i1);
					s0 = prevNode.tag;// ��һ��״̬ά����״̬
					s1 = nextNode.tag;// ��ǰ��״̬ά����״̬
					// ��һ��״̬ά����״̬�ڵ��ۻ�����
					prob = prevNode.prob;
					// ��һ��״̬ά����״̬����ǰ��״̬ά����״̬��ת�Ƹ���
					double transprob = s0.transprob(s1);

					prob = prob * transprob;
					emitprob = s1.emitprob(sym1);
					System.out
							.println("state:" + s1.name + " emit:" + emitprob);
					// ��һ�����ۻ���������һ�����ۻ����� * ��һ��״̬����һ��״̬��ת�Ƹ��� * ��ǰ�ڵ�ķ������
					prob = prob * emitprob;
					System.out.println("prob:" + prob);
					// �ҵ���ǰ�ڵ������ۻ�����

					if (nextNode.prob <= prob) {
						// ��¼��ǰ�ڵ������ۻ�����
						nextNode.prob = prob;
						// ��¼��ǰ�ڵ�����ǰ��
						nextNode.bestPrev = prevNode;
					}

				}
			}
		}
	}

	// �������Ѱ�����·������
	public ArrayList<Node> backward() {
		ArrayList<Node> maxNode = new ArrayList<Node>();
		Node endNode = al[symbols.size() - 1].get(0);// �����׶ζ�Ӧ�������״̬
		for (Node i = endNode; i != null; i = i.bestPrev) {
			maxNode.add(i);// ��ѡ�е���״̬���뵽���·��
		}
		return maxNode;
	}
}
