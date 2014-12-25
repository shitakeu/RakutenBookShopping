package jp.ksjApp.bookshopping.task;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import jp.ksjApp.bookshopping.Const;
import jp.ksjApp.bookshopping.parser.RakutenParser;
import jp.tksjApp.bookshopping.data.BookData;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

/**
 * 楽天BookAPI
 * @author mtb_cc_sin5
 *
 */
public class RakutenBookApiTask extends AsyncTask<String, Void, ArrayList<BookData>> {

	private static final String TAG = RakutenBookApiTask.class.getSimpleName();

	private String mSearchWord = "";
	private String mSearchGenre = "";
	private int mPage = 1;
	
	
	public RakutenBookApiTask(String searchWord, String searchGenre, int page) {
		mSearchWord = searchWord;
		mSearchGenre = searchGenre;
		mPage = page;
	}

	@Override
	protected ArrayList<BookData> doInBackground(String... params) {
		return requestApi();
	}

	private ArrayList<BookData> requestApi() {

		ArrayList<BookData> data = null;
		final HttpClient httpClient = new DefaultHttpClient();
		try {
			final HttpGet request = new HttpGet(createRakutenBookApiUrl(
					mSearchWord, mSearchGenre));
			final HttpResponse httpResponse = httpClient.execute(request);
			final InputStream in = httpResponse.getEntity().getContent();
			final RakutenParser rakutenParser = new RakutenParser();
			data = rakutenParser.xmlParser(in);
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private String createRakutenBookApiUrl(String query, String searchGenre) {
		final StringBuffer strbuf = new StringBuffer();
		strbuf.append(Const.RAKUTEN_BOOK_API_URL);

		strbuf.append("title=" + URLEncoder.encode(query));
		if (!TextUtils.isEmpty(searchGenre)) {
			strbuf.append("&");
			strbuf.append("booksGenreId=" + URLEncoder.encode(searchGenre));
		}
		strbuf.append("&");
		strbuf.append("applicationId=" + Const.RAKUTEN_APP_ID);
		strbuf.append("&");
		strbuf.append("affiliateId=" + Const.RAKUTEN_AFFILIATEI_ID);
		strbuf.append("&");
		strbuf.append("format=xml");
		strbuf.append("&");
		strbuf.append("page=" + mPage);
		

		Log.e(TAG, strbuf.toString());

		return strbuf.toString();
	}

}
