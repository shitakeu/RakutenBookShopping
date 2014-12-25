package jp.ksjApp.bookshopping.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

/**
 * @author shitakeu
 * 
 */
public class GoogleSuggestParser {

	@SuppressWarnings("unused")
	private static final String TAG = GoogleSuggestParser.class.getSimpleName();

	public ArrayList<String> xmlParser(InputStream is) {

		final ArrayList<String> result = new ArrayList<String>();
		String currentMsg = null;
		final XmlPullParser parser = Xml.newPullParser();
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
					if (tag.equals("CompleteSuggestion")) {
						currentMsg = "";
					} else if (currentMsg != null) {
						if (tag.equals("suggestion")) {
							for (int i = 0; i < parser.getAttributeCount(); i++) {
								if ("data".equals(parser.getAttributeName(i))) {
									currentMsg = parser.getAttributeValue(i);
								}
							}
						}
					}
					break;

				case XmlPullParser.END_TAG:
					tag = parser.getName();
					if (tag.equals("CompleteSuggestion")) {
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
