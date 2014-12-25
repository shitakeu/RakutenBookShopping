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
import jp.ksjApp.bookshopping.parser.RakutenRankingParser;
import jp.tksjApp.bookshopping.data.BookData;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

/**
 * 楽天BookAPI
 * @author mtb_cc_sin5
 *
 */
public class RakutenBookRankingApiTask extends AsyncTask<String, Void, ArrayList<BookData>> {

	@SuppressWarnings("unused")
	private static final String TAG = RakutenBookRankingApiTask.class.getSimpleName();

	
	public RakutenBookRankingApiTask() {
	}

	@Override
	protected ArrayList<BookData> doInBackground(String... params) {
		return requestApi();
	}

	private ArrayList<BookData> requestApi() {

		ArrayList<BookData> data = null;
		final HttpClient httpClient = new DefaultHttpClient();
		try {
			final HttpGet request = new HttpGet(getBookRankingApiUrl());
			final HttpResponse httpResponse = httpClient.execute(request);
			final InputStream in = httpResponse.getEntity().getContent();
			final RakutenRankingParser rankingParser = new RakutenRankingParser();
			data = rankingParser.xmlParser(in);
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private String getBookRankingApiUrl() {
		if(Const._DEBUG_){
			Log.d(TAG, Const.RAKUTEN_RANLING_API_URL);
		}
		return Const.RAKUTEN_RANLING_API_URL;
	}

}
