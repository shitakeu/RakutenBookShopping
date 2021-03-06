package jp.ksjApp.bookshopping.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jp.tksjApp.bookshopping.data.BookData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

/**
 * 楽天ブックス書籍検索API2 用Parser
 * 
 * @author shitakeu
 * 
 */
public class RakutenParser {

	@SuppressWarnings("unused")
	private static final String TAG = RakutenParser.class.getSimpleName();

	public ArrayList<BookData> xmlParser(InputStream is) {
		XmlPullParser parser = null;
		final ArrayList<BookData> result = new ArrayList<BookData>();
		BookData currentMsg = null;

		parser = Xml.newPullParser();

		try {
			parser.setInput(is, null);

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {

				String tag = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;

				case XmlPullParser.START_TAG:
					tag = parser.getName();
					if (tag.equals("Items")) {
					} else if (tag.equals("Item")) {
						currentMsg = new BookData();
					} else if (currentMsg != null) {
						if (tag.equals("itemUrl")) {
							currentMsg.url = parser.nextText();
						} else if (tag.equals("affiliateUrl")) {
							currentMsg.affiliateUrl = parser.nextText();
						} else if (tag.equals("title")) {
							currentMsg.name = parser.nextText();
						} else if (tag.equals("itemPrice")) {
							currentMsg.price = parser.nextText();
						} else if (tag.equals("largeImageUrl")) {
							currentMsg.thumbnailUrl = parser.nextText();
						} else if (tag.equals("publisherName")) {
							currentMsg.publisherName = parser.nextText();
						} else if (tag.equals("author")) {
							currentMsg.author = parser.nextText();
						} else if (tag.equals("reviewAverage")) {
							currentMsg.reviewRate = Float.parseFloat(parser
									.nextText());
						} else if (tag.equals("reviewCount")) {
							currentMsg.reviewCount = parser.nextText();
						}
					}
					break;

				case XmlPullParser.END_TAG:
					tag = parser.getName();
					if (tag.equals("Item")) {
						result.add(currentMsg);
						currentMsg = null;
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			Log.e(TAG, "XmlPullParserException : " + e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e(TAG, "IOException : " + e.getMessage());
			return null;
		}
		return result;
	}
}
