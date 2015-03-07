package rss;

import java.io.IOException;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import de.nava.informa.impl.basic.Channel;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;
import de.nava.informa.parsers.FeedParser;

public class FeedReader {

	/**
	 * @param args
	 * @throws de.nava.informa.core.ParseException 
	 */
	public static void main(String[] args) 
	throws ParseException, IOException, de.nava.informa.core.ParseException {
		ChannelBuilder builder = new ChannelBuilder();
		String url = "http://rss.news.yahoo.com/rss/topstories";
		Channel channel = (Channel) FeedParser.parse(builder, url);
		System.out.println("����: " + channel.getTitle());
		System.out.println("����: " + channel.getDescription());
		System.out.println("����:");
		for (Object x : channel.getItems())
		{
		    Item anItem = (Item) x;
		    System.out.print("����:"+anItem.getTitle() + " ������ ");
		    System.out.println(anItem.getDescription());
		}
	}

}
