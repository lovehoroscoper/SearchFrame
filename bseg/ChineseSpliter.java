package bseg;

import java.io.File;

/**
 * ���ķִ���
 */
public interface ChineseSpliter {
	/**
	 * �Ը������ı��������ķִ�
	 * 
	 * @param text
	 *            �������ı�
	 * @return �ִ���ϵĴ�����
	 */
	public String[] split(String text);

	public void split(File srcFile,File destFile); //�ڲ�����Split(String)
}
