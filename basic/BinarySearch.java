package basic;

public class BinarySearch {
	public static void main(String[] args) {
		int a[] = { 1, 4, 6, 7, 12, 14, 15 };
		System.out.println();
		int fromIndex = 0, toIndex = 6, key = 4;
		int low = fromIndex; // ��ʼλ��
		int high = toIndex - 1; // ����λ��
		while (low <= high) {
			int mid = (low + high) >>> 1; // �൱��mid = (low + high)/2
			int midVal = a[mid]; // ȡ�м��ֵ
			int cmp = midVal - key; // �м�ֵ��Ҫ�ҵĹؼ��ֱȽ�

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else {
				System.out.println("return" + mid); // ���ҳɹ��������ҵ���λ��
				return;
			}
		}
		System.out.println("return" + -(low + 1)); // û�ҵ������ظ�ֵ
		return;
	}
	
	int binarySearch(String[] a,String key){
		int low = 0; //��ʼλ��
		int high = a.length - 1; //����λ��

		while (low <= high) {
		    int mid = (low + high) >>> 1; //�൱��mid = (low + high)/2
		    String midVal = a[mid]; //ȡ�м��ֵ
		    int cmp = midVal.compareTo(key); //�м�ֵ��Ҫ�ҵĹؼ��ֱȽ�

		    if (cmp < 0)
				low = mid + 1;
		    else if (cmp > 0)
				high = mid - 1;
		    else
				return mid; // ���ҳɹ��������ҵ���λ��
		}
		return -(low + 1);  // û�ҵ������ظ�ֵ
	}
}
