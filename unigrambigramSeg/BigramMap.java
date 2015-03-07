package unigrambigramSeg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;

public class BigramMap{
	public int[] prevIds; // ǰ׺��id����
	public int[] freqs; // ���Ƶ�ʼ���
	public int id;// �ʱ����id	

	public BigramMap(int id,int len) {
		this.id = id;
		prevIds = new int[len];
		freqs = new int[len];
	}
	
	/**
	 * ����ʱ����
	 * @param prevId
	 * @param freq
	 */
	public void put(int prevId, int freq) {
		int index = indexOf(prevId);
		if (index == -1) { // ǰ׺������
			grow();
			for (index = prevIds.length - 1; index > 0 && prevIds[index - 1] > prevId; index--) {
				prevIds[index] = prevIds[index - 1];
				freqs[index] = freqs[index - 1];
			}
		}
		prevIds[index] = prevId;
		freqs[index] = freq;
	}
	
	public int get(int prevId) {
		int index = indexOf(prevId);
		if (index != -1)
			return freqs[index];
		return -1;
	}

	private void grow() {
		int newLength = prevIds.length + 1;
		prevIds = Arrays.copyOf(prevIds, newLength);
		freqs = Arrays.copyOf(freqs, newLength);
	}

	/**
	 * ���ֲ���
	 * @param prevId
	 * @return
	 */
	public int indexOf(int prevId) {
		int low = 0;
		int high = prevIds.length - 1;
		while (low <= high) {
			int mid = low + ((high - low) >>> 1);
			int midVal = prevIds[mid];

			if (midVal < prevId)
				low = mid + 1;
			else if (midVal > prevId)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -1;
	}

    
    public void add(int i ,int key, int val){
    	prevIds[i] = key;
    	freqs[i] = val;
    }

	public BigramMap(DataInputStream inStream) throws IOException{
		id = inStream.readInt(); /* ��ȡ�ʵ�id */
		
		int len = inStream.readInt(); /* ��ȡ�ļ���key��value���� */
		prevIds = new int[len];
		freqs = new int[len];
		
		for (int i = 0; i < len; i++) {
			prevIds[i] = inStream.readInt();
			freqs[i] = inStream.readInt();
		}
	}
	
    public void save(DataOutputStream outStream) throws IOException{
    	outStream.writeInt(id);
		/* д��key���� */
		outStream.writeInt(prevIds.length);

		/* д��ǰ׺�ʱ�� */
		for (int i = 0; i < prevIds.length; i++) {
			outStream.writeInt(prevIds[i]);
			outStream.writeInt(freqs[i]);
		}
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("BigramMap@"); //NOI18N
        
        for (int i=0; i < prevIds.length; i++) {
            sb.append ("["); //NOI18N
            sb.append (prevIds[i]);
            sb.append (":"); //NOI18N
            sb.append (freqs[i]);
            sb.append ("]"); //NOI18N
        }
		if (prevIds.length == 0) {
			sb.append("empty"); // NOI18N
		}
        return sb.toString();
    }
   
    
    public int find(int data) { // ����Ԫ��
		int index = 1; // �Ӹ�ڵ㿪ʼ�ң���ڵ�����1
		while (index < prevIds.length) { // ��λ�ò��ǿ�
			if (data < prevIds[index]) { // �ж�Ҫ�����ң�����������
				index = index << 1; // ������
			} else if (data == prevIds[index]) { // �ҵ���
				return freqs[index]; //���ؼ��Ӧ��ֵ
			} else {
				index = (index << 1) + 1; // ������
			}
		}
		return -1; // û�ҵ�
	}
	

	
	public void buildArray(int[] keys, int[] values) {
		sortArrays(keys, values);	//��������
		int pos = 1; // �Ѿ������λ��
		this.prevIds = new int[keys.length + 1]; // ��ȫ����������
		this.freqs = new int[keys.length + 1];
		ArrayDeque<Span> queue = new ArrayDeque<Span>(); //��ջ
		queue.add(new Span(0, keys.length)); //�������������
		while (!queue.isEmpty()) { //����ջ�л���Ԫ��
			Span current = queue.pop(); //ȡ��Ԫ��
			int rootId = BigramMap.getRoot(current.end - current.start)
					+ current.start;
			this.prevIds[pos] = keys[rootId];
			this.freqs[pos] = values[rootId];
			pos++;
			if (rootId > current.start)
				queue.add(new Span(current.start, rootId));
			rootId++;
			if (rootId < current.end)
				queue.add(new Span(rootId, current.end));
		}
	}
	
	public static void sortArrays(int[] keys, int[] values) {
		int i, j;
		int temp;
		//ð�ݷ�����
		for (i = 0; i < keys.length - 1; i++) {
			// ����������Ѿ��ź��������𽥼���ѭ������
			for (j = 0; j < keys.length - 1 - i; j++) {
				if (keys[j] > keys[j + 1]) {
					temp = keys[j]; //������
					keys[j] = keys[j + 1];
					keys[j + 1] = temp;

					temp = values[j]; //����ֵ
					values[j] = values[j + 1];
					values[j + 1] = temp;
				}
			}
		}
	}
	
	/**
	 * ȡ����ȫ������������
	 * @param num �ڵ���
	 * @return ��ڵ���
	 */
	static int getRoot(int num) {
		int n = 1; //�����������Ľڵ���
		while (n <= num) {
			n = n << 1;
		}
		int m = n >> 1;
		int bottom = num - m + 1;  //�ײ�ʵ�ʽڵ�����
		int leftMaxBottom = m >> 1; //������������������£���߽ڵ������
		if (bottom > leftMaxBottom) { // ����Ѿ�����
			bottom = leftMaxBottom;
		}
		
		int index = bottom; //��ߵĵײ�ڵ���
		if(m>1){ //�����ڲ��Ľڵ���
			index += ((m >> 1) - 1);
		}
		return index;
	}
	
	class Span {
		int start; // ��ʼ����
		int end; // ��������

		public Span(int s, int e) { //���췽��
			start = s;
			end = e;
		}
	}
}