package htmlParser;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionAccess {
	public static void main(String[] args) {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); // ��̬���غʹ���Driver����
			String url = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=d:\\db1.mdb";// ����������ʼ�����ݿ�url
			Connection con = DriverManager.getConnection(url); // ����������ʼ�����ݿ�����con
			if (con != null) // �鿴�����Ƿ�ɹ�
			{
				System.out.println("���ݿ����ӳɹ�");
			}
		} catch (Exception e) // �����쳣
		{
			System.out.println("���ݿ�����ʧ��");
			e.printStackTrace(); // ����쳣��Ϣ
		}
	}
}
