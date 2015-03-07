package htmlParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.TextExtractingVisitor;

public class FetchTable {
	static class Record {
		public String province = null;
		public String area = null;
		public String city = null;
		public String village = null;
		public String postcode = null;
		public String code = null;

		public void setProvince(String province) {
			this.province = province;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public void setVillage(String village) {
			this.village = village;
		}

		public void setPostcode(String postcode) {
			this.postcode = postcode;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getProvince() {
			return province;
		}

		public String getArea() {
			return area;
		}

		public String getCity() {
			return city;
		}

		public String getVillage() {
			return village;
		}

		public String getPostcode() {
			return postcode;
		}

		public String getCode() {
			return code;
		}
	}

	static class ProvinceNum {
		String provinceName = "";
		int pagaNum = 0;

		ProvinceNum(String provinceName, int pageNum) {
			this.provinceName = provinceName;
			this.pagaNum = pageNum;
		}

		public void setProvinceName(String provinceName) {
			this.provinceName = provinceName;
		}

		public void setPageNum(int pageNum) {
			this.pagaNum = pageNum;
		}

		public String getProvinceName() {
			return provinceName;
		}

		public int getPageNum() {
			return pagaNum;
		}
	}
	
	int count = 1;

	FetchTable() {
	}

	/**
	 * funname:readTable description: read Table information by url
	 * 
	 * @param：url is website
	 * @return:return String data authour:chen createdate:2009/12/02
	 * @throws ParserException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void readTable(ProvinceNum[] provinceNum) throws ParserException,
			IOException, SQLException {
		for (int i = 0; i < provinceNum.length; i++) {
			pageStrBuffer(provinceNum[i].getProvinceName(), provinceNum[i]
					.getPageNum());
			System.out.println("i:" + i);

		}
	}

	public void pageStrBuffer(String provinceName, int page)
			throws IOException, ParserException, SQLException {
		int pageNum = 1;
		do {
			String strPageNum = String.valueOf(pageNum);
			String strUrl = "http://www.soku.net/Code_Default.asp?Type=Sm&SearchKeyStr="
					+ provinceName + "&Page=" + strPageNum;
			URL url = new URL(strUrl);
			InputStream is = url.openStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			provinceStrBuf(br);
			pageNum = pageNum + 1;
		} while (pageNum <= page);
	}

	public void provinceStrBuf(BufferedReader br) throws IOException,
			ParserException, SQLException {
		StringBuffer strBuf = new StringBuffer();
		String strLine = br.readLine();
		while (strLine != null) {
			strBuf.append(strLine);
			strLine = br.readLine();
		}
		parserTable(strBuf.toString());
	}

	public void parserTable(String str) throws ParserException, SQLException {
		Lexer lexer = new Lexer(str);
		Parser parser = new Parser(lexer);
		parser.setEncoding("gbk");
		NodeFilter filter_title = new AndFilter(new TagNameFilter("tr"),
				new HasAttributeFilter("onmouseout",
						"this.style.background='#FFFFFF'"));
		NodeList nodelist = parser.extractAllNodesThatMatch(filter_title);
		int listCount = nodelist.size();
		for (int i = 0; i < listCount; i++) {
			Node node = nodelist.elementAt(i);
			readNode(node);
		}
	}

	public void readNode(Node node) throws ParserException, SQLException {
		Lexer lexer = new Lexer(node.toHtml());
		Parser parser = new Parser(lexer);
		parser.setEncoding("gbk");
		NodeFilter filter_title = null;
		NodeList nodelist = null;
		try {
			filter_title = new AndFilter(new TagNameFilter("a"),
					new HasAttributeFilter("href"));
			nodelist = parser.extractAllNodesThatMatch(filter_title);
		} catch (ParserException e) {
			e.printStackTrace();
		}
		mergeRecord(nodelist);
	}

	public void mergeRecord(NodeList nodelist) throws SQLException,
			ParserException {
		Record record = new Record();
		int listCount = nodelist.size();
		String[] address = new String[listCount];
		for (int i = 0; i < listCount; i++) {
			Node node_title = nodelist.elementAt(i);
			String strtext = extractTest(node_title.toHtml());
			address[i] = strtext;
			System.out.println("地址：" + address[i]);
		}
		record.setProvince(address[0]);
		record.setArea(address[1]);
		record.setCity(address[2]);
		record.setVillage(address[3]);
		record.setPostcode(address[4]);
		record.setCode(address[5]);
		writeDatabase(record);
	}

	public String extractTest(String contentStr) throws ParserException {
		//用HTML页面内容做参数
		Lexer le=new Lexer(contentStr);

		Parser parser_text = new Parser(le);
		TextExtractingVisitor visitor = new TextExtractingVisitor();
		parser_text.visitAllNodesWith(visitor);
		return  visitor.getExtractedText();
	}

	public Connection dataBaseConnection() {
		Connection conn = null;
		try {
			String strurl = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=d:\\db1.mdb";
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			conn = DriverManager.getConnection(strurl);
		} catch (Exception e) {
			System.out.println(e);
		}
		return conn;
	}

	public void writeDatabase(Record record) throws SQLException {
		Connection conn = null;
		conn = dataBaseConnection();
		System.out.println("conn");
		String strSql = "insert into Table_Address(省名,地区,县市,乡镇村,邮政编码,区号) values (?,?,?,?,?,?)";
		PreparedStatement psmt = conn.prepareStatement(strSql);
		psmt.setString(1, record.getProvince());
		psmt.setString(2, record.getArea());
		psmt.setString(3, record.getCity());
		psmt.setString(4, record.getVillage());
		psmt.setString(5, record.getPostcode());
		psmt.setString(6, record.getCode());
		// psmt.executeBatch();
		psmt.executeUpdate();
		System.out.println("count:" + count++);
	}

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		FetchTable fetchtable = new FetchTable();
		ProvinceNum[] provinceNum = { new ProvinceNum("湖南", 167),
				new ProvinceNum("湖北", 190), new ProvinceNum("广东", 95),
				new ProvinceNum("广西", 75), new ProvinceNum("河南", 105),
				new ProvinceNum("河北", 181), new ProvinceNum("山东", 117),
				new ProvinceNum("山西", 89), new ProvinceNum("陕西", 128),
				new ProvinceNum("青海", 23), new ProvinceNum("甘肃", 74),
				new ProvinceNum("西藏", 33), new ProvinceNum("云南", 237),
				new ProvinceNum("贵州", 199), new ProvinceNum("四川", 307),
				new ProvinceNum("内蒙古", 78), new ProvinceNum("海南", 19),
				new ProvinceNum("重庆", 104), new ProvinceNum("福建", 48),
				new ProvinceNum("安徽", 160), new ProvinceNum("吉林", 52),
				new ProvinceNum("江苏", 101), new ProvinceNum("江西", 92),
				new ProvinceNum("浙江", 151), new ProvinceNum("新疆", 43),
				new ProvinceNum("黑龙江", 86), new ProvinceNum("辽宁", 82),
				new ProvinceNum("宁夏", 14), new ProvinceNum("北京", 18),
				new ProvinceNum("上海", 17), new ProvinceNum("天津", 18) };
		try {
			fetchtable.readTable(provinceNum);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("run end");
	}
}
