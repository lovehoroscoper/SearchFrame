package htmlParser;

public class LoadDriver {
    public static void main(String[] args) {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            System.out.println("�ҵ���������");
        } catch (ClassNotFoundException ex) {
        	System.out.println("û���ҵ���������");
        }
    }
}