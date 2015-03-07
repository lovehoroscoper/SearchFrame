package spider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * ���SocketChannelע��Selector��
 * 
 * 
 */
public class SocketChannelSelector {
	public static SocketChannel createSocketChannel(String hostName, int port)
			throws IOException {
		SocketChannel sChannel = SocketChannel.open();
		sChannel.configureBlocking(false);
		sChannel.connect(new InetSocketAddress(hostName, port));
		return sChannel;
	}

	// 2������ע���ѡ�����ؼ���
	static SelectionKey key1;
	static SelectionKey key2;

	public static void main(String[] args) {
		// 1��ѡ������ע��2��Socket ͨ��
		Selector selector = null;
		try {
			// ����ѡ����
			selector = Selector.open();

			// ����2��ͨ��
			SocketChannel sChannel1 = createSocketChannel("hao123.com", 80);
			SocketChannel sChannel2 = createSocketChannel("www.lietu.com", 80);

			// ע��ѡ�������������е��¼�
			key1 = sChannel1.register(selector, sChannel1.validOps());
			key2 = sChannel2.register(selector, sChannel1.validOps());
		} catch (IOException e) {
		}

		// �ȴ��¼���ѭ��
		while (true) {
			try {
				// �ȴ�
				selector.select();
			} catch (IOException e) {
				break;
			}

			// �����¼��б�
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();

			// ����ÿһ���¼�
			while (it.hasNext()) {
				// �õ��ؼ���
				SelectionKey selKey = it.next();

				// ɾ���Ѿ�����Ĺؼ���
				it.remove();

				try {
					// �����¼�
					processSelectionKey(selKey);
				} catch (IOException e) {
					// �����쳣
					selKey.cancel();
				}
			}
		}
	}

	public static void processSelectionKey(SelectionKey selKey)
			throws IOException {
		ByteBuffer buf = ByteBuffer.allocateDirect(1024);
		// ȷ����������
		if (selKey.isValid() && selKey.isConnectable()) {
			// �õ�ͨ��
			SocketChannel sChannel = (SocketChannel) selKey.channel();

			// �Ƿ�������ϣ�
			boolean success = sChannel.finishConnect();
			if (!success) {
				// �쳣
				selKey.cancel();
			}
		}

		// ������Զ�ȡ����
		if (selKey.isValid() && selKey.isReadable()) {
			// �õ�ͨ��
			SocketChannel sChannel = (SocketChannel) selKey.channel();

			if (sChannel.read(buf) > 0) {
				// ת���ʼ
				buf.flip();

				while (buf.remaining() > 0) {
					System.out.print((char) buf.get());
				}

				// ��λ�����
				buf.clear();
			}
		}

		// �������д������
		if (selKey.isValid() && selKey.isWritable()) {
			// �õ�ͨ��
			SocketChannel sChannel = (SocketChannel) selKey.channel();
			// ����2���������Ĺؼ���
			// ������ֻдһ�����ݡ�
			if (!s1 && key1.equals(selKey)) {
				System.out.println("channel1 write data..");
				//buf.clear();
				//buf.put("HELO localhost\n".getBytes());
				//buf.flip();
				//sChannel.write(buf);
				sendQuery(new URL("www.lietu.com"),sChannel);
				s1 = true;
			} else if (!s2 && key2.equals(selKey)) {
				System.out.println("channel2 write data..");
				/*buf.clear();
				buf.put("HELO localhost\n".getBytes());
				buf.flip();
				sChannel.write(buf);*/
				sendQuery(new URL("hao123.com"),sChannel);
				s2 = true;
			}
		}
	}
	
    /**
     *@return true if the query was succesfully send, false otherwise 
     * (send buffer too small or problems sending the query).
     */
    // ------------------------------------------------------------
    protected static boolean sendQuery ( URL url, SocketChannel sc_ )
    {
        StringBuilder qb = new StringBuilder() ;
        qb.append("GET ") ;
        
        String path = url.getPath() ;
        if( path == null || path.length() == 0 ) path = "/" ;
        qb.append( path.replaceAll(" ","%20") ) ;
        if( url.getQuery() != null )
        {
            qb.append( '?' ) ;
            //qb.append( URLEncoder.encode( url.getQuery() )) ;
            qb.append(url.getQuery().replaceAll(" ","%20")) ;
        }
        qb.append( " HTTP/1.0\r\n" ) ;
        qb.append( "Host: " ).append( url.getHost() ).append( "\r\n" ) ;
        
        byte[] bytes_array = qb.toString().getBytes() ;
        int _DEFAULT_SEND_BUFFER_SIZE = 8192 * 2 ;
        ByteBuffer _send_buff = ByteBuffer.allocate( _DEFAULT_SEND_BUFFER_SIZE ) ;
        //_logger.trace( "\n--- sending ---\n" + qb.toString() + "\n------") ;
        _send_buff.clear() ;
        _send_buff.put( bytes_array ) ;
        _send_buff.flip() ;

        try
        {
            sc_.write( _send_buff ) ;
        }
        catch (IOException e)
        {
        	//if( _logger.isTraceEnabled() )
            //    _logger.trace("IOException when trying to send request to URL[" + query_.getURL() + "]");
            return false;
        }
        //write_total_time += new Date().getTime() - t ;

        return true ;
    }

	// �ж��Ѿ�д�����ݵı�־
	static boolean s1 = false;
	static boolean s2 = false;
}