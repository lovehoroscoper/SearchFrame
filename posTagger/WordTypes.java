package posTagger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WordTypes {
	public POSSem[] keys;// ���Լ�����������п��ܵ������������
	public int[] vals;// Ƶ��
	public int total; // �ʵ���Ƶ��

	/** Creates a new instance of IntMap */
	public WordTypes(int len) {
		keys = new POSSem[len];
		vals = new int[len];
	}

	public WordTypes(DataInputStream inStream) throws IOException {
		byte typecount = inStream.readByte(); //��������
		total = inStream.readInt();
		keys = new POSSem[typecount];
		vals = new int[typecount]; // ���ڵ��data��ֵ
		
		for (int i = 0; i < typecount; i++) {
			keys[i] = new POSSem(inStream);
			int val = inStream.readInt();
			vals[i] = val;
		}
	}

	public void save(DataOutputStream outStream) throws IOException {
		/* д��key������ */
		outStream.writeByte(keys.length);
		outStream.writeInt(total);

		/* д��ǰ׺�ʱ�� */
		for (int i = 0; i < keys.length; i++) {
			keys[i].save(outStream);
			outStream.writeInt(vals[i]);
		}
	}

	public void insert(int i ,byte key, int val) {
		keys[i] = new POSSem(key);
		vals[i] = val;
		total += val;
	}

	public void put(byte key, int val) {
		growArrays();

		keys[keys.length - 1] = new POSSem(key);
		vals[keys.length - 1] = val;
		total += val;
	}

	private void growArrays() {
		int newSize = keys.length + 1;
		// System.out.println("growArrays new size:"+newSize);
		POSSem[] newKeys = new POSSem[newSize];
		int[] newVals = new int[newSize];
		//Arrays.fill(newKeys, (byte) 127); // So binarySearch works
		System.arraycopy(keys, 0, newKeys, 0, keys.length);
		System.arraycopy(vals, 0, newVals, 0, vals.length);
		keys = newKeys;
		vals = newVals;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("WordId:Freq"); // NOI18N

		for (int i = 0; i <= keys.length - 1; i++) {
			sb.append("[");
			sb.append(keys[i]);
			sb.append(":");
			sb.append(vals[i]);
			sb.append("]");
		}
		if (keys.length <= 0) {
			sb.append("empty");
		}
		return sb.toString();
	}
}
