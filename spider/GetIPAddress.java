package spider;

import java.io.IOException;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class GetIPAddress {
	public static void main(String[] args) throws Exception {
		System.out.println(java.net.InetAddress.getByName("www.baidu.com"));
		//System.out.println(getADSLIP());
		//System.out.println(getIPByDomain("www.baidu.com"));
		
	}
	
	/**
	 * �����������õ���Ӧ��IP
	 * 
	 * @param host
	 * @return
	 * @throws UnknownHostException
	 */
	public static List<String> getIPByDomain(String host) throws Exception {
		List<String> ipList = new ArrayList<String>();
		InetAddress[] addressList = null;
		try {
			addressList = InetAddress.getAllByName(host);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("<<" + Thread.currentThread().getName() + " - " + e.toString()
					+ " retry 30 seconds later..>>");
			Thread.sleep(30000);
			try {
				addressList = InetAddress.getAllByName(host);
			} catch (UnknownHostException e1) {
			}
		}
		for (InetAddress address : addressList) {
			ipList.add(address.getHostAddress());
		}
		return ipList;
	}

	/**
	 * ��ȡ��ǰ������ADSL IP
	 * @return
	 */
	public static String getADSLIP() {
		String result = null;
		try {
			Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
			while (nis.hasMoreElements()) {
				NetworkInterface ni = nis.nextElement();
				//System.out.println("NetworkInterface "+ni.getName());
				if (ni.getName().startsWith("ppp")) {
					Enumeration<InetAddress> ads = ni.getInetAddresses();
					while (ads.hasMoreElements()) {
						//System.out.println("hasMoreElements");
						InetAddress ad = ads.nextElement();
						//System.out.println(ad.getHostAddress());
						if (ad instanceof Inet4Address) {
							result = ad.getHostAddress();
							return result;
						}
					}
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * ���ݴ��������host���Է���������(��ͨ��׺)
	 * 
	 * @return
	 */
	private static String getPanDomain(String domain1, String domain2) {
		// ������host��.�ָȻ��Ӻ���ǰ����
		String[] _tmp1 = domain1.split("[.]");
		String[] _tmp2 = domain2.split("[.]");
		String[] tmp1 = new String[_tmp1.length];
		for (int i = 0; i < _tmp1.length; i++) {
			tmp1[i] = _tmp1[_tmp1.length - 1 - i];
		}
		String[] tmp2 = new String[_tmp2.length];
		for (int i = 0; i < _tmp2.length; i++) {
			tmp2[i] = _tmp2[_tmp2.length - 1 - i];
		}

		// �ҳ�������ͬ��token
		List<String> token = new ArrayList<String>();
		for (int i = 0; i < tmp1.length; i++) {
			if (tmp1[i].equals(tmp2[i])) {
				token.add(tmp1[i]);
			} else {
				break;
			}
		}

		// ����ͬ��tokenƴװΪ��֮ǰ��������.*?����
		StringBuilder result = new StringBuilder();
		for (int i = (token.size() - 1); i >= 0; i--) {
			result.append(token.get(i));
			if (i > 0) {
				result.append(".");
			}
		}
		return result.toString();
	}
}
